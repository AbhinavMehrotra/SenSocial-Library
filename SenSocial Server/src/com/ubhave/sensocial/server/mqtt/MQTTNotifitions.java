package com.ubhave.sensocial.server.mqtt;


public enum MQTTNotifitions {
	start_stream("start_stream"),
	stop_stream("stop_stream"),
	pause_stream("pause_stream"),
	unpause_stream("unpause_stream"),
	nearby_bluetooths("nearby_bluetooths");
	
	private final String message;
	
	MQTTNotifitions(String message){
		this.message=message;
	}
	
	public String getMessage(){
		return message;
	}
}
