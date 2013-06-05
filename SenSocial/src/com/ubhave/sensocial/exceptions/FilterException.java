package com.ubhave.sensocial.exceptions;

/**
 * Exception for invalid Sensor name provided in the ClientServerConfig.xml 
 */
public class FilterException extends Exception{

	private static final long serialVersionUID = -1712285004927736276L;

	public FilterException(){
		super("Filter not found. Error: "+serialVersionUID+"\n Stream should have a filter, " +
				"to set the filter use setFilter(Filter filter) method");
	}
}
