package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.User;

public class ModalValue {

	public static String running="moving";
	public static String walking="not_moving";
	public static String sitting="talking";
	public static String silent="silent";
	public static String active="active";
	

	public static String isWithUser(String userId){
		String str="neighbour_";
		for(Device d:UserRegistrar.getUserById(userId).getDevices()){
			str+=d.getBluetoothMAC();
		}
		return str;
	}
	
	public static String isWithUser(User user){
		String str="with_user_";
		for(Device d:user.getDevices()){
			str+=d.getBluetoothMAC();
		}
		return str;
	}


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
	
	public static String withinUserLocation(String userId, double range){
		String str="range_"+range+"_userid_"+userId;
		return str;
	}

}
