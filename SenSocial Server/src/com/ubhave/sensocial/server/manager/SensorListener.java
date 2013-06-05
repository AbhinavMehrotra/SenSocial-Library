package com.ubhave.sensocial.server.manager;

import com.ubhave.sensocial.server.data.SocialEvent;

public interface SensorListener {

	public void onDataReceived(SocialEvent socialEvent);
	
}
