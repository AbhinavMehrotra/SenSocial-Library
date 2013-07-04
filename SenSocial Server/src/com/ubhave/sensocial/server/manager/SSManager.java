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
	
	public static SSManager getSSManager(){
		
		//Check if the mongoDB service running?
		
		if (ssManager == null)
		{
			synchronized (lock)
			{
				if (ssManager == null)
				{
					ssManager = new SSManager();
				}
			}
		}
		return ssManager;
	}

	private SSManager(){
		File clientFilters = new File("ClientFilters");  
		clientFilters.mkdir(); 
		File ppd = new File("PPD");  
		ppd.mkdir(); 
		new TCPServer().start();		
	}
	
	public User getUser(String osn_name){
		return UserRegistrar.getUser(osn_name);
	}
	
	public Set<User> getAllUsers(String userName){
		return UserRegistrar.getAllUsers(userName);
	}
	
	public Set<User> getAllUsers(){
		return UserRegistrar.getAllUsers();
	}	

	public void registerListener(SensorListener listener, String streamId){
		SensorListenerManager.add(listener, streamId);
	}
	
	public void removeListener(SensorListener listener){
		SensorListenerManager.remove(listener);
	}
	
}
