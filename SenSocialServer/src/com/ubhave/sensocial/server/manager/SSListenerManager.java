package com.ubhave.sensocial.server.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubhave.sensocial.server.data.SocialEvent;


public class SSListenerManager {


	private static Map<SSListener, Set<String>> listeners = new HashMap<SSListener, Set<String>>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param configuration Configuration name
	 */
	protected static void add(SSListener listener, String streamId) {
		if(listeners.containsKey(listener)){
			for( Map.Entry<SSListener, Set<String>> entry : listeners.entrySet() ) {
				if( entry.getKey().equals(listener)) {
					Set<String> configs=entry.getValue();
					configs.add(streamId);
					entry.setValue(configs);
					break;
				}
			}
		}
		else{
			Set<String> configs=new HashSet<String>();
			configs.add(streamId);
			listeners.put(listener,configs);
		}
		System.out.println("Listener registered!");			
	}


	/**
	 * Method to remove an instance of  listener (SensorListner)
	 * @param listener SocialNetworkPostListner object
	 */
	protected static void remove(SSListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Method to fire updates to the registered SensorListeners on specific configuration.
	 * @param socialEvent 
	 */
	public static void fireUpdate(SocialEvent socialEvent){
		System.out.println("Listener Manager Map: "+ listeners);
		System.out.println("Looking for stream id: "+ socialEvent.getFilteredSensorData().getStreamId());	
		if(listeners.isEmpty()){
			System.out.println("No stream is registered!");			
		}
		else{
			System.out.println("Listeners present!!");	
			for (Map.Entry<SSListener, Set<String>> listener: listeners.entrySet()) {
				System.out.println("Stream set: "+ listener.getValue());
				for(String str:listener.getValue()){
					if(str.equalsIgnoreCase(socialEvent.getFilteredSensorData().getStreamId())){
						System.out.println("Firing stream now!!");	
						listener.getKey().onDataReceived(socialEvent);
						System.out.println("Stream fired!!");	
						break;
					}
				}
			}
		}

	}

}
