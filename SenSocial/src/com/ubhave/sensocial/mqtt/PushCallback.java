package com.ubhave.sensocial.mqtt;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.ubhave.sensocial.listener.SocialNetworkListenerManager;
import com.ubhave.sensocial.listener.SocialNetworkListner;
import com.ubhave.sensocial.sensormanager.StartPullSensors;

public class PushCallback implements MqttCallback {

	final private String TAG = "SNnMB";
	private ContextWrapper context;
	private String BROKER_URL;
	public String clientId;                  
	public static final String TOPIC = "sensocial";
	private MqttClient mqttClient;
	private SharedPreferences sp;
	private ArrayList<SocialNetworkListner> listeners = new ArrayList<SocialNetworkListner>();

	public PushCallback(ContextWrapper context, String clientId) {
		this.context = context;
		sp=context.getSharedPreferences("SNnMB", 0);
		BROKER_URL= "tcp://"+sp.getString("mqtt", "")+":1883";
		this.clientId=clientId;
	}

	/**
	 * This method is called when the connection is lost. 
	 * It will try to connect again.
	 */
	public void connectionLost(Throwable cause) {
		Log.d(TAG,"Connection lost. Reason: "+ cause);
		try {
			mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
			mqttClient.setCallback(new PushCallback(context, clientId));
			mqttClient.connect();
			mqttClient.subscribe(TOPIC);
			Log.d(TAG, "MQTT Connected");
		} catch (MqttException e) {
			Toast.makeText(context, "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
			Log.e(TAG,e.toString());
		}
	}

	/**
	 * This method is called when the message is arrived. 
	 * It starts sensing process.
	 */
	public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
//		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
//		if(sp.getBoolean("sensing", false)==false){
//			Log.d(TAG, "Found a new update");
//			Editor ed=sp.edit();
//			ed.putBoolean("sensing", true);
//			ed.commit();
//			
//		}
//		SocialNetworkListenerManager.newUpdateArrived(context,message.toString());	
		Log.d(TAG, "Received a notification");
		new NotificationParser((Context)context).takeAction(message.toString());
	}

	/**
	 * We do not need this because we do not publish  
	 */
	public void deliveryComplete(MqttDeliveryToken token) {}


	/**
	 * Method to add an instance of  listener (SocialNetworkPostListner)
	 * @param listener SocialNetworkPostListner object
	 */
	
}
