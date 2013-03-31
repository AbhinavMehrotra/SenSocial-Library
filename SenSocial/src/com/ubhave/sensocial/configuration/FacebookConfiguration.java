package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FacebookConfiguration {

	SharedPreferences sp;

	public FacebookConfiguration(Context context){
		sp=context.getSharedPreferences("snmbData",0);
	}
	
	/**
	 * Getter for Facebook subscription.
	 * @return Boolean True if user has subscribed to facebook else False.
	 */
	public Boolean getFacebook() {
		return sp.getBoolean("facebook", false);
	}
	
	/**
	 * 'Getter for Facebook app's client id
	 * @return String Facebook app's client id
	 */
	public String getFacebookClientId() {
		return sp.getString("fbClientId", "");
	}
	
	/**
	 * 'Getter for Facebook app's client secret id
	 * @return String Facebook app's client secret id
	 */
	public String getFacebookClientSecretId() {
		return sp.getString("fbClientSecretId", "");
	}

	/**
	 * Method to set configuration for Facebook  app
	 * @param clientId String Facebook app's client id
	 * @param clientSecretId String Facebook app's client secret id
	 */
	public void subscribeToFacebook(String clientId, String clientSecretId) {
		Editor ed=sp.edit();
		ed.putBoolean("facebook", true);
		ed.putString("fbClientId", clientId);
		ed.putString("fbClientSecretId", clientSecretId);
		ed.commit();
	}

}
