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
package com.ubhave.sensocial.mqtt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.exceptions.ServerException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.SensorUtils;
import com.ubhave.sensocial.sensormanager.ContinuousStreamSensing;
import com.ubhave.sensormanager.ESException;

public class MQTTServiceRestartHandler {
	//To hide 
	protected MQTTServiceRestartHandler() {}

	private static SharedPreferences sp;
	private static Editor ed;

	/**
	 * This method will restart all the sensing again according to the filter xml.
	 * All configs saved in SharedPreferences will be deleted and set again.
	 * @param context
	 */
	protected static void run(Context context){
		sp=context.getSharedPreferences("SSDATA", 0);
		Boolean location_tracter=sp.getBoolean("locationtrackerrequired", false);
		Boolean server_client=sp.getBoolean("serverclient", false);
		try {
			SenSocialManager.getSenSocialManager(context, server_client, location_tracter);
		} catch (ServerException e1) {
			e1.printStackTrace();
			SenSocialManager.setContext(context);
		}	
		
		ed=sp.edit();

		//try to stop all sensing
		Set<String> sensors=new HashSet<String>();
		sensors=sp.getStringSet("StreamSensorSet", sensors);
		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
		SensorUtils aps=new SensorUtils(context);
		for(String sensor:sensors){
			sensorIds.add(aps.getSensorIdByName(sensor));
		}

		ArrayList<Integer> ids=new ArrayList<Integer>();
		for(int id: sensorIds){
			ids.add(id);
			try{
				Log.i("SNnMB", "Trying to stop sensing for: "+ aps.getSensorNameById(id));
				ContinuousStreamSensing.getInstance(context,ids).stopSensing();
			}
			catch(ESException e){
				Log.e("SNnMB", "Not able to stop sensing for: "+ aps.getSensorNameById(id) +"\n"+ e.toString());
			}
			finally{
				ids.clear();
			}
		}

		//delete all subscription ids
		for(int id:sensorIds){
			ed.remove(id + "_subId");
			ed.commit();
		}

		//delete condition sets for all configs
		Set<String> blank = new HashSet<String>();
		for(String config: sp.getStringSet("", blank)){
			ed.remove(config);
			ed.commit();
		}

		ed.remove("ConfigurationSet");
		ed.remove("SensorSet");
		ed.remove("OSNConfigurationSet");
		ed.remove("OSNSensorSet");
		ed.remove("StreamConfigurationSet");
		ed.remove("StreamSensorSet");
		ed.commit();
		
//		These statements will do the same I did above
//		ed.clear();
//		ed.commit();
		
		//Reset all configurations
		ConfigurationHandler.run(context);		
	}
}
