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