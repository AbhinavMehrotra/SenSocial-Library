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
package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.manager.Sensors;

public class ModalityType {
	public final static String null_condition="null";
	public final static String physical_activity="physical_activity";
	public final static String noise="communication";
	public final static String location="location";
	public final static String neighbour="neighbour";
	public final static String time="time";
	public final static String facebook_activity="facebook_activity";
	public final static String twitter_activity="twitter_activity";
	public final static String facebook_friends_location="facebook_friends_location";
	public final static String facebook_friends_around="facebook_friends_around";  
	public final static String twitter_friends_location="twitter_friends_location";
	public final static String twitter_friends_around="twitter_friends_around";  
	
	

	public static int getSensorId(String modalityType){
		int id=0;
		if(modalityType.equalsIgnoreCase("physical_activity")) id=Sensors.SENSOR_TYPE_ACCELEROMETER;
		if(modalityType.equalsIgnoreCase("noise")) id= Sensors.SENSOR_TYPE_MICROPHONE;
		if(modalityType.equalsIgnoreCase("location")) id= Sensors.SENSOR_TYPE_LOCATION;
		if(modalityType.equalsIgnoreCase("neighnour")) id= Sensors.SENSOR_TYPE_BLUETOOTH;
		return id;
	}
	
	public static String getSensorName(String modalityType){
		String str="";
		if(modalityType.equalsIgnoreCase("physical_activity")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_ACCELEROMETER);
		if(modalityType.equalsIgnoreCase("noise")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_MICROPHONE);
		if(modalityType.equalsIgnoreCase("location")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_LOCATION);
		if(modalityType.equalsIgnoreCase("neighbour")) str=Sensors.getSensorNameById(Sensors.SENSOR_TYPE_BLUETOOTH);
		return str;
	}
	
}
