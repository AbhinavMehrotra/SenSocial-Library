package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.data.FilteredSensorData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.manager.SensorListenerManager;
import com.ubhave.sensocial.sensordata.classifier.SensorDataClassifierManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

public class FilterSensedData {

	final private String TAG = "SNnMB";
	private ArrayList<SensorData> data;
	private ArrayList<Integer> SensorIds;
	private Hashtable<String,String> sensedData;
	private Context context;
	private AllPullSensors aps;
	private SharedPreferences sp;
	private Editor ed;
	private SensorDataClassifierManager sdcm;
	private String message;
	private String sensor_name;
	private String sensor_config;

	public FilterSensedData(Context context){
		this.context=context;
		sp=context.getSharedPreferences("snmbData",0);
		ed =sp.edit();
	}

	public void FilterAndFireData(ArrayList<SensorData> sensorDataList){
		data=sensorDataList;
		Boolean conditionMet;
		String requiredData = "";
		Set<String> configs=new HashSet<String>();
		configs=sp.getStringSet("FilterSet", null);
		Hashtable<String, String> trueConfigs= new Hashtable<String, String>();
		if(configs!=null){
			for(String config:configs){
				for(String condition:sp.getStringSet(config, null)){
					conditionMet=false;
					if(condition.startsWith("RD")){
						requiredData=condition.substring(2, condition.length());
						continue;
					}
					for(String activity:sp.getStringSet(condition, null)){
						if(activity.equalsIgnoreCase("all")){
							// no conditions for this configuration
							conditionMet=true;
							trueConfigs.put(config, requiredData);
							break;
						}
						else{
							//checking if the condition matches with the current context

							//require classifiers
						}
					}
					if(conditionMet==true)
						break;
				}
			}
			fireSensorData(trueConfigs);
		}
	}

	private void fireSensorData(Hashtable<String, String> trueConfigs){
		//ensures there is any configuration for which all conditions are satisfied
		if(!trueConfigs.isEmpty()){
			AllPullSensors aps= new AllPullSensors(context);
			Hashtable<String, String> configs= new Hashtable<String, String>();
			Set<Integer> sensorids=new HashSet<Integer>(); //used set to remove duplicates. This is again required to converted to ArrayList.
			ArrayList<Integer> sensorIds=new ArrayList<Integer>();
			configs=trueConfigs;
			Iterator<Entry<String, String>> it = configs.entrySet().iterator();
			while(it.hasNext()){
				Hashtable.Entry<String,String> config=it.next();
				sensor_name=config.getValue().substring(0, config.getValue().length()-6);
				sensorids.add(aps.getSensorIdByName(sensor_name));
			}		
			for(int sensorId:sensorids){
				sensorIds.add(sensorId);
			}
			pullRequiredData(trueConfigs,sensorIds);
		}
	}

	private void pullRequiredData(final Hashtable<String, String> configs, ArrayList<Integer> sensorIds){
		final ArrayList<SocialEvent> socialEventList = new ArrayList<SocialEvent>();
		try {
			new OneOffSensing(context, sensorIds, null){
				@Override
				public void onPreExecute(){
					Log.i(TAG, "Sensing for required data");
				}

				@Override
				public void onPostExecute(ArrayList<SensorData> data){
					//Create list of social events
					Iterator<Entry<String, String>> it = configs.entrySet().iterator();
					while(it.hasNext()){
						Hashtable.Entry<String,String> config=it.next();
						sensor_name=config.getValue().substring(0, config.getValue().length()-6);
						sensor_config=config.getKey();
						
						FilteredSensorData fsd= new FilteredSensorData();
						fsd.setConfigurationName(sensor_config);
						fsd.setRawData(getConfigurationData(data, aps.getSensorIdByName(sensor_name)));
						//fsd.setClassifiedData(classifiedData); Not included, require classifiers
						SocialEvent se= new SocialEvent();
						se.setFilteredSensorData(fsd);	
						socialEventList.add(se);
					}	
					//fire social even to listeners
					SensorListenerManager.fireUpdate(socialEventList);
				}
			}.execute();
		} catch (ESException e) {
			Log.e(TAG, e.toString());
		}

	}
	
	private SensorData getConfigurationData(ArrayList<SensorData> data,int sensorId){
		for(int i=0;i<data.size();i++){
			if(data.get(i).getSensorType()==sensorId){
				return data.get(i);
			}
		}
		return null;
	}
}
