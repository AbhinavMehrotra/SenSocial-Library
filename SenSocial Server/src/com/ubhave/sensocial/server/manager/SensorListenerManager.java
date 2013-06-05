package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ubhave.sensocial.server.data.SocialEvent;


public class SensorListenerManager {


	private static Map<SensorListener, String> listeners = new HashMap<SensorListener, String>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param configuration Configuration name
	 */
	protected static void add(SensorListener listener, String streamId) {
		listeners.put(listener,streamId);
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
	 * @param socialEvent 
	 */
	public static void fireUpdate(SocialEvent socialEvent){
		for (Map.Entry<SensorListener, String> listener: listeners.entrySet()) {
			if(listener.getValue().equalsIgnoreCase(socialEvent.getFilteredSensorData().getStreamId())){
				listener.getKey().onDataReceived(socialEvent);
			}
		}

	}

}
