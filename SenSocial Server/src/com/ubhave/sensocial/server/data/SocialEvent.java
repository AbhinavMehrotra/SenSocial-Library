package com.ubhave.sensocial.server.data;


public class SocialEvent {

	private String deviceId;
	private SocialData socialData;
	
	private SensorData filteredSensorData;

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
	public SensorData getFilteredSensorData() {
		return filteredSensorData;
	}

	/**
	 * @param filteredSensorData the filteredSensorData to set
	 */
	public void setFilteredSensorData(SensorData filteredSensorData) {
		this.filteredSensorData = filteredSensorData;
	}

	
}
