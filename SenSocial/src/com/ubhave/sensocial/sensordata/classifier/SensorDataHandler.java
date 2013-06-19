package com.ubhave.sensocial.sensordata.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubhave.mltoolkit.MachineLearningManager;
import com.ubhave.mltoolkit.classifier.Classifier;
import com.ubhave.mltoolkit.utils.MLException;
import com.ubhave.sensocial.data.DeviceSensorData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.manager.SSListenerManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.sensormanager.ContinuousStreamSensing;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensocial.tcp.TCPClient;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.data.SensorData;

public class SensorDataHandler {
	
	protected static SensorDataClassifier classifier;
	private static boolean isDataInteresting;
	private static SharedPreferences sp;

	public static void handleStreamData(SensorData data, Context context){
		//check if this data is required <condition=all>
		Set<String> configs=new HashSet<String>();
		sp=context.getSharedPreferences("SNnMB", 0);
		configs=sp.getStringSet("StreamConfigurationSet", null); //see SensorClassifier
		for(String c:configs){
			if(sp.getStringSet(c, null).contains("ALL") && ConfigurationHandler.getRequiredData(c).
					equalsIgnoreCase(new AllPullSensors(context).getSensorNameById(data.getSensorType()))){
				SocialEvent se=new SocialEvent();
				//now find location of data to be sent
				Map<String,String> map=new HashMap<String,String>();
				map=ConfigurationHandler.getRequiredDataLocationNType(c);
				if(map.containsValue("classified")){
					//classify it
					
					
				}
				else{
					DeviceSensorData d=new DeviceSensorData();
					d.setDeviceId(sp.getString("deviceid", null));
					d.setRawData(data);
					d.setStreamId(c);
					se.setFilteredSensorData(d);
				}
				if(map.containsKey("server")){
					//send to server
					ClientServerCommunicator.sendStream(context, se.toJSONString());
				}
				else{
					//send to client listener
					SSListenerManager.fireUpdate(se);
				}				
			}
			else{
				//it is not required directly, so classify it and check for filter now
				
			}
		}
	}

	public static void handleOSNDependentData(ArrayList<SensorData> data, Context context){
		ArrayList<String> cData=new ArrayList<String>();
		String sensor;
		//classify all data and get a list of classified data
		
		
		for(String c:sp.getStringSet("OSNConfigurationSet", null)){
			Boolean satisfied=true;
			for(String s:sp.getStringSet(c, null)){
				if(!cData.contains(s))
					satisfied=false;
			}
			if(satisfied){
				sensor=ConfigurationHandler.getRequiredData(c);
				AllPullSensors aps=new AllPullSensors(context);
				Map<String,String> map=new HashMap<String,String>();
				map=ConfigurationHandler.getRequiredDataLocationNType(c);
				if(map.containsKey("server")){
					try {
						ContinuousStreamSensing.startSensingForServer(c, aps.getSensorIdByName(sensor), map.get("server"));
					} catch (ESException e) {
						e.printStackTrace();
					}
				}
				else{
					//send to client listener
					try {
						ContinuousStreamSensing.startSensingForDevice(c, aps.getSensorIdByName(sensor), map.get("classified"));
					} catch (ESException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
//		try {
//			MachineLearningManager ml= MachineLearningManager.getMLManager(contex);
//			Classifier classfier=ml.getClassifier("NAIVE_BAYES");
//			
//		} catch (MLException e) {
//			e.printStackTrace();
//		}
		
	}
	
	
}
