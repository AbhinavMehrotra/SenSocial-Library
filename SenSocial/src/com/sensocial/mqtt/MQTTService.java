package com.sensocial.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import com.sensocial.configuration.ServerConfiguration;

public class MQTTService extends Service {

	final private String TAG = "SNnMB";
	public static String BROKER_URL;
	public static String clientId; 
	public static final String TOPIC = "sensocial";
	private MqttClient mqttClient;
	Thread th;
	private static final int START_STICKY = 1;

	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ServerConfiguration sc= new ServerConfiguration(getApplicationContext());
		BROKER_URL= "tcp://"+sc.getMQTTServerIP()+":1883";
		clientId=Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
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
	 * To check if client is connect to MQTT broker.
	 * I not then it connects it again.
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
