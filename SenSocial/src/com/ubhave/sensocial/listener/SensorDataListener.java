package com.ubhave.sensocial.listener;


/**
 * To receive sensordata for each real time updates implement this listener. <br/>
 * To get these updates you should register your listener by using "registerSensorDataListener" 
 * method in SenSocialManager class.
 * @author Abhinav
 */
public interface SensorDataListener {

	/**
	 * It will receive the update from social network, with all event classified by the default classifier.
	 * @param message Message posted on the social-network by the user.
	 * @param accelerometer Moving or Not moving
	 * @param bluetooth Bluetooth names
	 * @param location Latitude and Longitude
	 * @param microphone Silent or Not
	 */
	public void onSNUpdateForAllEvents(String message, String accelerometer, String bluetooth,
			String location, String microphone, String wifi);
	
	/**
	 * It will receive the update from social network when user is moving(walking/running).
	 * @param data Message posted on the social-network by the user.
	 */
	public void onSNUpdateWhenUserIsMoving(String message);
	
	
	/**
	 * It will receive the update from social network when user is not moving.
	 * @param data Message posted on the social-network by the user.
	 */
	public void onSNUpdateWhenUserIsNotMoving(String message);
	
	/**
	 * It will receive the update from social network when user is at silent place.
	 * @param data Message posted on the social-network by the user.
	 */
	public void onSNUpdateWhenUserIsAtSilentPlace(String message);
	
	
	/**
	 * It will receive the update from social network when user is not at silent place.
	 * @param data Message posted on the social-network by the user.
	 */
	public void onSNUpdateWhenUserIsNotAtSilentPlace(String message);
	
}
