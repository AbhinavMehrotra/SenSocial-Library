package com.ubhave.sensocial.server.filters;

import java.awt.GraphicsDevice.WindowTranslucency;

import com.ubhave.sensocial.server.manager.Sensors;


public enum Modality {
	NoCodition("all","NA"),
	Moving("Moving",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER)),
	Not_Moving("Not_Moving",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER)),
	Silent("Silent",getSensorNameById(Sensors.SENSOR_TYPE_MICROPHONE)),
	Talking("Talking",getSensorNameById(Sensors.SENSOR_TYPE_MICROPHONE)),
	Within_1_mile_of_UoB("Within_1_mile_of_lat_52.449_lon_-1.925",getSensorNameById(Sensors.SENSOR_TYPE_LOCATION)),
	//With_User_("friends_within_1_mile",getSensorNameById(Sensors.SENSOR_TYPE_LOCATION)),	
	logicalOR("LogicalOR","");
	
	private String activity;
	private final String sensor;
	
	Modality(String activityName, String sensorName){
		activity=activityName;
		sensor=sensorName;
	}
	
	public String getActivityName(){
		return activity;
	}
	
	public String getSensorName(){
		return sensor;
	}
	
	public static Modality getModaalityByName(String name){
		switch (name.toLowerCase()) {
		case "all":
			return NoCodition;
		case "moving":
			return Moving;
		case "not_moving":
			return Not_Moving;
		case "silent":
			return Silent;
		case "talking":
			return Talking;
		case "within_1_mile_of_uob":
			return Within_1_mile_of_UoB;
		case "logicalor":
			return logicalOR;
		}
		return null;
	}
	
//	public void setDevice(Device device){
//		if(activity=="within_1_mile_of")
//			activity="within_1_mile_of_"+device.getDeviceId();
//	}
	
	private static String getSensorNameById(int id){
		String sensor=null;
		if(id==Sensors.SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		else if(id==Sensors.SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		else if(id==Sensors.SENSOR_TYPE_LOCATION) sensor="location";
		else if(id==Sensors.SENSOR_TYPE_MICROPHONE) sensor="microphone";
		else if(id==Sensors.SENSOR_TYPE_WIFI) sensor="wifi";
		else if(id==Sensors.SENSOR_TYPE_TIME) sensor="time";		
		return sensor;
	}
}
