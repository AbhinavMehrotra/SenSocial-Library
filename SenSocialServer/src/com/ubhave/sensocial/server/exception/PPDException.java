package com.ubhave.sensocial.server.exception;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class PPDException extends Exception{
	
	private static final long serialVersionUID = 7270076537702205973L;

	public PPDException(String message){
		super("Error: "+serialVersionUID+"\n"+message);
	}
}
