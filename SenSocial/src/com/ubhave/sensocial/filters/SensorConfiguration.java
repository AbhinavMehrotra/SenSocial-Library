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
package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.SensorUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi")
public class SensorConfiguration {
	private SharedPreferences sp;
	private Context context;

	/**
	 * Constructor.
	 * @param context Application Context
	 */
	public SensorConfiguration(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA",0);
	}	

	/**
	 * Getter for the list of sensors ids which have been set as true.
	 * @return sensorIds ArrayList<Integer> 
	 */
	public ArrayList<Integer> getSubsribedSensors(){		
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		if(sp.getBoolean("accelerometer", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_ACCELEROMETER);
		if(sp.getBoolean("bluetooth", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_BLUETOOTH);
		if(sp.getBoolean("wifi", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_WIFI);
		if(sp.getBoolean("location", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_LOCATION);
		if(sp.getBoolean("microphone", false)) sensorIds.add(SensorUtils.SENSOR_TYPE_MICROPHONE);		
		return sensorIds;
	}

	/**
	 * Returns the sensor associated with the activity
	 * @param activity name (String)
	 * @return sensor name (String)
	 */
	protected static String getSensorNameForConditions(String activity){
		String sensorName = null;
		SensorUtils aps=new SensorUtils(SenSocialManager.getContext());
		Condition c=new Condition(activity);
		return aps.getSensorNameById(ModalityType.getSensorId(c.getModalityType()));		
	}

}

