package com.ubhave.sensocial.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MQTTManager {

	private final String TAG = "SNnMB";
//	private static String BROKER_URL="tcp://broker.mqttdashboard.com:1883";
	private static String BROKER_URL;
	private MqttClient mqttClient;
	private String deviceId;
	private String clientId;
	private String topic;
	private int keepAliveInterval=60*5;
	private MqttConnectOptions opt;
	private Context context;
	
	public MQTTManager(Context context, String deviceId) throws MqttException {
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		this.BROKER_URL=sp.getString("mqqt_broker_url", "null");
		this.context=context;
		this.deviceId=deviceId;
		this.topic="topic"+deviceId;
//		this.topic="sensocial";
		Log.i(TAG, "device id is: "+deviceId);
		opt=new MqttConnectOptions();
//		opt.setUserName("axm_mos");
//		opt.setPassword("Mos2013".toCharArray());
		opt.setKeepAliveInterval(keepAliveInterval);
		opt.setConnectionTimeout(10);
		mqttClient = new MqttClient(BROKER_URL, deviceId, new MemoryPersistence());
//		mqttClient = new MqttClient(BROKER_URL, "client", new MemoryPersistence());
		mqttClient.setCallback(new MQTTCallback(BROKER_URL, deviceId, this.topic));
	}
	
	public void connect(){
		try {
			mqttClient.connect(opt);
		} catch (MqttException e) {
			Log.e(TAG, "Error while connecting to mqtt broker: "+e.toString());
			connect();
		}
	}
	
	public void subscribeToDevice(){
		try {
			mqttClient.subscribe(this.topic);
		} catch (MqttException e) {
			Log.e(TAG, "Error while subscribing to mqtt broker: "+e.toString());
		}
	}

	public void publishToDevice(String message){
		try {
			MqttTopic mtopic=mqttClient.getTopic(this.topic);
			Log.i(TAG, "Published to : "+mtopic);
			MqttMessage msg= new MqttMessage(message.getBytes());
			mtopic.publish(msg);
		} catch (MqttException e) {
			Log.e(TAG, "Error while publishing to mqtt broker: "+e.toString());
		}
	}
	
	
	//inner class for callback
	public class MQTTCallback implements MqttCallback{

		final private String TAG = "SNnMB";
		private String BROKER_URL;
		private String deviceId;                  
		private String TOPIC;
		private MqttClient mqttClient;

		public MQTTCallback(String BROKER_URL, String deviceId, String TOPIC) {
			this.BROKER_URL= BROKER_URL;
			this.deviceId=deviceId;
			this.TOPIC=TOPIC;
		}
		
		public void connectionLost(Throwable arg0) {
			connect();			
		}

		public void deliveryComplete(MqttDeliveryToken arg0) {
			if(arg0==null)
				System.out.print("Message delivered");			
		}

		public void messageArrived(MqttTopic arg0, MqttMessage arg1)
				throws Exception {
			// argo-> device id		//arg1 --> message
			Log.i("SNnMB",arg0.toString()+":"+arg1.toString());
			new NotificationParser(context).takeAction(arg1.toString());
		}


	}


}
