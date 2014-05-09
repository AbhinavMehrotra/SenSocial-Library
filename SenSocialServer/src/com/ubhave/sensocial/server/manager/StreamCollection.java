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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;

public class StreamCollection {

	static int sensorId; 
	static String dataType;

	/**
	 * Constructor
	 * @param sensorId (int) Sensor id of the required sensor data
	 * @param dataType (String) Data type of the required sensor data
	 */
	public StreamCollection(int sensorId, String dataType){
		this.sensorId=sensorId;
		this.dataType=dataType;
	}

	/**
	 * Returns stream collection based on the User-Relations {@link UserRelation}.
	 * @param user {@link User}
	 * @param relation {@link UserRelation}
	 * @return Set<Stream> Stream collection based on the User-Relation
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	public Set<Stream> getStreamSet(User user, UserRelation relation) throws PPDException, SensorDataTypeException{
		Set<Stream> streams= new HashSet<Stream>();
		Set<Device> devices= new HashSet<Device>();
		ArrayList<Device> tempdevice= new ArrayList<Device>();
		ArrayList<String> relativeIds= new ArrayList<String>();

		//get set of devices
		switch(relation){
		case facebook_friends:
			relativeIds=user.getFacebookFriends();
			for(int i=0;i<relativeIds.size();i++){
				tempdevice=UserRegistrar.getUser(relativeIds.get(i)).getDevices();
				for(int j=0;j<tempdevice.size();j++){
					devices.add(tempdevice.get(j));
				}
			}
			break;
		case twitter_followers:
			relativeIds=user.getTwitterFollowers();
			for(int i=0;i<relativeIds.size();i++){
				tempdevice=UserRegistrar.getUser(relativeIds.get(i)).getDevices();
				for(int j=0;j<tempdevice.size();j++){
					devices.add(tempdevice.get(j));
				}
			}
			break;
//		case people_near_the_user:
//			Set<String> bluetooths=new HashSet<String>();
//			MQTTClientNotifier.sendQueryNotifications(MQTTNotifitions.nearby_bluetooths);
//			
//			//get the query result 
//			bluetooths=null;
//			
//			for(String b:bluetooths){
//				devices.add(UserRegistrar.getDeviceWithBluetooth(b));
//			}
			
		}

		//create streams
		for(Device d:devices){
			streams.add(d.getStream(sensorId, dataType));
		}

		return streams;
	}

	/**
	 * Returns stream collection based on the Geo-Relations {@link GeoRelation}.
	 * @param location {@link Location}
	 * @param relation {@link GeoRelation}
	 * @return Set<Stream> Stream collection based on the Geo-Relation
	 * @throws PPDException
	 * @throws SensorDataTypeException
	 */
	public Set<Stream> getStreamSet(Location location, GeoRelation relation) throws PPDException, SensorDataTypeException{
		Set<Stream> streams= new HashSet<Stream>();
		Set<Device> devices= new HashSet<Device>();

		//get set of devices


		//create streams
		for(Device d:devices){
			streams.add(d.getStream(sensorId, dataType));
		}

		return streams;
	}

}
