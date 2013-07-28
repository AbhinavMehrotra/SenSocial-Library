package com.ubhave.sensocial.mqtt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.exceptions.ServerException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.SensorUtils;
import com.ubhave.sensocial.sensormanager.ContinuousStreamSensing;
import com.ubhave.sensormanager.ESException;

public class MQTTServiceRestartHandler {
	//To hide 
	protected MQTTServiceRestartHandler() {}

	private static SharedPreferences sp;
	private static Editor ed;

	/**
	 * This method will restart all the sensing again according to the filter xml.
	 * All configs saved in SharedPreferences will be deleted and set again.
	 * @param context
	 */
	protected static void run(Context context){
		sp=context.getSharedPreferences("SSDATA", 0);
		Boolean location_tracter=sp.getBoolean("locationtrackerrequired", false);
		Boolean server_client=sp.getBoolean("serverclient", false);
		try {
			SenSocialManager.getSenSocialManager(context, server_client, location_tracter);
		} catch (ServerException e1) {
			e1.printStackTrace();
			SenSocialManager.setContext(context);
		}	
		
		ed=sp.edit();

		//try to stop all sensing
		Set<String> sensors=new HashSet<String>();
		sensors=sp.getStringSet("StreamSensorSet", sensors);
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		SensorUtils aps=new SensorUtils(context);
		for(String sensor:sensors){
			sensorIds.add(aps.getSensorIdByName(sensor));
		}

		ArrayList<Integer> ids=new ArrayList<Integer>();
		for(int id: sensorIds){
			ids.add(id);
			try{
				Log.i("SNnMB", "Trying to stop sensing for: "+ aps.getSensorNameById(id));
				ContinuousStreamSensing.getInstance(context,ids).stopSensing();
			}
			catch(ESException e){
				Log.e("SNnMB", "Not able to stop sensing for: "+ aps.getSensorNameById(id) +"\n"+ e.toString());
			}
			finally{
				ids.clear();
			}
		}

		//delete all subscription ids
		for(int id:sensorIds){
			ed.remove(id + "_subId");
			ed.commit();
		}

		//delete condition sets for all configs
		Set<String> blank = new HashSet<String>();
		for(String config: sp.getStringSet("", blank)){
			ed.remove(config);
			ed.commit();
		}

		ed.remove("ConfigurationSet");
		ed.remove("SensorSet");
		ed.remove("OSNConfigurationSet");
		ed.remove("OSNSensorSet");
		ed.remove("StreamConfigurationSet");
		ed.remove("StreamSensorSet");
		ed.commit();
		
//		These statements will do the same I did above
//		ed.clear();
//		ed.commit();
		
		//Reset all configurations
		ConfigurationHandler.run(context);		
	}
}
