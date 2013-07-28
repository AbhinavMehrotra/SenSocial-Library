package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

/**
 * OneOffSensing class enables fetching sensor data for the instance of time.
 * It extends AsyncTask so that the sensing process is done in the background.
 */
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
		Log.d(TAG, "One-off sensing constructor, sensor ids: "+SensorIds);
	}

	@Override
	protected ArrayList<SensorData> doInBackground(Void... params)
	{
		Log.d(TAG, "One-off sensing doInBackground, sensor ids: "+SensorIds);
		try{		
			for(int i=0;i<SensorIds.size();i++){
				Log.d(TAG, "Sampling from Sensor");
				if(SensorDataCollector.isRegistered(SensorIds.get(i))){
					Log.e(TAG, "One-Off sensing process, sensor already registered. Found the latest data available for: "+SensorIds.get(i));
					sensorData.add(SensorDataCollector.getData(SensorIds.get(i)));
				}
				else if(SensorDataCollector.isPresent(SensorIds.get(i))){
					Log.e(TAG, "One-Off sensing process, found the latest data available for: "+SensorIds.get(i));
					sensorData.add(SensorDataCollector.getData(SensorIds.get(i)));
				}
				else{
					Log.e(TAG, "One-Off sensing process, latest data not available & sensing for: "+SensorIds.get(i));
					SensorData sd=sensorManager.getDataFromSensor(SensorIds.get(i));
					if(sd.getSensorType()==SensorUtils.SENSOR_TYPE_LOCATION)
						sd=LocationValidator.validateLocation(sd);
					sensorData.add(sd);
					SensorDataCollector.addData(sd);
				}
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
