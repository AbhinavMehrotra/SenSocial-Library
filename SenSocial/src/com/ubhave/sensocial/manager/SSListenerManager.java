package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensormanager.data.SensorData;

public class SSListenerManager {


	private static Map<SSListener, Set<String>> listeners = new HashMap<SSListener, Set<String>>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param configuration Configuration name
	 */
	protected static void add(SSListener listener, String configuration) {
		if(listeners.containsKey(listener)){
			for( Map.Entry<SSListener, Set<String>> entry : listeners.entrySet() ) {
				if( entry.getKey().equals(listener)) {
					Set<String> configs=entry.getValue();
					configs.add(configuration);
					entry.setValue(configs);
					break;
				}
			}
		}
		else{
			Set<String> configs=new HashSet<String>();
			configs.add(configuration);
			listeners.put(listener,configs);
		}
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
			for (Map.Entry<SSListener, Set<String>> listener: listeners.entrySet()) {
				for(String str:listener.getValue()){
					if(str.equalsIgnoreCase(socialEvent.get(i).getFilteredSensorData().getStreamId())){
						listener.getKey().onDataSensed(socialEvent.get(i));
						break;
					}
				}
			}
		}
	}

	public static void fireUpdate(SocialEvent socialEvent){
		Log.e("SNnMB", "Firing data to listeners!");
		for (Map.Entry<SSListener, Set<String>> listener: listeners.entrySet()) {
			for(String str:listener.getValue()){
				if(str.equalsIgnoreCase(socialEvent.getFilteredSensorData().getStreamId())){
					Log.e("SNnMB", "Found listener for: "+socialEvent.getFilteredSensorData().getStreamId());
					listener.getKey().onDataSensed(socialEvent);
					break;
				}
			}
		}
	}

}
