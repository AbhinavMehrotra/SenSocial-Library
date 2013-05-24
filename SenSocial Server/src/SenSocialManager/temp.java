package SenSocialManager;


import java.util.ArrayList;

import com.ubhave.sensocial.data.SocialEvent;

public class temp {

	//temporary- to check the structure.
	public void temp1(){
		Device device = new Device("Abhinav");
		int sensorId = Sensors.SENSOR_TYPE_ACCELEROMETER;
		SSManager ssm =new SSManager(null); //null-> DB config
		Stream s=ssm.getStream(device, sensorId);
		Filter f=new Filter("f1");
		f.addCondition(Activity.act1);  
		f.addCondition(Activity.act2);
		s.setFilter(f);
		
		SensorListener sl=new SensorListener() {

			public void onDataReceived(SocialEvent socialEvent) {
				// receive the stream here
				
			}

		};
		
		ssm.startStream(s, sl);
		
	}
	
	public void temp2(){
		// if I dont know the device name
		int sensorId = Sensors.SENSOR_TYPE_ACCELEROMETER;
		SSManager ssm =new SSManager(null); //null-> DB config
		ArrayList<Device> dList=ssm.getAllDevices(); 
		Stream s=null;
		for(Device d:dList){
			if(d.getUserName().equals("Abhinav")){
				s=ssm.getStream(d, sensorId);
				break;
				
			}
		}
		
		Filter f=new Filter("f1");
		f.addCondition(Activity.act1);  
		f.addCondition(Activity.act2);
		s.setFilter(f);
		SensorListener sl=new SensorListener() {

			public void onDataReceived(SocialEvent socialEvent) {
				// receive the stream here
				
			}

		};
		
		ssm.startStream(s, sl);
	}
}
