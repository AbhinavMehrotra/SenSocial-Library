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

import com.ubhave.sensocial.listener.SocialNetworkListenerManager;
import com.ubhave.sensocial.listener.SocialNetworkListner;
import com.ubhave.sensocial.sensormanager.StartPullSensors;

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
		sp=getSharedPreferences("snmbData",0);  //PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
		if(newUpdate.contains("1") && sp.getBoolean("sensing", false)==false){
			Log.d(TAG, "Found a new update");
			Editor ed=sp.edit();
			ed.putBoolean("sensing", true);
			ed.commit();
			SocialNetworkListenerManager.newUpdateArrived(getApplicationContext(),newUpdate);	
			new StartPullSensors(getApplicationContext(), newUpdate).StartSensing();
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