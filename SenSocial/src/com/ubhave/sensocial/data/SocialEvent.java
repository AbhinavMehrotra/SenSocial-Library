package com.ubhave.sensocial.data;

import com.ubhave.sensormanager.data.SensorData;

public abstract class SocialEvent {

	private SocialData socialData;
	
	private FilteredSensorData filteredSensorData;

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
	public FilteredSensorData getFilteredSensorData() {
		return filteredSensorData;
	}

	/**
	 * @param filteredSensorData the filteredSensorData to set
	 */
	public void setFilteredSensorData(FilteredSensorData filteredSensorData) {
		this.filteredSensorData = filteredSensorData;
	}

	
}
