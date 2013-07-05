package com.ubhave.sensocial.server.filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubhave.sensocial.server.data.SocialEvent;

public class FrequencyFilterRegistrar {
	
	private static Map<String, String> freqFilter = new HashMap<String, String>();

	
	protected static void add(String clientStreamId, String serverStreamIds) {
		freqFilter.put(clientStreamId,serverStreamIds);
	}
	

	protected static void remove(String aggregatedStreamId) {
		freqFilter.remove(aggregatedStreamId);
	}

	/*
	 * Returns true if other streams are dependent of this stream.
	 */
	public static Boolean isPresent(String streamId){
		for (Map.Entry<String, String> entry: freqFilter.entrySet()) {
			if(entry.getKey().contains(streamId)){
				return true;
			}
		}
		return false;
	}
	
	
	public static String getStreamId(String streamId){
		String stream = null;
		for (Map.Entry<String, String> entry: freqFilter.entrySet()) {
			if(entry.getKey().contains(streamId)){
				stream = entry.getValue();
				break;
			}
		}
		return stream;
	}

}
