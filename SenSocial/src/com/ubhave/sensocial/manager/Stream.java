/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
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
import com.ubhave.sensocial.filters.ModalityType;
import com.ubhave.sensocial.sensormanager.SensorUtils;

/**
 * Stream class can be used to instantiate the stream of sensor data on device
 */
public class Stream {

	private int sensorId;
	private String streamId;
	private String dataType;
	private Context context;
	private Filter filter;
	private final String TAG = "SNnMB";

	/**
	 * Constructor
	 * @param StringsensorId
	 * @param String dataType
	 * @param Context Appplication context
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
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
	 * Sets filter on stream and returns new stream with the filter
	 * @return Stream object
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


	/**
	 * Getter for sensor-id
	 * @return String sensor-id
	 */
	public int getSensorId() {
		return sensorId;
	}

	/**
	 * Getter for stream-id
	 * @return String stream-id
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * Getter for stream's required data type
	 * @return String data-type
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Getter for filter
	 * @return Filter object
	 */
	public Filter getFilter(){
		return (this.filter);
	}

	/**
	 * Starts the stream
	 */
	public void startStream(){
		Log.e(TAG, "Start stream: "+ getStreamId());
		if(this.getFilter()==null){
			Log.e(TAG, "Filter is null");
			ArrayList<Condition> conditions= new ArrayList<Condition>();
			conditions.add(new Condition(ModalityType.null_condition, "", ""));
			
			GenerateFilter.createXML(context,conditions, this.getStreamId(), 
					new SensorUtils(context).getSensorNameById(this.sensorId), dataType);
			
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
					new SensorUtils(context).getSensorNameById(this.sensorId), dataType);
			
		}
		
		FilterSettings.startConfiguration(getStreamId());
		ConfigurationHandler.run(context);	
	}

	/**
	 * Pauses the stream
	 */
	public void pauseStream(){
		Log.e(TAG, "Pause stream: "+ getStreamId());
		FilterSettings.stopConfiguration(this.getStreamId()); 
		ConfigurationHandler.run(context);	
	}

	/**
	 * Unpauses the stream
	 */
	public void unpauseStream(){
		Log.e(TAG, "Unpause stream: "+ getStreamId());
		FilterSettings.startConfiguration(this.getStreamId());
		ConfigurationHandler.run(context);	
	}

}
