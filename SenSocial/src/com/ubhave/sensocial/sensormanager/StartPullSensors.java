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
//package com.ubhave.sensocial.sensormanager;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.ubhave.sensormanager.ESException;
//
//public class StartPullSensors {
//
//	final private String TAG = "SNnMB";
//	private ArrayList<Integer> SensorIds;
//	private final Context context;
//	private String message;
//	private AllPullSensors aps;
//	private SharedPreferences sp;
//
//	public StartPullSensors(Context context){
//		aps=new AllPullSensors(context);
//		this.SensorIds=aps.getIds();
//		this.context=context;
//		sp=context.getSharedPreferences("SSDATA", 0);
//	}
//	
////	public void startOneOffSensingWithOSN(String message){
////		try {
////			new OneOffSensing(context, SensorIds, message).execute();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////
////	/**
////	 * Method to initiate sensing from the configured sensors.
////	 */
////	public void startIndependentOneOffSensing(){
////		try {
////			new OneOffSensing(context, SensorIds, null).execute();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////
////	public void startIndependentContinuousStreamSensing(){
////		try {
////			new ContinuousStreamSensing (context, SensorIds).startSensing();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////	
////	public void stopIndependentContinuousStreamSensing(){
////		try {
////			new ContinuousStreamSensing (context, SensorIds).stopSensing();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////	
//	public void startIndependentContinuousStreamSensing(String sensor){
//		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
//		Set<String> blankSet=new HashSet<String>();
//		for(String sensor_name:sp.getStringSet("StreamSensorSet", blankSet)){
//			sensorIds.add(aps.getSensorIdByName(sensor_name));
//		}
//		try {
//			ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();
//			sensorIds.add(aps.getSensorIdByName(sensor));
//			ContinuousStreamSensing.getInstance(context, sensorIds).startSensing();
//		} catch (ESException e) {
//			Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
//		}
//	}
//	
//	public void stopIndependentContinuousStreamSensing(String sensor){
//		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
//		Set<String> blankSet=new HashSet<String>();
//		for(String sensor_name:sp.getStringSet("StreamSensorSet", blankSet)){
//			sensorIds.add(aps.getSensorIdByName(sensor_name));
//		}
//		try {
//			ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();
//			sensorIds.remove(aps.getSensorIdByName(sensor));
//			ContinuousStreamSensing.getInstance(context, sensorIds).startSensing();
//		} catch (ESException e) {
//			Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
//		}
//	}
//}
