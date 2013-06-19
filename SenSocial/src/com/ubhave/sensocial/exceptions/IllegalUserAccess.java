package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class IllegalUserAccess extends Exception{

	private static final long serialVersionUID = 0000;

	public IllegalUserAccess(String msg){
		super("Error: "+serialVersionUID+"\n"+msg);
	}
}
