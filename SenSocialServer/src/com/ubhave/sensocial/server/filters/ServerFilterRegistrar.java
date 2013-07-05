package com.ubhave.sensocial.server.filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubhave.sensocial.server.manager.Stream;

public class ServerFilterRegistrar {

//private static Map<String, Set<String>> filterRecord = new HashMap<String, Set<String>>();
	private static Map<String, String> filterRecord = new HashMap<String, String>();
	private static Map<String, String> conditions = new HashMap<String, String>();


	
	protected static void add(String streamId, String aggrStreamId) {
		filterRecord.put(streamId, aggrStreamId);
	}
	
	protected static void remove(String streamId, String aggrStreamId) {
		filterRecord.remove(streamId);
	}
	
	public static Boolean isPresent(String aggrStreamId){
		Boolean flag =false;
		for (Map.Entry<String, String> entry: filterRecord.entrySet()) {
			if(entry.getValue().equalsIgnoreCase(aggrStreamId)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public static String getStreamId(String aggrStreamId){
		String id=null;
		for (Map.Entry<String, String> entry: filterRecord.entrySet()) {
			if(entry.getValue().equalsIgnoreCase(aggrStreamId)){
				id=entry.getKey();
				break;
			}
		}
		return id;
	}
	
	protected static void addCondition(String streamId, String condition) {
		conditions.put(streamId, condition);
	}
	
	protected static void removeCondition(String streamId, String condition) {
		conditions.remove(streamId);
	}
	
	protected static String getCondition(String streamId) {
		String con=null;
		for (Map.Entry<String, String> entry: filterRecord.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(streamId)){
				con=entry.getValue();
				break;
			}
		}
		return con;
	}
	
//	protected static void add(String filterId, String clientStreamId) {
//		for(Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
//			if(entry.getKey().equalsIgnoreCase(filterId)){
//				Set<String> s=new HashSet<String>();
//				s=entry.getValue();
//				s.add(clientStreamId);
//				filterRecord.remove(filterId);
//				filterRecord.put(filterId, s);	
//			}
//			else{
//				Set<String> s=new HashSet<String>();
//				s.add(clientStreamId);
//				filterRecord.put(filterId, s);				
//			}
//		}
//	}
//	protected static void remove(String filterId, String clientStreamId) {
//		for(Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
//			if(entry.getKey().equalsIgnoreCase(filterId)){
//				Set<String> s=new HashSet<String>();
//				s=entry.getValue();
//				s.remove(clientStreamId);
//				filterRecord.remove(filterId);
//				filterRecord.put(filterId, s);	
//				break;
//			}
//		}
//	}

//	public static String isPresent(String clientStreamId){
//		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
//			if(entry.getValue().contains(clientStreamId)){
//				return entry.getKey();
//			}
//		}
//		return null;
//	}
//	
//	public static Set<String> getClientStreamId(String filterId){
//		Set<String> streams = new HashSet<String>();
//		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
//			if(entry.getKey().equalsIgnoreCase(filterId)){
//				streams = entry.getValue();
//				break;
//			}
//		}
//		return streams;
//	}
//	
//	public static String getFilterId(String clientStreamId){
//		for (Map.Entry<String, Set<String>> entry: filterRecord.entrySet()) {
//			if(entry.getValue().contains(clientStreamId)){
//				return entry.getKey();
//			}
//		}
//		return null;
//	}
	
	
	

}
