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
package com.ubhave.sensocial.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.FilterSettings;

/**
 * MyDevice class allows to access the properties of the local device, and creation,
 * removal of streams .
 */
public class MyDevice {

	private String deviceId;
	private Context context;
	private User user;

	/**
	 * Constructor
	 * @param context Application context
	 * @param user User of the device
	 */
	protected MyDevice(Context context, User user) {
		this.context=context;
		this.user=user;
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		deviceId=sp.getString("deviceid", "null");
	}


	/**
	 * Getter for Stream
	 * @param sensorId
	 * @param dataType
	 * @return Stream object
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(sensorId, dataType, context);
		return stream;
	}


	/**
	 * Removes the stream from the device
	 * @param stream
	 */
	public void removeStream(Stream stream){
		FilterSettings.removeConfiguration(stream.getStreamId());
		ConfigurationHandler.run(context);	 
	}

	/**
	 * Getter for device-id
	 * @return String device-id
	 */
	public String getDeviceId(){
		return deviceId;
	}

	/**
	 * Getter for User
	 * @return User object
	 */
	public User getUser(){
		return this.user;
	}

	/**
	 * Getter for Location
	 * @return Location object
	 */
	public Location getLastKnownLocation(){		
		Location location;
		//look for location in shared prefrences
		location= new Location(0, 0);
		//TODO: logic here
		return location;
	}



}
