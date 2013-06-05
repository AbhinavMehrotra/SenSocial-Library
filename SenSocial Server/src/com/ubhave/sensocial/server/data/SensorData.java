package com.ubhave.sensocial.server.data;


public class SensorData {

	private SensorData rawData;

	private String classifiedData;
	
	private String streamId;

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
	 * @return the configurationName
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * @param streamId the configurationName to set
	 */
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
	
	
}
