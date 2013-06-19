package com.ubhave.sensocial.server.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

public class MQTTManager {

	private final String TAG = "SNnMB";
	private final static String BROKER_URL="tcp://broker.mqttdashboard.com:1883";//"tcp://localhost:1883";
	private MqttClient mqttClient;
	private String deviceId;
	private String clientId;
	private final String topic="SenSocial";
	private int keepAliveInterval=60*5;
	private MqttConnectOptions opt;
	
	public MQTTManager(String deviceId) throws MqttException {
		this.deviceId=deviceId;
		opt=new MqttConnectOptions();
		opt.setKeepAliveInterval(keepAliveInterval);
		mqttClient = new MqttClient(BROKER_URL, "SenSocialServer", new MemoryPersistence());
		mqttClient.setCallback(new MQTTCallback(BROKER_URL, deviceId, deviceId));
	}
	
	public void connect(){
		try {
			mqttClient.connect(opt);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void subscribeToDevice(){
		try {
			mqttClient.subscribe(this.deviceId);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void publishToDevice(String message){
		try {
			MqttTopic topic=mqttClient.getTopic(this.deviceId);
			MqttMessage msg= new MqttMessage(message.getBytes());
			topic.publish(msg);
		} catch (MqttException e) {
			e.printStackTrace();
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
			// argo-> device id
			//arg1 --> message
			System.out.print(arg0.toString()+arg1.toString());
		}


	}


}
