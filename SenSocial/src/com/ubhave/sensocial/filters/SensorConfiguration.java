package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.Set;

import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.sensormanager.AllPullSensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SensorConfiguration {
	private SharedPreferences sp;
	private Context context;

	/**
	 * Constructor.
	 * @param context Application Context
	 */
	public SensorConfiguration(Context context){
		this.context=context;
		sp=context.getSharedPreferences("snmbData",0);
	}
	
//	<<These methods are removed as now sensor configuration is automated. So now we have only one method: subscribeSensors()>>
//	/**
//	 * Method to add accelerometer in the list of configured sensors. <br/>
//	 * If you  wish to get data for this sensor then set it as TRUE.
//	 * @param accelerometer Boolean
//	 */
//	public void setAccelerometer(Boolean accelerometer) {
//		Editor ed=sp.edit();
//		ed.putBoolean("accelerometer", accelerometer);
//		ed.commit();
//	}
//	
//	/**
//	 * Method to add accelerometer in the list of configured sensors. <br/>
//	 * If you  wish to get data for this sensor then set it as TRUE.
//	 * @param bluetooth Boolean
//	 */
//	public void setBluetooth(Boolean bluetooth) {
//		Editor ed=sp.edit();
//		ed.putBoolean("bluetooth", bluetooth);
//		ed.commit();
//	}
//	
//	/**
//	 * Method to add accelerometer in the list of configured sensors. <br/>
//	 * If you  wish to get data for this sensor then set it as TRUE.
//	 * @param wifi Boolean
//	 */
//	public void setWifi(Boolean wifi) {
//		Editor ed=sp.edit();
//		ed.putBoolean("wifi", wifi);
//		ed.commit();
//	}
//	
//	/**
//	 * Method to add accelerometer in the list of configured sensors. <br/>
//	 * If you  wish to get data for this sensor then set it as TRUE.
//	 * @param location Boolean
//	 */
//	public void setLocation(Boolean location) {
//		Editor ed=sp.edit();
//		ed.putBoolean("location", location);
//		ed.commit();
//	}
//	
//	/**
//	 * Method to add accelerometer in the list of configured sensors. <br/>
//	 * If you  wish to get data for this sensor then set it as TRUE.
//	 * @param microphone Boolean
//	 */
//	public void setMicrophone(Boolean microphone) {
//		Editor ed=sp.edit();
//		ed.putBoolean("microphone", microphone);
//		ed.commit();
//	}
	
	/**
	 * Method to subscribe sensors
	 * @param sensors Set<String> of all sensor names which needs to be subscribed
	 */
	protected void subscribeSensors(Set<String> sensors){
		unsubscribeAllSensors(); 
		Editor ed=sp.edit();
		for(String sensor:sensors){
			ed.putBoolean(sensor, true);
		}
		ed.commit();
	}
	
	private void unsubscribeAllSensors(){
		Editor ed=sp.edit();
		ed.putBoolean("accelerometer", false);
		ed.putBoolean("bluetooth", false);
		ed.putBoolean("wifi", false);
		ed.putBoolean("location", false);
		ed.putBoolean("microphone", false);
	}
	
	/**
	 * Method to get the list of sensors ids which have been set as true.
	 * @return sensorIds ArrayList<Integer> 
	 */
	public ArrayList<Integer> getSubsribedSensors(){		
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		if(sp.getBoolean("accelerometer", false)) sensorIds.add(AllPullSensors.SENSOR_TYPE_ACCELEROMETER);
		if(sp.getBoolean("bluetooth", false)) sensorIds.add(AllPullSensors.SENSOR_TYPE_BLUETOOTH);
		if(sp.getBoolean("wifi", false)) sensorIds.add(AllPullSensors.SENSOR_TYPE_WIFI);
		if(sp.getBoolean("location", false)) sensorIds.add(AllPullSensors.SENSOR_TYPE_LOCATION);
		if(sp.getBoolean("microphone", false)) sensorIds.add(AllPullSensors.SENSOR_TYPE_MICROPHONE);		
		return sensorIds;
	}
	
}
 
