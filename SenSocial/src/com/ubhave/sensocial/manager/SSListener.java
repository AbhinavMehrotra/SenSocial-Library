package com.ubhave.sensocial.manager;

import com.ubhave.sensocial.data.SocialEvent;

/**
 * Listener interface to receive social events
 * @param SocialEvent object
 */
public interface SSListener {

	/**
	 * Called when a new social-event is sensed.
	 * @param social_event
	 */
	void onDataSensed(SocialEvent social_event);
}
