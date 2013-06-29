package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.filters.Condition;
import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.Filter;
import com.ubhave.sensocial.filters.FilterSettings;
import com.ubhave.sensocial.filters.Modality;
import com.ubhave.sensocial.filters.ModalityType;
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
//		PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);
//		if(dataType.equalsIgnoreCase("raw")){
//			if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId).toLowerCase(), null, "raw")){
//				throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
//			}
//		}else if(dataType.equalsIgnoreCase("classified")){
//			if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId).toLowerCase(), null, "raw") ||
//					!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId), null, "classified")){
//				throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
//			}
//		}
//		else{
//			throw new SensorDataTypeException(dataType);
//		}
	}

	/**
	 * 
	 * @return 
	 * @throws PPDException If there exists any activity of which the associated sensor 
	 * (or SensorData from this sensor on client) is not declared in PPD.
	 */
	public Stream setFilter(Filter filter) throws PPDException{		

		Stream newStream;
		try {
			newStream = new Stream(this.sensorId, this.dataType, this.context);
			Log.i("SNnMB","Filter set & stream id is: "+ newStream.getStreamId());
			newStream.filter=filter;
		} catch (SensorDataTypeException e) {
			newStream=this;
			Log.e(TAG, "Something went wrong while creating a new stream");
		}
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
		Log.e(TAG, "Start stream: "+ getStreamId());
		if(this.getFilter()==null){
			Log.e(TAG, "Filter is null");
			ArrayList<Condition> conditions= new ArrayList<Condition>();
			conditions.add(new Condition(ModalityType.null_condition, "", ""));
			
			GenerateFilter.createXML(context,conditions, this.getStreamId(), 
					new AllPullSensors(context).getSensorNameById(this.sensorId), dataType);
			
		}
		else{
			Log.e(TAG, "Filter present");
			ArrayList<Condition> conditions=new ArrayList<Condition>();
			conditions=this.getFilter().getConditions();

			//check PPD for the sensors associated to activities 
//			PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);	
//			for(int i=0;i<activities.size();i++){			
//				if(!ppd.isAllowed(activities.get(i).getSensorName(), null, "classified")){
//					throw new PPDException(activities.get(i).getSensorName()); 
//				}
//			}


			GenerateFilter.createXML(context, conditions, getStreamId(), 
					new AllPullSensors(context).getSensorNameById(this.sensorId), dataType);
			
		}
		
		FilterSettings.startConfiguration(getStreamId());
		ConfigurationHandler.run(context);	
	}


	public void pauseStream(){
		Log.e(TAG, "Pause stream: "+ getStreamId());
		FilterSettings.stopConfiguration(this.getStreamId()); 
		ConfigurationHandler.run(context);	
	}

	public void unpauseStream(){
		Log.e(TAG, "Unpause stream: "+ getStreamId());
		FilterSettings.startConfiguration(this.getStreamId());
		ConfigurationHandler.run(context);	
	}

}
