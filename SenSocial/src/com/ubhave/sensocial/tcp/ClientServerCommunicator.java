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
package com.ubhave.sensocial.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.privacy.PPDParser;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Provides the way to  access TCP client class.
 * Used for sending data to the server.
 */
public class ClientServerCommunicator {

	/**
	 * Registers user on the server by sending the basic information passed as arguments.
	 * @param context Application context
	 * @param userId User-id
	 * @param deviceId Device-id
	 * @param mac Bluetooth-MAC
	 */
	public static void registerUser(Context context, String userId, String deviceId, String mac){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", "registration");
			obj.put("deviceid", deviceId);
			obj.put("userid", userId);
			obj.put("bluetoothmac", mac);
			obj.put("ppd", PPDParser.getPPDJSONString());
			TCPClient.getInstance(sp.getString("serverip", ""),sp.getInt("serverport", 0)).startSending(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers user's Facebook account on the server by sending the basic information passed as arguments.
	 * @param context Application context
	 * @param name User-name
	 * @param userId User-id
	 * @param facebookName Facebook-name
	 * @param token Facebook-token
	 */
	public static void registerFacebook(Context context, String name, String userId, String facebookName, String token){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		JSONObject obj = new JSONObject();
		JSONObject facebook = new JSONObject();
		try {
			facebook.put("osnname", "facebook");
			facebook.put("name", name);
			facebook.put("userid", userId);
			facebook.put("username", facebookName);
			facebook.put("token", token);
			obj.put("name", "osn");
			obj.put("userid", userId);
			obj.put("deviceid", sp.getString("uuid", ""));
			obj.put("osn",facebook);
			if(sp.getString("userid", "null").equals("null")){
				System.out.println("ot able to update location because the user id is null");
			}
			else{
				TCPClient.getInstance(sp.getString("serverip", ""),sp.getInt("serverport", 0)).startSending(obj.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Registers user's Twitter account on the server by sending the basic information passed as arguments.
	 * @param context Application context
	 * @param name User-name
	 * @param userId User-id
	 * @param twitterName Twitter-name
	 * @param token Twitter-token
	 */
	public static void registerTwitter(Context context, String name, String userId, String twitterName, String token){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		JSONObject obj = new JSONObject();
		JSONObject twitter = new JSONObject();
		try {
			twitter.put("osnname", "twitter");
			twitter.put("name", name);
			twitter.put("userid", userId);
			twitter.put("username", twitterName);
			twitter.put("token", token);
			obj.put("name", "osn");
			obj.put("userid", userId);
			obj.put("deviceid", sp.getString("uuid", ""));
			obj.put("osn",twitter);
			if(sp.getString("userid", "null").equals("null")){
				System.out.println("Not able to update location because the user id is null");
			}
			else{
				TCPClient.getInstance(sp.getString("serverip", ""),sp.getInt("serverport", 0)).startSending(obj.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the location data to the server
	 * @param context Application context
	 * @param latitude location latitude
	 * @param longitude Location longitude
	 */
	public static void updateLocation(Context context, String latitude, String longitude){
		if(latitude.equalsIgnoreCase("unknown") || longitude.equalsIgnoreCase("unknown")){
			Log.e("SNnMB", "Location is unknown");		
			return;
		}
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		JSONObject obj = new JSONObject();
		JSONObject location = new JSONObject();
		try {
			location.put("lat", latitude);
			location.put("lon", longitude);
			obj.put("userid", sp.getString("userid", "null"));
			obj.put("name", "location");
			obj.put("deviceid", sp.getString("deviceid", ""));
			obj.put("location",location);
			if(sp.getString("userid", "null").equals("null")){
				System.out.println("Not able to update location because the user id is null");
			}
			else{
				TCPClient.getInstance(sp.getString("serverip", ""),sp.getInt("serverport", 0)).startSending(obj.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the streams data to the server.
	 * @param context Application context
	 * @param socialEventString Social-event in string format
	 */
	public static void sendStream(Context context, String socialEventString){
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", "stream");
			obj.put("userid", sp.getString("userid", "null"));
			obj.put("deviceid", sp.getString("deviceid", "null"));
			obj.put("stream",socialEventString);
			if(sp.getString("userid", "null").equals("null")){
				System.out.println("Not able to send the stream because the user id is null");
			}
			else{
				TCPClient.getInstance(sp.getString("serverip", ""),sp.getInt("serverport", 0)).startSending(obj.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
//	Registration:  { �name�: �registration�, "userid":<...>, �deviceid�:�<<unique_device_id>>�, "bluetoothmac":"<...>", "ppd":<...>}
//	OSN authentication:  { �name�: �osn�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �osn�:�<<json_object_for_specific_osn>>� }
//	json_object_for_specific_osn: {�osnname�:��, �name�:��,  �userid�:��, �username�:��, �token�:�� }
//	Location tracking:  { �name�: �location�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �location�:�{�lat�:0,�lon�:0}� }
//	Stream:  { �name�: �stream�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �stream�:�social_event�}
}
