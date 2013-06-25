package com.ubhave.sensocial.server.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

public class MQTTManager {

	private final String TAG = "SNnMB";
	private final static String BROKER_URL="tcp://broker.mqttdashboard.com:1883";
//	private final static String BROKER_URL="tcp://mqtt.cs.bham.ac.uk:1883";
	private MqttClient mqttClient;
	private String clientId;
	private String topic;
	private int keepAliveInterval=60*5;
	private MqttConnectOptions opt;
	
	public MQTTManager(String deviceId) throws MqttException {
		clientId="SensocialServer";
		topic="topic"+deviceId;
		opt=new MqttConnectOptions();
		System.out.println("topic: "+topic);
		opt.setKeepAliveInterval(keepAliveInterval);
		opt.setConnectionTimeout(10);
//		opt.setUserName("axm_mos");
//		opt.setPassword("Mos2013".toCharArray());
		mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
		mqttClient.setCallback(new MQTTCallback(BROKER_URL, clientId, topic));
	}
	
	public void connect(){
		try {
			mqttClient.connect(opt);
			System.out.print("Connected!");
		} catch (MqttException e) {
			System.out.print("Connection error!! "+e.toString());
			connect();
		}
	}
	
	public void subscribeToDevice(){
		try {
			mqttClient.subscribe(this.topic);
			System.out.print("Subscribed");
		} catch (MqttException e) {
			e.printStackTrace();
 		}
	}

	public void publishToDevice(String message) throws MqttPersistenceException, MqttException{
			MqttTopic topic=mqttClient.getTopic(this.topic);
			System.out.println("In publish: topic="+topic);
			MqttMessage msg= new MqttMessage(message.getBytes());
			topic.publish(msg);
			System.out.print("Published");
			mqttClient.disconnect();
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
			System.out.println("Connection lost!");			
		}

		public void deliveryComplete(MqttDeliveryToken arg0) {
			if(arg0==null)
				System.out.println("Message delivered");			
		}

		public void messageArrived(MqttTopic arg0, MqttMessage arg1)
				throws Exception {
			// argo-> device id
			//arg1 --> message
			//for testing
			System.out.print(arg0.toString()+arg1.toString());
			
		}
	}


}
