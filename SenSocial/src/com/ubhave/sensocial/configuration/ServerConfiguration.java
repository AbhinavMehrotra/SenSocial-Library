package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.exceptions.ServerException;

public class ServerConfiguration {
	private static final String TAG = "snmbData";
	SharedPreferences sp;
	private Context context;
	private String brokerUrl;

	
	/**
	 * Constructor for ServerConfiguration.
	 * @param context Application Context
	 * @param mqttcofig MQTTServerConfiguration object. (It can be null if you wish to use HTTP service.
	 */
	public ServerConfiguration(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA", 0);
		Editor ed=sp.edit();
		ed.putInt("refreshInterval", 60000);  //default interval time
		ed.commit();
	}
	
	
	/**
	 * Sets MQTT broker URL
	 * @param urlWithPort
	 */
	public void setMQTTBrokerURL(String urlWithPort){
		Editor ed=sp.edit();
		ed.putString("mqqt_broker_url", urlWithPort);
		ed.commit();
	}
	
	
	/**
	 * Gets MQTT broker URL
	 * @return
	 */
	public String getMQTTBrokerURL(){
		return sp.getString("mqqt_broker_url", "");
	}

	
	/**
	 * Method to set the server address. <br/>
	 * This URL should be the path of the server where all the PHP scripts are hosted. <br/>
	 * It should always end with "/".
	 * @param url String
	 * @throws ServerException
	 */
	public void setServerProjectURL(String url) throws ServerException{
		if(!url.endsWith("/")){
			throw new ServerException(url+" is not a valid URL. \nURL should contain / at the end, eg- https//:cs.bham.ac.uk/sensocial/");
		}
		else{
			Log.i(TAG, "Server url: "+ url);
			Editor ed=sp.edit();
			ed.putString("serverurl", url);
			ed.commit();
		}
	}

	
	/**
	 * Gets the server address where the project is hosted.
	 * @return String
	 */
	public String getServerProjectURL(){
		return sp.getString("serverurl", "");
	}
	
	/**
	 * Setter
	 * @param serverIP
	 */
	public void setServerIP(String serverIP){
		Log.i(TAG, "ServerIP: "+ serverIP);
		Editor ed=sp.edit();
		ed.putString("serverip", serverIP);
		ed.commit();
	}
	/**
	 * Gets MQTT broker URL
	 * @return
	 */
	public String getServerIP(){
		return sp.getString("serverip", "");
	}
	
	
	/**
	 * Sets server port
	 * @param port
	 */
	public void setServerPort(int port){
		Log.i(TAG, "Server port: "+ port);
			Editor ed=sp.edit();
			ed.putInt("serverport", port);
			ed.commit();
	}

	
	/**
	 * Gets server port
	 * @return
	 */
	public int getServerPort(){
		return sp.getInt("serverport", 0);
	}
	
	
	/**
	 * Method to set the interval to check for trigger. <br/>
	 * To be set only if you are using HTTP triggers.
	 * @param secondss Seconds
	 */
	public void setRefreshInterval(int seconds){
		Log.i(TAG, "RefreshInterval: "+ seconds);
		Editor ed=sp.edit();
		ed.putInt("refreshinterval", seconds*1000);
		ed.commit();
		Log.i(TAG, "RI set as: "+ sp.getInt("refreshinterval", 0));
	}
	
	
}
