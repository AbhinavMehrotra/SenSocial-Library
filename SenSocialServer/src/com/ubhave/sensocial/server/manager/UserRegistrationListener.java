package com.ubhave.sensocial.server.manager;


/**
 * UserRegistrationListener is an interface.
 * It can be implemented to listen new user registrations
 */
public interface UserRegistrationListener {

	public void onNewUserRegistered(User user);
}
