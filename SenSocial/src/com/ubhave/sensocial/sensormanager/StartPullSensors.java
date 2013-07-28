//package com.ubhave.sensocial.sensormanager;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.ubhave.sensormanager.ESException;
//
//public class StartPullSensors {
//
//	final private String TAG = "SNnMB";
//	private ArrayList<Integer> SensorIds;
//	private final Context context;
//	private String message;
//	private AllPullSensors aps;
//	private SharedPreferences sp;
//
//	public StartPullSensors(Context context){
//		aps=new AllPullSensors(context);
//		this.SensorIds=aps.getIds();
//		this.context=context;
//		sp=context.getSharedPreferences("SSDATA", 0);
//	}
//	
////	public void startOneOffSensingWithOSN(String message){
////		try {
////			new OneOffSensing(context, SensorIds, message).execute();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////
////	/**
////	 * Method to initiate sensing from the configured sensors.
////	 */
////	public void startIndependentOneOffSensing(){
////		try {
////			new OneOffSensing(context, SensorIds, null).execute();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////
////	public void startIndependentContinuousStreamSensing(){
////		try {
////			new ContinuousStreamSensing (context, SensorIds).startSensing();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////	
////	public void stopIndependentContinuousStreamSensing(){
////		try {
////			new ContinuousStreamSensing (context, SensorIds).stopSensing();
////		} catch (ESException e) {
////			Log.e(TAG, e.toString());
////		}
////	}
////	
//	public void startIndependentContinuousStreamSensing(String sensor){
//		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
//		Set<String> blankSet=new HashSet<String>();
//		for(String sensor_name:sp.getStringSet("StreamSensorSet", blankSet)){
//			sensorIds.add(aps.getSensorIdByName(sensor_name));
//		}
//		try {
//			ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();
//			sensorIds.add(aps.getSensorIdByName(sensor));
//			ContinuousStreamSensing.getInstance(context, sensorIds).startSensing();
//		} catch (ESException e) {
//			Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
//		}
//	}
//	
//	public void stopIndependentContinuousStreamSensing(String sensor){
//		ArrayList<Integer> sensorIds=new ArrayList<Integer>();
//		Set<String> blankSet=new HashSet<String>();
//		for(String sensor_name:sp.getStringSet("StreamSensorSet", blankSet)){
//			sensorIds.add(aps.getSensorIdByName(sensor_name));
//		}
//		try {
//			ContinuousStreamSensing.getInstance(context,sensorIds).stopSensing();
//			sensorIds.remove(aps.getSensorIdByName(sensor));
//			ContinuousStreamSensing.getInstance(context, sensorIds).startSensing();
//		} catch (ESException e) {
//			Log.e("SNnMB", "Error at sensor classifier: "+e.toString());
//		}
//	}
//}
