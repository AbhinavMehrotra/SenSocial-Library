package com.ubhave.sensocial.server.manager;

/**
 * Location class wraps latitude and longitude as a single object
 */
public class Location {

	private double latitude;
	private double longitude;
	
	public Location(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	/**
	 * Getter for Latitude
	 * @return (double) Latitude
	 */
	public double getLatitude(){
		return this.latitude;
	}

	/**
	 * Getter for Longitude
	 * @return (double) Longitude
	 */
	public double getLongitude(){
		return this.longitude;
	}

	/**
	 * Setter for Latitude
	 * @param (String) latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Setter for Longitude
	 * @param (String) longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
