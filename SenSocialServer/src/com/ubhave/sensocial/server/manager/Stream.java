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



