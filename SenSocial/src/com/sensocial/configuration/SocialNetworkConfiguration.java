package com.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SocialNetworkConfiguration {
	SharedPreferences sp;

	public SocialNetworkConfiguration(Context context){
		sp=context.getSharedPreferences("snmbData",0);
	}

	/**
	 * Method to get the subscription of Facebook. 
	 * @return Boolean True if Facebook is subscribed else False
	 */
	public Boolean getFacebook() {
		return sp.getBoolean("facebook", false);
	}

	/**
	 * Method to get the subscription of Facebook.
	 * @param facebook True to subscribe else False
	 * @param appId Facebook app id
	 */
	public void setFacebook(Boolean facebook, String appId) {
		Editor ed=sp.edit();
		ed.putBoolean("facebook", facebook);
		ed.putString("fbid", appId);
		ed.commit();
	}
	
	/**
	 * Method to get the subscription of Twitter. 
	 * @return Boolean True if Twitter is subscribed else False
	 */
	public Boolean getTwitter() {
		return sp.getBoolean("twitter", false);
	}
	
	/**
	 * Method to get the subscription of Twitter.
	 * @param twitter True to subscribe else False
	 * @param consumerKey Twitter app consumer key
	 * @param consumerSecretKey Twitter app consumer secret key
	 */
	public void setTwitter(Boolean twitter, String consumerKey, String consumerSecretKey) {
		Editor ed=sp.edit();
		ed.putBoolean("twitter", twitter);
		ed.putString("consumerKey", consumerKey);
		ed.putString("consumerSecretKey", consumerSecretKey);
		ed.commit();
	}
}
