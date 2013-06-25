package com.ubhave.sensocial.privacy;

import android.content.Context;

import com.ubhave.sensocial.filters.ConfigurationHandler;

public class PrivacySettings {


	public static void startSensing(Context context, String ppdSensorName, String ppdLocation, String ppdDataType){
		PPDGenerator.startSensing(ppdSensorName, ppdLocation, ppdDataType);
		//resume all streams relevant to sensor for the given location
		ConfigurationHandler.run(context);		
	}
	

	public static void stopSensing(Context context, String ppdSensorName, String ppdLocation){
		PPDGenerator.stopSensing(ppdSensorName, ppdLocation);
		//stop all streams relevant to sensor for the given location
		ConfigurationHandler.run(context);
	}

}

