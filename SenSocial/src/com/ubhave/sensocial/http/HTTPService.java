package com.ubhave.sensocial.http;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ubhave.sensocial.filters.GetNewFilterFromServerr;
import com.ubhave.sensocial.listener.SocialNetworkListenerManager;
import com.ubhave.sensocial.listener.SocialNetworkListner;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.StartPullSensors;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class HTTPService extends Service {

	final private String TAG = "SNnMB";  
	private static long UPDATE_INTERVAL;
	private static Timer timer;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sp=getApplicationContext().getSharedPreferences("snmbData",0);  //PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Log.e(TAG, "Starting HTTP service.\n Refresh Interval: "+sp.getInt("refreshInterval", 60000));
		UPDATE_INTERVAL=10000;//sp.getInt("refreshInterval", 60000);
	    timer = new Timer();
		callAsynchronousTask();
		return START_STICKY;
	}
	
	@Override 
	public void onDestroy(){
		super.onDestroy();
		stopSNMBService();
	}
	
	/**
	 * Method to start the timer in the service
	 */
	public void callAsynchronousTask() {
	    final Handler handler = new Handler();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
	                		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
	                		.permitNetwork()
	                		.build());
	                        startServiceWork();
	                        StrictMode.setThreadPolicy(old);
	                    } catch (Exception e) {
	                        Log.e(TAG, e.toString());
	                    }
	                }
	            });
	        }
	    };
	    timer.schedule(doAsynchronousTask, 0, UPDATE_INTERVAL); 
	}
	
	/**
	 * Method to check for update. <br/>
	 * If there is new update then it start sensing from the configured sensors and fires the message to the 
	 * registered SocialNetworkListeners.
	 */
	private void startServiceWork(){
		CheckUpdate checkIt =new CheckUpdate(sp.getString("server", ""));
		String newUpdate=checkIt.checkNow();
		Log.d(TAG, "Latest update available: "+newUpdate);
		SharedPreferences sp=getSharedPreferences("snmbData",0);
/*		<<Working code>>
		if(newUpdate.contains("1") && sp.getBoolean("sensing", false)==false){
			Log.d(TAG, "Found a new update");
			Editor ed=sp.edit();
			ed.putBoolean("sensing", true);
			ed.commit();
			SocialNetworkListenerManager.newUpdateArrived(getApplicationContext(),newUpdate);	
			new StartPullSensors(getApplicationContext(), newUpdate).StartSensing();
		}
*/		
		
		/* The newUpdate is the trigger message received from server.
		 * First char refers to the new filter set by the developer, it can be Y or N.
		 * Second char can  be 1,2, or 3. 1:sense for OSN update, 2: independent one-off sensing, 3: independent continuous sensing.
		 * Third char refers to the new update from OSN, 1 means new update and 0 means no update.	
		 */
		char ch= newUpdate.charAt(0);
		if(ch=='y' || ch=='Y'){
			//unsubscribe all sensing
			Editor ed=sp.edit();
			ed.putBoolean("streamsensing", false);
			ed.commit();
			new StartPullSensors(getApplicationContext()).stopIndependentContinuousStreamSensing();
			
			//get new filter and subscribe sensors
			ed.putBoolean("Filter", true);
			ed.commit();
			new GetNewFilterFromServerr().downloadFilter(getApplicationContext());
		}
		
		if(sp.getBoolean("Filter", false) && sp.getBoolean("SensorsConfigured", false)){
			sensingType(newUpdate);
		}
		
	}
	
	private void sensingType(String newUpdate){
		char ch= newUpdate.charAt(1);
		switch (ch){
		case ('0'):
			Log.d(TAG, "No new configuration for this client.");
		case ('1'):
			if(newUpdate.startsWith("1Y")&& sp.getBoolean("sensing", false)==false){
				Log.d(TAG, "Sensing configured for for OSN update only. \nFound a new update.");
				Editor ed=sp.edit();
				ed.putBoolean("sensing", true);
				ed.commit();
				SocialNetworkListenerManager.newUpdateArrived(getApplicationContext(),newUpdate);	
				new StartPullSensors(getApplicationContext()).startOneOffSensingWithOSN(newUpdate);
			}
			else{
				Log.d(TAG, "Sensing configured for for OSN update only. \nBut no new update found.");	
			}
			break;
		case ('2'):
			if(newUpdate.startsWith("2Y")&& sp.getBoolean("oneoffsensing", false)==false){
				Log.d(TAG, "Independent one-off sensing configured.");
				Editor ed=sp.edit();
				ed.putBoolean("oneoffsensing", true);
				ed.commit();
				SocialNetworkListenerManager.newUpdateArrived(getApplicationContext(),newUpdate);
				new StartPullSensors(getApplicationContext()).startIndependentOneOffSensing();
			}
			else{
				Log.d(TAG, "Independent one-off sensing configured./nBut null trigger for oneoff.");
				Editor ed=sp.edit();
				ed.putBoolean("oneoffsensing", false);
				ed.commit();
			}
			break;
		case ('3'):
			if(newUpdate.startsWith("3Y")&& sp.getBoolean("streamsensing", false)==false){
				Log.d(TAG, "Independent continuous-stream sensing configured.");
				Editor ed=sp.edit();
				ed.putBoolean("streamsensing", true);
				ed.commit();
				SocialNetworkListenerManager.newUpdateArrived(getApplicationContext(),newUpdate);
				new StartPullSensors(getApplicationContext()).startIndependentContinuousStreamSensing();
			}
			else if(newUpdate.startsWith("3N")){
				Log.d(TAG, "Independent continuous-stream sensing unconfigured.");
				Editor ed=sp.edit();
				ed.putBoolean("streamsensing", false);
				ed.commit();
				new StartPullSensors(getApplicationContext()).stopIndependentContinuousStreamSensing();
			}
			break;
		default:
			Log.e(TAG, "Error on server-side configuration (i.e. - first character is not 1,2,3, or 4).");			
		}
	}
	
	/**
	 * Method to stop the service and timer in it.
	 */
	private void stopSNMBService(){
		if (timer != null) timer.cancel();
		Log.i(getClass().getSimpleName(), "Service Timer stopped...");
		SharedPreferences sp=getSharedPreferences("snmbData",0);
		Editor ed=sp.edit();
		ed.putBoolean("sensing", false);
		ed.commit();
	}



	
}