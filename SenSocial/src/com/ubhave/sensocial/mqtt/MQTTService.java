package com.ubhave.sensocial.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/**
 * Background service to keep the connection with MQQT broker
 */
public class MQTTService extends Service {

	final private String TAG = "SNnMB";
	public static String BROKER_URL;
	public static String clientId; 
	public static final String TOPIC = "sensocial";
	private MqttClient mqttClient;
	Thread th;
	private static final int START_STICKY = 1;
	private SharedPreferences sp;

	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sp=getSharedPreferences("SSDATA", 0);
		this.BROKER_URL=sp.getString("mqqt_broker_url", "null");
		Log.i(TAG, "MQTT service onStart");
		Log.i(TAG, BROKER_URL);
		clientId=sp.getString("deviceid", "null");
		Log.i(TAG, "client id: "+clientId);
		//connectIt();
		StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());
		//connectIt();
		try {
			MQTTManager m = new MQTTManager(getApplicationContext(), clientId);
			m.connect();
			m.publishToDevice("MQTT service started: "+clientId);
			m.subscribeDevice();
		} catch (MqttException e) {
			e.printStackTrace();
		}
        StrictMode.setThreadPolicy(old);
        if(sp.getBoolean("mqttservicestart", false)){
        	//service was closed by OS and restarted
        	MQTTServiceRestartHandler.run(getApplicationContext());
        }
        else{
        	//service started for the first time
        	Editor ed=sp.edit();
        	ed.putBoolean("mqttservicestart", true);
        	ed.commit();
        }
		return START_STICKY;
	}

	

	@Override
	public void onDestroy() {
		try {
			mqttClient.disconnect(0);
		} catch (MqttException e) {
			Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(TAG,e.toString());
		}
	}

}
