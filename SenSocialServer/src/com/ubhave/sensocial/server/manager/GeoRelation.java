package com.ubhave.sensocial.server.manager;

public enum GeoRelation {
	in_this_city("in_this_city"),
	within_1mile("within_1mile");
	
	private final String message;
	
	GeoRelation(String message){
		this.message=message;
	}
	
	public String getMessage(){
		return message;
	}

}
