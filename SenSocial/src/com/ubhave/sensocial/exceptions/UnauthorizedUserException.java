package com.ubhave.sensocial.exceptions;

/**
 * UnauthorizedUserException caused when the user is not authorized to the Social Network 
 * and we are trying to unauthorize that user.
 */
public class UnauthorizedUserException extends Exception{

	private static final long serialVersionUID = 5786828698872671049L;

	public UnauthorizedUserException(String snName) {
		super("Error: "+serialVersionUID+"\nUser has not been authorized to "+snName);
	}

}
