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

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.LocationData;

/**
 * LocationValidator class enables validating the location data.
 * <br>**Bug fix for unknown/(0,0) location**.
 */
public class LocationValidator {

	private static LocationManager lm;
	
	protected static SensorData validateLocation(SensorData data){
		JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), data.getSensorType());
		String str=formatter.toJSON(data).toString();
		JSONObject obj;
		String lat = "0", lon = "0";
		try {
			obj = new JSONObject(str);
			lat=obj.get("latitude").toString();
			lon=obj.get("longitude").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(lat.equalsIgnoreCase("unknown") || lat.equalsIgnoreCase("0") || lon.equalsIgnoreCase("unknown") || lon.equalsIgnoreCase("0")){
			Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			LocationData loc_data = (LocationData) data;
			loc_data.setLocation(loc);
			return loc_data;
		}
		else{
			return data;			
		}
	}
}
