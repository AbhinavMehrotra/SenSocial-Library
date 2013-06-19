package com.sensocialdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.override;
import com.sensocial.R;
import com.ubhave.sensocial.configuration.FacebookConfiguration;
import com.ubhave.sensocial.configuration.MQTTServerConfiguration;
import com.ubhave.sensocial.configuration.ServerConfiguration;
import com.ubhave.sensocial.configuration.TwitterConfiguration;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.exceptions.IllegalUserAccess;
import com.ubhave.sensocial.exceptions.PPDException;
import com.ubhave.sensocial.exceptions.SensorDataTypeException;
import com.ubhave.sensocial.exceptions.ServerException;
import com.ubhave.sensocial.manager.SSListener;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.manager.Stream;
import com.ubhave.sensocial.manager.User;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

public class MainActivity extends Activity implements SSListener{

	String TAG="SNnMB";
	SharedPreferences sp;
	SenSocialManager ssm;
	FacebookConfiguration fbconfig;
	TwitterConfiguration tconfig;
	ServerConfiguration serverConfig;
	Button fbBtn,twBtn,button;
	MQTTServerConfiguration mqtt;
	Stream stream;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp=getSharedPreferences("ssdemo", 0);	
		fbconfig= new FacebookConfiguration(getApplicationContext());
		tconfig=new TwitterConfiguration(getApplicationContext());
		serverConfig=new ServerConfiguration(getApplicationContext());
		fbBtn=(Button) findViewById(R.id.fb);
		twBtn=(Button) findViewById(R.id.tw);
		button=(Button) findViewById(R.id.startservice);
		updateButton();
		try {
			ServerConfiguration sc=new ServerConfiguration(getApplicationContext());
			sc.setServerIP("192.168.0.10");
			sc.setServerURL("hhtp://localhost/");
			sc.setServerPort(4444);
			ssm=SenSocialManager.getSensorManager(getApplicationContext(), true);
			String uid=ssm.setUserId("abhinav");
			System.out.println(uid);
			User user=ssm.getUser(uid);
			stream=user.getMyDevice().getStream(AllPullSensors.SENSOR_TYPE_ACCELEROMETER, "raw");
			ssm.registerListener(this, stream.getStreamId());
		} catch (PPDException e) {
			Log.e(TAG, "Main activity, 1: "+e.toString());
		} catch (SensorDataTypeException e) {
			Log.e(TAG, "Main activity, 2: "+e.toString());
		} catch (ServerException e) {
			Log.e(TAG, "Main activity, 3: "+e.toString());
		} catch (IllegalUserAccess e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}



	public void fblogin(View v){
//		if(!sp.getString("fbname", "null").equals("null")){
//			Toast.makeText(getApplicationContext(), "Already logged-in!", Toast.LENGTH_LONG).show();
//			return;			
//		}
		fbconfig.setFacebookId("518620884845095", "647777d181d707d4d2993be83c8489d0");
		ssm.authenticateFacebook(MainActivity.this, fbconfig);
//		try {
//			ESSensorManager manager= ESSensorManager.getSensorManager(getApplicationContext());
//			com.ubhave.sensormanager.SensorDataListener l=new com.ubhave.sensormanager.SensorDataListener() {
//				
//				public void onDataSensed(SensorData data) {
//					// TODO Auto-generated method stub
//					System.out.println("got it");
//				}
//				
//				public void onCrossingLowBatteryThreshold(boolean isBelowThreshold) {
//					// TODO Auto-generated method stub
//					
//				}
//			};
//			
//			manager.subscribeToSensorData(AllPullSensors.SENSOR_TYPE_ACCELEROMETER, l);
//			
//		} catch (ESException e) {
//			e.printStackTrace();
//		}
	}



	public void showRecords(View v){
		startActivity(new Intent(MainActivity.this, ShowSensedDataActivity.class));		
	}


	public void updateButton() {
		Log.d(TAG, "sevice not running");
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
//				try {
//					serverConfig.setRefreshInterval(30);
//					serverConfig.setServerURL("http://studentweb.cs.bham.ac.uk/~axm514/");
//					ssm.startService(serverConfig);
					Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_LONG).show();
					stream.startStream();
//				} catch (Exception e) {
//					Log.e("ssdemo", "update button"+e.toString());
//				}
			}
		});
	}


		@override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(data!=null){
				ssm.facebookOnActivityResult(MainActivity.this, requestCode, resultCode, data, fbconfig);			
			}
			else{
				Toast.makeText(getApplicationContext(), "Error in login,  try again",Toast.LENGTH_LONG).show();
			}
		}

		public void onDataSensed(SocialEvent sensor_event) {

			Log.e("SNnMB", "Received: "+ sensor_event.getFilteredSensorData().getRawData());
			//Toast.makeText(getApplicationContext(), "Social Event: " +sensor_event.getFilteredSensorData().getStreamId(),Toast.LENGTH_LONG).show();

		}


		//	public boolean onOptionsItemSelected(MenuItem item) 
		//	{
		//		try {
		//			switch (item.getItemId()) 
		//			{
		//			case R.id.saccelerometer:
		//				sensorConfig.setAccelerometer(true);
		//				Toast.makeText(getApplicationContext(), "Subscribed to accelerometer", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.unsaccelerometer:
		//				ssm.stopSensingFromThisSensor("accelerometer");
		//				Toast.makeText(getApplicationContext(), "Unsubscribed to accelerometer", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.sbluetooth:
		//				sensorConfig.setBluetooth(true);
		//				Toast.makeText(getApplicationContext(), "Subscribed to bluetooth", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.unsbluetooth:
		//				ssm.stopSensingFromThisSensor("bluetooth");
		//				Toast.makeText(getApplicationContext(), "Unsubscribed to bluetooth", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.smicrophone:
		//				sensorConfig.setMicrophone(true);
		//				Toast.makeText(getApplicationContext(), "Subscribed to microphone", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.unsmicrophone:
		//				ssm.stopSensingFromThisSensor("microphone");
		//				Toast.makeText(getApplicationContext(), "Unsubscribed to microphone", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.slocation:
		//				sensorConfig.setLocation(true);
		//				Toast.makeText(getApplicationContext(), "Subscribed to location", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.unslocation:
		//				ssm.stopSensingFromThisSensor("location");
		//				Toast.makeText(getApplicationContext(), "Unsubscribed to location", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.swifi:
		//				sensorConfig.setWifi(true);
		//				Toast.makeText(getApplicationContext(), "Subscribed to wifi", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.unswifi:
		//				ssm.stopSensingFromThisSensor("wifi");
		//				Toast.makeText(getApplicationContext(), "Unsubscribed to wifi", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.tenri:
		//				serverConfig.setRefreshInterval(10);
		//				Toast.makeText(getApplicationContext(), "RI set as 10sec", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.sixtyri:
		//				serverConfig.setRefreshInterval(60);
		//				Toast.makeText(getApplicationContext(), "RI set as 60sec", Toast.LENGTH_LONG).show();
		//				return true;
		//			case R.id.delete:
		//				DataBaseMethods dbm=new DataBaseMethods(getApplication());
		//				dbm.deleteAll();
		//				Toast.makeText(getApplicationContext(), "All records deleted!", Toast.LENGTH_LONG).show();
		//				return true;
		//			}
		//		} catch (InvalidSensorNameException e) {
		//			Toast.makeText(getApplicationContext(), "Invalid Sensor Name", Toast.LENGTH_LONG).show();
		//		}
		//		return false;
		//	}

	}
