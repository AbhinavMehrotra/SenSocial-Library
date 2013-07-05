package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.manager.Location;

public class DummyClassifier {

	public static double calculateDistanceInMiles(Location StartP, Location EndP) {  
		//		Haversine formula-wiki used by google
		//		double Radius=6371; //kms
		double Radius=3963.1676; //miles
		double lat1 = StartP.getLatitude();  
		double lat2 = EndP.getLatitude();  
		double lon1 = StartP.getLongitude();  
		double lon2 = EndP.getLongitude();  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
				Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));  
		return Radius * c;  
	} 

}
