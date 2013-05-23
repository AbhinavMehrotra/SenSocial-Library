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
	private AllPullSensors aps;

	public StartPullSensors(Context context){
		aps=new AllPullSensors(context);
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
	
	public void startIndependentContinuousStreamSensing(String sensor){
		try {
			ArrayList<Integer> SensorIds=new ArrayList<Integer>();
			SensorIds.add(aps.getSensorIdByName(sensor));
			new ContinuousStreamSensing (context, SensorIds).startSensing();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void stopIndependentContinuousStreamSensing(String sensor){
		try {
			ArrayList<Integer> SensorIds=new ArrayList<Integer>();
			SensorIds.add(aps.getSensorIdByName(sensor));
			new ContinuousStreamSensing (context, SensorIds).stopSensing();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}
	}
}
