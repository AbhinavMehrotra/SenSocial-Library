package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import com.ubhave.sensocial.filters.SensorConfiguration;

import android.content.Context;
import android.content.SharedPreferences;

public class AllPullSensors {

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_WIFI = 5010;
	SharedPreferences sp;
	private SensorConfiguration SC;
	
	public AllPullSensors(Context context){
		sp=context.getSharedPreferences("snmbData",0);
		SC=new SensorConfiguration(context);
	}
	
	/**
	 * This method assigns the ids of all pull sensors cofigured by the user in an array and return it
	 * @return int[] Array of sensor ids
	 */
	public ArrayList<Integer> getIds(){
		return SC.getSubsribedSensors();
	}
	
	public String getSensorNameById(int id){
		String sensor=null;
		if(id==SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		if(id==SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		if(id==SENSOR_TYPE_LOCATION) sensor="location";
		if(id==SENSOR_TYPE_MICROPHONE) sensor="microphone";
		if(id==SENSOR_TYPE_WIFI) sensor="wifi";		
		return sensor;
	}
	
	public int getSensorIdByName(String sensor){
		int id=0;
		if(sensor.equalsIgnoreCase("accelerometer")) id = SENSOR_TYPE_ACCELEROMETER;
		else if(sensor.equalsIgnoreCase("bluetooth")) id = SENSOR_TYPE_BLUETOOTH;
		else if(sensor.equalsIgnoreCase("location")) id = SENSOR_TYPE_LOCATION;
		else if(sensor.equalsIgnoreCase("microphone")) id = SENSOR_TYPE_MICROPHONE;
		else if(sensor.equalsIgnoreCase("wifi")) id = SENSOR_TYPE_WIFI;
		return id;
	}

}
