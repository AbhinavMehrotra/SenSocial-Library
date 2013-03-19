package com.sensocial.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SenSocialService {

	/**
	 * Method to start the service. <br/>
	 * If the MQTT server IP has been set then it will start the MQTT service
	 * else it starts HTPP service.
	 * @param context
	 */
	public static void startService(Context context) {
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		if(!sp.getString("mqtt","no").equals("no")){
			Intent myIntent = new Intent(context, com.sensocial.http.HTTPService.class);
			context.startService(myIntent);
		}else{
			Intent myIntent = new Intent(context, com.sensocial.mqtt.MQTTService.class);
			context.startService(myIntent);
		}
	}

}
