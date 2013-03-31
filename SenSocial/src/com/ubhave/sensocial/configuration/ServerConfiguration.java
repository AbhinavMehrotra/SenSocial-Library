package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ubhave.sensocial.exceptions.InvalidUrlException;

public class ServerConfiguration {
	SharedPreferences sp;

	/**
	 * Constructor for ServerConfiguration.
	 * @param context Application Context
	 * @param mqttcofig MQTTServerConfiguration object. (It can be null if you wish to use HTTP service.
	 */
	public ServerConfiguration(Context context, MQTTServerConfiguration mqttcofig){
		sp=context.getSharedPreferences("snmbData",0);
		if(mqttcofig!=null){
			Editor ed=sp.edit();
			ed.putString("mqtt", mqttcofig.getMQTTServerIP());
			ed.commit();
		}
		else{
			Editor ed=sp.edit();
			ed.putString("mqtt", "null");
			ed.commit();
		}
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
			ed.putInt("refreshInterval", 60*1000);  //default interval time
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
	 * Method to set the interval to check for trigger. <br/>
	 * To be set only if you are using HTTP triggers.
	 * @param secondss Seconds
	 */
	public void setRefreshInterval(int seconds){
		Editor ed=sp.edit();
		ed.putInt("refreshInterval", seconds*1000);
		ed.commit();
	}
	
	
}
