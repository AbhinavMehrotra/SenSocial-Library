package com.ubhave.sensocial.manager;

/**
 * Location class allows wrapping latitude & longitude in a single object. 
 */
public class Location {

	private double latitude;
	private double longitude;
	
	/**
	 * Constructor
	 * @param latitude
	 * @param longitude
	 */
	public Location(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	/**
	 * Getter for latitude
	 * @return double latitude
	 */
	public double getLatitude(){
		return this.latitude;
	}
	
	/**
	 * Getter for longitude
	 * @return double longitude
	 */
	public double getLongitude(){
		return this.longitude;
	}
}
