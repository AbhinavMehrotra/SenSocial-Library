package com.ubhave.sensocial.sensordata.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ubhave.sensocial.data.DeviceSensorData;
import com.ubhave.sensocial.data.SocialData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.filters.Condition;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.ModalityType;
import com.ubhave.sensocial.manager.SSListenerManager;
import com.ubhave.sensocial.mqtt.MQTTNotifitions;
import com.ubhave.sensocial.sensormanager.SensorUtils;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.data.SensorData;

/**
 * SensorDataHandler class handles the data fetched from the sensors and fires it to
 * the respective listener on the client or to the server.
 */
public class SensorDataHandler {

	protected static SensorDataClassifier classifier;
	private static SharedPreferences sp;

	/**
	 * Handles the data from stream sensors.
	 * @param data List of Sensor data
	 * @param context Application context
	 */
	public static void handleStreamData(ArrayList<SensorData> data, final Context context){
		Log.e("SNnMB", "HandleStreamData: "+data);
		sp=context.getSharedPreferences("SSDATA", 0);
		Set<String> configs=new HashSet<String>();
		configs=sp.getStringSet("StreamConfigurationSet", null); //see SensorClassifier
		//check if any data is required without any condition <condition=all>
		for(SensorData sd:data){
			Log.e("SNnMB", "Looking for: "+new SensorUtils(context).getSensorNameById(sd.getSensorType()));
			for(String c:configs){
				if(sp.getStringSet(c, null).contains(ModalityType.null_condition) && ConfigurationHandler.getRequiredData(c).
						equalsIgnoreCase(new SensorUtils(context).getSensorNameById(sd.getSensorType()))){
					Log.e("SNnMB", "Found: "+new SensorUtils(context).getSensorNameById(sd.getSensorType())+" in config: "+c);
					Log.e("SNnMB", "Required data for config: "+c+", is: "+ConfigurationHandler.getRequiredData(c));
					SocialEvent se=new SocialEvent();
					//now find location of data to be sent
					Map<String,String> map=new HashMap<String,String>();
					map=ConfigurationHandler.getRequiredDataLocationNType(c);
					Log.e("SNnMB", "Required data location and type: "+map);
					if(map.containsValue("classified")){
						Log.e("SNnMB", "Required classified data ");
						//classify it
						DeviceSensorData d=new DeviceSensorData();
						d.setDeviceId(sp.getString("deviceid", null));
						d.setClassifiedData(DummyClassifier.getClassifiedData(sd));
						d.setStreamId(c);
						se.setFilteredSensorData(d);					
					}
					else{
						Log.e("SNnMB", "Required raw data ");
						DeviceSensorData d=new DeviceSensorData();
						d.setDeviceId(sp.getString("deviceid", null));
						d.setRawData(sd);
						d.setStreamId(c);
						se.setFilteredSensorData(d);
					}
					if(map.containsKey("server")){
						Log.e("SNnMB", "Required at server ");
						//send to server
						ClientServerCommunicator.sendStream(context, se.toJSONString());
					}
					else{
						Log.e("SNnMB", "Required at client ");
						Log.e("SNnMB", "Data to be fired: "+se.toJSONString());
						//send to client listener
						SSListenerManager.fireUpdate(se);
					}				
				}				
			}			
		}


		for(final String config:sp.getStringSet("StreamConfigurationSet", null)){
			Boolean satisfied=true;
			for(String condition:sp.getStringSet(config, null)){ //get all conditions for each config
				Condition con=new Condition(condition);
				String sensorName=ModalityType.getSensorName(con.getModalityType());
				String operator=con.getOperator();
				String value=con.getModalValue();
				if(!DummyClassifier.isSatisfied(data,sensorName, operator,value)){
					satisfied=false;
				}				
			}	
			if(satisfied){
				System.out.println("All conditions satisfied!!");
				String sensor=ConfigurationHandler.getRequiredData(config);
				SensorUtils aps=new SensorUtils(context);
				final Map<String,String> map=ConfigurationHandler.getRequiredDataLocationNType(config);	
				Log.i("SNnMB", "RequiredDataLocationNType: "+map);

				ArrayList<Integer> SensorIds=new ArrayList<Integer>();
				SensorIds.add(aps.getSensorIdByName(sensor));
				try {
					new OneOffSensing(context, SensorIds){
						@Override
						public void onPreExecute()
						{
							Log.i("SNnMB", "Required data sensed:  onPreExecute");
						}
						@Override
						public void onPostExecute(final ArrayList<SensorData> data){
							Log.i("SNnMB", "Required data sensed:  onPostExecute");
							SensorData sensor_data = null;
							for(SensorData x:data){
								Log.i("SNnMB", "Data to be fired: "+x.toString());
								sensor_data=x;
							}
							SocialEvent se=new SocialEvent();
							DeviceSensorData d=new DeviceSensorData();
							d.setDeviceId(sp.getString("deviceid", null));
							d.setStreamId(config);
							if(map.containsValue("raw")){
								d.setRawData(sensor_data);
								Log.i("SNnMB", "Raw Data to be fired: "+sensor_data.toString());

							}
							else{
								d.setClassifiedData(DummyClassifier.getClassifiedData(sensor_data));
								Log.i("SNnMB", "Classified Data to be fired: "+DummyClassifier.getClassifiedData(sensor_data));
							}

							se.setFilteredSensorData(d);
							if(map.containsKey("server")){
								ClientServerCommunicator.sendStream(context, se.toJSONString()); 
							}
							else{
								SSListenerManager.fireUpdate(se);
							}							
						}
					}.execute();
				} catch (ESException e) {
					e.printStackTrace();
				}				
			}


		}

	}


