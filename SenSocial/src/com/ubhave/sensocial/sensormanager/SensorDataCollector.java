package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;

import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

public class SensorDataCollector {

	private static Map<Integer, SensorData> sensorDataMap=new HashMap<Integer, SensorData>();
	private static SensorData sensor_data;

	public static void addData(SensorData data){
		sensorDataMap.put(data.getSensorType(), data);
	}

	public static void addData(ArrayList<SensorData> data){
		for(SensorData sd:data)
			sensorDataMap.put(sd.getSensorType(), sd);
	}


	public static ArrayList<SensorData> getData(ArrayList<Integer> sensorIds){
		ArrayList<SensorData> sensordata=new ArrayList<SensorData>();
		for(int id:sensorIds){
			sensordata.add(getData(id));
		}
		return sensordata;
	}
	
	public static SensorData getData(int sensorId){
		SensorData data=null;
		if(isRegistered(sensorId)){
			for(Map.Entry<Integer, SensorData> map: sensorDataMap.entrySet()){
				if(map.getKey()==sensorId){
					data=map.getValue();
					break;
				}
			}
			return data;
		}
		else{
			try {
				sensor_data=null;
				ArrayList<Integer> ids=new ArrayList<Integer>();
				ids.add(sensorId);
				new OneOffSensing(SenSocialManager.getContext(), ids){
					@Override
					public void onPostExecute(ArrayList<SensorData> result){
						sensor_data= result.get(0);							
					}
				}.execute();
			} catch (ESException e) {
				e.printStackTrace();
			}		
		}
		return sensor_data;
	}

	public static Boolean isRegistered(int sensorId){
		AllPullSensors aps=new AllPullSensors(SenSocialManager.getContext());
		String sensorName= aps.getSensorNameById(sensorId);
		SharedPreferences sp=SenSocialManager.getContext().getSharedPreferences("SSDATA", 0);
		Set<String> sensors=sp.getStringSet("StreamSensorSet", null);
		if(sensors==null || !sensors.contains(sensorName))
			return false;
		return true;		
	}

}
