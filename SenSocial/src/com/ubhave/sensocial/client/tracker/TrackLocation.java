package com.ubhave.sensocial.client.tracker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

public class TrackLocation {

	private final String TAG="SNnMB";
	private long UPDATE_INTERVAL;
	String latitude;
	private String longitude;
	private Timer timer;
	private Context context;
	private ESSensorManager sensorManager;
	private SensorData sensorData;
	private String serverUrl;
	private SharedPreferences sp;
	private String uuid;

	public TrackLocation(Context context) throws ESException{
		this.context=context;
		this.timer=new Timer();
		sensorManager = ESSensorManager.getSensorManager(context);
		sp=context.getSharedPreferences("SSDATA", 0);
		serverUrl=sp.getString("server", "");
		uuid=sp.getString("uuid", "null");
		UPDATE_INTERVAL= sp.getInt("refreshinterval", 60*60*1000);
		Log.i(TAG, "TrackLocation: ref int-"+UPDATE_INTERVAL);
	}

	public void startTracking() {
		final Handler handler = new Handler();
		TimerTask doAsynchronousTask = new TimerTask() {       
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {       
						try {
							sensorData=sensorManager.getDataFromSensor(AllPullSensors.SENSOR_TYPE_LOCATION);
							JSONFormatter formatter = DataFormatter.getJSONFormatter(AllPullSensors.SENSOR_TYPE_LOCATION);
							JSONObject jsondata=formatter.toJSON(sensorData);
							System.out.print("location: "+jsondata.get("latitude"));
							

							JSONFormatter formatter1 = DataFormatter.getJSONFormatter(sensorData.getSensorType());
							String str=formatter1.toJSON(sensorData).toJSONString();
							System.out.print("str: "+str);
							System.out.print("sensorData: "+sensorData);
							
							
							latitude=jsondata.get("latitude").toString();
							longitude=jsondata.get("longitude").toString();
							StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
							StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
							.permitNetwork()
							.build());
//							sendLocationToServer(latitude,longitude);
							ClientServerCommunicator.updateLocation(context, latitude, longitude);
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
	 * Method to send the ScreenName for Social Network  and the Unique Id.
	 */
	private void sendLocationToServer(final String newlatitude, final String newlongitude){
		Thread th= new Thread(){
			public void run(){
				try{
					HttpClient httpclient = new DefaultHttpClient();
					String uri=serverUrl+"LocationReciever.php?uuid="+uuid+"&latitude="+newlatitude+"&longitude="+newlongitude;
					Log.d(TAG, "Sending names to: "+uri);
					HttpPost httppost = new   HttpPost(uri);  
					HttpResponse response = httpclient.execute(httppost);
					Log.d(TAG, "Success"+response.getParams());
				} catch (MalformedURLException e) {
					Log.e(TAG, e.toString());
				} catch (IOException e) {
					Log.e(TAG, e.toString());
				}
			}
		};
		th.start();		
	}
}
