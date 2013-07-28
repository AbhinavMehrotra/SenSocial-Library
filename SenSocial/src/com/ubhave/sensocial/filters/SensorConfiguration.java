package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.SensorUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi")
public class SensorConfiguration {
	private SharedPreferences sp;
	private Context context;

	/**
	 * Constructor.
	 * @param context Application Context
	 */
	public SensorConfiguration(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA",0);
	}	

	/**
	 * Getter for the list of sensors ids which have been set as true.
	 * @return sensorIds ArrayList<Integer> 
	 */
	public ArrayList<Integer> getSubsribedSensors(){		
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		if(sp.getBoolean("accelerometer", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_ACCELEROMETER);
		if(sp.getBoolean("bluetooth", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_BLUETOOTH);
		if(sp.getBoolean("wifi", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_WIFI);
		if(sp.getBoolean("location", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_LOCATION);
		if(sp.getBoolean("microphone", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_MICROPHONE);		
		return sensorIds;
	}

	/**
	 * Returns the sensor associated with the activity
	 * @param activity name (String)
	 * @return sensor name (String)
	 */
	protected static String getSensorNameForConditions(String activity){
		String sensorName = null;
		SensorUtils aps=new SensorUtils(SenSocialManager.getContext());
		Condition c=new Condition(activity);
		return aps.getSensorNameById(ModalityType.getSensorId(c.getModalityType()));		
	}

}

