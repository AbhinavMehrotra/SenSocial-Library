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
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//
//import com.sensocial.R;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class SendSensorDataToServer extends AsyncTask<String,Void,String>{
//	final private String TAG = "SNnMB";
//	private HttpURLConnection connection = null;
//	private DataOutputStream outputStream = null;
//	private String pathToOurFile = "/mnt/sdcard/";
//	private String serverUrl;
//	private String lineEnd= "\r\n", twoHyphens= "--", boundary=  "*****";
//	private int bytesRead, bytesAvailable, bufferSize;
//	private byte[] buffer;
//	private int maxBufferSize = 1*1024*1024;
//	private String serverResponseMessage;
//	private Context context;
//	
//	/**
//	 * Constructor to set file attributes.
//	 * @param folderName Folder where file exists
//	 * @param fileName Name of sensor data file
//	 * @param context Application context
//	 */
//	public SendSensorDataToServer(String folderName, String fileName, Context context){
//		this.pathToOurFile=this.pathToOurFile+folderName+"/"+fileName;
//		this.context=context;
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		this.serverUrl= fileName;  //sp.getString("sever", null);
//	}
//
//	/**
//	 * Sends the sensor data file to the server. 
//	 */
//	@Override
//	protected String doInBackground(String... arg0) {
//		try{
//			Log.d(TAG, "try Sending File");
//			
//			InputStream fileInputStream=context.getResources().openRawResource(R.raw.ppd);
//			
//			
//			//FileInputStream fileInputStream = new FileInputStream(context.getResources().openRawResource(R.raw.ppd));
//			URL url = new URL(serverUrl);
//			connection = (HttpURLConnection) url.openConnection();
//			// Allow Inputs & Outputs
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setUseCaches(false);
//
//			// Enable POST method
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
//			outputStream = new DataOutputStream( connection.getOutputStream() );
//			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
//			outputStream.writeBytes(lineEnd);
//
//			bytesAvailable = fileInputStream.available();
//			bufferSize = Math.min(bytesAvailable, maxBufferSize);
//			buffer = new byte[bufferSize];
//			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//			while (bytesRead > 0){
//				Log.d(TAG, buffer.toString());
//				outputStream.write(buffer, 0, bufferSize);
//				bytesAvailable = fileInputStream.available();
//				bufferSize = Math.min(bytesAvailable, maxBufferSize);
//				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//			}
//			outputStream.writeBytes(lineEnd);
//			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//			// Responses from the server (code and message)
//			int serverResponseCode = connection.getResponseCode();
//			serverResponseMessage = connection.getResponseMessage();
//			Log.d("response",""+serverResponseCode);
//			Log.d("serverResponseMessage",""+serverResponseMessage);
//			fileInputStream.close();
//			outputStream.flush();
//			outputStream.close();
//			serverResponseMessage+="\n and try end";
//		}
//		catch (Exception ex){
//			Log.e(TAG, ex.toString());
//			serverResponseMessage=ex.toString();
//		}
//		return ("Bytes read: "+serverResponseMessage);
//	}
//	
//	/**
//	 * Delete the file of sensor data after it has been sent to the server. 
//	 * It also set sensing as false, so that any new trigger can now start sensing.
//	 */
//	@Override
//	protected void onPostExecute(String result){
//		Log.d(TAG, "File Sending Complete");
//		Log.d(TAG, result);
//		File file = new File(pathToOurFile);
//		boolean deleted = file.delete();
//		Log.d(TAG, "File Deleted:" + deleted);
//		SharedPreferences sp=context.getSharedPreferences("SSDATA",0);
//		Editor ed=sp.edit();
//		ed.putBoolean("sensing", false);
//		ed.commit();
//	}
//
//}
