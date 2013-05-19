package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterConfiguration {

	SharedPreferences sp;

	public TwitterConfiguration(Context context){
		sp=context.getSharedPreferences("snmbData",0);
	}
	
	/**
	 * Getter for Twitter subscription.
	 * @return Boolean True if user has subscribed to twitter else False.
	 */
	public Boolean getTwitter() {
		return sp.getBoolean("twitter", false);
	}
	
	/**
	 * Getter for Twitter app's Consumer Key
	 * @return String Twitter app's Consumer Key
	 */
	public String getTwitterConsumerKey() {
		return sp.getString("consumerKey", "");
	}
	
	/**
	 * Getter for Twitter app's Consumer Secret Key
	 * @return String Twitter app's Consumer Secret Key
	 */
	public String getTwitterConsumerSecretKey() {
		return sp.getString("consumerSecretKey", "");
	}
	
	
	/**
	 * Method to get the subscription of Twitter.
	 * @param twitter True to subscribe else False
	 * @param consumerKey Twitter app consumer key
	 * @param consumerSecretKey Twitter app consumer secret key
	 */
	public void setTwitterIds(String consumerKey, String consumerSecretKey) {
		Editor ed=sp.edit();
		ed.putBoolean("twitter", true);
		ed.putString("consumerKey", consumerKey);
		ed.putString("consumerSecretKey", consumerSecretKey);
		ed.commit();
	}

}
