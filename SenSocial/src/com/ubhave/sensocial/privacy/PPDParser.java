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

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class PPDParser {

	/**
	 * Returns whether the sensor is allowed for the location and with given granularity.
	 * @param ppdSensorName
	 * @param ppdLocation
	 * @param ppdDataType
	 * @return
	 */
	public static Boolean isAllowed(String ppdSensorName, String ppdLocation, String ppdDataType){
		try{
			Boolean flag=false;
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				PPDGenerator.createDefaultPPD();
				return true;
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			System.out.print("Getting PPD file from mobile device");
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			System.out.print("JSON array for PPD: "+ ppd);
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					System.out.println("Checking sensor: "+ppdSensorName);
					String dataType=((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase());
					if(ppdDataType.equalsIgnoreCase(PPDDataType.CLASSIFIED) && !dataType.equalsIgnoreCase(PPDDataType.NULL)){
						flag = true;
						break;
					}
					else if(((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase()).equalsIgnoreCase(ppdDataType)){
						flag = true;
						break;
					}
					else{
						System.out.println("PPD settings for: "+ppdSensorName+", at- "+ppdLocation+" is- "+ ((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase()));
					}
				}
				else{
					System.out.println("Sensor: "+ppdSensorName+", not found!!");
				}
			}
			fis.close();
			return flag;			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
			return false;
		}
	}
	
	/**
	 * Returns PPD in JSON string format
	 * @return
	 */
	public static String getPPDJSONString(){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				PPDGenerator.createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			return data;			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
			return "Error while parsing the file!!";
		}
	}
}
