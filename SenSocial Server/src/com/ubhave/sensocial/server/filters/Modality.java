package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.Sensors;


public enum Modality {
	NoCodition("all","NA"),
	standing("standing",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER)),
	sitting("sitting",getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER)),
	every_ten_minutes("every_ten_minutes",getSensorNameById(Sensors.SENSOR_TYPE_TIME)),
	every_thirty_minutes("every_thirty_minutes",getSensorNameById(Sensors.SENSOR_TYPE_TIME)),
	friends_within_1_mile("friends_within_1_mile",getSensorNameById(Sensors.SENSOR_TYPE_LOCATION)),
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
	
	public Modality getModaalityByName(String name){
		switch (name) {
		case "all":
			return NoCodition;
		case "standing":
			return standing;
		case "sitting":
			return sitting;
		case "every_ten_minutes":
			return every_ten_minutes;
		case "every_thirty_minutes":
			return every_thirty_minutes;
		case "friends_within_1_mile":
			return friends_within_1_mile;
		case "logicalOR":
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
