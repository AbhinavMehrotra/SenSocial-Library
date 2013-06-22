package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.Modality;
import com.ubhave.sensormanager.ESException;

public class SensorClassifier {
	/**
	 * This method create two sets of configurations: OSN dependent(OSNConfigurationSet) 
	 * & Continuous stream dependent(StreamConfigurationSet).
	 * <We used two sets of configuration so that after classification of streams we filter only for Stream configs>
	 * OSN dependent configuration require one-off sensing when ever there is a new update from OSN.
	 * Continuous stream dependent configurations require sensor subscription for continuous sensing.
	 * This method also create two sets of sensors: OSN dependent (OSNSensorSet) 
	 * & Stream dependent (StreamSensorSet).
	 * And subscribes/un-subscribes sensors.
	 * @param filterConfigs
	 */
	@SuppressLint("NewApi")
	public static void run(Context context, Map<String, Set<String>> filterConfigs) {
		System.out.println("Sensor classifier: run");
		Map<String, Set<String>> osnConfigs =new HashMap<String, Set<String>>();
		Map<String, Set<String>> streamConfigs =new HashMap<String, Set<String>>();
		Set<String> blankSet=new HashSet<String>();
		//Classify new configurations as OSN & Stream
		for(Map.Entry<String, Set<String>> c: filterConfigs.entrySet()){
			if(c.getValue().contains(Modality.facebook_trigger.getActivityName()) ||
					c.getValue().contains(Modality.twitter_post.getActivityName())){
				System.out.println("OSN config: "+c.getKey());
				osnConfigs.put(c.getKey(), c.getValue());
			}
			else{
				System.out.println("Stream config: "+c.getKey());
				streamConfigs.put(c.getKey(), c.getValue());
			}				
		}

		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		//Check if OSN config has been added/removed
		if(osnConfigs.size()>sp.getStringSet("OSNConfigurationSet", blankSet).size()){
			//added new OSN config 
			System.out.println("add OSN config");
			addOSNConfig(context,osnConfigs);
		}
		else if(osnConfigs.size()<sp.getStringSet("OSNConfigurationSet", blankSet).size()){
			//removed OSN config
			System.out.println("remove OSN config");
			removeOSNConfig(context,osnConfigs);
		}
		else{
			System.out.println("No OSN config to be added/removed");
			System.out.println("Size: "+osnConfigs.size()+" , "+sp.getStringSet("OSNConfigurationSet", blankSet).size());
		}

		//Check if Stream config has been added/removed
		if(streamConfigs.size()>sp.getStringSet("StreamConfigurationSet", blankSet).size()){
			//added new Stream config 			
			System.out.println("add Stream config");
			addAndSubscribeStreamConfig(context,streamConfigs);
		}
		else if(streamConfigs.size()<sp.getStringSet("StreamConfigurationSet", blankSet).size()){
			//removed Stream config
			System.out.println("remove stream config");
			removeAndUnsubscribeStreamConfig(context,streamConfigs);
		}
		else{
			System.out.println("No Stream config to be added/removed");
			System.out.println("Size: "+streamConfigs.size()+" , "+sp.getStringSet("StreamConfigurationSet", blankSet).size());
		}
	}

	//This method adds new elements to the OSN configuration and OSN sensor set
	private static void addOSNConfig(Context context, Map<String, Set<String>> osnConfigs){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		Editor ed=sp.edit();
		Set<String> config=new HashSet<String>();
		Set<String> sensor=new HashSet<String>();
		Set<String> blankSet=new HashSet<String>();
		config=sp.getStringSet("OSNConfigurationSet", blankSet);
		sensor=sp.getStringSet("OSNSensorSet", blankSet);
		for(Map.Entry<String, Set<String>> c: osnConfigs.entrySet()){
			if(!config.contains(c.getKey())){
				//new element found
				for(String s:c.getValue())
					sensor.add(Modality.valueOf(s).getSensorName());
			}
		}
		ed.putStringSet("OSNSensorSet", sensor);
		ed.putStringSet("OSNConfigurationSet", config);
		ed.commit();
	}

	//This method removes unused elements to the OSN configuration and OSN sensor set
	private static void removeOSNConfig(Context context, Map<String, Set<String>> osnConfigs){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		Editor ed=sp.edit();
		Set<String> config=new HashSet<String>();
		Set<String> sensor=new HashSet<String>();
		Set<String> blankSet=new HashSet<String>();
		config=sp.getStringSet("OSNConfigurationSet", blankSet);
		sensor=sp.getStringSet("OSNSensorSet", blankSet);
		for(String c:config){
			if(!osnConfigs.containsKey(c)){
				//removed element found
				config.remove(c);
			}
		}
		for(Map.Entry<String, Set<String>> e:osnConfigs.entrySet()){
			for(String s:e.getValue())
				sensor.add(Modality.valueOf(s).getSensorName());
		}
		ed.putStringSet("OSNSensorSet", sensor);
		ed.putStringSet("OSNConfigurationSet", config);
		ed.commit();
	}

