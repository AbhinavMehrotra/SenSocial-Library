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

import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.server.exception.AggregatorNullPointerException;
import com.ubhave.sensocial.server.exception.IncompatibleStreamTypeException;

public class Aggregator {

	private Set<Stream> streams;
	private Set<String> streamIds;
	private int sensorId;
	private String dataType;
	private Stream aggregatedStream;

	/**
	 * Composes the set of streams received on the server and gives out a single stream
	 * @param streams Set of streams which are already created on single or many devices.
	 * @throws IncompatibleStreamTypeException
	 */
	public Aggregator(Set<Stream> streams) throws IncompatibleStreamTypeException{
		//check whether all streams in this set have same sensorId & datType
		for(Stream s:streams){
			sensorId=s.getSensorId();
			dataType=s.getDataType();
			break; // take first element and break
		}		
		for(Stream s:streams){   
			if(sensorId!=s.getSensorId()  || !dataType.equalsIgnoreCase(s.getDataType())){
				throw new IncompatibleStreamTypeException();
			}
		}
		this.streams=streams;
		this.aggregatedStream = new Stream(this, sensorId, dataType);
		for(Stream s:this.streams){
			streamIds.add(s.getStreamId());
		}
		AggregatorRegistrar.add(aggregatedStream.getStreamId(), streamIds);	
	}

	/**
	 * Returns single aggregated streams
	 * @return (Stream) Aggregated stream
	 */
	public Stream createStream(){	
		return this.aggregatedStream;
	}

	/**
	 * Removes all the streams from the aggregator
	 * @throws AggregatorNullPointerException
	 */
	public void removeAllStreams() throws AggregatorNullPointerException{
		if(this.aggregatedStream==null){
			throw new AggregatorNullPointerException("All ready deleted");
		}
		AggregatorRegistrar.remove(aggregatedStream.getStreamId());
		this.streams=null;
		this.streamIds=null;
		this.sensorId=0;
		this.dataType=null;
		this.aggregatedStream=null;
	}

	/**
	 * Removes the stream from the aggregator
	 * @param streamId (String) Stream id
	 * @throws AggregatorNullPointerException
	 */
	public void removeStream(String streamId) throws AggregatorNullPointerException{
		Boolean found=false;
		Set<String> newStreamIds= new HashSet<String>();
		Set<Stream> allStreams= new HashSet<Stream>();
		allStreams=this.getAgrregatedStreams();
		for(Stream s:allStreams){
			if(s.getStreamId().equalsIgnoreCase(streamId)){
				allStreams.remove(s);
				found=true;
				break;
			}
		}
		if(found==false){
			throw new AggregatorNullPointerException("Stream with id: "+streamId+" not found");
		}
		for(Stream s:allStreams){
			newStreamIds.add(s.getStreamId());
		}			
		AggregatorRegistrar.remove(aggregatedStream.getStreamId());
		AggregatorRegistrar.add(aggregatedStream.getStreamId(), newStreamIds);
	}

	/**
	 * Returns the set of streams aggregated by this aggregator
	 * @return Set<Stream> Set of streams 
	 */
	public Set<Stream> getAgrregatedStreams() {
		return streams;
	}
	
//	public Stream setFilter(Filter filter){
//		//this filter is server side filter		
//
//		Stream newStream=new Stream(this, this.sensorId, this.dataType);
//		String config=filter.getFilterName();
//
//		ArrayList<String> activities= new ArrayList<String>();
//		for(Activity s:filter.getConditions())
//			activities.add(s.getActivityName());
//
//		GenerateFilter.createXML(activities, config, 
//				Sensors.getSensorNameById(this.sensorId), dataType);
//
//		return newStream; 
//	}


}
