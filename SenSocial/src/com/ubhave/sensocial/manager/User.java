package com.ubhave.sensocial.manager;

import java.util.Set;

import android.content.Context;

public class User {

	private String name;
	private String fbName;
	private String twName;
	private MyDevice device;
	private Set<String> fbFriends;
	private Set<String> twFollowers;
	
	public User(Context context, String name, String fbName, String twName, 
			Set<String> fbFriends, Set<String> twFollowers){
		this.name=name;
		this.fbName=fbName;
		this.twName=twName;
		this.device=new MyDevice(context,this);
		this.fbFriends=fbFriends;
		this.twFollowers=twFollowers;		
	}

	
	

	public String getUserName() {
		return name;
	}



	/**
	 * Method to retrieve Facebook username, after successful login.
	 * It returns null if user is not logged-in.
	 * @return String Facebook userName
	 */
	public String getFacebookName() {
		return fbName;
	}



	/**
	 * Method to retrieve Twitter username, after successful login.
	 * It returns null if user is not logged-in.
	 * @return String Twitter userName
	 */
	public String getTwitterName() {
		return twName;
	}




	public MyDevice getMyDevice() {
		return device;
	}




	public Set<String> getFacebookFriends() {
		return fbFriends;
	}

	
	
	public Set<String> getTwitterFollowers() {
		return twFollowers;
	}
	
}
