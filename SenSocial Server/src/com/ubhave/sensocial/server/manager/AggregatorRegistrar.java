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
	

	protected static void remove(String aggregatedStreamId) {
		aggregators.remove(aggregatedStreamId);
	}


	
	public static Boolean isAggregated(String streamId){
		for (Map.Entry<String, Set<String>> entry: aggregators.entrySet()) {
			if(entry.getValue().contains(streamId)){
				return true;
			}
		}
		return false;
	}
	
	
	public static Set<String> getStreamIds(String streamId){
		Set<String> streamIds = new HashSet<String>();
		for (Map.Entry<String, Set<String>> entry: aggregators.entrySet()) {
			streamIds = entry.getValue();
		}
		return streamIds;
	}

	
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
