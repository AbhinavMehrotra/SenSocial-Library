package SenSocialManager;

import java.util.ArrayList;
import java.util.UUID;

import DataBase.DBConfig;


public class SSManager {
	
//	private static SSManager ssManager;
//	private static Object lock = new Object();
//	
//	public static SSManager getSSManager(){
//		if (ssManager == null)
//		{
//			synchronized (lock)
//			{
//				if (ssManager == null)
//				{
//					ssManager = new SSManager();
//				}
//			}
//		}
//		return ssManager;
//	}
	
	public SSManager(DBConfig config){
		//get DB config and it should not be null
		
	}

	public Device getDevice(String deviceName){
		Device device=new Device(deviceName);
		return device;
	}
	
	public ArrayList<Device> getAllDevices(){
		
		return null;
	}
	
	public Stream getStream(Device device, int sensorId){
		Stream stream=new Stream(device, sensorId);
		return stream;
	}
	

	public void startStream(Stream stream, SensorListener listener){
		//register listener
	}
	

	public void stopStream(SensorListener listener){
		//remove listener
	}
	
	public void removeStream(Stream stream){
		//remove stream and filter
	}
	
	//how to start OSN and independent streams??
	
}
