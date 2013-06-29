package com.ubhave.sensocial.sensormanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.DropBoxManager.Entry;
import android.util.Log;

import com.ubhave.sensocial.data.DeviceSensorData;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.manager.SSListenerManager;
import com.ubhave.sensocial.sensordata.classifier.SensorDataHandler;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class ContinuousStreamSensing {

	final private String TAG = "SNnMB";
	private static ESSensorManager sensorManager;
	private static Context context;
	private ArrayList<Integer> SensorIds;
	private String message;
	private static SharedPreferences sp;
	private static Editor ed;
	private static int subscriptionId;
	private static AllPullSensors aps;
	private static ArrayList<SensorData> sensordata;
	private static ContinuousStreamSensing instance;
	private static Map<Integer, SensorData> sensordataCollection;

	public static ContinuousStreamSensing getInstance(Context context, ArrayList<Integer> SensorIds) throws ESException{
		return new ContinuousStreamSensing(context, SensorIds);
		//		if(instance==null){
		//			instance=new ContinuousStreamSensing(context, SensorIds);
		//		}
		//		return instance;
	}

	private ContinuousStreamSensing(Context context, ArrayList<Integer> SensorIds) throws ESException
	{
		this.context=context;
		this.SensorIds=SensorIds;
		sensorManager = ESSensorManager.getSensorManager(context);
		sp=context.getSharedPreferences("SSDATA",0);
		aps=new AllPullSensors(context);
		sensordata=new ArrayList<SensorData>();
		sensordataCollection=new HashMap<Integer, SensorData>();
	}

	protected void startSensing() throws ESException{
		SensorDataListener listener = new SensorDataListener() {

			public void onDataSensed(SensorData arg) {
				System.out.println("on Data Sensed");
				System.out.println("Data: "+arg);
				//				sensordata.add(arg);
//				if(sensordata.size()==SensorIds.size()){
//					Log.d("SNnMB", "Data sensed for all sensor-ids");
//					SensorDataHandler.handleStreamData(sensordata, context);
//					sensordata.clear();
//				}
				sensordataCollection.put(arg.getSensorType(), arg);
				SensorDataCollector.addData(arg);
				if(sensordataCollection.size()==SensorIds.size()){
					Log.d("SNnMB", "Data sensed for all sensor-ids");
					for(Map.Entry<Integer, SensorData> x: sensordataCollection.entrySet()){
						sensordata.add(x.getValue());
					}
					SensorDataHandler.handleStreamData(sensordata, context);
					sensordata.clear();
					sensordataCollection.clear();		
				}

			}

			public void onCrossingLowBatteryThreshold(boolean arg0) {
				// can pause sensing
			}
		};
		ed=sp.edit();
		for(int i=0;i<SensorIds.size();i++){
			System.out.println("Continuous sensing (Start sensing): "+SensorIds.get(i));
			System.out.println(SensorIds);
			subscriptionId=sensorManager.subscribeToSensorData (SensorIds.get(i), listener);
			ed.putInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", subscriptionId);			
		}
		ed.commit();
	}

	protected void stopSensing() throws ESException{
		for(int i=0;i<SensorIds.size();i++){
			subscriptionId=sp.getInt(aps.getSensorNameById(SensorIds.get(i))+"_subId", 0);
			if(subscriptionId != 0){
				sensorManager.unsubscribeFromSensorData(subscriptionId);
			}		
		}
	}
	
//	public static void startSensingForServer(final String streamId, int sensorId, final String dataType) throws ESException{
//		SensorDataListener listener = new SensorDataListener() {
//
//			public void onDataSensed(SensorData data) {
//				SocialEvent se=new SocialEvent();
//				DeviceSensorData d=new DeviceSensorData();
//				d.setDeviceId(sp.getString("deviceid", null));
//				d.setRawData(data);
//				d.setStreamId(streamId);
//				if(dataType.equalsIgnoreCase("raw")){
//					se.setFilteredSensorData(d);
//				}
//				else{
//					//classify it
//				}
//				ClientServerCommunicator.sendStream(context, se.toJSONString()); 
//			}
//
//			public void onCrossingLowBatteryThreshold(boolean arg0) {
//				// can pause sensing
//			}
//		};
//		ed=sp.edit();
//		subscriptionId=sensorManager.subscribeToSensorData (sensorId, listener);
//		ed.putInt(aps.getSensorNameById(sensorId)+"_subId", subscriptionId);			
//		ed.commit();
//	}
//
//	public static void startSensingForDevice(final String streamId, int sensorId, final String dataType) throws ESException{
//		SensorDataListener listener = new SensorDataListener() {
//
//			public void onDataSensed(SensorData data) {
//				//arg0.getSensorType();
//				SocialEvent se=new SocialEvent();
//				DeviceSensorData d=new DeviceSensorData();
//				d.setDeviceId(sp.getString("deviceid", null));
//				d.setRawData(data);
//				d.setStreamId(streamId);
//				if(dataType.equalsIgnoreCase("raw")){
//					se.setFilteredSensorData(d);
//				}
//				else{
//					//classify it
//				}
//				SSListenerManager.fireUpdate(se);
//
//			}
//
//			public void onCrossingLowBatteryThreshold(boolean arg0) {
//				// can pause sensing
//			}
//		};
//		ed=sp.edit();
//		subscriptionId=sensorManager.subscribeToSensorData (sensorId, listener);
//		ed.putInt(aps.getSensorNameById(sensorId)+"_subId", subscriptionId);			
//		ed.commit();
//	}

}
