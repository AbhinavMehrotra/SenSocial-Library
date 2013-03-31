package com.ubhave.sensocial.exceptions;

/**
 * InvalidSensorNameException is caused when the sensor name passed as an argument is not valid.
 */
public class InvalidSensorNameException extends Exception{

	private static final long serialVersionUID = -772506000566419596L;

	public InvalidSensorNameException() {
		super("Error: "+serialVersionUID+"\nInvalid sensor name");
	}

}
