package com.ubhave.sensocial.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
		sp=getSharedPreferences("SNnMB", 0);
		BROKER_URL= "tcp://"+sp.getString("mqtt", "")+":1883";
		clientId=sp.getString("uuid", "null");
		connectIt();
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

	/**
	 * Method to connect to MQTT broker.
	 */
	public void connectIt(){
		try {
			mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
			mqttClient.setCallback(new PushCallback(this, clientId));
			mqttClient.connect();
			mqttClient.subscribe(TOPIC);
			Log.d(TAG, "MQTT Connected");
		} catch (MqttException e) {
			Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(TAG,e.toString());
		}
	}

	/**
	 * We are not using this method.
	 * It was used to check if client is connect to MQTT broker.
	 * If not then it connects it again.
	 */
	public void isConnected(){
		th= new Thread(){
			public void run(){
				try{
					sleep(120000);
					while(true){
						if(!mqttClient.isConnected()){
							connectIt();
						}
						//waits for 2mins
						wait(120000);
					}

				} catch (InterruptedException e) {
					Log.e(TAG,e.toString());
				}
			}
		};
		th.start();
	}
}
