/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
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
	private final static String BROKER_URL="tcp://mqtt.cs.bham.ac.uk:1883";
	private MqttClient mqttClient;
	private String clientId;
	private String topic;
	private int keepAliveInterval=60*5;
	private MqttConnectOptions opt;
	
	/**
	 * Constructor
	 * @param deviceId (String) Device id
	 * @throws MqttException
	 */
	public MQTTManager(String deviceId) throws MqttException {
		clientId="SensocialServer";
		topic="topic"+deviceId;
		opt=new MqttConnectOptions();
		System.out.println("topic: "+topic);
		opt.setKeepAliveInterval(keepAliveInterval);
		opt.setConnectionTimeout(10);
		opt.setUserName("axm_mos");
		opt.setPassword("Mos2013".toCharArray());
		mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
		mqttClient.setCallback(new MQTTCallback(BROKER_URL, clientId, topic));
	}
	
	/**
	 * Creates connection with the MQTT broker
	 */
	public void connect(){
		try {
			mqttClient.connect(opt);
			System.out.print("Connected!");
		} catch (MqttException e) {
			System.out.print("Connection error!! "+e.toString());
			connect();
		}
	}
	
	/**
	 * Subscribes to the device with its topic
	 */
	public void subscribeToDevice(){
		try {
			mqttClient.subscribe(this.topic);
			System.out.print("Subscribed");
		} catch (MqttException e) {
			e.printStackTrace();
 		}
	}

	/**
	 * Publishes message for the client
	 * @param message (String) Message that needs to be published
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void publishToDevice(String message) throws MqttPersistenceException, MqttException{
			MqttTopic topic=mqttClient.getTopic(this.topic);
			System.out.println("In publish: topic="+topic);
			MqttMessage msg= new MqttMessage(message.getBytes());
			topic.publish(msg);
			System.out.println("Published");
			mqttClient.disconnect();
	}
	
	
	/**
	 * Callback class to receive message
	 */
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
