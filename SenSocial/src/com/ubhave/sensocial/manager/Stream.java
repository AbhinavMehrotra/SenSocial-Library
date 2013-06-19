package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.exceptions.FilterException;
import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.Modality;
import com.ubhave.sensocial.filters.Filter;
import com.ubhave.sensocial.filters.FilterSettings;
import com.ubhave.sensocial.filters.PrivacyPolicyDescriptorParser;
import com.ubhave.sensocial.sensormanager.AllPullSensors;


public class Stream {

	private int sensorId;
	private String streamId;
	private String dataType;
	private Context context;
	private Filter filter;
	private final String TAG = "SNnMB";

	protected Stream(int sensorId, String dataType, Context context) throws PPDException,SensorDataTypeException{
		this.sensorId=sensorId;
		this.dataType=dataType;
		this.context=context;
		this.filter=null; 
		this.streamId=UUID.randomUUID().toString();
		//check PPD for the sensors associated to stream 
		PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);
		if(dataType.equalsIgnoreCase("raw")){
			if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId).toLowerCase(), null, "raw")){
				throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
			}
		}else if(dataType.equalsIgnoreCase("classified")){
			if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId).toLowerCase(), null, "raw") ||
					!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId), null, "classified")){
				throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
			}
		}
		else{
			throw new SensorDataTypeException(dataType);
		}
	}

	/**
	 * 
	 * @return 
	 * @throws PPDException If there exists any activity of which the associated sensor 
	 * (or SensorData from this sensor on client) is not declared in PPD.
	 */
	public Stream setFilter(Filter filter) throws PPDException{		
		String config=filter.getFilterName();

		Stream newStream;
		try {
			newStream = new Stream(this.sensorId, this.dataType, this.context);
			newStream.filter=filter;
		} catch (SensorDataTypeException e) {
			newStream=this;
			Log.e(TAG, "Something went wrong while creating a new stream");
		}


		ArrayList<Modality> activities=new ArrayList<Modality>();
		activities=filter.getConditions();

		//check PPD for the sensors associated to activities 
		PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);	
		for(int i=0;i<activities.size();i++){			
			if(!ppd.isAllowed(activities.get(i).getSensorName(), null, "classified")){
				throw new PPDException(activities.get(i).getSensorName()); 
			}
		}

		ArrayList<String> act= new ArrayList<String>();
		for(Modality s:activities)
			act.add(s.getActivityName());

		GenerateFilter.createXML(context, act, config, 
				new AllPullSensors(context).getSensorNameById(this.sensorId), dataType);

		return newStream; 
	}


	public int getSensorId() {
		return sensorId;
	}
	
	public String getStreamId() {
		return streamId;
	}

	public String getDataType() {
		return dataType;
	}

	public Filter getFilter(){
		return (this.filter);
	}

	public void startStream(){
		if(this.getFilter()==null){
//			throw new FilterException();
			ArrayList<String> act= new ArrayList<String>();
			act.add("ALL");

			GenerateFilter.createXML(context,act, this.streamId, 
					new AllPullSensors(context).getSensorNameById(this.sensorId), dataType);
			
		}
		FilterSettings.startConfiguration(this.streamId);
		ConfigurationHandler.run(context);	
	}


	public void pauseStream(){
		FilterSettings.stopConfiguration(this.getFilter().getFilterName()); 
	}

	public void unpauseStream(){
		FilterSettings.startConfiguration(this.getFilter().getFilterName());
	}

}
