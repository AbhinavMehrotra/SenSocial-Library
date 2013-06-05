package com.ubhave.sensocial.server.exception;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class IncompatibleStreamTypeException extends Exception{
	
	private static final long serialVersionUID = 7270076537082205973L;

	public IncompatibleStreamTypeException(){
		super("Error: "+serialVersionUID+"\nIncompatible Streams in the set.");
	}
}
