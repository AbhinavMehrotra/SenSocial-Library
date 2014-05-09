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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.DropBoxManager.Entry;
import android.util.Log;

import com.ubhave.sensocial.data.DeviceSensorData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.manager.SSListenerManager;
import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

/**
 * ContinuousStreamSensing class enables the process of subscribing to any supported sensor.
 * This is used for stream sensing process.
 */
public class ContinuousStreamSensing {

	final private String TAG = "SNnMB";
	private static ESSensorManager sensorManager;
	private static Context context;
	private ArrayList<Integer> SensorIds;
	private String message;
	private static SharedPreferences sp;
	private static Editor ed;
	private static int subscriptionId;
	private static SensorUtils aps;
	private static ArrayList<SensorData> sensordata;
	private static ContinuousStreamSensing instance;
	private static Map<Integer, SensorData> sensordataCollection;

	public static ContinuousStreamSensing getInstance(Context context, ArrayList<Integer> SensorIds) throws ESException{
		return new ContinuousStreamSensing(context, SensorIds);
		//		if(instance==null){
		//			instance=new ContinuousStreamSensing(context, SensorIds);
		//		}
		//		return instance;
	}

	private ContinuousStreamSensing(Context context, ArrayList<Integer> SensorIds) throws ESException
	{
		this.context=context;
		this.SensorIds=SensorIds;
		sensorManager = ESSensorManager.getSensorManager(context);
		sp=context.getSharedPreferences("SSDATA",0);
		aps=new SensorUtils(context);
		sensordata=new ArrayList<SensorData>();
		sensordataCollection=new HashMap<Integer, SensorData>();
	}

	/**
	 * Starts sensing from all the sensors provided as arguments in constructor
	 * @throws ESException
	 */
	protected void startSensing() throws ESException{
		SensorDataListener listener = new SensorDataListener() {

			public void onDataSensed(SensorData arg) {
				System.out.println("on Data Sensed");
				if(arg.getSensorType()==SensorUtils.SENSOR_TYPE_LOCATION)
					arg=LocationValidator.validateLocation(arg);
				if( !sensordataCollection.containsKey(arg.getSensorType()) ) 
					sensordataCollection.put(arg.getSensorType(), arg);
				else{
					for( Map.Entry<Integer, SensorData> entry : sensordataCollection.entrySet() ) {
						if( entry.getKey().equals(arg.getSensorType()) ) {
							entry.setValue(arg);
							break;
						}
					}
				}
				SensorDataCollector.addData(arg);
				if(sensordataCollection.size()==SensorIds.size()){
					Log.d("SNnMB", "Data sensed for all sensor-ids");
					Log.e("SNnMB", "Map: "+sensordataCollection);
					for(Map.Entry<Integer, SensorData> x: sensordataCollection.entrySet()){
						sensordata.add(x.getValue());
					}
					SensorDataHandler.handleStreamData(sensordata, context);
					sensordata.clear();
					sensordataCollection.clear();		
				}

			}

			public void onCrossingLowBatteryThreshold(boolean arg0) {
				// can pause sensing
			}
		};
		ed=sp.edit();
		for(int i=0;i<SensorIds.size();i++){
			System.out.println("Continuous sensing (Start sensing): "+SensorIds.get(i));
			System.out.println(SensorIds);
			subscriptionId=sensorManager.subscribeToSensorData (SensorIds.get(i), listener);
			ed.putInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", subscriptionId);			
		}
		ed.commit();
	}

	/**
	 * Stops sensing from all the sensors provided as arguments in constructor
	 * @throws ESException
	 */
	public void stopSensing() throws ESException{
		for(int i=0;i<SensorIds.size();i++){
			subscriptionId=sp.getInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", 0);
			if(subscriptionId != 0){
				sensorManager.unsubscribeFromSensorData(subscriptionId);
			}		
		}
	}
}
