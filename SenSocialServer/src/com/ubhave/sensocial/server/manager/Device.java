package com.ubhave.sensocial.server.manager;

import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;


public class Device {

	private String deviceId;
	private Location location;
	private String bluetoothMAC;
	private User user;

	protected Device(User user, String deviceId, String bluetoothMAC,  Location location) {
		this.user=user;
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
		MQTTClientNotifier.sendStreamNotification(deviceId,MQTTNotifitions.unpause_stream, stream.getStreamId()); 
	}
	
	public User getUser() {
		return user;
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
