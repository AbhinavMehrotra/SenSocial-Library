package com.ubhave.sensocial.sensordata.classifier;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class ClassifyLocationData {

	private Context context;
	private String TAG= "SNnMB";
	
	protected ClassifyLocationData(Context context) {
		this.context=context;
	}
	
	public String getAddress(double lat, double lng) {
	    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
	    String add="";
	    try {
	        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
	        Address obj = addresses.get(0);
	        add = obj.getAddressLine(0);
	        add = add + "\n" + obj.getCountryName();
	        add = add + "\n" + obj.getCountryCode();
	        add = add + "\n" + obj.getAdminArea();
	        add = add + "\n" + obj.getPostalCode();
	        add = add + "\n" + obj.getSubAdminArea();
	        add = add + "\n" + obj.getLocality();
	        add = add + "\n" + obj.getSubThoroughfare();
	        Log.v(TAG, "Address: " + add);
	    } catch (IOException e) {
	    	Log.e(TAG, e.toString());
	    }
	    return add;
	}

}
