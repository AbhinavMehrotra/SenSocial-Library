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

}
