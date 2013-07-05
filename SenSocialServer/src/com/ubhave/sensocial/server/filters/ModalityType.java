package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.manager.Sensors;

public class ModalityType {
	public final static String null_condition="null";
	public final static String physical_activity="physical_activity";
	public final static String noise="communication";
	public final static String location="location";
	public final static String neighbour="neighbour";
	public final static String time="time";
	public final static String facebook_activity="facebook_activity";
	public final static String twitter_activity="twitter_activity";
	public final static String facebook_friends_location="facebook_friends_location";
	public final static String facebook_friends_around="facebook_friends_around";  
	public final static String twitter_friends_location="twitter_friends_location";
	public final static String twitter_friends_around="twitter_friends_around";  
	
	

	public static int getSensorId(String modalityType){
		int id=0;
		if(modalityType.equalsIgnoreCase("physical_activity")) id=Sensors.SENSOR_TYPE_ACCELEROMETER;
		if(modalityType.equalsIgnoreCase("noise")) id= Sensors.SENSOR_TYPE_MICROPHONE;
		if(modalityType.equalsIgnoreCase("location")) id= Sensors.SENSOR_TYPE_LOCATION;
		if(modalityType.equalsIgnoreCase("neighnour")) id= Sensors.SENSOR_TYPE_BLUETOOTH;
		return id;
	}
	
	public static String getSensorName(String modalityType){
		String str="";
		if(modalityType.equalsIgnoreCase("physical_activity")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER);
		if(modalityType.equalsIgnoreCase("noise")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_MICROPHONE);
		if(modalityType.equalsIgnoreCase("location")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_LOCATION);
		if(modalityType.equalsIgnoreCase("neighbour")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_BLUETOOTH);
		return str;
	}
	
}
