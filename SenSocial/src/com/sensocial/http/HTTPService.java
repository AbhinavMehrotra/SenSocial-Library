package com.sensocial.http;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.util.Log;

import com.sensocial.R;
import com.sensocial.configuration.ServerConfiguration;
import com.sensocial.http.CheckUpdate;
import com.sensocial.sensormanager.*;

public class HTTPService extends Service {

	final private String TAG = "SNnMB";
	private NotificationManager nm;
	private AudioManager am;
	final int uniqID=13247;  //required for notifications
	boolean flagR=true;
	String myString = null;
	public static int tabService=0;
	Vibrator vibrator;
	int NOTIFY_ID=0;
	String Oldrid="";
	boolean notificationflag=false;
	int lastlength=0;    
	private static long UPDATE_INTERVAL = 30*1000;  //Trigger time interval
	Context ctx;
	private static Timer timer;

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
	 * If there is new update then it start sensing from the configured sensors.
	 */
	private void startServiceWork(){
		ServerConfiguration sc= new ServerConfiguration(getApplicationContext());
		CheckUpdate checkIt =new CheckUpdate(sc.getServerURL());
		String newUpdate=checkIt.checkNow();
		Log.d(TAG, "Latest update available: "+newUpdate);
		SharedPreferences sp=getSharedPreferences("snmbData",0);
		if(newUpdate.contains("1") && sp.getBoolean("sensing", false)==false){
			Log.d(TAG, "Found a new update");
			Editor ed=sp.edit();
			ed.putBoolean("sensing", true);
			ed.commit();
			StartPullSensors sps=new StartPullSensors(getApplicationContext());
			sps.StartSensing();
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