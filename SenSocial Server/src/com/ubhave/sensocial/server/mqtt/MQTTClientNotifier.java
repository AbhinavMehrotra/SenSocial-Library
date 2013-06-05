package com.ubhave.sensocial.server.mqtt;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Device;

public class MQTTClientNotifier {

	public static void sendFacebookUpdate(String id, String message,long time){
		ArrayList<Device> devices=UserRegistrar.getUser(id).getDevices();
		for(Device d:devices){
			try {
				MQTTManager mqtt = new MQTTManager(d.getDeviceId());
				mqtt.connect();
				mqtt.publishToDevice(message +":" + time);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}			
	}
	
	public static void sendStreamNotification(MQTTNotifitions message, int streamId){
		//for start stream client should check this stream id in the exsiting filters and it is not there the request for new file
		
	}

	public static void sendQueryNotifications(MQTTNotifitions message){
		// send notifications to query something and get result
		//example-> get all bluetooths available nearby
	}
}
