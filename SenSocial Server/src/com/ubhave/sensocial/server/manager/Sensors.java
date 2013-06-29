package com.ubhave.sensocial.server.manager;


public class Sensors {

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_WIFI = 5010;
	public final static int SENSOR_TYPE_TIME=1111;
	
	
	public static String getSensorNameById(int id){
		String sensor=null;
		if(id==SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		else if(id==SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		else if(id==SENSOR_TYPE_LOCATION) sensor="location";
		else if(id==SENSOR_TYPE_MICROPHONE) sensor="microphone";
		else if(id==SENSOR_TYPE_WIFI) sensor="wifi";	
		else if(id==SENSOR_TYPE_TIME) sensor="time";
		return sensor;
	}

	public static int getSensorIdByName(String name){
		int sensor=0;
		if(name.equalsIgnoreCase("accelerometer")) sensor=SENSOR_TYPE_ACCELEROMETER;
		else if(name.equalsIgnoreCase("bluetooth")) sensor=SENSOR_TYPE_BLUETOOTH;
		else if(name.equalsIgnoreCase("location")) sensor=SENSOR_TYPE_LOCATION;
		else if(name.equalsIgnoreCase("microphone")) sensor=SENSOR_TYPE_MICROPHONE;
		else if(name.equalsIgnoreCase("wifi")) sensor=SENSOR_TYPE_WIFI;	
		else if(name.equalsIgnoreCase("time")) sensor=SENSOR_TYPE_TIME;
		return sensor;
	}
}
