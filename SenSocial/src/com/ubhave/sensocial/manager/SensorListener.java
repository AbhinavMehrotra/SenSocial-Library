package com.ubhave.sensocial.manager;

import com.ubhave.sensocial.data.SocialEvent;

public interface SensorListener {

	/**
	 * Listener
	 * @param sensor_event SocialEvent
	 */
	void onDataSensed(SocialEvent sensor_event);
}
