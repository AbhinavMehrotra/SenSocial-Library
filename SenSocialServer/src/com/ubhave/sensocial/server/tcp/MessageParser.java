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
package com.ubhave.sensocial.server.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.osn.FacebookEventNotifier;

/**
 * Messagge parser for message received from the clients via TCP socket
 */
public class MessageParser {
	
	/**
	 * Parses the message received via TCP socket and takes actions to handle it
	 * @param message
	 */
	protected static void run(String message){
		try {
			JSONObject obj= new JSONObject(message);
			String name= obj.getString("name");
			String deviceId=null, userId=null;
			if(!name.equalsIgnoreCase("facebook") && !name.equalsIgnoreCase("twitter")){
				deviceId= obj.getString("deviceid");
				userId=obj.getString("userid");
			}
			switch (getSwitchValue(name)) {
			case 1:
				UserRegistrar.registerUser(obj.getString("userid"),deviceId, obj.getString("bluetoothmac"), obj.getString("ppd"));
				break;
			case 2:
				JSONObject osn=obj.getJSONObject("osn");
				if(osn.getString("osnname").equalsIgnoreCase("facebook")){
					UserRegistrar.registerFacebook(userId, deviceId, osn.getString("name"), osn.getString("username"),osn.getString("token"));
				}
				else{
					UserRegistrar.registerTwitter(userId, deviceId, osn.getString("name"), osn.getString("username"),osn.getString("token"));					
				}
				break;
			case 3:
				JSONObject location=obj.getJSONObject("location");
				UserRegistrar.updateLocation(userId, deviceId, location.getString("lat"), location.getString("lon"));
				break;
			case 4:
				System.out.println("Stream recieved in Message Parser: "+obj.getString("stream"));
				StreamReceiver.onReceive(userId, deviceId, obj.getString("stream"));
				break;
			case 5:
				System.out.println("Stream recieved in Message Parser: "+ obj.getJSONObject("facebook").toString());
				FacebookEventNotifier.parseJSON(obj.getJSONObject("facebook"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private static int getSwitchValue(String name){
		if(name.equalsIgnoreCase("registration")) return 1;
		if(name.equalsIgnoreCase("osn")) return 2;
		if(name.equalsIgnoreCase("location")) return 3;
		if(name.equalsIgnoreCase("stream")) return 4;
		if(name.equalsIgnoreCase("facebook")) return 5;		
		return 0;
	}
	
	
/*
		Registration:  { �name�: �registration�, "userid":<...>, �deviceid�:�<<unique_device_id>>�, "bluetoothmac":"<...>"}
		OSN authentication:  { �name�: �osn�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �osn�:�<<json_object_for_specific_osn>>� }
		json_object_for_specific_osn: {�osnname�:��, �name�:��,  �userid�:��, �username�:��, �token�:�� }
		Location tracking:  { �name�: �location�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �location�:�{�lat�:0,�lon�:0}� }
		Stream:  { �name�: �stream�, "userid":<...>,  �deviceid�:�<<unique_device_id>>�, �stream�:�social_event�}
*/
	
}
