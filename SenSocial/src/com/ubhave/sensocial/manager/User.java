package com.ubhave.sensocial.manager;

import java.util.Set;

import android.content.Context;

/**
 * User class allows accessing 
 */
public class User {

	private String name;
	private String fbName;
	private String twName;
	private MyDevice device;
	private Set<String> fbFriends;
	private Set<String> twFollowers;
	
	/**
	 * Constructor
	 * @param Context Application context
	 * @param String user-name 
	 * @param String facebook name
	 * @param String twitter name
	 * @param String facebook friends
	 * @param String twitter followers
	 */
	protected User(Context context, String name, String fbName, String twName, 
			Set<String> fbFriends, Set<String> twFollowers){
		this.name=name;
		this.fbName=fbName;
		this.twName=twName;
		this.device=new MyDevice(context,this);
		this.fbFriends=fbFriends;
		this.twFollowers=twFollowers;		
	}

	
	/**
	 * Getter for userName
	 * It returns null if user is not logged-in to any OSN.
	 * @return String userName
	 */
	public String getUserName() {
		return name;
	}



	/**
	 * Getter for Facebook userName
	 * It returns null if user is not logged-in.
	 * @return String Facebook userName
	 */
	public String getFacebookName() {
		return fbName;
	}



	/**
	 * Getter for Twitter userName
	 * It returns null if user is not logged-in.
	 * @return String Twitter userName
	 */
	public String getTwitterName() {
		return twName;
	}

	/**
	 * Getter for MyDevice
	 * @return MyDevice object
	 */
	public MyDevice getMyDevice() {
		return device;
	}
	
}
