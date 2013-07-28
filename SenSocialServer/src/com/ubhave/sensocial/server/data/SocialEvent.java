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
