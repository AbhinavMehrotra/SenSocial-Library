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
//package com.ubhave.sensocial.manager;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.util.Log;
//
//import com.ubhave.sensocial.http.IdSenderToEnableTrigger;
//
//public class SenSocialService {
//
//	private static final String TAG = "SNnMB";
//	private static String SERVICE_CLASSNAME;
//	
//	/**
//	 * Method to start the service. <br/>
//	 * If the MQTT server IP has been set then it will start the MQTT service
//	 * else it starts HTPP service.
//	 * @param context Application-Context
//	 */
//	protected static void startService(Context context) {
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		if(!sp.getBoolean("sendId", false)){			
//			new IdSenderToEnableTrigger(sp.getString("fbusername", "null"), sp.getString("new_access_token", "null"),
//					sp.getString("twitterusername", "null"), sp.getString("uuid", "null"), sp.getString("server", "null"))
//					.sendIdToServer();
//			Editor ed=sp.edit();
//			ed.putBoolean("sendId", true);
//			ed.commit();
//		}		
//		if(sp.getString("mqtt","null").equals("null")){
//			Log.e(TAG, "starting http service");
//			Intent myIntent = new Intent(context, com.ubhave.sensocial.http.HTTPService.class);
//			context.startService(myIntent);
//		}else{
//			Log.e(TAG, "starting mqtt service");
//			Intent myIntent = new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class);
//			context.startService(myIntent);
//		}
//	}
//	
//	/**
//	 * Method to stop the service. It will stop HTTP or MQTT service, which ever service will be running.
//	 * @param context Application-Context
//	 */	
//	protected static void stopService(Context context) {
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		if(sp.getString("mqtt","null").equals("null")){
//			Intent myIntent = new Intent(context, com.ubhave.sensocial.http.HTTPService.class);
//			context.stopService(myIntent);
//		}else{
//			Intent myIntent = new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class);
//			context.stopService(myIntent);
//		}
//	}
//	
//	protected static Boolean isRunning(Context context){
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		if(sp.getString("mqtt","null").equals("null")){
//			SERVICE_CLASSNAME="com.ubhave.sensocial.http.HTTPService";
//		}else{
//			SERVICE_CLASSNAME="com.ubhave.sensocial.mqtt.MQTTService";
//		}
//		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//			if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
//				return true;
//			}
//		}
//		return false;		
//	}
//
//}
