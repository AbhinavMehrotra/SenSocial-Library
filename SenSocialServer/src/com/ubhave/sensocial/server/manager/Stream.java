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
package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.filters.Condition;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.GenerateFilter;
import com.ubhave.sensocial.server.filters.ModalityType;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;



public class Stream {

	private Device device;
	private int sensorId;
	private String dataType;
	private Filter filter;
	private String streamId;
	private Boolean isAggregated;
	private Aggregator aggregator;
	private final String TAG = "SNnMB";

	/**
	 * Constructor
	 * @param device {@link Device}
	 * @param sensorId (String) Sensor id of the required data
	 * @param dataType (String) data type of the required sensor data
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	protected Stream(Device device, int sensorId, String dataType)throws PPDException,SensorDataTypeException{
		this.device=device;
		this.sensorId=sensorId;
		this.dataType=dataType;
		this.filter=null;
		this.streamId=UUID.randomUUID().toString();
		this.isAggregated=false;
		System.out.println("device id is:  "+device.getDeviceId());
		StreamRegistrar.add(device.getDeviceId(), (Stream)this);
	}


	/**
	 * Constructor- used for aggregated stream
	 * @param aggregator {@link Aggregator}
	 * @param sensorId (int) Sensor id of the required data
	 * @param dataType (String) data type of the required sensor data
	 */
	protected Stream(Aggregator aggregator, int sensorId, String dataType){
		this.aggregator=aggregator;
		this.device=null;
		this.sensorId=sensorId;
		this.dataType=dataType;
		this.filter=null;
		this.streamId=UUID.randomUUID().toString();
		this.isAggregated=true;		
	}



	/**
	 * Sets filter on the stream
	 * @return {@link Filter}
	 * @throws PPDException If there exists any activity of which the associated sensor 
	 * (or SensorData from this sensor on client) is not declared in PPD.
	 */
	public Stream setFilter(Filter filter)throws PPDException{		
		this.filter=filter;
		Stream newStream;

		try {
			if(this.isAggregated()){
				newStream = new Stream(getAggregator(), this.sensorId, this.dataType);
				newStream.filter=filter;
			}
			else{
				newStream = new Stream(this.device, this.sensorId, this.dataType);
				newStream.filter=filter;
			}
		} catch (SensorDataTypeException e) {
			newStream=this;
			System.out.print(TAG+" Error: Something went wrong while creating a new stream with filter ");
		}
		return newStream; 
	}

	/**
	 * Starts the stream
	 * @throws PPDException
	 * @throws XMLFileException
	 */
	public void startStream() throws PPDException, XMLFileException{
		Set<Stream> allStreams= new HashSet<Stream>();
		if(this.isAggregated)
			allStreams=this.getAggregator().getAgrregatedStreams();		
		else
			allStreams.add(this);

		//Generate filter xml file
		if(this.filter!=null){
			ArrayList<Condition> conditions=new ArrayList<Condition>();
			conditions=filter.getConditions();
			//			ArrayList<String> act= new ArrayList<String>();
			//			for(Condition s:conditions)
			//				act.add(s.getConditionString());

			for(Stream s:allStreams){
				GenerateFilter.createXML(s.getDevice().getUser(), s.getDevice().getDeviceId(),conditions, s.getStreamId(), 
						Sensors.getSensorNameById(s.getSensorId()), s.getDataType());				
			}
		}
		else{
			ArrayList<Condition> conditions= new ArrayList<Condition>();
			conditions.add(new Condition(ModalityType.null_condition, "", ""));
			for(Stream s:allStreams){
				GenerateFilter.createXML(s.getDevice().getUser(), s.getDevice().getDeviceId(),conditions, this.getStreamId(), 
						Sensors.getSensorNameById(s.getSensorId()), s.getDataType());				
			}
		}
		for(Stream s:allStreams){
			MQTTClientNotifier.sendStreamNotification(s.getDevice().getDeviceId(), MQTTNotifitions.start_stream, this.getStreamId());
		}

		//		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.start_stream, this.getStreamId());
	}

	/**
	 * Pauses the stream
	 */
	public void pauseStream(){
		//stop streaming without deleting filter
		//notify clients to set configuration attribute "sense"="false"
		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.pause_stream, this.getStreamId());

	}

	/**
	 * Un-pauses the stream
	 */
	public void unpauseStream(){
		//start without sending new filter
		//notify clients to set configuration attribute "sense"="true"
		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.unpause_stream, this.getStreamId());
	}

	/**
	 * Returns the device on which the stream is created
	 * @return {@link Device} Object
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * Getter for sensor id of the required sensor data
	 * @return int Sensor id
	 */
	public int getSensorId() {
		return sensorId;
	}

	/**
	 * Getter for data type of the required sensor data
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Getter for Filter associated with this stream
	 * @return {@link Filter} Object
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Getter for stream id
	 * @return {@link String} Stream id
	 */
	public String getStreamId() {
		return streamId;
	}

	/**
	 * Return whether the stream is aggregated
	 * @return {@link Boolean}
	 */
	public Boolean isAggregated() {
		return isAggregated;
	}
	
	/**
	 * Returns the Aggregator of the stream
	 * @return {@link Aggregator} Object
	 */
	public Aggregator getAggregator() {
		return aggregator;
	}

}



