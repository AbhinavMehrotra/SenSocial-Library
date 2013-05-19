package com.ubhave.sensocial.manager;

import java.util.HashMap;
import java.util.Map;

public class SensorListenerManager {


	private static Map<SensorListener, String> listeners = new HashMap<SensorListener, String>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param configuration Configuration name
	 */
	protected static void add(SensorListener listener, String configuration) {
		listeners.put(listener,configuration);
	}
	
	
	/**
	 * Method to remove an instance of  listener (SensorListner)
	 * @param listener SocialNetworkPostListner object
	 */
	protected static void remove(SensorListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Method to fire updates to the registered SensorListeners on specific configuration.
	 * @param sensor_data Sensor Data for which the listener is configured
	 * @param configuration Configuration name
	 */
	protected static void fireUpdate(Object sensor_data, String configuration) {
		for (Map.Entry<SensorListener, String> listener: listeners.entrySet()) {
			if(listener.getValue().equalsIgnoreCase(configuration)){
				listener.getKey().onDataSensed(sensor_data);
			}
		}
	}
	
}
