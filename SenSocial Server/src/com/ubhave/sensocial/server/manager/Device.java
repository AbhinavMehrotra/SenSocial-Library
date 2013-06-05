package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;


public class Device {

	private String deviceId;
	private Location location;
	private String bluetoothMAC;

	protected Device(String deviceId, String bluetoothMAC,  Location location) {
		this.deviceId=deviceId;
		this.bluetoothMAC=bluetoothMAC;
		this.location=location;
	}


	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(this, sensorId, dataType);
		return stream;
	}

	public void removeStream(Stream stream){
		//notify clients to delete the filter
		MQTTClientNotifier.sendStreamNotification(MQTTNotifitions.unpause_stream, stream.getSensorId()); 
	}

	public String getDeviceId() {
		return deviceId;
	}
	
	public String getBluetoothMAC() {
		return bluetoothMAC;
	}

	public Location getLocation() {
		return location;
	}

}
