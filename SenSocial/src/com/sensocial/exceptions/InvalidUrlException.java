package com.sensocial.exceptions;

public class InvalidUrlException extends Exception{
	/**
	 * Exception for invalid ServerURL
	 */
	private static final long serialVersionUID = -8884943660956294973L;

	public InvalidUrlException(String msg){
		super("Error:"+serialVersionUID+"\n"+msg);
	}
}
