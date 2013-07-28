package com.ubhave.sensocial.server.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.ubhave.sensocial.server.database.UserRegistrar;

/**
 * User class represents the user of the application and the device.
 * 
 */
public class User {

	private String name;
	private String fbName;
	private String twName;
	private String id;
	private ArrayList<String> deviceIds;
	private ArrayList<String> fbFriends;
	private ArrayList<String> twFollowers;
	private HashMap<String, ArrayList<Double>> location;
	
	/**
	 * Constructor
	 * @param name (String) Usre name
	 * @param userid (String) User id
	 * @param fbName (String) Facebook name or id
	 * @param twName (String) Twitter name or id
	 * @param deviceIds (ArrayList<String>) List of device ids
	 * @param fbFriends (ArrayList<String>) List of Facebook friends 
	 * @param twFollowers (ArrayList<String>) List of Twitter followers
	 * @param location (HashMap<String, ArrayList<Double>>) Map of location and devices
	 */
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

	
	/**
	 * Getter for user name 
	 * @return (String) User name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for Facebook name 
	 * @return (String) Facebook name
	 */
	public String getFbName() {
		return fbName;
	}

	/**
	 * Getter for Twitter name 
	 * @return (String) Twitter name
	 */
	public String getTwName() {
		return twName;
	}

	/**
	 * Getter for user id 
	 * @return (String) User id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter for user's device list 
	 * @return (ArrayList<Device>) List of devices
	 */
	public ArrayList<Device> getDevices() {
		ArrayList<Device> devices= new ArrayList<Device>();
		Location l;
		for(String d:this.deviceIds){
			try{
			l= new Location(location.get(d).get(0), location.get(d).get(1));
			}
			catch(NullPointerException e){
				l= new Location(0,0);
			}
			devices.add(new Device(this, d, UserRegistrar.getBluetooth(d), l));
		}
		return devices;
	}

	/**
	 * Getter for users Facebook friends list 
	 * @return (ArrayList<String>) List of Facebook friends
	 */
	public ArrayList<String> getFacebookFriends() {
		return fbFriends;
	}

	/**
	 * Getter for Twitter followers list 
	 * @return (ArrayList<String>) List of Twitter followers
	 */
	public ArrayList<String> getTwitterFollowers() {
		return twFollowers;
	}
	

}
