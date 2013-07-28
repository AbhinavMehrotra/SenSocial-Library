package com.ubhave.sensocial.server.manager;

import java.util.HashSet;
import java.util.Set;

public class UserRegistrationListenerManager {

	
	private static Set<UserRegistrationListener> listeners = new HashSet<UserRegistrationListener>();
	
	public static void addListener(UserRegistrationListener listener){
		listeners.add(listener);
	}
	
	public static void removeListener(UserRegistrationListener listener){
		listeners.remove(listener);
	}
	
	public static void fireUpdates(User user){
		for(UserRegistrationListener l:listeners)
			l.onNewUserRegistered(user);
	}
}
