package com.ubhave.sensocial.filters;

import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;

public class ModalityType {
	public final static String null_condition="null";
	public final static String physical_activity="physical_activity";
	public final static String noise="noise";
	public final static String location="location";
	public final static String time="time";
	public final static String neighbour="neighbour";
	public final static String facebook_activity="facebook_activity";
	public final static String twitter_activity="twitter_activity"; 

	public static int getSensorId(String modalityType){
		int id=0;
		if(modalityType.equalsIgnoreCase("physical_activity")) id=AllPullSensors.SENSOR_TYPE_ACCELEROMETER;
		if(modalityType.equalsIgnoreCase("noise")) id= AllPullSensors.SENSOR_TYPE_MICROPHONE;
		if(modalityType.equalsIgnoreCase("location")) id= AllPullSensors.SENSOR_TYPE_LOCATION;
		if(modalityType.equalsIgnoreCase("neighnour")) id= AllPullSensors.SENSOR_TYPE_BLUETOOTH;
		return id;
	}
	
	public static String getSensorName(String modalityType){
		String str="";
		AllPullSensors aps=new AllPullSensors(SenSocialManager.getContext());
		if(modalityType.equalsIgnoreCase("physical_activity")) str=aps.getSensorNameById(AllPullSensors.SENSOR_TYPE_ACCELEROMETER);
		if(modalityType.equalsIgnoreCase("noise")) str=aps.getSensorNameById(AllPullSensors.SENSOR_TYPE_MICROPHONE);
		if(modalityType.equalsIgnoreCase("location")) str=aps.getSensorNameById(AllPullSensors.SENSOR_TYPE_LOCATION);
		if(modalityType.equalsIgnoreCase("neighbour")) str=aps.getSensorNameById(AllPullSensors.SENSOR_TYPE_BLUETOOTH);
		return str;
	}

}
