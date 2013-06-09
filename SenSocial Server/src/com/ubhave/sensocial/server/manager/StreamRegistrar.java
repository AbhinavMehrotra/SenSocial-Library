package com.ubhave.sensocial.server.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StreamRegistrar {

private static Map<String, Set<Stream>> streamRecord = new HashMap<String, Set<Stream>>();

	
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
}