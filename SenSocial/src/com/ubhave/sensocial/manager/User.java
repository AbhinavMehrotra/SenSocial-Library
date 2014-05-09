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
