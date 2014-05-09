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
package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import com.ubhave.sensocial.filters.SensorConfiguration;

import android.content.Context;
import android.content.SharedPreferences;

public class SensorUtils {

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_WIFI = 5010;
	SharedPreferences sp;
	private SensorConfiguration SC;
	
	public SensorUtils(Context context){
		sp=context.getSharedPreferences("SSDATA",0);
		SC=new SensorConfiguration(context);
	}
	
	/**
	 * Getter for the list of sensors subscribed at the moment 
	 * @return int[] Array of sensor ids
	 */
	public ArrayList<Integer> getIds(){
		return SC.getSubsribedSensors();
	}
	
	/**
	 * Returns sensor-name by giving sensor-id
	 * @param String sensor-id
	 * @return String sensor-name
	 */
	public String getSensorNameById(int id){
		String sensor=null;
		if(id==SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		if(id==SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		if(id==SENSOR_TYPE_LOCATION) sensor="location";
		if(id==SENSOR_TYPE_MICROPHONE) sensor="microphone";
		if(id==SENSOR_TYPE_WIFI) sensor="wifi";		
		return sensor;
	}
	
	/**
	 * Returns sensor-id by giving sensor=name
	 * @param String sensor-name
	 * @return int sensor-id
	 */
	public int getSensorIdByName(String sensor){
		int id=0;
		if(sensor.equalsIgnoreCase("accelerometer")) id = SENSOR_TYPE_ACCELEROMETER;
		else if(sensor.equalsIgnoreCase("bluetooth")) id = SENSOR_TYPE_BLUETOOTH;
		else if(sensor.equalsIgnoreCase("location")) id = SENSOR_TYPE_LOCATION;
		else if(sensor.equalsIgnoreCase("microphone")) id = SENSOR_TYPE_MICROPHONE;
		else if(sensor.equalsIgnoreCase("wifi")) id = SENSOR_TYPE_WIFI;
		return id;
	}

}
