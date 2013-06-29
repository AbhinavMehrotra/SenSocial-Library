package com.ubhave.sensocial.server.filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubhave.sensocial.server.manager.Stream;

public class ServerFilterRegistrar {

private static Map<String, Set<String>> filterRecord = new HashMap<String, Set<String>>();

	
	protected static void add(String filterId, String clientStreamId) {
		for(Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(filterId)){
				Set<String> s=new HashSet<String>();
				s=entry.getValue();
				s.add(clientStreamId);
				filterRecord.remove(filterId);
				filterRecord.put(filterId, s);	
			}
			else{
				Set<String> s=new HashSet<String>();
				s.add(clientStreamId);
				filterRecord.put(filterId, s);				
			}
		}
	}
	
	
	

	protected static void remove(String filterId, String clientStreamId) {
		for(Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(filterId)){
				Set<String> s=new HashSet<String>();
				s=entry.getValue();
				s.remove(clientStreamId);
				filterRecord.remove(filterId);
				filterRecord.put(filterId, s);	
				break;
			}
		}
	}

	public static String isPresent(String clientStreamId){
		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
			if(entry.getValue().contains(clientStreamId)){
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static Set<String> getClientStreamId(String filterId){
		Set<String> streams = new HashSet<String>();
		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(filterId)){
				streams = entry.getValue();
				break;
			}
		}
		return streams;
	}
	
	public static String getFilterId(String clientStreamId){
		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
			if(entry.getValue().contains(clientStreamId)){
				return entry.getKey();
			}
		}
		return null;
	}
	
	
	

}
