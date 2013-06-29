package com.ubhave.sensocial.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.override;
import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
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
			if(sensorData!=null){
				JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorData.getRawData().getSensorType());
				String str=formatter.toJSON(sensorData.getRawData()).toJSONString();
				json.put("rawdata", str);
				json.put("classifieddata", sensorData.getClassifiedData());
				json.put("streamid", sensorData.getStreamId());
				json.put("deviceid", sensorData.getDeviceId());
			}
			if(socialData!=null){
				json.put("osnfeed", socialData.getOSNFeed());
				json.put("osnname", socialData.getOSNName());
				json.put("osnfeedtype", socialData.getFeedType());
			}
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
			if(sensorData!=null){
				JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorData.getRawData().getSensorType());
				String str=formatter.toJSON(sensorData.getRawData()).toJSONString();
				json.put("rawdata", str);
				json.put("classifieddata", sensorData.getClassifiedData());
				json.put("streamid", sensorData.getStreamId());
				json.put("deviceid", sensorData.getDeviceId());
			}
			if(socialData!=null){
				json.put("osnfeed", socialData.getOSNFeed());
				json.put("osnname", socialData.getOSNName());
				json.put("osnfeedtype", socialData.getFeedType());
			}
		} catch (JSONException e) {
			Log.e("SNnMB", e.toString());
		}

		return json;
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
