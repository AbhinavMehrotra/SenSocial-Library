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

import com.ubhave.sensocial.server.data.SocialEvent;

/**
 * FrequencyFilterRegistrar class is used to register the frequency based streams
 */
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
