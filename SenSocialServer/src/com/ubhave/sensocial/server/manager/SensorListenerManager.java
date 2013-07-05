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
		System.out.println("New stream is registered!");	
		listeners.put(listener,streamId);
	}


	/**
	 * Method to remove an instance of  listener (SensorListner)
	 * @param listener SocialNetworkPostListner object
	 */
	protected static void remove(SensorListener listener) {
		System.out.println("A stream is removed!");	
		listeners.remove(listener);
	}

	/**
	 * Method to fire updates to the registered SensorListeners on specific configuration.
	 * @param socialEvent 
	 */
	public static void fireUpdate(SocialEvent socialEvent){
		if(listeners.isEmpty()){
			System.out.println("No stream is registered!");			
		}
		else{
			System.out.println("In fire-update looking for stream id: "+ socialEvent.getFilteredSensorData().getStreamId());
			for (Map.Entry<SensorListener, String> listener: listeners.entrySet()) {
				System.out.println("Stream id present: "+ listener.getValue());
				if(listener.getValue().equalsIgnoreCase(socialEvent.getFilteredSensorData().getStreamId())){
					System.out.println("Listener found");
					listener.getKey().onDataReceived(socialEvent);
				}
				else{
					System.out.println("Stream not found");					
				}
			}
		}

	}

}
