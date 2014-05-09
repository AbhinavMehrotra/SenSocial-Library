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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.FilterSettings;
import com.ubhave.sensocial.filters.ServerStreamRegistrar;
import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensocial.sensormanager.SensorUtils;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensocial.sensormanager.SensorDataCollector;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

/**
 * NotificationParser class parses the message received via mqtt service
 */
public class NotificationParser {
	private Context context;
	private SharedPreferences sp;
	
	protected NotificationParser(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA", 0);
	}

	/**
	 * Parse the message and take action accordingly.
	 * @param message
	 */
	protected void takeAction(final String message){		
		Log.i("SNnMB", "MQTT message: "+message);
		if(message.startsWith(MQTTNotifitions.start_stream.getMessage())){
			String streamId=message.substring(13);  //<<message --> start_stream:streamId>>
			ServerStreamRegistrar.addStreamId(streamId);
			String url=sp.getString("serverurl", null)+"ClientFilters/Filter"+streamId+".xml";
			//			String url=sp.getString("serverurl", null)+"ClientFilters/Filter"+"abc.xml";
			String destination=streamId+".xml";
			new DownloadFilter(context, url, destination).execute();
		}
		else if(message.startsWith(MQTTNotifitions.stop_stream.getMessage())){
			String streamId=message.substring(12);
			ServerStreamRegistrar.removeStreamId(streamId);
			FilterSettings.removeConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.pause_stream.getMessage())){
			String streamId=message.substring(13);
			FilterSettings.stopConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.unpause_stream.getMessage())){
			String streamId=message.substring(15);
			FilterSettings.startConfiguration(streamId);
			ConfigurationHandler.run(context);
		}
		else if(message.startsWith(MQTTNotifitions.facebook_update.getMessage())){
			Log.i("SNnMB", "Facebook update recieved!");
			SensorUtils aps=new SensorUtils(context);
			SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
			final ArrayList<Integer> sensorIds=new ArrayList<Integer>();
			for(String s:sp.getStringSet("OSNSensorSet", null))
				sensorIds.add(aps.getSensorIdByName(s));
			Log.i("SNnMB", "Sensors: "+sp.getStringSet("OSNSensorSet", null));
			Log.i("SNnMB", "Sensor ids: "+sensorIds);
			try {
				StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
	    		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
	    		.permitNetwork()
	    		.build());
				Log.d("SNnMB","Start sensing");
				new OneOffSensing(context, sensorIds){
					@Override
					public void onPostExecute(ArrayList<SensorData> data){
						Log.d("SNnMB","Stopped sensing");
						if(data!=null){
							SensorDataHandler.handleOSNDependentData(data, context, message);
						}

					}
				}.execute();
	            StrictMode.setThreadPolicy(old);
			} catch (Exception e) {
				Log.e("SNnMB","Error at Notification parser: "+e.toString());
			}
//			ArrayList<SensorData> sensorData=new ArrayList<SensorData>();
//			try {
//				ESSensorManager sensorManager = ESSensorManager.getSensorManager(context);
//				for(int i=0;i<sensorIds.size();i++){
//					if(SensorDataCollector.isRegistered(sensorIds.get(i))){
//						Log.i("SNnMB", "One-Off sensing process, found the latest data available for: "+sensorIds.get(i));
//						sensorData.add(SensorDataCollector.getData(sensorIds.get(i)));
//					}
//					else{
//						Log.i("SNnMB", "One-Off sensing process, latest data not available & sensing for: "+sensorIds.get(i));
//						sensorData.add(sensorManager.getDataFromSensor(sensorIds.get(i)));
//					}
//				}
//			} catch (Exception e) {
//				Log.e("SNnMB","Error at Notification parser: "+e.toString());
//			}
//			if(sensorData!=null){
//				Log.i("SNnMB", "Sensor data: "+sensorData);
//				SensorDataHandler.handleOSNDependentData(sensorData, context, message);
//			}
		}
		else if(message.startsWith(MQTTNotifitions.nearby_bluetooths.getMessage())){

		}
	}
}

