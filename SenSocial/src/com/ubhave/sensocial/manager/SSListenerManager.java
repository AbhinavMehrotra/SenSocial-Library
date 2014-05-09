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
package com.ubhave.sensocial.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensormanager.data.SensorData;

/**
 * SSListenerManager class allows registration of {@link SSListener} interface and firing 
 * social events to the registered listeners.
 */
public class SSListenerManager {


	private static Map<SSListener, Set<String>> listeners = new HashMap<SSListener, Set<String>>();

	/**
	 * Method to add an instance of  listener (SensorListner) with its associated configuration name
	 * @param listener SocialListner object
	 * @param streamId Configuration name
	 */
	protected static void add(SSListener listener, String streamId) {
		if(listeners.containsKey(listener)){
			for( Map.Entry<SSListener, Set<String>> entry : listeners.entrySet() ) {
				if( entry.getKey().equals(listener)) {
					Set<String> configs=entry.getValue();
					configs.add(streamId);
					entry.setValue(configs);
					break;
				}
			}
		}
		else{
			Set<String> configs=new HashSet<String>();
			configs.add(streamId);
			listeners.put(listener,configs);
		}
	}

	/**
	 * Method to remove an instance of  listener (SensorListner)
	 * @param listener SocialNetworkPostListner object
	 */
	protected static void remove(SSListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire list of social event to the registered SensorListeners on specific configuration.
	 * @param socialEvent ArrayList
	 */
	public static void fireUpdate(ArrayList<SocialEvent> socialEvent){
		for(int i=0;i<socialEvent.size();i++){
			for (Map.Entry<SSListener, Set<String>> listener: listeners.entrySet()) {
				for(String str:listener.getValue()){
					if(str.equalsIgnoreCase(socialEvent.get(i).getFilteredSensorData().getStreamId())){
						listener.getKey().onDataSensed(socialEvent.get(i));
						break;
					}
				}
			}
		}
	}

	/**
	 * Fire social events to the registered SensorListeners on specific configuration.
	 * @param socialEvent ArrayList
	 */
	public static void fireUpdate(SocialEvent socialEvent){
		Log.e("SNnMB", "Firing data to listeners!");
		for (Map.Entry<SSListener, Set<String>> listener: listeners.entrySet()) {
			for(String str:listener.getValue()){
				if(str.equalsIgnoreCase(socialEvent.getFilteredSensorData().getStreamId())){
					Log.e("SNnMB", "Found listener for: "+socialEvent.getFilteredSensorData().getStreamId());
					listener.getKey().onDataSensed(socialEvent);
					break;
				}
			}
		}
	}

}