	/**
	 * Handles the data from OSN linked sensors.
	 * @param data List of Sensor data
	 * @param context Application context
	 * @param message Data from OSN
	 */
	public static void handleOSNDependentData(ArrayList<SensorData> data, final Context context, final String message){
		Log.i("SNnMB", "Inside handleOSNDependentData ");
		final SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		Set<String> blankSet=new HashSet<String>();
		Log.i("SNnMB", "OSNConfigurationSet: "+sp.getStringSet("OSNConfigurationSet", null));
		
		for(final String config:sp.getStringSet("OSNConfigurationSet", null)){
			Boolean satisfied=true;
			Log.i("SNnMB", "Conditions for config: "+config+", are: "+sp.getStringSet(config, blankSet));
			for(String condition:sp.getStringSet(config, blankSet)){ //get all conditions for each config
				Condition con=new Condition(condition);
				String sensorName=ModalityType.getSensorName(con.getModalityType());
				String operator=con.getOperator();
				String value=con.getModalValue();
				if(!DummyClassifier.isSatisfied(data,sensorName, operator,value)){
					satisfied=false;
				}
			}	
			if(satisfied){
				String sensor=ConfigurationHandler.getRequiredData(config);
				SensorUtils aps=new SensorUtils(context);
				final Map<String,String> map=ConfigurationHandler.getRequiredDataLocationNType(config);	
				ArrayList<Integer> SensorIds=new ArrayList<Integer>();
				SensorIds.add(aps.getSensorIdByName(sensor));
				try {
					new OneOffSensing(context, SensorIds){
						@Override
						public void onPostExecute(ArrayList<SensorData> data){
							SocialEvent se=new SocialEvent();
							SocialData socialData=new SocialData();
							try{
							JSONObject obj=new JSONObject(message.substring(MQTTNotifitions.facebook_update.getMessage().length()+1));
							socialData.setUserName(sp.getString("name", null));
							socialData.setOSNFeed(obj.getString("message"));
							socialData.setFeedType(obj.getString("notificationtype"));
							socialData.setTime(obj.getString("time"));
							socialData.setOSNName(obj.getString("osnname"));
							}
							catch(Exception e){
								Log.e("SNnMB", "Errror in sensor-data-handler: "+e.toString());
								socialData.setOSNFeed("not available");
								socialData.setFeedType("not available");
								socialData.setTime("not available");
								socialData.setOSNName("not available");
							}
							DeviceSensorData d=new DeviceSensorData();
							d.setDeviceId(sp.getString("deviceid", null));
							d.setStreamId(config);
							if(map.containsValue("raw"))
								d.setRawData(data.get(0));
							else
								d.setClassifiedData(DummyClassifier.getClassifiedData(data.get(0)));

							se.setFilteredSensorData(d);
							se.setSocialData(socialData);
							if(map.containsKey("server")){
								ClientServerCommunicator.sendStream(context, se.toJSONString()); 
							}
							else{
								SSListenerManager.fireUpdate(se);
							}							
						}
					}.execute();
				} catch (ESException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}
	}
}



