package com.ubhave.sensocial.data;

import org.json.JSONException;
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
		sd.setFeedType("unknown");
		sd.setOSNFeed("unknown");
		sd.setOSNName("unknown");
		sd.setTime("unknown");
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
	public JSONObject toJSON(){
		//convert to json-object
		JSONObject json =new JSONObject();
		try {
			if(sensorData!=null){
				JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), sensorData.getRawData().getSensorType());
				String str=formatter.toJSON(sensorData.getRawData()).toString();
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
