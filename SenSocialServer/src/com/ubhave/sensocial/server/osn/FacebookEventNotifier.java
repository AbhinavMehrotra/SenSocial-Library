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
package com.ubhave.sensocial.server.osn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;

public class FacebookEventNotifier{

	/**
	 * Parses the JSON String sent by the Facebook server as a real-time trigger, and takes appropriate actions.
	 * @param json JSONString sent by the Facebook server
	 */
	public static void parseJSON(JSONObject json){
		try {
			JSONArray entries= json.getJSONArray("entry");
			JSONObject entry;
			String id,message;
			long time;
			JSONArray fields;
			//most of the time it will be single entry, but due to network issue facebook server may collect previous unsent ones
			//and send them all together
			for(int i=0; i<entries.length();i++){ 
				entry=entries.getJSONObject(i);
				id=entry.get("uid").toString();
				time=entry.getLong("time");
				fields=entry.getJSONArray("changed_fields");
				if(isInteresting(id)){
					for(int j=0;j<fields.length();j++){
						System.out.println("Entry "+ j+1 +": "+id+","+time+","+fields.getString(j));
						FacebookGetters fg=new FacebookGetters(id);
						message=fg.getUpdatedData(fields.getString(j), time);
						//check for friend list update
						if(message.contains("are now friends") || message.contains("is now friends with")){
							System.out.println("Update Facebook FriendList");
							UserRegistrar.updateFacebookFriendList(id);
						}
						else{
							System.out.println("Send Facebook update to client");
							MQTTClientNotifier.sendFacebookUpdate(id, message,time, fields.getString(j));
						}
					}
				}
				else{
					for(int j=0;j<fields.length();j++){
						System.out.println("Entry not interesting (user not registered)- id: "+id+", time: "+time+", "+fields.getString(j));					
					}
				}
			}
		} catch (JSONException e) {
			System.out.println("Error with parsing facebook updates: "+e.toString());
		}
	}

	/**
	 * Checks whether the Facebook-id is registered with any user
	 * @param uid (String) Facebook id
	 * @return Boolean
	 */
	private static Boolean isInteresting(String uid){
		return UserRegistrar.isFacebookIdPresent(uid);
	}

}
