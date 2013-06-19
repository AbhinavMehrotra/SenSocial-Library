package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid ServerURL or MQTT IP
 */
public class ServerException extends Exception{
	
	private static final long serialVersionUID = -8884943660956294973L;

	public ServerException(String msg){
		super("Error: "+serialVersionUID+"\n"+msg);
	}
}
