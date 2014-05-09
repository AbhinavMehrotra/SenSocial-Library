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
