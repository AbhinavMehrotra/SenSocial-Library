package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class InvalidSensorException extends Exception{

	private static final long serialVersionUID = 6083424806199606604L;

	public InvalidSensorException(String msg){
		super("Error: "+serialVersionUID+"\n"+msg);
	}
}
