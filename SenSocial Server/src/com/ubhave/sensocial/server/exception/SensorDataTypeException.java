package com.ubhave.sensocial.server.exception;

public class SensorDataTypeException extends Exception{

	private static final long serialVersionUID = -8627641094879583116L;

	public SensorDataTypeException(String msg){
		super("Invalid SensorDataType: "+serialVersionUID+"\n"+msg+ " not a valid type. Use: raw or classified.");
	}

}
