package com.ubhave.sensocial.server.data;

import org.json.JSONObject;

import com.mongodb.util.JSON;
import com.ubhave.sensormanager.data.SensorData;

public class SocialEvent {

	private SocialData socialData;

	private DeviceSensorData sensorData;

	public SocialEvent(){
		//required to set all elements
	}

	public SocialEvent(SensorData rawData, String classifiedData, String streamId, String deviceId,
			String oSNFeed, String oSNName, String feedType){
		sensorData=new DeviceSensorData();
		sensorData.setClassifiedData(classifiedData);
		sensorData.setRawData(rawData);
		sensorData.setStreamId(streamId);
		sensorData.setDeviceId(deviceId);
		socialData=new SocialData();
		socialData.setOSNFeed(oSNFeed);
		socialData.setOSNName(oSNName);
		socialData.setFeedType(feedType);
	}

	
	public static SocialEvent getSocialEvent(JSONObject obj){
		SensorData rawData = null;
		String classifiedData = "Not found", streamId = "Not found", deviceId = "Not found", oSNFeed = "Not found", oSNName = "Not found", feedType = "Not found";
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
			feedType= obj.getString("feedtype");
		}
		catch(Exception e){
			System.out.println("Feed type not present or "+e.toString());
		}
		
		
		return new SocialEvent(rawData, classifiedData, streamId, deviceId, oSNFeed, oSNName, feedType);
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
