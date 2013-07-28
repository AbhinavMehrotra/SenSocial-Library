package com.ubhave.sensocial.server.manager;
import com.ubhave.sensocial.server.manager.SSManager;

/**
 * Dummy class.
 * It can be used to create an instance of {@link SSManager} which creates the TCP listener.
 */
public class Run1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Run this to create TCP listener and activate MQTT service
		SSManager.getSSManager();
	}

}
