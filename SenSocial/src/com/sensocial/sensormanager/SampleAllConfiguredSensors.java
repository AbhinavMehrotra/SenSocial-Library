package com.sensocial.sensormanager;
/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This demo application was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.sensocial.http.SendSensorDataToServer;
import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

public class SampleAllConfiguredSensors extends AsyncTask<Void, Void, ArrayList<SensorData>>
{
	final private String TAG = "SNnMB";
	private final ESSensorManager sensorManager;
	private final Context context;
	private ArrayList<Integer> SensorIds;

	public SampleAllConfiguredSensors(Context context, ArrayList<Integer> SensorIds) throws ESException
	{
		this.context=context;
		this.SensorIds=SensorIds;
		sensorManager = ESSensorManager.getSensorManager(context);
	}

	/**
	 * Starts sensing
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
				e.printStackTrace();
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
		try {
			File root = new File(Environment.getExternalStorageDirectory(), "SensorData");
			if (!root.exists()) {
				root.mkdirs();
			}
			File file = new File(root, "SNMB.txt");
			FileOutputStream os = new FileOutputStream(file, true);
			OutputStreamWriter out = new OutputStreamWriter(os);
			if (data != null){
				for(int i=0;i<data.size();i++){
					JSONFormatter formatter = DataFormatter.getJSONFormatter(SensorIds.get(i));
					out.write(formatter.toJSON(data.get(i)).toJSONString());
					out.write("\r\n   \r\n");
				}
			}
			else {
				out.write("Null (e.g., sensor off)");
			}
			out.close();
			new SendSensorDataToServer("SensorData","SNMB.txt",context).execute();			
			Log.d(TAG, "Started file sender");

		} catch (FileNotFoundException e) {
			Log.e(TAG,e.toString());
		} catch (IOException e) {
			Log.e(TAG,e.toString());
		} 
		Log.d(TAG,"Stopped sensing");
	}

}
