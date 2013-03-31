package com.ubhave.sensocial.filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.http.SendSensorDataToServer;
import com.ubhave.sensocial.listener.SensorDataListenerManager;
import com.ubhave.sensocial.sensordata.classifier.SensorDataClassifierManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.data.SensorData;

public class SensorDataFilterManager {
	final private String TAG = "SNnMB";
	private ArrayList<SensorData> data;
	private ArrayList<Integer> SensorIds;
	private Hashtable<String,String> sensedData;
	private Context context;
	private AllPullSensors aps;
	private SharedPreferences sp;
	private SensorDataClassifierManager sdcm;
	private String message;

	public SensorDataFilterManager(ArrayList<SensorData> data, ArrayList<Integer> SensorIds, Context context, String message){
		this.data=data;
		this.SensorIds=SensorIds;
		this.context=context;
		this.message=message;
		aps=new AllPullSensors(context);
		sp=context.getSharedPreferences("snmbData",0);
		sdcm=new SensorDataClassifierManager();
		sensedData = new Hashtable<String,String>();
	}

	public void pushSensorDataAccordingToXML(){
		String config;
		try {
			File root = new File(Environment.getExternalStorageDirectory(), "SenSocialData");
			if (!root.exists()) {
				root.mkdirs();
			}
			File serverFile = new File(root, "SensorDataForServer.txt");
			File clientFile = new File(root, "SensorDataForClient.txt");
			FileOutputStream sos = new FileOutputStream(serverFile, true);
			FileOutputStream cos = new FileOutputStream(clientFile, true);
			OutputStreamWriter sout = new OutputStreamWriter(sos);
			OutputStreamWriter cout = new OutputStreamWriter(cos);
			for(int i=0;i<data.size();i++){
				config=getCongif(aps.getSensorNameById(SensorIds.get(i)));
				if(isServerTrue(config)){
					if(isClassifiedTrueForServer(config)){
						sout.write(sdcm.classifyThisData(data.get(i), SensorIds.get(i), "null")); //classified data
						sout.write("\r\n   \r\n");
					}
					else{
						JSONFormatter formatter = DataFormatter.getJSONFormatter(SensorIds.get(i));
						sout.write(formatter.toJSON(data.get(i)).toJSONString());
						sout.write("\r\n   \r\n");

					}
				}
				if(isClientTrue(config)){
					if(isClassifiedTrueForClient(config)){
						String classifiedData=sdcm.classifyThisData(data.get(i), SensorIds.get(i), message); //classified data
						sensedData.put(aps.getSensorNameById(SensorIds.get(i)), classifiedData);
						cout.write(classifiedData); 
						cout.write("\r\n   \r\n");
					}
					else{
						sensedData.put(aps.getSensorNameById(SensorIds.get(i)),aps.getSensorNameById(SensorIds.get(i)) +" not configured in XML");
						JSONFormatter formatter = DataFormatter.getJSONFormatter(SensorIds.get(i));
						cout.write(formatter.toJSON(data.get(i)).toJSONString());
						cout.write("\r\n   \r\n");
					}
				}
			}
			sout.close();
			cout.close();
			sensedData.put("message", message);
			SensorDataListenerManager.fireUpdateForAllEvent(sensedData);
			new SendSensorDataToServer("SenSocialData","SensorDataForServer.txt",context).execute();			
			Log.d(TAG, "Started file sender");
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());			
		}
	}

	private String getCongif(String sensor){	
		String config = "";
		if(sensor.equals("accelerometer")) config=sp.getString("accelerometerconfig", "");
		if(sensor.equals("bluetooth")) config=sp.getString("bluetoothconfig", "");
		if(sensor.equals("wifi")) config=sp.getString("wificonfig", "");
		if(sensor.equals("location")) config=sp.getString("locationconfig", "");
		if(sensor.equals("microphone")) config=sp.getString("microphoneconfig", "");
		return config;		
	}

	private Boolean isServerTrue(String str){
		if(str.charAt(4)=='t'){
			return true;
		}
		return false;		
	}

	private Boolean isClientTrue(String str){
		if(str.charAt(1)=='t'){
			return true;
		}
		return false;		
	}

	private Boolean isClassifiedTrueForServer(String str){
		if(str.charAt(5)=='c'){
			return true;
		}
		return false;		
	}

	private Boolean isClassifiedTrueForClient(String str){
		if(str.charAt(2)=='c'){
			return true;
		}
		return false;    
	}
}
