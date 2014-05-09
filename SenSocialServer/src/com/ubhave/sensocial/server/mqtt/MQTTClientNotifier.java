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

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Device;

public class MQTTClientNotifier {

	/**
	 * Sends Facebook update notification to the clients.
	 * @param id (String) User-id
	 * @param message (String) Facebook update
	 * @param time (long) time of update of the Facebook message
	 * @param notificationType (String) Feed type
	 */
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}			
	}
	
	/**
	 * Sends stream notification to the clients.
	 * It notifies clients to start/stop/pause/un-pause streams.
	 * @param deviceId (String) Device id
	 * @param message (MQTTNotifications) Notification message that needs to be pushed to the client
	 * @param streamId (String) Stream id
	 */
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

	/**
	 * Not in use.
	 * Sends request to clients to get real-time data which is device specific
	 * @param message
	 */
	public static void sendQueryNotifications(MQTTNotifitions message){
		// send notifications to query something and get result
		//example-> get all bluetooths available nearby
	}
}
