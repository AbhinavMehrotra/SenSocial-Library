
package com.sensocial.manager;

import com.sensocial.exceptions.InvalidUrlException;


public interface SenSocialManagerInterface
{
	/**
	 * Method to subscribe for Facebook updates.
	 * @param facebook True to subscribe else False
	 * @param appId  Facebook appId if subscribing for Facebook else null
	 */
	public void subscribeToFacebook(Boolean facebook, String appId);

	/**
	 * Method to subscribe for Twitter updates.
	 * @param twitter True to subscribe else False
	 * @param consumerKey  Twitter app consumer key
	 * @param consumerSecretKey  Twitter app consumer secret key
	 */
	public void subscribeToTwitter(Boolean twitter, String consumerKey, String consumerSecretKey);
	
	/**
	 * Method to subscribe for sensors. <br/>
	 * Give True to subscribe else False.	
	 * @param accelerometer
	 * @param bluetooth
	 * @param wifi
	 * @param location
	 * @param microphone
	 */
	public void sensorSubscription(Boolean accelerometer, Boolean bluetooth, Boolean wifi, Boolean location, Boolean microphone);
	
	
	/**
	 * Method to set server configuration.
	 * @param serverUrl
	 * @throws InvalidUrlException
	 */
	public void setServerURL(String serverUrl)  throws InvalidUrlException;
	
	/**
	 * Method to set server configuration where MQTT broker is running.
	 * @param serverIP
	 * @throws InvalidUrlException
	 */
	public void setMQTTServerIP(String serverIP)  throws InvalidUrlException;
	
}
