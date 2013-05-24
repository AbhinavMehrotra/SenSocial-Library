package SenSocialManager;

import java.util.ArrayList;


public class Stream {

	private Device device;
	private int sensorId;
	private ArrayList<Stream> streamList;
	
	protected Stream(Device device, int sensorId){
		this.device=device;
		this.sensorId=sensorId;
	}
	
	protected Stream(ArrayList<Stream> streamList){
		this.streamList=streamList;
	}

	public void setFilter(Filter filter){
		//here we can check if the Stream object is from single device or from multiple stream of different devices.
		
		//create XML
	}
	
	
	
}