	//This method adds new elements to the Stream configuration and Stream sensor set.
	//It will also subscribe the new sensors
	private static void addAndSubscribeStreamConfig(Context context, Map<String, Set<String>> streamConfigs){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		Editor ed=sp.edit();
		Set<String> config=new HashSet<String>();
		Set<String> sensor=new HashSet<String>();
		Set<String> sensorNew=new HashSet<String>();
		Set<String> blankSet=new HashSet<String>();
		config=sp.getStringSet("StreamConfigurationSet", null);
		sensor=sp.getStringSet("StreamSensorSet", sensor);
		for(String s:sensor)
			sensorNew.add(s);
		System.out.println("Sensor: "+sensor);
		System.out.println("Sensor New: "+sensorNew);
		
		for(Map.Entry<String, Set<String>> c: streamConfigs.entrySet()){
			System.out.println("Map: "+c);
			if(config==null || !config.contains(c.getKey())){
				//new element found
				for(String s:c.getValue()){
					if(s.equalsIgnoreCase("ALL")){
						sensorNew.add(ConfigurationHandler.getRequiredData(c.getKey()));
						System.out.println("Sensor name (for ALL): " + ConfigurationHandler.getRequiredData(c.getKey()));
						System.out.println("Set- Sensor: "+sensorNew);
					}
					else{
						sensorNew.add(Modality.valueOf(s).getSensorName());
						System.out.println("Sensor name: "+Modality.valueOf(s).getSensorName());
					}
				}
			}
		}

		System.out.println("sensor: "+sensor);
		System.out.println("sensorNew: "+sensorNew);

		//check for new sensors and subscribe 
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		AllPullSensors aps=new AllPullSensors(context);
		for(String sensor_name:sensor){
			sensorIds.add(aps.getSensorIdByName(sensor_name));
		}

		System.out.println("sensor: "+sensor);
		System.out.println("sensorNew: "+sensorNew);


		//		if(sensor!=sp.getStringSet("StreamSensorSet", blankSet)){
		//if(sensorTemp.size()>0){ //
		if(!sensor.containsAll(sensorNew)){
			//new sensor required
			System.out.println("new sensor required");
			try {
				ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();

			} catch (ESException e) {
				Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
			}
			for(String s:sensorNew){
				if(!sp.getStringSet("StreamSensorSet", blankSet).contains(s)){
					//found new sensor to be added
					sensorIds.add(aps.getSensorIdByName(s));
					System.out.println("sensor id: "+aps.getSensorIdByName(s));
				}					
			}
			try {
				System.out.println("All sensor Ids: "+sensorIds);
				ContinuousStreamSensing.getInstance(context,sensorIds).startSensing();
			} catch (ESException e) {
				Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
			}
			ed.putStringSet("StreamSensorSet", sensorNew);
			//			ed.putStringSet("StreamConfigurationSet", config);
			ed.commit();
		}
		else{
			System.out.println("StreamSensorSet is same as before, no new sensors required.");	
			for(String s: sp.getStringSet("StreamSensorSet", blankSet))
				System.out.println("StreamSensorSet: "+s);	
			for(String s: sensor)
				System.out.println("Sensor: "+s);
		}

		if(config==null)
			config=new HashSet<String>();
		for(Map.Entry<String, Set<String>> c: streamConfigs.entrySet()){
			config.add(c.getKey());
		}
		ed.putStringSet("StreamConfigurationSet", config);
		ed.commit();
	}

	//This method removes unused elements to the Stream configuration and Stream sensor set.
	//It will also un-subscribe the unused sensors
	private static void removeAndUnsubscribeStreamConfig(Context context, Map<String, Set<String>> streamConfigs){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		Editor ed=sp.edit();
		Set<String> config=new HashSet<String>();
		Set<String> sensor=new HashSet<String>();
		Set<String> blankSet=new HashSet<String>();
		config=sp.getStringSet("StreamConfigurationSet", blankSet);
		sensor=sp.getStringSet("StreamSensorSet", blankSet);
		for(String c:config){
			if(!streamConfigs.containsKey(c)){
				//removed element found
				Log.i("SNnMB", "CONFIG TO BE REMOVED FOUND"+c);
				config.remove(c);
			}
		}
		Set<String> sensorNew=new HashSet<String>();
		for(Map.Entry<String, Set<String>> e: streamConfigs.entrySet()){
			for(String s:e.getValue())
				sensorNew.add(Modality.valueOf(s).getSensorName());
		}
		Log.i("SNnMB", "New sensors: "+sensorNew);
		//check for new sensors and subscribe 
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		AllPullSensors aps=new AllPullSensors(context);
		for(String sensor_name:sp.getStringSet("StreamSensorSet", blankSet)){
			sensorIds.add(aps.getSensorIdByName(sensor_name));
		}
		try {
			Log.i("SNnMB", "Stop sensing from: "+sensorIds);
			ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();
		} catch (ESException e) {
			Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
		}
		if(!sensorNew.equals(sensor)) {
			//sensor to be removed
			for(String s:sensor){
				if(!sensorNew.contains(s)){
					//found sensor
					sensorIds.remove(sensorIds.indexOf(aps.getSensorIdByName(s)));
				}					
			}
			try {
				Log.i("SNnMB", "Start sensing from: "+sensorIds);
				ContinuousStreamSensing.getInstance(context,sensorIds).startSensing();
			} catch (ESException e) {
				Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
			}
		}		
		ed.putStringSet("StreamSensorSet", sensorNew);
		ed.putStringSet("StreamConfigurationSet", config);
		ed.commit();
	}
}
