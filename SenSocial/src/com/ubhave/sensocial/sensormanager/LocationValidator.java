package com.ubhave.sensocial.sensormanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.LocationData;

/**
 * LocationValidator class enables validating the location data.
 * <br>**Bug fix for unknown/(0,0) location**.
 */
public class LocationValidator {

	private static LocationManager lm;
	
	protected static SensorData validateLocation(SensorData data){
		JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), data.getSensorType());
		String str=formatter.toJSON(data).toString();
		JSONObject obj;
		String lat = "0", lon = "0";
		try {
			obj = new JSONObject(str);
			lat=obj.get("latitude").toString();
			lon=obj.get("longitude").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(lat.equalsIgnoreCase("unknown") || lat.equalsIgnoreCase("0") || lon.equalsIgnoreCase("unknown") || lon.equalsIgnoreCase("0")){
			Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			LocationData loc_data = (LocationData) data;
			loc_data.setLocation(loc);
			return loc_data;
		}
		else{
			return data;			
		}
	}
}
