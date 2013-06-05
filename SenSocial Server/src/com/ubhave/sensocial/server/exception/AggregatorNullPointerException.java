package com.ubhave.sensocial.server.exception;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class AggregatorNullPointerException extends Exception{
	
	private static final long serialVersionUID = 7270076539082205973L;

	public AggregatorNullPointerException(String message){
		super("Error: "+serialVersionUID+"\n"+message);
	}
}
