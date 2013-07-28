package com.ubhave.sensocial.client.tracker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.sensormanager.OneOffSensing;
import com.ubhave.sensocial.sensormanager.SensorUtils;
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
	private String serverUrl;
	private SharedPreferences sp;
	private String uuid;

	/**
	 * Constructor
	 * @param context Application context
	 * @throws ESException
	 */
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

	/**
	 * Starts tracking the location at specified refresh intervals
	 */
	public void startTracking() {
		final Handler handler = new Handler();
		TimerTask doAsynchronousTask = new TimerTask() {       
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {       
						try {
							SensorUtils aps=new SensorUtils(context);
							ArrayList<Integer> SensorIds=new ArrayList<Integer>();
							SensorIds.add(SensorUtils.SENSOR_TYPE_LOCATION);
							try {
								new OneOffSensing(context, SensorIds){
									@Override
									public void onPostExecute(ArrayList<SensorData> data){
										Log.d("SNnMB","Stopped sensing for location tracking");
										if(data!=null){
											JSONFormatter formatter = DataFormatter.getJSONFormatter(context,SensorUtils.SENSOR_TYPE_LOCATION);
											JSONObject jsondata=formatter.toJSON(data.get(0));
											System.out.print("location: "+jsondata.get("latitude"));
											

											JSONFormatter formatter1 = DataFormatter.getJSONFormatter(context, data.get(0).getSensorType());
											String str=formatter1.toJSON(data.get(0)).toString();
											System.out.print("str: "+str);
											System.out.print("sensorData: "+data.get(0));											
											
											latitude=jsondata.get("latitude").toString();
											longitude=jsondata.get("longitude").toString();
											StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
											StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
											.permitNetwork()
											.build());
											ClientServerCommunicator.updateLocation(context, latitude, longitude);
											StrictMode.setThreadPolicy(old);
										}

									}
								}.execute();
							} catch (ESException e) {
								Log.e("SNnMB","Error at Notification parser: "+e.toString());
							}							
						} catch (Exception e) {
							Log.e(TAG, e.toString());
						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, UPDATE_INTERVAL); 
	}

//
//	
//	private void sendLocationToServer(final String newlatitude, final String newlongitude){
//		Thread th= new Thread(){
//			public void run(){
//				try{
//					HttpClient httpclient = new DefaultHttpClient();
//					String uri=serverUrl+"LocationReciever.php?uuid="+uuid+"&latitude="+newlatitude+"&longitude="+newlongitude;
//					Log.d(TAG, "Sending names to: "+uri);
//					HttpPost httppost = new   HttpPost(uri);  
//					HttpResponse response = httpclient.execute(httppost);
//					Log.d(TAG, "Success"+response.getParams());
//				} catch (MalformedURLException e) {
//					Log.e(TAG, e.toString());
//				} catch (IOException e) {
//					Log.e(TAG, e.toString());
//				}
//			}
//		};
//		th.start();		
//	}
}
