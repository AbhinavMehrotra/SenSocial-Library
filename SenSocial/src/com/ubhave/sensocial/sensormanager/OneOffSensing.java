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
	private final Context context;
	private ArrayList<Integer> SensorIds;
	private String message;

	public OneOffSensing(Context context, ArrayList<Integer> SensorIds, String message) throws ESException
	{
		this.context=context;
		this.SensorIds=SensorIds;
		this.message=message;
		sensorManager = ESSensorManager.getSensorManager(context);
	}

	/**
	 * Here the sensing will start for all the configured sensors.
	 */
	@Override
	protected ArrayList<SensorData> doInBackground(Void... params)
	{
		ArrayList<SensorData> sensorData=new ArrayList<SensorData>();		
		for(int i=0;i<SensorIds.size();i++){
			try{
				Log.d("Sensor Task", "Sampling from Sensor");
				sensorData.add(sensorManager.getDataFromSensor(SensorIds.get(i)));
			}
			catch (ESException e){
				Log.e(TAG, e.toString());
				return null;
			}
		}
		return(sensorData);
	}

	/**
	 * Writes the sensor data in a file. 
	 * After writing it calls the AsyncTask "SendSensorDataToServer" which sends the data to server.
	 */
	@Override
	public void onPostExecute(ArrayList<SensorData> data){
		Log.d(TAG,"Stopped sensing");
		if(data!=null){
//			new SensorDataFilterManager(data, SensorIds, context, message).pushSensorDataAccordingToXML();
//			new FilterSensedData(context).FilterAndFireData(data);	
			SensorDataHandler.handleOSNDependentData(data, context);
		}
		
	}

}
