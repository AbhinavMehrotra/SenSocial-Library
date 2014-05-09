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

import java.io.File;
import java.util.Set;

import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.tcp.TCPServer;



public class SSManager {

	private static SSManager ssManager;
	private static Object lock = new Object();
	private static MongoClient mongoClient;

	/**
	 * Returns the instance of {@link SSManager}
	 * @return {@link SSManager}
	 */
	public static SSManager getSSManager(){

		//Check if the mongoDB service running?

		if (ssManager == null)
		{
			if (ssManager == null)
			{
				ssManager = new SSManager();
			}
		}
		return ssManager;
	}

	/**
	 * Constructor
	 */
	private SSManager(){
		File clientFilters = new File("ClientFilters");  
		clientFilters.mkdir(); 
		File ppd = new File("PPD");  
		ppd.mkdir(); 
		new TCPServer().start();		
	}

	/**
	 * Returns the User who has his/her any OSN account with the given OSN-name
	 * @param osn_name (String) Users' name/id on OSN
	 * @return {@link User} Object
	 */
	public User getUser(String osn_name){
		return UserRegistrar.getUser(osn_name);
	}

	/**
	 * Returns all users with the given user name
	 * @param userName (String) User name
	 * @return Set<User> {@link User}
	 */
	public Set<User> getAllUsers(String userName){
		return UserRegistrar.getAllUsers(userName);
	}

	/**
	 * Returns all registered users.
	 * To know about new user implement {@link UserRegistrationListener} and 
	 * register it with {@link UserRegistrationListenerManager} to get acknowledgement 
	 * about new users.
	 * @return Set<User> {@link User}
	 */
	public Set<User> getAllUsers(){
		return UserRegistrar.getAllUsers();
	}	

	/**
	 * Registers {@link SSListener} to the SSListenerManager
	 * @param listener {@link SSListener}
	 * @param streamId (String) Stream id
	 */
	public void registerListener(SSListener listener, String streamId){
		SSListenerManager.add(listener, streamId);
	}

	/**
	 * Removes {@link SSListener} from the SSListenerManager
	 * @param listener {@link SSListener}
	 */
	public void removeListener(SSListener listener){
		SSListenerManager.remove(listener);
	}

}
