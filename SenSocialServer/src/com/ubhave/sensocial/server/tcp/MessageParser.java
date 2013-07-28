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
		Registration:  { “name”: “registration”, "userid":<...>, “deviceid”:”<<unique_device_id>>”, "bluetoothmac":"<...>"}
		OSN authentication:  { “name”: “osn”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “osn”:”<<json_object_for_specific_osn>>” }
		json_object_for_specific_osn: {“osnname”:””, ”name”:””,  ”userid”:””, “username”:””, “token”:”” }
		Location tracking:  { “name”: “location”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “location”:”{“lat”:0,”lon”:0}” }
		Stream:  { “name”: “stream”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “stream”:”social_event”}
*/
	
}
