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
package com.ubhave.sensocial.server.data;

import org.json.JSONObject;

import com.ubhave.sensormanager.data.SensorData;

public class SocialEvent {

	private SocialData socialData;

	private DeviceSensorData sensorData;

	/**
	 * Constructor
	 */
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
	
	/**
	 * Constructor
	 * @param rawData SensorData in raw format
	 * @param classifiedData SensorData in classified format
	 * @param streamId Stream id
	 * @param deviceId Device id
	 * @param userName User-name
	 * @param oSNFeed OSN feed
	 * @param oSNName OSN name
	 * @param feedType Feed type
	 * @param time Time at which feed arrived
	 */
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
		socialData.setTime(time);
	}

	/**
	 * Converts JSON String to SocialEvent
	 * @param JSONObject object
	 * @return SocialEvent object
	 */
	public static SocialEvent getSocialEvent(JSONObject obj){
		SensorData rawData = null;
		String classifiedData = "Not found", streamId = "Not found", deviceId = "Not found", userName = "Not found", oSNFeed = "Not found", oSNName = "Not found", feedType = "Not found", time= "Not found";
		try{
			rawData=(SensorData) obj.get("rawdata");
		}
		catch(Exception e){
			System.out.println("Raw data not present or "+e.toString());
		}
		try{
			classifiedData= obj.getString("classifieddata");
		}
		catch(Exception e){
			System.out.println("Classified data not present or "+e.toString());
		}
		try{
			streamId= obj.getString("streamid");
		}
		catch(Exception e){
			System.out.println("Stream id not present or "+e.toString());
		}
		try{
			deviceId= obj.getString("deviceid");
		}
		catch(Exception e){
			System.out.println("Device id not present or "+e.toString());
		}
		try{
			userName= obj.getString("username");
		}
		catch(Exception e){
			System.out.println("OSN user-name not present or "+e.toString());
		}
		try{
			oSNFeed= obj.getString("osnfeed");
		}
		catch(Exception e){
			System.out.println("OSN feed not present or "+e.toString());
		}
		try{
			oSNName= obj.getString("osnname");
		}
		catch(Exception e){
			System.out.println("OSN name not present or "+e.toString());
		}
		try{
			feedType= obj.getString("osnfeedtype");
		}
		catch(Exception e){
			System.out.println("Feed type not present or "+e.toString());
		}
		try{
			time= obj.getString("osntime");
		}
		catch(Exception e){
			System.out.println("Feed time not present or "+e.toString());
		}
		
		
		return new SocialEvent(rawData, classifiedData, streamId, deviceId, userName, oSNFeed, oSNName, feedType, time);
	}

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
