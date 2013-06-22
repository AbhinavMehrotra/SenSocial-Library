package com.ubhave.sensocial.sensordata.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ubhave.sensocial.data.DeviceSensorData;
import com.ubhave.sensocial.data.SocialData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.manager.SSListenerManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.data.SensorData;

public class SensorDataHandler {

	protected static SensorDataClassifier classifier;
	private static boolean isDataInteresting;
	private static SharedPreferences sp;

	public static void handleStreamData(ArrayList<SensorData> data, final Context context){
		sp=context.getSharedPreferences("SSDATA", 0);
		Set<String> configs=new HashSet<String>();
		configs=sp.getStringSet("StreamConfigurationSet", null); //see SensorClassifier
		//check if any data is required without any condition <condition=all>
		for(SensorData sd:data){
			for(String c:configs){
				if(sp.getStringSet(c, null).contains("ALL") && ConfigurationHandler.getRequiredData(c).
						equalsIgnoreCase(new AllPullSensors(context).getSensorNameById(sd.getSensorType()))){
					SocialEvent se=new SocialEvent();
					//now find location of data to be sent
					Map<String,String> map=new HashMap<String,String>();
					map=ConfigurationHandler.getRequiredDataLocationNType(c);
					if(map.containsValue("classified")){
						//classify it
						DeviceSensorData d=new DeviceSensorData();
						d.setDeviceId(sp.getString("deviceid", null));
						d.setClassifiedData(DummyClassifier.classifyData(sd));
						d.setStreamId(c);
						se.setFilteredSensorData(d);					
					}
					else{
						DeviceSensorData d=new DeviceSensorData();
						d.setDeviceId(sp.getString("deviceid", null));
						d.setRawData(sd);
						d.setStreamId(c);
						se.setFilteredSensorData(d);
					}
					if(map.containsKey("server")){
						//send to server
						ClientServerCommunicator.sendStream(context, se.toJSONString());
					}
					else{
						//send to client listener
						SSListenerManager.fireUpdate(se);
					}				
				}				
			}			
		}
		//classify all the data and send the classified data to check filter configs
		Set<String> cData=new HashSet<String>();
		for(SensorData sd:data){
			cData.add(DummyClassifier.classifyData(sd));
		}

		for(final String c:sp.getStringSet("StreamConfigurationSet", null)){
			Boolean satisfied=true;
			for(String s:sp.getStringSet(c, null)){
				if(!cData.contains(s))
					satisfied=false;
			}
			if(satisfied){
				String sensor=ConfigurationHandler.getRequiredData(c);
				AllPullSensors aps=new AllPullSensors(context);
				final Map<String,String> map=ConfigurationHandler.getRequiredDataLocationNType(c);	
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
							d.setStreamId(c);
							if(map.containsValue("raw")){
								d.setRawData(sensor_data);
								Log.i("SNnMB", "Raw Data to be fired: "+sensor_data.toString());

							}
							else{
								d.setClassifiedData(DummyClassifier.classifyData(sensor_data));
								Log.i("SNnMB", "Classified Data to be fired: "+DummyClassifier.classifyData(sensor_data));
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				


				//				if(map.containsKey("server")){
				//					try {
				//						ContinuousStreamSensing.startSensingForServer(c, aps.getSensorIdByName(sensor), map.get("server"));
				//					} catch (ESException e) {
				//						e.printStackTrace();
				//					}
				//				}
				//				else{
				//					//send to client listener
				//					try {
				//						ContinuousStreamSensing.startSensingForDevice(c, aps.getSensorIdByName(sensor), map.get("classified"));
				//					} catch (ESException e) {
				//						e.printStackTrace();
				//					}
				//				}
			}
		}

	}

	//	public static void handleStreamData(SensorData data, Context context){
	//		//check if this data is required <condition=all>
	//		Set<String> configs=new HashSet<String>();
	//		sp=context.getSharedPreferences("SNnMB", 0);
	//		configs=sp.getStringSet("StreamConfigurationSet", null); //see SensorClassifier
	//		for(String c:configs){
	//			if(sp.getStringSet(c, null).contains("ALL") && ConfigurationHandler.getRequiredData(c).
	//					equalsIgnoreCase(new AllPullSensors(context).getSensorNameById(data.getSensorType()))){
	//				SocialEvent se=new SocialEvent();
	//				//now find location of data to be sent
	//				Map<String,String> map=new HashMap<String,String>();
	//				map=ConfigurationHandler.getRequiredDataLocationNType(c);
	//				if(map.containsValue("classified")){
	//					//classify it
	//					DeviceSensorData d=new DeviceSensorData();
	//					d.setDeviceId(sp.getString("deviceid", null));
	//					d.setClassifiedData(DummyClassifier.classifyData(data));
	//					d.setStreamId(c);
	//					se.setFilteredSensorData(d);					
	//				}
	//				else{
	//					DeviceSensorData d=new DeviceSensorData();
	//					d.setDeviceId(sp.getString("deviceid", null));
	//					d.setRawData(data);
	//					d.setStreamId(c);
	//					se.setFilteredSensorData(d);
	//				}
	//				if(map.containsKey("server")){
	//					//send to server
	//					ClientServerCommunicator.sendStream(context, se.toJSONString());
	//				}
	//				else{
	//					//send to client listener
	//					SSListenerManager.fireUpdate(se);
	//				}				
	//			}
	//			else{
	//				//it is not required directly, so classify it and check for filter now
	//
	//			}
	//		}
	//	}

	public static void handleOSNDependentData(ArrayList<SensorData> data, final Context context, final String message){
		ArrayList<String> cData=new ArrayList<String>();
		//classify all data and get a list of classified data
		for(SensorData sd:data){
			cData.add(DummyClassifier.classifyData(sd));
		}

		for(final String c:sp.getStringSet("OSNConfigurationSet", null)){
			Boolean satisfied=true;
			for(String s:sp.getStringSet(c, null)){
				if(!cData.contains(s))
					satisfied=false;
			}
			if(satisfied){
				String sensor=ConfigurationHandler.getRequiredData(c);
				AllPullSensors aps=new AllPullSensors(context);
				final Map<String,String> map=ConfigurationHandler.getRequiredDataLocationNType(c);	
				ArrayList<Integer> SensorIds=new ArrayList<Integer>();
				SensorIds.add(aps.getSensorIdByName(sensor));
				try {
					new OneOffSensing(context, SensorIds){
						@Override
						public void onPostExecute(ArrayList<SensorData> data){
							SocialEvent se=new SocialEvent();
							SocialData socialData=new SocialData();
							//TODO: parse the message and set all different fields
							socialData.setOSNFeed(message);
							DeviceSensorData d=new DeviceSensorData();
							d.setDeviceId(sp.getString("deviceid", null));
							d.setStreamId(c);
							if(map.containsValue("raw"))
								d.setRawData(data.get(0));
							else
								d.setClassifiedData(DummyClassifier.classifyData(data.get(0)));

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
				//				sensor=ConfigurationHandler.getRequiredData(c);
				//				AllPullSensors aps=new AllPullSensors(context);
				//				Map<String,String> map=new HashMap<String,String>();
				//				map=ConfigurationHandler.getRequiredDataLocationNType(c);
				//				if(map.containsKey("server")){
				//					try {
				//						ContinuousStreamSensing.startSensingForServer(c, aps.getSensorIdByName(sensor), map.get("server"));
				//					} catch (ESException e) {
				//						e.printStackTrace();
				//					}
				//				}
				//				else{
				//					//send to client listener
				//					try {
				//						ContinuousStreamSensing.startSensingForDevice(c, aps.getSensorIdByName(sensor), map.get("classified"));
				//					} catch (ESException e) {
				//						e.printStackTrace();
				//					}
				//				}
			}

		}

	}


}
