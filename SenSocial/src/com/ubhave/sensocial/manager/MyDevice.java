package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.FilterSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyDevice {

	private String uuid;
	private Context context;
	private User user;

	protected MyDevice(Context context, User user) {
		this.context=context;
		this.user=user;
		SharedPreferences sp=context.getSharedPreferences("SNnMB", 0);
		if(sp.getString("uuid", "null").equals("null")){
			uuid=UUID.randomUUID().toString();
			Editor ed=sp.edit();
			ed.putString("uuid", uuid);
			ed.commit();
		}
		else{
			uuid=sp.getString("uuid", "null");
		}
	}
	

	public Stream getStream(int sensorId, String dataType) throws PPDException, SensorDataTypeException{
		Stream stream=new Stream(sensorId, dataType, context);
		return stream;
	}
	

	public void removeStream(Stream stream){
		FilterSettings.removeConfiguration(stream.getFilter().getFilterName()); 
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
