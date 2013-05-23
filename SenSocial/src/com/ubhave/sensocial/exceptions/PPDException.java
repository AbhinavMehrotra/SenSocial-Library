package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class PPDException extends Exception{
	
	private static final long serialVersionUID = 7270076537702205973L;

	public PPDException(String sensor){
		super("Error: "+serialVersionUID+"\n"+sensor.toUpperCase() +" not allowed in Privacy Policy Descriptor");
	}
}
