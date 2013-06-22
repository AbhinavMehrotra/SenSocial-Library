package com.ubhave.sensocial.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SenSocialBroadcastReceiver extends BroadcastReceiver {   

	/**
	 * It will start the service after the device reboot.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
		if(!sp.getString("mqtt","no").equals("no")){
			Intent myIntent = new Intent(context, com.ubhave.sensocial.http.HTTPService.class);
			context.startService(myIntent);
		}else{
			Intent myIntent = new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class);
			context.startService(myIntent);
		}
	}
}
