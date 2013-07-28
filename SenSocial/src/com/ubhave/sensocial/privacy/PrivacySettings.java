package com.ubhave.sensocial.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ubhave.sensocial.filters.ConfigurationHandler;

public class PrivacySettings {


	public static Boolean enableSensing(Context context, String ppdSensorName, String ppdLocation, String ppdDataType){
		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
		if(sp.getBoolean("accelerometerenabled", false)){
			return false;
		}
		else{
			Editor ed=sp.edit();
			ed.putBoolean("accelerometerenabled", true);
			ed.commit();
		}		
		
		PPDGenerator.enableSensing(ppdSensorName, ppdLocation, ppdDataType);
		//resume all streams relevant to sensor for the given location
		ConfigurationHandler.run(context);	
		return true;
	}
	

	public static Boolean disableSensing(Context context, String ppdSensorName, String ppdLocation){
		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
		if(!sp.getBoolean("accelerometerenabled", true)){
			return false;
		}
		else{
			Editor ed=sp.edit();
			ed.putBoolean("accelerometerenabled", false);
			ed.commit();
		}
		
		PPDGenerator.disableSensing(ppdSensorName, ppdLocation);
		//stop all streams relevant to sensor for the given location
		ConfigurationHandler.run(context);
		return true;
	}

}

