package com.ubhave.sensocial.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * SenSocialBroadcastReceiver class is a receiver for the reboot event.
 * This class is not required to be instantiated, but it should be included in the
 * manifest with tag of receiver for reboot.
 * <br>Add this code: <br> 
 * {@code <receiver }<br>&nbsp;{@code 
 *          android:name="com.sensocial.manager.SenSocialServiceReceiver" }<br>&nbsp;{@code 
 *           android:enabled="true" }<br>&nbsp;{@code 
 *           android:exported="false" }<br>&nbsp;{@code 
 *           android:label="BootListener" > }<br>&nbsp;{@code 
 *           <intent-filter> }<br>&nbsp;&nbsp;&nbsp;{@code 
 *               <action android:name="android.intent.action.BOOT_COMPLETED" /> }<br>&nbsp;&nbsp;&nbsp;{@code 
 *               <category android:name="android.intent.category.HOME" /> }<br>&nbsp;{@code 
 *           </intent-filter> }<br>{@code 
 *       </receiver -->
 * }
 */
public class SenSocialBroadcastReceiver extends BroadcastReceiver {   

	/**
	 * Starts the service on device reboot.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class);
		context.startService(myIntent);

	}
}
