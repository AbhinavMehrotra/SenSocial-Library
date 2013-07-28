package com.ubhave.sensocial.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.FilterSettings;

/**
 * MyDevice class allows to access the properties of the local device, and creation,
 * removal of streams .
 */
public class MyDevice {

	private String deviceId;
	private Context context;
	private User user;

	/**
	 * Constructor
	 * @param context Application context
	 * @param user User of the device
	 */
	protected MyDevice(Context context, User user) {
		this.context=context;
		this.user=user;
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		deviceId=sp.getString("deviceid", "null");
	}


	/**
	 * Getter for Stream
	 * @param sensorId
	 * @param dataType
	 * @return Stream object
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(sensorId, dataType, context);
		return stream;
	}


	/**
	 * Removes the stream from the device
	 * @param stream
	 */
	public void removeStream(Stream stream){
		FilterSettings.removeConfiguration(stream.getStreamId());
		ConfigurationHandler.run(context);	 
	}

	/**
	 * Getter for device-id
	 * @return String device-id
	 */
	public String getDeviceId(){
		return deviceId;
	}

	/**
	 * Getter for User
	 * @return User object
	 */
	public User getUser(){
		return this.user;
	}

	/**
	 * Getter for Location
	 * @return Location object
	 */
	public Location getLastKnownLocation(){		
		Location location;
		//look for location in shared prefrences
		location= new Location(0, 0);
		//TODO: logic here
		return location;
	}



}
