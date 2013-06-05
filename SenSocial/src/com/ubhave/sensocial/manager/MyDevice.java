package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.FilterConfiguration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyDevice {

	private String uuid;
	private Context context;

	protected MyDevice(Context context) {
		this.context=context;
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
		FilterConfiguration.deleteConfiguration(stream.getFilter().getFilterName()); 
	}

	public String getDeviceUUID(){
		return uuid;
	}
	
	public String getUserName(){
		//look for the user of device
		return null;
	}

	public Location getLocation(){		
		Location location;
		//look for location in shared prefrences
		location= new Location(0, 0);
		return location;
	}



}
