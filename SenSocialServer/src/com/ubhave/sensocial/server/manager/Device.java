package com.ubhave.sensocial.server.manager;

import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;

/**
 * Device class represents the device of users.
 * Note: Some users can have multiple devices.
 */
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

	/**
	 * Creates and returns a new stream of sensor data from this device.
	 * @param (int) Sensor id of the required data
	 * @param (String) Data type of the required data. This can be raw or classified.
	 * @return	Stream Object
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(this, sensorId, dataType);
		return stream;
	}

	/**
	 * Removes the given stream from the device.
	 * @param Stream Object
	 */
	public void removeStream(Stream stream){
		//notify clients to delete the filter
		MQTTClientNotifier.sendStreamNotification(deviceId,MQTTNotifitions.unpause_stream, stream.getStreamId()); 
	}
	
	/**
	 * Returns the user who owns this device
	 * @return User Object
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Returns the device id
	 * @return String Device id
	 */
	public String getDeviceId() {
		return deviceId;
	}
	
	/**
	 * Returns the Bluetooth MAC of this device
	 * @return String BluetoothMAC
	 */
	public String getBluetoothMAC() {
		return bluetoothMAC;
	}

	/**
	 * Returns the latest location of the device
	 * @return Location Object
	 */
	public Location getLocation() {
		return location;
	}

}
