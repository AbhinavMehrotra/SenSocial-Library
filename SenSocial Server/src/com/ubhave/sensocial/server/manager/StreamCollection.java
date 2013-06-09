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


	public StreamCollection(int sensorId, String dataType){
		this.sensorId=sensorId;
		this.dataType=dataType;
	}

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
		case twitter_followers:
			relativeIds=user.getTwitterFollowers();
			for(int i=0;i<relativeIds.size();i++){
				tempdevice=UserRegistrar.getUser(relativeIds.get(i)).getDevices();
				for(int j=0;j<tempdevice.size();j++){
					devices.add(tempdevice.get(j));
				}
			}
			
		case people_near_the_user:
			Set<String> bluetooths=new HashSet<String>();
			MQTTClientNotifier.sendQueryNotifications(MQTTNotifitions.nearby_bluetooths);
			
			//get the query result 
			bluetooths=null;
			
			for(String b:bluetooths){
				devices.add(UserRegistrar.getDeviceWithBluetooth(b));
			}
			
		}

		//create streams
		for(Device d:devices){
			streams.add(d.getStream(sensorId, dataType));
		}

		return streams;
	}

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
