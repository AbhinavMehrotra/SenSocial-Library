package com.ubhave.sensocial.server.manager;

import com.ubhave.sensocial.server.data.SocialEvent;

/**
 * SSListener is an interface.
 * It can be implemented to listen social events
 */
public interface SSListener {

	public void onDataReceived(SocialEvent socialEvent);
	
}
