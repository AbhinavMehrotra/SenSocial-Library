package com.ubhave.sensocial.server.mqtt;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Device;

public class MQTTClientNotifier {

	public static void sendFacebookUpdate(String id, String message,long time, String notificationType){
		ArrayList<Device> devices=UserRegistrar.getUser(id).getDevices();
		for(Device d:devices){
			try {
				JSONObject obj=new JSONObject();
				obj.put("message", message);
				obj.put("notificationtype", notificationType);
				obj.put("time", time);
				obj.put("osnname", "FACEBOOK");
				
				MQTTManager mqtt = new MQTTManager(d.getDeviceId());
				mqtt.connect();
				mqtt.publishToDevice(MQTTNotifitions.facebook_update.getMessage()+":"+ obj.toString());
			} catch (MqttException | JSONException e) {
				e.printStackTrace();
			}
		}			
	}
	
	public static void sendStreamNotification(String deviceId, MQTTNotifitions message, String streamId){
		//the notified client should check this stream id in the exsiting filters and it is not there the request for new file
		try {
			MQTTManager mqtt = new MQTTManager(deviceId);
			mqtt.connect();
			mqtt.publishToDevice(message.getMessage() +":" + streamId);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
	}

	public static void sendQueryNotifications(MQTTNotifitions message){
		// send notifications to query something and get result
		//example-> get all bluetooths available nearby
	}
}
