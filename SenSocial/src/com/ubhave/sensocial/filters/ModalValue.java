package com.ubhave.sensocial.filters;

import com.ubhave.sensocial.manager.Location;


public class ModalValue {
	public static String moving ="moving";
	public static String not_moving="not_moving";
	public static String running="running";
	public static String walking="walking";
	public static String sitting="sitting";
	public static String talking="talking";
	public static String silent="silent";
	public static String active="active";

	public static String isWithinLocationRange(Location location, int rangeInMiles){
		String str="latitude_"+location.getLatitude()+"_longitude_"+location.getLongitude()+"_range_"+rangeInMiles;
		return str;
	}
	
	public static String isWithinTimeRange(double startTime, double endTime){
		String str="between_"+startTime+"_and_"+endTime;
		return str;
	}
	
	public static String isWithFrequency(int seconds){
		String str="frequency_"+seconds;
		return str;
	}
	
	public static String isWithNeigbourDevice(String bluetooth_MAC_or_Name){
		String str="neighbour_"+bluetooth_MAC_or_Name;
		return str;
	}

}
