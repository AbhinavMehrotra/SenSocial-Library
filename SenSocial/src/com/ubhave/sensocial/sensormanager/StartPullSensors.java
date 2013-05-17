package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class StartPullSensors {

	final private String TAG = "SNnMB";
	private ArrayList<Integer> SensorIds;
	private final Context context;
	private String message;

	public StartPullSensors(Context context){
		AllPullSensors aps=new AllPullSensors(context);
		this.SensorIds=aps.getIds();
		this.context=context;
	}
	
	public void startOneOffSensingWithOSN(String message){
		try {
			new OneOffSensing(context, SensorIds, message).execute();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * Method to initiate sensing from the configured sensors.
	 */
	public void startIndependentOneOffSensing(){
		try {
			new OneOffSensing(context, SensorIds, null).execute();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}

	public void startIndependentContinuousStreamSensing(){
		try {
			new ContinuousStreamSensing (context, SensorIds).startSensing();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void stopIndependentContinuousStreamSensing(){
		try {
			new ContinuousStreamSensing (context, SensorIds).stopSensing();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}
}
