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
