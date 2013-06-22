package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.sensormanager.AllPullSensors;

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
			if(!sensor.equalsIgnoreCase("all")){  //not to subscribe any sensor if the condition is all 
				ed.putBoolean(sensor, true);				
			}
		}
		ed.commit();
	}
	
	/**
	 * Method to unsubscribe all sensors.
	 * This is called before setting a new filter.
	 */
	private void unsubscribeAllSensors(){
		Editor ed=sp.edit();
		ed.putBoolean("accelerometer", false);
		ed.putBoolean("bluetooth", false);
		ed.putBoolean("wifi", false);
		ed.putBoolean("location", false);
		ed.putBoolean("microphone", false);
	}
	
	/**
	 * Method to subscribe all sensors.
	 * This is called before setting a new filter.
	 */
	private void subscribeAllSensors(){
		Editor ed=sp.edit();
		ed.putBoolean("accelerometer", true);
		ed.putBoolean("bluetooth", true);
		ed.putBoolean("wifi", true);
		ed.putBoolean("location", true);
		ed.putBoolean("microphone", true);
	}
		
	/**
	 * Method to unsubscribe a configuration 
	 * @param configName String (Configuration Name)
	 */
	public void unsubscribeConfiguration(String configName){
		Set<String> conditions= new HashSet<String>();
		Set<String> sensors= new HashSet<String>();
		Editor ed=sp.edit();
		conditions= sp.getStringSet(configName, null);
		if(conditions!=null){
			for(String condition:conditions){
				ed.remove(condition);
			}
		}
		ed.remove(configName);
		ed.commit();
		//unsubscribe and then subscribe all the sensors for remaining configurations.
		unsubscribeAllSensors();
		for(String configuration:sp.getStringSet("FilterSet", null)){
			for(String condition:sp.getStringSet(configuration,null)){
				//not subscribing the sensor for required data, as after the condition 
				//is satisfied we do oneoff sensing for this sensor
				if(condition.startsWith("RD")){ 
					continue;
				}
				sensors.clear();
				for(String activity:sp.getStringSet(condition, null)){
					sensors.add(getSensorNameForActivity(activity));
				}
				subscribeSensors(sensors);
			}
		}
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
	
	/**
	 * Method to find the sensor associated with the activity
	 * @param activity name (String)
	 * @return sensor name (String)
	 */
	protected static String getSensorNameForActivity(String activity){
		String sensorName = null;
		for( Modality activities:Modality.values()){
			if(activities.getActivityName().equalsIgnoreCase(activity)){
				sensorName=activities.getSensorName();
				break;
			}
		}
		return sensorName;		
	}
	
}
 
