package com.ubhave.sensocial.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.override;
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
		socialData.setOSNFeed(oSNFeed);
		socialData.setOSNName(oSNName);
		socialData.setFeedType(feedType);
	}
	
	/**
	 * Returns the SocialEvent in json-string format
	 */
	@override
	public String toJSONString(){
		//convert to string
		JSONObject json =new JSONObject();
		try {
			json.put("raw_data", sensorData.getRawData());
			json.put("classified_data", sensorData.getClassifiedData());
			json.put("stream_id_data", sensorData.getStreamId());
			json.put("device_id", sensorData.getDeviceId());
			json.put("osn_feed", socialData.getOSNFeed());
			json.put("osn_name", socialData.getOSNName());
			json.put("_osn_feed_type", socialData.getFeedType());
		} catch (JSONException e) {
			Log.e("SNnMB", e.toString());
		}
		
		return json.toString();
	}
	
	/**
	 * Returns the social event in json format
	 */
	public JSONObject toJSON(){
		//convert to json-object
				JSONObject json =new JSONObject();
				try {
					json.put("raw_data", sensorData.getRawData());
					json.put("classified_data", sensorData.getClassifiedData());
					json.put("stream_id_data", sensorData.getStreamId());
					json.put("device_id", sensorData.getDeviceId());
					json.put("osn_feed", socialData.getOSNFeed());
					json.put("osn_name", socialData.getOSNName());
					json.put("_osn_feed_type", socialData.getFeedType());
				} catch (JSONException e) {
					Log.e("SNnMB", e.toString());
				}
				
				return json;
	}

	/**
	 * @return the socialData
	 */
	public SocialData getSocialData() {
		return socialData;
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
		return sensorData;
	}

	/**
	 * @param filteredSensorData the filteredSensorData to set
	 */
	public void setFilteredSensorData(DeviceSensorData filteredSensorData) {
		this.sensorData = filteredSensorData;
	}

	
}
