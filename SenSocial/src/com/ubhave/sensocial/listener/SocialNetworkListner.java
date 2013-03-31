package com.ubhave.sensocial.listener;

/**
 * To receive real time updates implement this listener. <br/>
 * To get these updates you should register your listener by using "registerSocialNetworkListener" 
 * method in SenSocialManager class.
 * @author Abhinav
 */
public interface SocialNetworkListner {

	/**
	 * This will receive message posted on Facebook in real time.
	 * You will only receive message here if you have authenticated and enabled Facebook triggers..
	 * @param message (String) Message posted on Facebook.
	 */
	public void onFacebookPostReceived(String message);
	
	/**
	 * This will receive tweet posted on Twitter in real time.
	 * You will only receive message here if you have authenticated and enabled Facebook triggers..
	 * @param message (String) Tweet posted on Twitter.
	 */
	public void onTwitterPostReceived(String message);
}
