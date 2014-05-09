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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;
import android.util.Log;

import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

/**
 * Provides the solution to store sensor data in memory which can be used by other streams.
 * This avoids sensing data again if the recent data is available.
 * Sensor data gets expired after 5 minutes and then it is discarded from the memory.
 */
public class SensorDataCollector {

	private static Map<Integer, SensorData> sensorDataMap=new HashMap<Integer, SensorData>();


	/**
	 * Deletes the sensor data from memory
	 * @param sensorId Unique id of the sensor. It can be attained from SensorUtils class.
	 */
	public static void deleteData(int sensorId){
		sensorDataMap.remove(sensorId);
	}
	
	/**
	 * Stores sensor data in memory
	 * @param data Sensor data
	 */
	public static void addData(SensorData data){
		Log.e("SNnMB", "\nSensor data added for: " + data.getSensorType());
		sensorDataMap.put(data.getSensorType(), data);
	}

	/**
	 * Stores list of sensor data in memory
	 * @param data Sensor data
	 */
	public static void addData(ArrayList<SensorData> data){
		for(SensorData sd:data){
			Log.e("SNnMB", "\nSensor data added for: " + sd.getSensorType());
			sensorDataMap.put(sd.getSensorType(), sd);
		}
	}


//	public static ArrayList<SensorData> getData(ArrayList<Integer> sensorIds){
//		ArrayList<SensorData> sensordata=new ArrayList<SensorData>();
//		for(int id:sensorIds){
//			sensordata.add(getData(id));
//		}
//		return sensordata;
//	}
//	
	/**
	 * Returns the sensor data if it exists in the memory.
	 * @param sensorId Unique id of the sensor. It can be attained from SensorUtils class.
	 * @return
	 */
	public static SensorData getData(int sensorId){
		SensorData data=null;
		if(isPresent(sensorId)){
			for(Map.Entry<Integer, SensorData> map: sensorDataMap.entrySet()){
				if(map.getKey()==sensorId){
					data=map.getValue();
					break;
				}
			}
			return data;
		}
		return null;
	}

	/**
	 * Returns whether the sensor data is present in memory or not.
	 * @param sensorId Unique id of the sensor. It can be attained from SensorUtils class.
	 * @return Boolean
	 */
	public static Boolean isPresent(int sensorId){
		Log.e("SNnMB", "\nChecking isPresent in the sensorDataMap: " + sensorDataMap);
		Boolean present =sensorDataMap.containsKey(sensorId);		
		if(present){
			long delay = System.currentTimeMillis() - sensorDataMap.get(sensorId).getTimestamp();
			if(delay> (5*60*1000)){
				sensorDataMap.remove(sensorId);
				present=false;
			}
		}
		Log.e("SNnMB", "\nChecking isPresent for: " + sensorId+ " & returning: "+present);		
		return present;
	}
	
	/**
	 * Returns whether a sensor is subscribed for continuous sensing.
	 * If True then the data will be present in the memory.
	 * @param sensorId Unique id of the sensor. It can be attained from SensorUtils class.
	 * @return Boolean
	 */
	public static Boolean isRegistered(int sensorId){
		Log.e("SNnMB", "\nChecking isRegistered for: " + sensorId);
		SensorUtils aps=new SensorUtils(SenSocialManager.getContext());
		String sensorName= aps.getSensorNameById(sensorId);
		SharedPreferences sp=SenSocialManager.getContext().getSharedPreferences("SSDATA", 0);
		Set<String> sensors=sp.getStringSet("StreamSensorSet", null);
		if(sensors==null || !sensors.contains(sensorName))
			return false;
		SensorData data = null;
		for(Map.Entry<Integer, SensorData> map: sensorDataMap.entrySet()){
			if(map.getKey()==sensorId){
				data=map.getValue();
				break;
			}
		}			
		return true;		
	}

}
