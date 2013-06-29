package com.ubhave.sensocial.server.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.osn.FacebookEventNotifier;

public class MessageParser {
	protected static void run(String message){
		try {
			JSONObject obj= new JSONObject(message);
			String name= obj.getString("name");
			String deviceId=null, userId=null;
			deviceId= obj.getString("deviceid");
			userId=obj.getString("userid");

			switch (name) {
			case "registration":
				UserRegistrar.registerUser(obj.getString("userid"),deviceId, obj.getString("bluetoothmac"), obj.getString("ppd"));
				break;
			case "osn":
				JSONObject osn=obj.getJSONObject("osn");
				if(osn.getString("osnname").equalsIgnoreCase("facebook")){
					UserRegistrar.registerFacebook(userId, deviceId, osn.getString("name"), osn.getString("username"),osn.getString("token"));
				}
				else{
					UserRegistrar.registerTwitter(userId, deviceId, osn.getString("name"), osn.getString("username"),osn.getString("token"));					
				}
				break;
			case "location":
				JSONObject location=obj.getJSONObject("location");
				UserRegistrar.updateLocation(userId, deviceId, location.getString("lat"), location.getString("lon"));
				break;
			case "stream":
				StreamReceiver.onReceive(userId, deviceId, obj.getString("stream"));
				break;
			case "facebook":
				FacebookEventNotifier.parseJSON(obj.getJSONObject("facebook"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//	Registration:  { “name”: “registration”, "userid":<...>, “deviceid”:”<<unique_device_id>>”, "bluetoothmac":"<...>"}
	//	OSN authentication:  { “name”: “osn”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “osn”:”<<json_object_for_specific_osn>>” }
	//	json_object_for_specific_osn: {“osnname”:””, ”name”:””,  ”userid”:””, “username”:””, “token”:”” }
	//	Location tracking:  { “name”: “location”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “location”:”{“lat”:0,”lon”:0}” }
	//	Stream:  { “name”: “stream”, "userid":<...>,  “deviceid”:”<<unique_device_id>>”, “stream”:”social_event”}

}
