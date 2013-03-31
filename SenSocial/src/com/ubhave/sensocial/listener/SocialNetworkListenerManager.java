package com.ubhave.sensocial.listener;

import java.util.ArrayList;

import android.content.Context;


public class SocialNetworkListenerManager {
	
	private static ArrayList<SocialNetworkListner> listeners = new ArrayList<SocialNetworkListner>();

	/**
	 * Method to add an instance of  listener (SocialNetworkListner)
	 * @param listener SocialNetworkPostListner object
	 */
	public static void add(SocialNetworkListner listener) {
		listeners.add(listener);
	}

	/**
	 * Method to remove an instance of  listener (SocialNetworkListner)
	 * @param listener SocialNetworkPostListner object
	 */
	public static void remove(SocialNetworkListner listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Method used by the service class to push the data received by the service from server.
	 * It only be invoked by the service class. <br/>
	 * It checks for the first two characters if these are 10 then it is from Facebook else 11 means it is from Twitter.
	 * @param msg Data received by the service class from the server
	 */
	public static void newUpdateArrived(Context context, String data){
		String snName = null, message = null;
		//logic for data to classify between twitter/facebook
		if(data.startsWith("10")){
			snName="FACEBOOK";
		}
		else{
			snName="TWITTER";
		}
		message=data.substring(2);
		fireUpdate(snName, message);
	}

	/**
	 * Method to fire social-network updates to all the SocialNetworPostkListener(s).
	 * @param socialNetworkName  Name of the social-network from which an update has been received.
	 * @param message Message posted on the social-network by the user.
	 */
	private static void fireUpdate(String socialNetworkName, String message) {
		for (SocialNetworkListner listener:listeners) {
			if(socialNetworkName.equals("FACEBOOK")){
				listener.onFacebookPostReceived(message);
			}
			else{
				listener.onTwitterPostReceived(message);
			}
		}
	}
}
