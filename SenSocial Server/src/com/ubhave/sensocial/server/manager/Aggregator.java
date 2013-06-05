package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.server.exception.AggregatorNullPointerException;
import com.ubhave.sensocial.server.exception.IncompatibleStreamTypeException;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.filters.Modality;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.GenerateFilter;
import com.ubhave.sensocial.server.filters.PrivacyPolicyDescriptorParser;

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

	public Stream createStream(){	
		return this.aggregatedStream;
	}

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
