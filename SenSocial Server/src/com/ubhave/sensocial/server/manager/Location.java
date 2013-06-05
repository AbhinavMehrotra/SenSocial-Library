package com.ubhave.sensocial.server.manager;

public class Location {

	private double latitude;
	private double longitude;
	
	public Location(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public double getLongitude(){
		return this.longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
