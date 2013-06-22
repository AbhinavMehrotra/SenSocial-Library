package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

public class OneOffSensing extends AsyncTask<Void, Void, ArrayList<SensorData>>
{
	final private String TAG = "SNnMB";
	private final ESSensorManager sensorManager;
	private ArrayList<Integer> SensorIds;
	private ArrayList<SensorData> sensorData=new ArrayList<SensorData>();

	public OneOffSensing(Context context, ArrayList<Integer> SensorIds) throws ESException
	{
		this.SensorIds=SensorIds;
		sensorManager = ESSensorManager.getSensorManager(context);
	}

	@Override
	protected ArrayList<SensorData> doInBackground(Void... params)
	{
		try{		
			for(int i=0;i<SensorIds.size();i++){
				Log.d(TAG, "Sampling from Sensor");
				sensorData.add(sensorManager.getDataFromSensor(SensorIds.get(i)));
			}
		}
		catch (ESException e){
			Log.e(TAG, e.toString());
			return null;
		}

		Log.i(TAG, "Data sensed is:");
		for(SensorData x:sensorData)
			Log.i(TAG, x.toString());
		return(sensorData);
	}


}
