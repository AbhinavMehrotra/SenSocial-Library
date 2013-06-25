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

	public static String isWithUser(String userId){
		String str="with_user_";
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
		String str="within_location_"+location.getLatitude()+"_"+location.getLongitude()+"_range_"+rangeInMiles;
		return str;
	}

}
