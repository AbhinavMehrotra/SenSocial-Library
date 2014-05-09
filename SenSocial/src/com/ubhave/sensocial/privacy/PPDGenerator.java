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
package com.ubhave.sensocial.privacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class PPDGenerator {


	/**
	 * Creates a default PPD xml file with all sensor settings as enabled.
	 */
	public static void createDefaultPPD(){
		try{
			String server="raw";
			String client="raw";
			JSONObject accelerometer=new JSONObject();
			accelerometer.put("name", "accelerometer");
			accelerometer.put("server", server);
			accelerometer.put("client", client);
			JSONObject microphone=new JSONObject();
			microphone.put("name", "microphone");
			microphone.put("server", server);
			microphone.put("client", client);
			JSONObject wifi=new JSONObject();
			wifi.put("name", "wifi");
			wifi.put("server", server);
			wifi.put("client", client);
			JSONObject location=new JSONObject();
			location.put("name", "location");
			location.put("server", server);
			location.put("client", client);
			JSONObject bluetooth=new JSONObject();
			bluetooth.put("name", "bluetooth");
			bluetooth.put("server", server);
			bluetooth.put("client", client);

			JSONArray ppd=new JSONArray();
			ppd.put(accelerometer);
			ppd.put(microphone);
			ppd.put(wifi);
			ppd.put(location);
			ppd.put(bluetooth);	

			JSONObject obj=new JSONObject();
			obj.put("ppd",ppd);
			String str=obj.toString();

			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(str);
			out.close();
			os.close();

		}
		catch(Exception e){
			Log.e("SNnMB", "Error while creating a PPD file. "+e.toString());
		}
	}

	/**
	 * Enables the sensor data flow of the provided sensor and only to the provided location.
	 * @param ppdSensorName 
	 * @param ppdLocation
	 * @param ppdDataType
	 */
	public static void enableSensing(String ppdSensorName, String ppdLocation, String ppdDataType){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					((JSONObject)ppd.get(i)).put(ppdLocation, ppdDataType);
					break;
				}
			}
			fis.close();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(obj.toString());
			out.close();
			os.close();		
			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
		}

	}

	/**
	 * Disables the sensor data flow of the provided sensor and only to the provided location.
	 * @param ppdSensorName
	 * @param ppdLocation
	 */
	public static void disableSensing(String ppdSensorName, String ppdLocation){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					((JSONObject)ppd.get(i)).put(ppdLocation, PPDDataType.NULL);
					break;
				}
			}
			fis.close();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(obj.toString());
			out.close();
			os.close();		
			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
		}
	}

}

//{'ppd':[{'name':'accelerometer', 'server':'raw', 'client':'raw'}, .....]}
