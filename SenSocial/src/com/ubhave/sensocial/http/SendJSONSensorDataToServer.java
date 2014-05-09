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
//package com.ubhave.sensocial.http;
//
//import java.io.DataOutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import com.ubhave.sensormanager.data.SensorData;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class SendJSONSensorDataToServer extends AsyncTask<String,Void,String>{
//	final private String TAG = "SNnMB";
//	private HttpURLConnection connection = null;
//	private DataOutputStream outputStream = null;
//	private String serverUrl;
//	private String serverResponseMessage;
//	private Context context;
//	SensorData sensorData;
//	
//	/**
//	 * Constructor
//	 * @param context Application context
//	 * @param sensorData SensorData received after completion of sensing task
//	 */
//	public SendJSONSensorDataToServer( Context context, SensorData sensorData){
//		this.context=context;
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		this.serverUrl=sp.getString("sever", null);
//	}
//
//	/**
//	 * Sends the sensor data in JSON format to the server. 
//	 */
//	@Override
//	protected String doInBackground(String... arg0) {
//		try{
//			Log.d(TAG, "Sending JSON data to server");
//			URL url = new URL(serverUrl);
//			connection = (HttpURLConnection) url.openConnection();
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setUseCaches(false);
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Connection", "Keep-Alive");
//			outputStream = new DataOutputStream( connection.getOutputStream() );
//			outputStream.writeBytes(sensorData.toString());
//			
//			int serverResponseCode = connection.getResponseCode();
//			serverResponseMessage = connection.getResponseMessage();
//			Log.d("response",""+serverResponseCode);
//			Log.d("serverResponseMessage",""+serverResponseMessage);
//			outputStream.flush();
//			outputStream.close();
//
//		}
//		catch (Exception ex){
//			Log.e(TAG, ex.toString());
//			serverResponseMessage=ex.toString();
//		}
//		return ("Bytes read: "+serverResponseMessage);
//	}
//	
//	@Override
//	protected void onPostExecute(String result){
//		Log.d(TAG, "Sending JSON sensor data complete");
//		Log.d(TAG, "Server response: "+result);
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		Editor ed=sp.edit();
//		ed.putBoolean("sensing", false);
//		ed.commit();
//	}
//
//}
