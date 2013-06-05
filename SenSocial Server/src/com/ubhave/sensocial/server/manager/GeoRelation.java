package com.ubhave.sensocial.server.manager;

public enum GeoRelation {
	in_this_city("start_stream"),
	within_1km("stop_stream");
	
	private final String message;
	
	GeoRelation(String message){
		this.message=message;
	}
	
	public String getMessage(){
		return message;
	}

}
