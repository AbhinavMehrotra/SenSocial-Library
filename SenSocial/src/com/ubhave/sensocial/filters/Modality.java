package com.ubhave.sensocial.filters;

import com.ubhave.sensocial.sensormanager.AllPullSensors;

public enum Modality {
	NoCodition("all","NA"),
	act1("standing",getSensorNameById(AllPullSensors.SENSOR_TYPE_ACCELEROMETER)),
	act2("sitting",getSensorNameById(AllPullSensors.SENSOR_TYPE_ACCELEROMETER)),
	logicalOR("LogicalOR","");
	
	private final String activity;
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
	
	private static String getSensorNameById(int id){
		String sensor=null;
		if(id==AllPullSensors.SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		else if(id==AllPullSensors.SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		else if(id==AllPullSensors.SENSOR_TYPE_LOCATION) sensor="location";
		else if(id==AllPullSensors.SENSOR_TYPE_MICROPHONE) sensor="microphone";
		else if(id==AllPullSensors.SENSOR_TYPE_WIFI) sensor="wifi";		
		return sensor;
	}
}
