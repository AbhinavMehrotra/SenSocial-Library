package com.sensocial.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;

public class StartPullSensors {

	final private String TAG = "SNnMB";
	private ArrayList<Integer> SensorIds;
	private final Context context;
	
	public StartPullSensors(Context context){
		AllPullSensors aps=new AllPullSensors(context);
		this.SensorIds=aps.getIds();
		this.context=context;
	}
	
	/**
	 * Method to initiate sensing from the configured sensors.
	 */
	public void StartSensing(){
			try {
				new SampleAllConfiguredSensors(context, SensorIds).execute();
			} catch (ESException e) {
				Log.e(TAG, e.toString());
			}
	}
}
