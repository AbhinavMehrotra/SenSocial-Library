package com.sensocial.manager;

import android.content.Context;

import com.sensocial.configuration.SensorConfiguration;
import com.sensocial.configuration.ServerConfiguration;
import com.sensocial.configuration.SocialNetworkConfiguration;
import com.sensocial.exceptions.InvalidUrlException;

public class SenSocialManager implements SenSocialManagerInterface{

	private SensorConfiguration sensorConfig;
	private ServerConfiguration serverConfig;
	private SocialNetworkConfiguration SNConfig;
	
	public SenSocialManager(Context context) {
		sensorConfig=new SensorConfiguration(context);
		serverConfig=new ServerConfiguration(context);
		SNConfig=new SocialNetworkConfiguration(context);
	}

	public void subscribeToFacebook(Boolean facebook, String appId) {
		SNConfig.setFacebook(facebook, appId);		
	}
	
	public void subscribeToTwitter(Boolean twitter, String consumerKey, String consumerSecretKey) {
		SNConfig.setTwitter(twitter, consumerKey, consumerSecretKey);		
	}

	public void sensorSubscription(Boolean accelerometer, Boolean bluetooth,
			Boolean wifi, Boolean location, Boolean microphone) {
		sensorConfig.setAccelerometer(accelerometer);
		sensorConfig.setBluetooth(bluetooth);
		sensorConfig.setWifi(wifi);
		sensorConfig.setLocation(location);
		sensorConfig.setMicrophone(microphone);
	}

	public void setServerURL(String serverUrl) throws InvalidUrlException {
		serverConfig.setServerURL(serverUrl);		
	}

	public void setMQTTServerIP(String serverIP)  throws InvalidUrlException{
		serverConfig.setMQTTServerIP(serverIP);
	}

}
