package com.ubhave.sensocial.listener;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;

public class SensorDataListenerManager {

	private static ArrayList<SensorDataListener> listeners = new ArrayList<SensorDataListener>();

	/**
	 * Method to add an instance of  listener (SensorDataListner)
	 * @param listener SocialNetworkPostListner object
	 */
	public static void add(SensorDataListener listener) {
		listeners.add(listener);
	}

	/**
	 * Method to remove an instance of  listener (SensorDataListner)
	 * @param listener SocialNetworkPostListner object
	 */
	public static void remove(SensorDataListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Method to fire updates to the registered SensorDataListeners.
	 * @param message Message posted on the social-network by the user.
	 * @param event Activity of user
	 */
	public static void newUpdateArrived(String message, String event){
		fireUpdate(message, event);
	}

	/**
	 * Method to fire updates to the registered SensorDatListeners on specific method.
	 * @param message Message posted on the social-network by the user.
	 * @param event Activity of user
	 */
	private static void fireUpdate(String message, String event) {
		for (SensorDataListener listener:listeners) {
			if(event.equals("silent")){
				listener.onSNUpdateWhenUserIsAtSilentPlace(message);
			}
			else if(event.equals("moving")){
				listener.onSNUpdateWhenUserIsMoving(message);
			}			
			else if(event.equals("not_silent")){
				listener.onSNUpdateWhenUserIsNotAtSilentPlace(message);
			}
			else if(event.equals("not_moving")){
				listener.onSNUpdateWhenUserIsNotMoving(message);
			}
		}
	}
	

	/**
	 * Method to fire updates to the registered SensorDatListeners on onSNUpdateForAllEvents method.
	 * @param data Hashtable<String, String> contains sensor name as key and sensed event for the sensor
	 */
	public static void fireUpdateForAllEvent(Hashtable<String, String> data){
		data=putDefaultValueIfNotPresent(data);
		for (SensorDataListener listener:listeners) {
			listener.onSNUpdateForAllEvents(data.get("message"), data.get("accelerometer"), 
					data.get("bluetooth"), data.get("location"), data.get("microphone"), data.get("wifi"));
		}
	}
	
	private static Hashtable<String, String> putDefaultValueIfNotPresent(Hashtable<String, String> data){
		if(data.get("accelerometer")==null) data.put("accelerometer", "not configured");
		if(data.get("bluetooth")==null) data.put("bluetooth", "not configured");
		if(data.get("location")==null) data.put("location", "not configured");
		if(data.get("microphone")==null) data.put("microphone", "not configured");
		if(data.get("wifi")==null) data.put("wifi", "not configured");
		return data;		
	}
	
}
