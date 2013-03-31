package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid ServerURL or MQTT IP
 */
public class InvalidUrlException extends Exception{
	
	private static final long serialVersionUID = -8884943660956294973L;

	public InvalidUrlException(String msg){
		super("Error: "+serialVersionUID+"\n"+msg);
	}
}
