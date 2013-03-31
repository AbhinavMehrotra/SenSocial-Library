package com.ubhave.sensocial.exceptions;

/**
 * Exception for ClientServerConfig.xml not found or cannot be parsed.
 */
public class XMLFileException extends Exception{
	
	private static final long serialVersionUID = -8627641094879538116L;

	public XMLFileException(String msg){
		super("NullPointerException: "+serialVersionUID+"\n"+msg);
	}
}
