/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.User;

public class ModalValue {
	
	public static String moving ="moving";
	public static String not_moving="not_moving";
	public static String running="running";
	public static String walking="walking";
	public static String sitting="sitting";
	public static String talking="talking";
	public static String silent="silent";
	public static String active="active";


	

	public static String isWithUser(String userId){
		String str="neighbour_";
		for(Device d:UserRegistrar.getUserById(userId).getDevices()){
			str+=d.getBluetoothMAC();
		}
		return str;
	}
	
	public static String isWithNeigbourDevice(String bluetooth_MAC_or_Name){
		String str="neighbour_"+bluetooth_MAC_or_Name;
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
	
	public static String withinOSNFriendsLocationRange(double range){
		String str="range_"+range;
		return str;
	}

}
