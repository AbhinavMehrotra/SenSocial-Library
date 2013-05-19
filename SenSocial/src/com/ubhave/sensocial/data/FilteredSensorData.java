package com.ubhave.sensocial.data;

import com.ubhave.sensormanager.data.SensorData;

public class FilteredSensorData {

	private SensorData rawData;

	private String classifiedData;
	
	private String configurationName;

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
	public String getConfigurationName() {
		return configurationName;
	}

	/**
	 * @param configurationName the configurationName to set
	 */
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
	
	
}
