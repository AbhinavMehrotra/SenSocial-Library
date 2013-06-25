package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.filters.Modality;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.GenerateFilter;
import com.ubhave.sensocial.server.filters.PrivacyPolicyDescriptorParser;
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

	protected Stream(Device device, int sensorId, String dataType)throws PPDException,SensorDataTypeException{
		this.device=device;
		this.sensorId=sensorId;
		this.dataType=dataType;
		this.filter=null;
		this.streamId=UUID.randomUUID().toString();
		this.isAggregated=false;
		StreamRegistrar.add(device.getDeviceId(), this);
	}


	//used for aggregated stream
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
	 * 
	 * @return 
	 * @throws PPDException If there exists any activity of which the associated sensor 
	 * (or SensorData from this sensor on client) is not declared in PPD.
	 */
	public Stream setFilter(Filter filter)throws PPDException{		
		this.filter=filter;
		Stream newStream;
		try {
			newStream = new Stream(this.device, this.sensorId, this.dataType);
			newStream.filter=filter;
		} catch (SensorDataTypeException e) {
			newStream=this;
			System.out.print(TAG+" Error: Something went wrong while creating a new stream with filter ");
		}
		return newStream; 
	}


	public void startStream() throws PPDException, XMLFileException{
		Set<Stream> allStreams= new HashSet<Stream>();
		if(this.isAggregated)
			allStreams=this.getAggregator().getAgrregatedStreams();		
		else
			allStreams.add(this);

		//check PPD for the sensors associated to activities
//		Map<String,String> ppd= new HashMap<String, String>();
//		for(Stream s:allStreams){
//			ppd.put(Sensors.getSensorNameById(s.getSensorId()), s.getDataType());			
//		}
//		if(this.filter!=null){
//			ArrayList<Modality> activities=new ArrayList<Modality>();
//			activities=filter.getConditions();
//			for(Modality a:activities){
//				ppd.put(a.getSensorName(), "classified");
//			}
//		}
//		PrivacyPolicyDescriptorParser ppdParser= new PrivacyPolicyDescriptorParser();
//		if(!ppdParser.isAllowed(this.getDevice().getDeviceId(), ppd)){
//			throw new PPDException("Sensors associated with some stream are not allowed in the PPD");
//		}

		//Generate filter xml file
		if(this.filter!=null){
			ArrayList<Modality> activities=new ArrayList<Modality>();
			activities=filter.getConditions();
			ArrayList<String> act= new ArrayList<String>();
			for(Modality s:activities)
				act.add(s.getActivityName());

			for(Stream s:allStreams){
				GenerateFilter.createXML(s.getDevice().getUser(), s.getDevice().getDeviceId(),act, s.getStreamId(), 
						Sensors.getSensorNameById(s.getSensorId()), s.getDataType());				
			}
		}
		else{
			ArrayList<String> act= new ArrayList<String>();
			act.add("ALL");
			for(Stream s:allStreams){
				GenerateFilter.createXML(s.getDevice().getUser(), s.getDevice().getDeviceId(),act, s.getStreamId(), 
						Sensors.getSensorNameById(s.getSensorId()), s.getDataType());				
			}
		}
		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.start_stream, this.getStreamId());
		
//		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.start_stream, this.getStreamId());
	}


	public void pauseStream(){
		//stop streaming without deleting filter
		//notify clients to set configuration attribute "sense"="false"
		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.pause_stream, this.getStreamId());

	}

	public void unpauseStream(String streamConfig){
		//start without sending new filter
		//notify clients to set configuration attribute "sense"="true"
		MQTTClientNotifier.sendStreamNotification(device.getDeviceId(), MQTTNotifitions.unpause_stream, this.getStreamId());
	}

	public Device getDevice() {
		return device;
	}

	public int getSensorId() {
		return sensorId;
	}

	public String getDataType() {
		return dataType;
	}

	public Filter getFilter() {
		return filter;
	}

	public String getStreamId() {
		return streamId;
	}

	public Boolean getIsAggregated() {
		return isAggregated;
	}
	public Aggregator getAggregator() {
		return aggregator;
	}

}



