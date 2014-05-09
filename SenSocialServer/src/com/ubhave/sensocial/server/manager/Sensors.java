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


public class Sensors {

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_WIFI = 5010;
	public final static int SENSOR_TYPE_TIME=1111;
	
	/**
	 * Returns sensor name for the given sensor id
	 * @param id (int) Sensor id
	 * @return (String) Sensor name
	 */
	public static String getSensorNameById(int id){
		String sensor=null;
		if(id==SENSOR_TYPE_ACCELEROMETER) sensor="accelerometer";
		else if(id==SENSOR_TYPE_BLUETOOTH) sensor="bluetooth";
		else if(id==SENSOR_TYPE_LOCATION) sensor="location";
		else if(id==SENSOR_TYPE_MICROPHONE) sensor="microphone";
		else if(id==SENSOR_TYPE_WIFI) sensor="wifi";	
		else if(id==SENSOR_TYPE_TIME) sensor="time";
		return sensor;
	}

	/**
	 * Returns sensor-id for the given sensor name
	 * @param name (String) Sensor name
	 * @return (int) sensor id
	 */
	public static int getSensorIdByName(String name){
		int sensor=0;
		if(name.equalsIgnoreCase("accelerometer")) sensor=SENSOR_TYPE_ACCELEROMETER;
		else if(name.equalsIgnoreCase("bluetooth")) sensor=SENSOR_TYPE_BLUETOOTH;
		else if(name.equalsIgnoreCase("location")) sensor=SENSOR_TYPE_LOCATION;
		else if(name.equalsIgnoreCase("microphone")) sensor=SENSOR_TYPE_MICROPHONE;
		else if(name.equalsIgnoreCase("wifi")) sensor=SENSOR_TYPE_WIFI;	
		else if(name.equalsIgnoreCase("time")) sensor=SENSOR_TYPE_TIME;
		return sensor;
	}
}
