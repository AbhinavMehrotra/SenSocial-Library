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
package com.ubhave.sensocial.server.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.filters.ServerFilterParser;
import com.ubhave.sensocial.server.filters.ServerFilterRegistrar;
import com.ubhave.sensocial.server.manager.AggregatorRegistrar;
import com.ubhave.sensocial.server.manager.SSListenerManager;

public class StreamReceiver {
	public static void onReceive(String userId, String deviceId, String socialEventString){
		try {
			JSONObject obj=new JSONObject(socialEventString);
			String streamId=obj.getString("streamid");
			
			
			SocialEvent socialEvent= SocialEvent.getSocialEvent(obj);	
			SSListenerManager.fireUpdate(socialEvent);	
			
			if(AggregatorRegistrar.isAggregated(streamId)){				
				streamId=AggregatorRegistrar.getAggregatedStreamId(streamId);
				System.out.println("This is aggregated stream: "+streamId);
				obj.put("streamid", streamId);
				socialEvent= SocialEvent.getSocialEvent(obj);
				SSListenerManager.fireUpdate(socialEvent);	
				
				if(ServerFilterRegistrar.isPresent(streamId)){
					System.out.println("Stream present in server filter");
					String id=ServerFilterRegistrar.getStreamId(streamId);
					if(ServerFilterParser.isSatisfied(id, socialEvent)){
						obj.put("streamid", id);
						socialEvent= SocialEvent.getSocialEvent(obj);
						SSListenerManager.fireUpdate(socialEvent);
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
//SensorData rawData, String classifiedData, String streamId, String deviceId,
//String oSNFeed, String oSNName, String feedType