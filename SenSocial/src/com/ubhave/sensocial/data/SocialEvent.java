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
package com.ubhave.sensocial.data;

import org.json.*;

import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensormanager.data.SensorData;

public class SocialEvent {

	private SocialData socialData;

	private DeviceSensorData sensorData;

	public SocialEvent(){
		//by default set all elements as unknown
		DeviceSensorData dsd=new DeviceSensorData();
		dsd.setClassifiedData("unknown");
		dsd.setDeviceId("unknown");
		dsd.setStreamId("unknown");
		dsd.setRawData(null);		
		SocialData sd=new SocialData();
		sd.setUserName("unknown");
		sd.setFeedType("unknown");
		sd.setOSNFeed("unknown");
		sd.setOSNName("unknown");
		sd.setTime("unknown");
	}

	public SocialEvent(SensorData rawData, String classifiedData, String streamId, String deviceId,
			String userName, String oSNFeed, String oSNName, String feedType, String time){
		sensorData=new DeviceSensorData();
		sensorData.setClassifiedData(classifiedData);
		sensorData.setRawData(rawData);
		sensorData.setStreamId(streamId);
		sensorData.setDeviceId(deviceId);
		socialData=new SocialData();
		socialData.setUserName(userName);
		socialData.setOSNFeed(oSNFeed);
		socialData.setOSNName(oSNName);
		socialData.setFeedType(feedType);
		socialData.setTime(convertTime(Long.parseLong(time)));
	}
	protected static String convertTime(long miliseconds){
		miliseconds*=1000;
		int seconds = (int) (miliseconds / 1000) % 60 ;
		int minutes = (int) ((miliseconds / (1000*60)) % 60);
		int hours   = (int) ((miliseconds / (1000*60*60)) % 24)+1;
		int days    = (int) ((miliseconds / (1000*60*60*24)));
		int months= (int) ( days/(365.242/12)%12)+1;
		int years= (int) ( days/365.242)+1970;	
		days =(int) (days%365.242)%30;		
		String time= hours+":"+minutes+":"+seconds+"-"+days+":"+months+":"+years;
		return time;		
	}
	/**
	 * Returns the SocialEvent in json-string format
	 */
	public String toJSONString(){
		//convert to string
		JSONObject json =new JSONObject();
		if(sensorData!=null){
			try{
				JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), sensorData.getRawData().getSensorType());
				String str=formatter.toJSON(sensorData.getRawData()).toString();
				json.put("rawdata", str);
			}
			catch(Exception e){
				try {
					json.put("rawdata", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "Raw data not present");
			}
			try{
				json.put("classifieddata", sensorData.getClassifiedData());
			}
			catch(Exception e){
				try {
					json.put("classifieddata", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "Classified data not present");
			}
			try{
				json.put("streamid", sensorData.getStreamId());
			}
			catch(Exception e){
				try {
					json.put("streamid", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "Stream id not present");
			}
			try{
				json.put("deviceid", sensorData.getDeviceId());
			}
			catch(Exception e){
				try {
					json.put("deviceid", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "Device id not present");
			}
		}
		if(socialData!=null){
			try{
				json.put("username", socialData.getUserName());
			}
			catch(Exception e){
				try {
					json.put("username", "user name unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "OSN feed not present");
			}
			
			try{
				json.put("osnfeed", socialData.getOSNFeed());
			}
			catch(Exception e){
				try {
					json.put("osnfeed", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "OSN feed not present");
			}
			
			try{
				json.put("osnname", socialData.getOSNName());
			}
			catch(Exception e){
				try {
					json.put("osnname", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "OSN name not present");
			}
			try{
				json.put("osntime", socialData.getTime());
			}
			catch(Exception e){
				try {
					json.put("osntime", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "OSN Feed time not present");
			}
			try{
				json.put("osnfeedtype", socialData.getFeedType());
			}
			catch(Exception e){
				try {
					json.put("osnfeedtype", "unknown");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				Log.e("SNnMB", "OSN Feed type not present");
			}
		}
		return json.toString();
	}

	/**
	 * Returns the social event in json format
	 */
//	public JSONObject toJSON(){
//		//convert to json-object
//		JSONObject json =new JSONObject();
//		try {
//			if(sensorData!=null){
//				JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), sensorData.getRawData().getSensorType());
//				String str=formatter.toJSON(sensorData.getRawData()).toString();
//				json.put("rawdata", str);
//				json.put("classifieddata", sensorData.getClassifiedData());
//				json.put("streamid", sensorData.getStreamId());
//				json.put("deviceid", sensorData.getDeviceId());
//			}
//			if(socialData!=null){
//				json.put("osnfeed", socialData.getOSNFeed());
//				json.put("osnname", socialData.getOSNName());
//				json.put("osnfeedtype", socialData.getFeedType());
//			}
//		} catch (JSONException e) {
//			Log.e("SNnMB", e.toString());
//		}
//
//		return json;
//	}

	/**
	 * @return the socialData
	 */
	public SocialData getSocialData() {
		return this.socialData;
	}

	/**
	 * @param socialData the socialData to set
	 */
	public void setSocialData(SocialData socialData) {
		this.socialData = socialData;
	}

	/**
	 * @return the filteredSensorData
	 */
	public DeviceSensorData getFilteredSensorData() {
		return this.sensorData;
	}

	/**
	 * @param filteredSensorData the filteredSensorData to set
	 */
	public void setFilteredSensorData(DeviceSensorData filteredSensorData) {
		this.sensorData = filteredSensorData;
	}


}
