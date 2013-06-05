package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ubhave.sensocial.data.SocialEvent;

public class SSListenerManager {


	private static Map<SSListener, String> listeners = new HashMap<SSListener, String>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param configuration Configuration name
	 */
	protected static void add(SSListener listener, String configuration) {
		listeners.put(listener,configuration);
	}
	
	
	/**
	 * Method to remove an instance of  listener (SensorListner)
	 * @param listener SocialNetworkPostListner object
	 */
	protected static void remove(SSListener listener) {
		listeners.remove(listener);
	}
	
//	/**
//	 * Method to fire updates to the registered SensorListeners on specific configuration.
//	 * @param sensor_data Sensor Data for which the listener is configured
//	 * @param configuration Configuration name
//	 */
//	protected static void fireUpdate(Object sensor_data, String configuration) {
//		for (Map.Entry<SensorListener, String> listener: listeners.entrySet()) {
//			if(listener.getValue().equalsIgnoreCase(configuration)){
//				listener.getKey().onDataSensed(sensor_data);
//			}
//		}
//	}
	
	/**
	 * Method to fire updates to the registered SensorListeners on specific configuration.
	 * @param socialEvent ArrayList
	 */
	public static void fireUpdate(ArrayList<SocialEvent> socialEvent){
		for(int i=0;i<socialEvent.size();i++){
			for (Map.Entry<SSListener, String> listener: listeners.entrySet()) {
				if(listener.getValue().equalsIgnoreCase(socialEvent.get(i).getFilteredSensorData().getConfigurationName())){
					listener.getKey().onDataSensed(socialEvent.get(i));
				}
			}
		}
	}
	
}
