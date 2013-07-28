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
