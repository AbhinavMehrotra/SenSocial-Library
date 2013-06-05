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

		//		//check PPD for the sensors associated to stream 
		//				PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);
		//				if(dataType.equalsIgnoreCase("raw")){
		//					if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId), null, "raw")){
		//						throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
		//					}
		//				}else if(dataType.equalsIgnoreCase("classified")){
		//					if(!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId), null, "raw") ||
		//							!ppd.isAllowed(new AllPullSensors(context).getSensorNameById(sensorId), null, "classified")){
		//						throw new PPDException(new AllPullSensors(context).getSensorNameById(sensorId)); 
		//					}
		//				}
		//				else{
		//					throw new SensorDataTypeException(dataType);
		//				}




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
			System.out.print(TAG+" Error: Something went wrong while creating a new stream with filter "+ filter.getFilterName());
		}
		return newStream; 
	}


	public void startStream() throws PPDException, XMLFileException{
		//check PPD for the sensors associated to activities
		Map<String,String> ppd= new HashMap<String, String>();
		ppd.put(Sensors.getSensorNameById(this.getSensorId()), this.getDataType());

		if(this.filter!=null){
			ArrayList<Modality> activities=new ArrayList<Modality>();
			activities=filter.getConditions();
			for(Modality a:activities){
				ppd.put(a.getSensorName(), "classified");
			}
		}
		PrivacyPolicyDescriptorParser ppdParser= new PrivacyPolicyDescriptorParser();
		if(!ppdParser.isAllowed(this.getDevice().getDeviceId(), ppd)){
			throw new PPDException("Sensors associated with the stream not allowed in the PPD");
		}

		//Generate filter xml file
		if(this.filter!=null){
			ArrayList<Modality> activities=new ArrayList<Modality>();
			activities=filter.getConditions();
			ArrayList<String> act= new ArrayList<String>();
			for(Modality s:activities)
				act.add(s.getActivityName());

			String config=filter.getFilterName();
			GenerateFilter.createXML(this.getDevice().getDeviceId(),act, this.getStreamId(), 
					Sensors.getSensorNameById(this.sensorId), dataType);
		}
		else{
			ArrayList<String> act= new ArrayList<String>();
			act.add("ALL");
			String config=filter.getFilterName();
			GenerateFilter.createXML(this.getDevice().getDeviceId(),act, this.getStreamId(), 
					Sensors.getSensorNameById(this.sensorId), dataType);
		}
		
		MQTTClientNotifier.sendStreamNotification(MQTTNotifitions.start_stream, this.getSensorId());
	}


	public void pauseStream(){
		//stop streaming without deleting filter
		//notify clients to set configuration attribute "sense"="false"
		MQTTClientNotifier.sendStreamNotification(MQTTNotifitions.pause_stream, this.getSensorId());
		
	}

	public void unpauseStream(String streamConfig){
		//start without sending new filter
		//notify clients to set configuration attribute "sense"="true"
		MQTTClientNotifier.sendStreamNotification(MQTTNotifitions.unpause_stream, this.getSensorId());
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



