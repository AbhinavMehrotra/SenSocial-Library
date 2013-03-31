package com.ubhave.sensocial.exceptions;

/**
 * Null pointer Exception is caused when any object is null.
 */
public class NullPointerException extends Exception{
	
	
	private static final long serialVersionUID = 4403337149522688805L;

	
	public NullPointerException(String msg){
		super("NullPointerException: "+serialVersionUID+"\n"+msg);
	}
}
