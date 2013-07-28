package com.ubhave.sensocial.server.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StreamRegistrar {

private static Map<String, Set<Stream>> streamRecord = new HashMap<String, Set<Stream>>();

	/**
	 * Adds stream along with the device-id to the map.
	 * @param deviceId (String) Device id
	 * @param stream (Stream) Object
	 */
	protected static void add(String deviceId, Stream stream) {
		for(Map.Entry<String, Set<Stream>> entry: streamRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(deviceId)){
				Set<Stream> s=new HashSet<Stream>();
				s=entry.getValue();
				s.add(stream);
				streamRecord.remove(deviceId);
				streamRecord.put(deviceId, s);	
			}
			else{
				Set<Stream> s=new HashSet<Stream>();
				s.add(stream);
				streamRecord.put(deviceId, s);				
			}
		}
	}
	
	/**
	 * Removes the stream from the specified device-id
	 * @param deviceId (String) Device id
	 * @param stream (Stream) Object
	 */
	protected static void remove(String deviceId, Stream stream) {
		for(Map.Entry<String, Set<Stream>> entry: streamRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(deviceId)){
				Set<Stream> s=new HashSet<Stream>();
				s=entry.getValue();
				s.remove(stream);
				streamRecord.remove(deviceId);
				streamRecord.put(deviceId, s);	
				break;
			}
		}
	}

	/**
	 * Returns the set of streams mapped to the device with the given device id
	 * @param deviceId (String) Device id
	 * @return (Set<Stream>) {@link Stream}
	 */
	public static Set<Stream> getStream(String deviceId){
		Set<Stream> streams = new HashSet<Stream>();
		for (Map.Entry<String, Set<Stream>> entry: streamRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(deviceId)){
				streams = entry.getValue();
				break;
			}
		}
		return streams;
	}
	
	/**
	 * Returns the stream with the given stream id
	 * @param streamId (String) Stream id
	 * @return {@link Stream} Object
	 */
	public static Stream getStreamById(String streamId){
		Stream s=null;
		for (Map.Entry<String, Set<Stream>> entry: streamRecord.entrySet()) {
			for(Stream stream: entry.getValue()){
				if(stream.getStreamId().equalsIgnoreCase(streamId)){
					s=stream;
					break;
				}
			}
		}
		return s;
	}
	
	
}