package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class ContinuousStreamSensing {
	
	final private String TAG = "SNnMB";
	private final ESSensorManager sensorManager;
	private final Context context;
	private ArrayList<Integer> SensorIds;
	private String message;
	private SharedPreferences sp;
	private Editor ed;
	private int subscriptionId;
	private AllPullSensors aps;

	public ContinuousStreamSensing(Context context, ArrayList<Integer> SensorIds) throws ESException
	{
		this.context=context;
		this.SensorIds=SensorIds;
		sensorManager = ESSensorManager.getSensorManager(context);
		sp=context.getSharedPreferences("snmbData",0);
	}

	protected void startSensing() throws ESException{
		SensorDataListener listener = new SensorDataListener() {

			public void onDataSensed(SensorData arg0) {
				// TODO Auto-generated method stub

			}

			public void onCrossingLowBatteryThreshold(boolean arg0) {
				// can pause sensing
			}
		};
		ed=sp.edit();
		for(int i=0;i<SensorIds.size();i++){
			subscriptionId=sensorManager.subscribeToSensorData (SensorIds.get(i), listener);
			ed.putInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", subscriptionId);			
		}
		ed.commit();
	}
	
	protected void stopSensing() throws ESException{
		for(int i=0;i<SensorIds.size();i++){
			subscriptionId=sp.getInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", 0);
			if(subscriptionId != 0){
				sensorManager.unsubscribeFromSensorData(subscriptionId);
			}		
		}
	}
}
