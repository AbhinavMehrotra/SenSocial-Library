package com.ubhave.sensocial.server.manager;
import java.util.ArrayList;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.filters.Condition;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.ModalValue;
import com.ubhave.sensocial.server.filters.ModalityType;
import com.ubhave.sensocial.server.filters.Operator;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.SSManager;
import com.ubhave.sensocial.server.manager.SensorListener;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.User;


public class Run2 {
	public static void main(String[] args) {
		SSManager sm=SSManager.getSSManager();
        for(User u:sm.getAllUsers()){
        	System.out.println("User:"+u.getId());
        	ArrayList<Device> devices=u.getDevices();
        	for(Device d:devices){
            	System.out.println("Device:"+d.getDeviceId());
        		try {
					Stream stream=d.getStream(Sensors.SENSOR_TYPE_ACCELEROMETER, "classified");
					Filter filter=new Filter();
					ArrayList<Condition> conditions=new ArrayList<Condition>();
					conditions.add(new Condition(ModalityType.physical_activity, Operator.equal_to, ModalValue.not_moving));
					conditions.add(new Condition(ModalityType.facebook_friends_location, Operator.equal_to, ModalValue.withinOSNFriendsLocationRange(1)));
					//conditions.add(new Condition(ModalityType.neighbour, Operator.equal_to, ModalValue.isWithNeigbourDevice("A0:6C:EC:89:4C:01")));
					
					
					filter.addConditions(conditions);
					Stream s=stream.setFilter(filter);
					SensorListener l=new SensorListener() {
						
						public void onDataReceived(SocialEvent socialEvent) {
							System.out.println("Social Event received");
							System.out.println("Stream id: "+socialEvent.getFilteredSensorData().getStreamId());
							System.out.println("Raw data: "+socialEvent.getFilteredSensorData().getClassifiedData());						
						}
					};
					sm.registerListener(l, s.getStreamId());
					s.startStream();
					
				} catch (PPDException | SensorDataTypeException | XMLFileException e1) {
					e1.printStackTrace();
				}
        	}
        }
        if(sm.getAllUsers()==null){
        	System.out.println("NULL");
        }

	}

}
