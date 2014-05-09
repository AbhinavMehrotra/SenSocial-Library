/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
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
	
	/**
	 * Returns if the aggregated stream is present in the ServerFilterRegistrar.
	 * @param aggrStreamId (String) STream id of the aggregated stream
	 * @return Boolean
	 */
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
	
	/**
	 * Returns the real stream id of the given aggregated stream id 
	 * @param aggrStreamId (String) Aggregated stream id
	 * @return (String) Stream  id
	 */
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
