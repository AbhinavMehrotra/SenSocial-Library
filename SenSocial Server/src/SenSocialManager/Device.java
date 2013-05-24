package SenSocialManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.ubhave.sensocial.data.SocialEvent;


public class Device {

	private String deviceName;
	private UUID uuid;

	protected Device(String deviceName) {
		this.deviceName=deviceName;

		//look for the device in the data base and assign its UUID
	}
	
	public String getDeviceName(){
		return deviceName;
	}

	public UUID getDeviceUUID(){
		return uuid;
	}
	
	public String getUserName(){
		//look for the user of device
		return null;
	}

	public Set<String> getFriends(){
		Set<String> friends=new HashSet<String>();

		//look for friends in the data base
		return friends;		
	}

	public Location getLocation(){		
		Location location;
		//look for location in database and set it
		location= new Location(0, 0);
		return location;
	}

	public ArrayList<SocialEvent> getSavedStreams(Device device){

		//return all the social events for this stream
		return null; 

	}

}
