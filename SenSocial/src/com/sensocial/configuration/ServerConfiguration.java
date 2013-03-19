package com.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sensocial.exceptions.InvalidUrlException;

public class ServerConfiguration {
	SharedPreferences sp;

	public ServerConfiguration(Context context){
		sp=context.getSharedPreferences("snmbData",0);
	}

	/**
	 * Method to set the server address. <br/>
	 * This URL should be the path of the server where all the PHP scripts are hosted. <br/>
	 * It should always end with "/".
	 * @param url String
	 * @throws InvalidUrlException
	 */
	public void setServerURL(String url) throws InvalidUrlException{
		if(!url.endsWith("/")){
			throw new InvalidUrlException(url+" is not a valid URL. \nURL should contain / at the end, eg- https//:cs.bham.ac.uk/sensocial/");
		}
		else{
			Editor ed=sp.edit();
			ed.putString("server", url);
			ed.commit();
		}
	}

	/**
	 * Method to get the server address where the PHP scripts are running.
	 * @return String
	 */
	public String getServerURL(){
		return sp.getString("server", "");
	}
	
	/**
	 * Method to set the server IP where the MQTT broker is running. <br/>
	 * This URL should be the path of the server where all the PHP scripts are hosted. <br/>
	 * It should not end with "/".
	 * @param url String
	 * @throws InvalidUrlException
	 */
	public void setMQTTServerIP(String url) throws InvalidUrlException{
		if(url.endsWith("/")){
			throw new InvalidUrlException(url+" is not a valid URL. \nURL should not contain / at the end, eg- https//:cs.bham.ac.uk or 10.101.10.101");
		}
		else{
			Editor ed=sp.edit();
			ed.putString("mqtt", url);
			ed.commit();
		}
	}
	
	/**
	 * Method to get the server IP where the MQTT broker is running.
	 * @return String
	 */
	public String getMQTTServerIP(){
		return sp.getString("mqtt", "");
	}
}
