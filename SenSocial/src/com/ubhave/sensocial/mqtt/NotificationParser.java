package com.ubhave.sensocial.mqtt;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.DownloadFilter;
import com.ubhave.sensocial.filters.FilterSettings;
import com.ubhave.sensocial.filters.ServerStreamRegistrar;
import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensocial.sensormanager.SensorDataCollector;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

public class NotificationParser {
	private Context context;
	private SharedPreferences sp;
	protected NotificationParser(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA", 0);
	}

	protected void takeAction(final String message){		
		Log.i("SNnMB", "MQTT message: "+message);
		if(message.startsWith(MQTTNotifitions.start_stream.getMessage())){
			String streamId=message.substring(13);  //<<message --> start_stream:streamId>>
			ServerStreamRegistrar.addStreamId(streamId);
			String url=sp.getString("serverurl", null)+"ClientFilters/Filter"+streamId+".xml";
			//			String url=sp.getString("serverurl", null)+"ClientFilters/Filter"+"abc.xml";
			String destination=streamId+".xml";
			new DownloadFilter(context, url, destination).execute();
		}
		else if(message.startsWith(MQTTNotifitions.stop_stream.getMessage())){
			String streamId=message.substring(12);
			ServerStreamRegistrar.removeStreamId(streamId);
			FilterSettings.removeConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.pause_stream.getMessage())){
			String streamId=message.substring(13);
			FilterSettings.stopConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.unpause_stream.getMessage())){
			String streamId=message.substring(15);
			FilterSettings.startConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.facebook_update.getMessage())){
			Log.i("SNnMB", "Facebook update recieved!");
			AllPullSensors aps=new AllPullSensors(context);
			SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
			final ArrayList<Integer> sensorIds=new ArrayList<Integer>();
			for(String s:sp.getStringSet("OSNSensorSet", null))
				sensorIds.add(aps.getSensorIdByName(s));
			Log.i("SNnMB", "Sensors: "+sp.getStringSet("OSNSensorSet", null));
			Log.i("SNnMB", "Sensor ids: "+sensorIds);
			try {
				StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
	    		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
	    		.permitNetwork()
	    		.build());
				Log.d("SNnMB","Start sensing");
				new OneOffSensing(context, sensorIds){
					@Override
					public void onPostExecute(ArrayList<SensorData> data){
						Log.d("SNnMB","Stopped sensing");
						if(data!=null){
							SensorDataHandler.handleOSNDependentData(data, context, message);
						}

					}
				}.execute();
	            StrictMode.setThreadPolicy(old);
			} catch (Exception e) {
				Log.e("SNnMB","Error at Notification parser: "+e.toString());
			}
//			ArrayList<SensorData> sensorData=new ArrayList<SensorData>();
//			try {
//				ESSensorManager sensorManager = ESSensorManager.getSensorManager(context);
//				for(int i=0;i<sensorIds.size();i++){
//					if(SensorDataCollector.isRegistered(sensorIds.get(i))){
//						Log.i("SNnMB", "One-Off sensing process, found the latest data available for: "+sensorIds.get(i));
//						sensorData.add(SensorDataCollector.getData(sensorIds.get(i)));
//					}
//					else{
//						Log.i("SNnMB", "One-Off sensing process, latest data not available & sensing for: "+sensorIds.get(i));
//						sensorData.add(sensorManager.getDataFromSensor(sensorIds.get(i)));
//					}
//				}
//			} catch (Exception e) {
//				Log.e("SNnMB","Error at Notification parser: "+e.toString());
//			}
//			if(sensorData!=null){
//				Log.i("SNnMB", "Sensor data: "+sensorData);
//				SensorDataHandler.handleOSNDependentData(sensorData, context, message);
//			}
		}
		else if(message.startsWith(MQTTNotifitions.nearby_bluetooths.getMessage())){

		}
	}
}

