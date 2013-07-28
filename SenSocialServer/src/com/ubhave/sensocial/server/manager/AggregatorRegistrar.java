package com.ubhave.sensocial.server.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubhave.sensocial.server.data.SocialEvent;

public class AggregatorRegistrar {
	
	private static Map<String, Set<String>> aggregators = new HashMap<String, Set<String>>();

	
	protected static void add(String aggregatedStreamId, Set<String> streamIds) {
		aggregators.put(aggregatedStreamId,streamIds);
	}
	

	protected static void addIfExists(String aggregatedStreamId, String streamId){
		for(Map.Entry<String, Set<String>> e: aggregators.entrySet()){
			if(e.getKey().equalsIgnoreCase(aggregatedStreamId)){
				Set<String> s= e.getValue();
				s.add(streamId);
				aggregators.put(aggregatedStreamId, s);
				break;
			}
		}
	}
	
	protected static void remove(String aggregatedStreamId) {
		aggregators.remove(aggregatedStreamId);
	}


	/**
	 * Returns whether the given stream is an aggregated stream
	 * @param streamId (string) Stream id
	 * @return Boolean
	 */
	public static Boolean isAggregated(String streamId){
		for (Map.Entry<String, Set<String>> entry: aggregators.entrySet()) {
			if(entry.getValue().contains(streamId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the  Streams associated with the given aggregated stream 
	 * @param streamId (String) Aggregated stream id
	 * @return (Set<String>) Streams associated with the given aggregated stream 
	 */
	public static Set<String> getStreamIds(String streamId){
		Set<String> streamIds = new HashSet<String>();
		for (Map.Entry<String, Set<String>> entry: aggregators.entrySet()) {
			streamIds = entry.getValue();
		}
		return streamIds;
	}

	/**
	 * Return the Aggregated stream id with which the given stream id is associated.
	 * @param streamId (String) Stream id
	 * @return (String) Aggregated stream id
	 */
	public static String getAggregatedStreamId(String streamId){
		String aggretorId=null;
		for (Map.Entry<String, Set<String>> entry: aggregators.entrySet()) {
			if(entry.getValue().contains(streamId)){
				aggretorId=entry.getKey();
				break;
			}
		}
		return aggretorId;
	}

}
