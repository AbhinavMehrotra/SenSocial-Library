package com.ubhave.sensocial.server.data;

import com.ubhave.sensormanager.data.SensorData;


public class DeviceSensorData {
	
	protected DeviceSensorData(){}

	private SensorData rawData;

	private String classifiedData;
	
	private String streamId;
	
	private String deviceId;

	/**
	 * @return the rawData
	 */
	public SensorData getRawData() {
		return rawData;
	}

	/**
	 * @param rawData the rawData to set
	 */
	public void setRawData(SensorData rawData) {
		this.rawData = rawData;
	}

	/**
	 * @return the classifiedData
	 */
	public String getClassifiedData() {
		return classifiedData;
	}

	/**
	 * @param classifiedData the classifiedData to set
	 */
	public void setClassifiedData(String classifiedData) {
		this.classifiedData = classifiedData;
	}

	/**
	 * @return the streamId
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * @param streamId the streamId to set
	 */
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	
	
}
