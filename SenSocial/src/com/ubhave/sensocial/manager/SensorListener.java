package com.ubhave.sensocial.manager;

public interface SensorListener {

	/**
	 * Listener for sensor data
	 * @param sensor_data The data-type may be String (for classified data) or SensorData (for raw data). 
	 */
	void onDataSensed(Object sensor_data);
}
