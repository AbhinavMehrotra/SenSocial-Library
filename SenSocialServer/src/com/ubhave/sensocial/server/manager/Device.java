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
