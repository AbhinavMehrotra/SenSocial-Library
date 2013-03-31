package com.ubhave.sensocial.sensordata.classifier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.listener.SensorDataListenerManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.data.SensorData;

public class SensorDataClassifierManager {

	private String TAG="SNnMB";

	public String classifyThisData(SensorData sensorData, int sensorId, String message){
		JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorId);
		JSONObject jsondata=formatter.toJSON(sensorData);			
		return analyseIt(jsondata, sensorId, message);
	}



	protected String analyseIt(JSONObject data, int i, String message){
		String result=null;
		if(i==AllPullSensors.SENSOR_TYPE_ACCELEROMETER){	
			JSONArray xaxis = (JSONArray) data.get("xAxis");			
			JSONArray yaxis = (JSONArray) data.get("yAxis");
			double x[] = new double[xaxis.size()];
			double y[] = new double[yaxis.size()];
			Log.i(TAG, "data is: "+xaxis);	
			Log.i(TAG, "data size: "+xaxis.size());	
			int j=0;
			while(j<xaxis.size() && j<yaxis.size()){
				x[j]=Double.parseDouble(xaxis.get(j).toString());
				y[j]=Double.parseDouble(yaxis.get(j).toString());
				j++;
			}
			Log.e(TAG, "Done parsing");
			if(new ClassifyAccelerometerData().isMoving(0.15,x, y)){
				result="Accelerometer-USER_IS_MOVING";
				if(!message.equals("null")){
					SensorDataListenerManager.newUpdateArrived(message, "Accelerometer-USER_IS_MOVING");
				}
			}
			else{
				result="Accelerometer-USER_IS_NOT_MOVING";
				if(!message.equals("null")){
					SensorDataListenerManager.newUpdateArrived(message, "Accelerometer-USER_IS_NOT_MOVING");
				}
			}
			Log.e(TAG, result);
		}
		else if(i==AllPullSensors.SENSOR_TYPE_BLUETOOTH){
			JSONArray devices= (JSONArray) data.get("devices");
			int j=0;
			String names="Bluetooth-";
			while(j<devices.size()){
				names+=((JSONObject) devices.get(j)).get("name")+"*";
				j++;
			}
			result=names.substring(0, names.length()-1);
		}
		else if(i==AllPullSensors.SENSOR_TYPE_LOCATION){
			result="Location-"+data.get("latitude").toString()+"&"+ data.get("longitude").toString();
			result=result.replaceAll("\\.", "*");
		}
		else if(i==AllPullSensors.SENSOR_TYPE_MICROPHONE){
			String jsonAmp= data.get("amplitude").toString();
			String[] arr=jsonAmp.split(",");
			
			double x[] = new double[arr.length];
			int j=0;			
			while(j<arr.length){
				x[j]=Double.parseDouble(arr[j]);
				j++;
			}
			AnalyzeMicrophoneData amd=new AnalyzeMicrophoneData();
			if(amd.isSpeaking(500,x)){
				result="Microphone-Speaking";
				if(!message.equals("null")){
					SensorDataListenerManager.newUpdateArrived(message, "not_silent");
				}
			}
			else{
				result="Microphone-Silent";
				if(!message.equals("null")){
					SensorDataListenerManager.newUpdateArrived(message, "silent");
				}
			}
		}
		else if(i==AllPullSensors.SENSOR_TYPE_WIFI){
			JSONArray devices= (JSONArray) data.get("scanResult");
			int j=0;
			String names="WIFI-";
			while(j<devices.size()){
				names+=((JSONObject) devices.get(j)).get("ssid")+"*";
				j++;
			}
			result=names.substring(0, names.length()-1);
		}
		else{
			Log.e(TAG, "Wrong sensor type");
			result="Error";
		}
		Log.e(TAG, "Results: "+result);
		return result;
	}



}
