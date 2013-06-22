package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.sensocial.exceptions.ServerException;

public class MQTTServerConfiguration {
	
	SharedPreferences sp;
	private String ip;

	public MQTTServerConfiguration(Context context) {
		sp=context.getSharedPreferences("SSDATA",0);
	}

	/**
	 * Method to set the server IP where the MQTT broker is running. <br/>
	 * This URL should be the path of the server where all the PHP scripts are hosted. <br/>
	 * It should not end with "/".
	 * @param url String
	 * @throws ServerException
	 */
	public void setMQTTServerIP(String IP) throws ServerException{
		if(IP.endsWith("/")){
			throw new ServerException(IP+" is not a valid URL. \nURL should not contain / at the end, eg- https//:cs.bham.ac.uk or 10.101.10.101");
		}
		else{
			this.ip=IP;
		}
	}
	
	/**
	 * Method to get the server IP where the MQTT broker is running.
	 * @return String
	 */
	public String getMQTTServerIP(){
		return ip;
	}
	
	
}
