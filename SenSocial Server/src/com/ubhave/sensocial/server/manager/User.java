package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.ubhave.sensocial.server.database.UserRegistrar;

public class User {

	private String name;
	private String fbName;
	private String twName;
	private String id;
	private ArrayList<String> deviceIds;
	private ArrayList<String> fbFriends;
	private ArrayList<String> twFollowers;
	private HashMap<String, ArrayList<Double>> location;
	
	public User(String name, String id, String fbName, String twName, ArrayList<String> deviceIds, 
			ArrayList<String> fbFriends, ArrayList<String> twFollowers, HashMap<String, ArrayList<Double>> location){
		this.name=name;
		this.id=id;
		this.fbName=fbName;
		this.twName=twName;
		this.deviceIds=deviceIds;
		this.fbFriends=fbFriends;
		this.twFollowers=twFollowers;
		this.deviceIds=deviceIds;
		this.location=location;
	}

	
	public String getName() {
		return name;
	}

	public String getFbName() {
		return fbName;
	}

	public String getTwName() {
		return twName;
	}

	public String getId() {
		return id;
	}

	public ArrayList<Device> getDevices() {
		ArrayList<Device> devices= new ArrayList<Device>();
		Location l;
		for(String d:this.deviceIds){
			l= new Location(location.get(d).get(0), location.get(d).get(1));			
			devices.add(new Device(this, d, UserRegistrar.getBluetooth(d), l));
		}
		return devices;
	}

	public ArrayList<String> getFacebookFriends() {
		return fbFriends;
	}

	public ArrayList<String> getTwitterFollowers() {
		return twFollowers;
	}
	

}
