package com.ubhave.sensocial.manager;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.FilterSettings;

public class MyDevice {

	private String uuid;
	private Context context;
	private User user;

	protected MyDevice(Context context, User user) {
		this.context=context;
		this.user=user;
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
	

		uuid=sp.getString("deviceid", "null");
		Log.e("SNnMB", "Device id in MYDevice is: "+uuid);
		
//		if(sp.getString("deviceid", "null").equals("null")){
//			uuid=UUID.randomUUID().toString().substring(0, 6);
//			Editor ed=sp.edit();
//			ed.putString("deviceid", uuid);
//			ed.commit();
//		}
//		else{
//			uuid=sp.getString("deviceid", "null");
//		}
	}


	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(sensorId, dataType, context);
		return stream;
	}


	public void removeStream(Stream stream){
		FilterSettings.removeConfiguration(stream.getStreamId());
		ConfigurationHandler.run(context);	 
	}

	public String getDeviceId(){
		return uuid;
	}

	public User getUser(){
		return this.user;
	}

	public Location getLastKnownLocation(){		
		Location location;
		//look for location in shared prefrences
		location= new Location(0, 0);
		return location;
	}



}
