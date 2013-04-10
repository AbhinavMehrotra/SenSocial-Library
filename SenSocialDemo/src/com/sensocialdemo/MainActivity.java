package com.sensocialdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.override;
import com.sensocial.R;
import com.ubhave.sensocial.configuration.FacebookConfiguration;
import com.ubhave.sensocial.configuration.MQTTServerConfiguration;
import com.ubhave.sensocial.configuration.SensorConfiguration;
import com.ubhave.sensocial.configuration.ServerConfiguration;
import com.ubhave.sensocial.configuration.TwitterConfiguration;
import com.ubhave.sensocial.exceptions.InvalidSensorNameException;
import com.ubhave.sensocial.exceptions.InvalidUrlException;
import com.ubhave.sensocial.listener.SensorDataListener;
import com.ubhave.sensocial.listener.SocialNetworkListner;
import com.ubhave.sensocial.manager.SenSocialManager;

public class MainActivity extends Activity implements SocialNetworkListner, SensorDataListener{

	String TAG="SNnMB";
	SharedPreferences sp;
	SenSocialManager ssm;
	FacebookConfiguration fbconfig;
	TwitterConfiguration tconfig;
	SensorConfiguration sensorConfig;
	ServerConfiguration serverConfig;
	Button fbBtn,twBtn,button;
	MQTTServerConfiguration mqtt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp=getSharedPreferences("ssdemo", 0);		
		try {
			ssm=new SenSocialManager(getApplicationContext());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		fbconfig= new FacebookConfiguration(getApplicationContext());
		tconfig=new TwitterConfiguration(getApplicationContext());
		sensorConfig=new SensorConfiguration(getApplicationContext());
		mqtt=new MQTTServerConfiguration(this);
		try {
			mqtt.setMQTTServerIP("broker.mqttdashboard.com");
		} catch (InvalidUrlException e) {
			Log.e(TAG, e.toString());
		}
		//serverConfig=new ServerConfiguration(getApplicationContext(), mqtt);
		serverConfig=new ServerConfiguration(getApplicationContext(), null);
		fbBtn=(Button) findViewById(R.id.fb);
		twBtn=(Button) findViewById(R.id.tw);
		button=(Button) findViewById(R.id.startservice);
		updateButton();
		ssm.registerSocialNetworkListener(this);
		ssm.registerSensorDataListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		try {
			switch (item.getItemId()) 
			{
			case R.id.saccelerometer:
				sensorConfig.setAccelerometer(true);
				Toast.makeText(getApplicationContext(), "Subscribed to accelerometer", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsaccelerometer:
				ssm.stopSensingFromThisSensor("accelerometer");
				Toast.makeText(getApplicationContext(), "Unsubscribed to accelerometer", Toast.LENGTH_LONG).show();
				return true;
			case R.id.sbluetooth:
				sensorConfig.setBluetooth(true);
				Toast.makeText(getApplicationContext(), "Subscribed to bluetooth", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsbluetooth:
				ssm.stopSensingFromThisSensor("bluetooth");
				Toast.makeText(getApplicationContext(), "Unsubscribed to bluetooth", Toast.LENGTH_LONG).show();
				return true;
			case R.id.smicrophone:
				sensorConfig.setMicrophone(true);
				Toast.makeText(getApplicationContext(), "Subscribed to microphone", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsmicrophone:
				ssm.stopSensingFromThisSensor("microphone");
				Toast.makeText(getApplicationContext(), "Unsubscribed to microphone", Toast.LENGTH_LONG).show();
				return true;
			case R.id.slocation:
				sensorConfig.setLocation(true);
				Toast.makeText(getApplicationContext(), "Subscribed to location", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unslocation:
				ssm.stopSensingFromThisSensor("location");
				Toast.makeText(getApplicationContext(), "Unsubscribed to location", Toast.LENGTH_LONG).show();
				return true;
			case R.id.swifi:
				sensorConfig.setWifi(true);
				Toast.makeText(getApplicationContext(), "Subscribed to wifi", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unswifi:
				ssm.stopSensingFromThisSensor("wifi");
				Toast.makeText(getApplicationContext(), "Unsubscribed to wifi", Toast.LENGTH_LONG).show();
				return true;
			case R.id.tenri:
				serverConfig.setRefreshInterval(10);
				Toast.makeText(getApplicationContext(), "RI set as 10sec", Toast.LENGTH_LONG).show();
				return true;
			case R.id.sixtyri:
				serverConfig.setRefreshInterval(60);
				Toast.makeText(getApplicationContext(), "RI set as 60sec", Toast.LENGTH_LONG).show();
				return true;
			case R.id.delete:
				DataBaseMethods dbm=new DataBaseMethods(getApplication());
				dbm.deleteAll();
				Toast.makeText(getApplicationContext(), "All records deleted!", Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (InvalidSensorNameException e) {
			Toast.makeText(getApplicationContext(), "Invalid Sensor Name", Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public void fblogin(View v){
		if(!sp.getString("fbname", "null").equals("null")){
			Toast.makeText(getApplicationContext(), "Already logged-in!", Toast.LENGTH_LONG).show();
			return;			
		}
		fbconfig.subscribeToFacebook("518620884845095", "647777d181d707d4d2993be83c8489d0");
		ssm.authenticateAndEnableFacebookTriggers(MainActivity.this, fbconfig);
	}

	public void twlogin(View v){
		if(!sp.getString("twname", "null").equals("null")){
			Toast.makeText(getApplicationContext(), "Already logged-in!", Toast.LENGTH_LONG).show();
			return;			
		}
		tconfig.subscribeToTwitter("vbsG14ISG49JNs0ux0A2g", "lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0");
		ssm.authenticateAndEnableTwitterTriggers(MainActivity.this, tconfig);
	}

	public void showRecords(View v){
		startActivity(new Intent(MainActivity.this, ShowSensedDataActivity.class));		
	}


	public void updateButton() {
		if (!ssm.serviceIsRunning()) {
			Log.d(TAG, "sevice not running");
			button.setText("Start Service");
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					try {
						sensorConfig.setAccelerometer(true);
						serverConfig.setRefreshInterval(30);
						serverConfig.setServerURL("http://studentweb.cs.bham.ac.uk/~axm514/");
						ssm.startService(sensorConfig, serverConfig);
					} catch (Exception e) {
						Log.e("ssdemo", e.toString());
					}
					updateButton();
				}
			});
		} else {
			Log.d(TAG, "sevice is running");
			button.setText("Stop Service");
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					ssm.stopService();
					updateButton();
				}
			});
		}
	}


	@override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			ssm.facebookOnActivityResult(MainActivity.this, requestCode, resultCode, data, fbconfig);			
			if(!ssm.getFacebookUserName().equals("null")){
				Toast.makeText(getApplicationContext(), "Facebook Login Successful!", Toast.LENGTH_LONG).show();
				Editor ed=sp.edit();
				ed.putString("fbname", ssm.getFacebookUserName());
				ed.commit();
				//fbBtn.setClickable(false);
			}
		}
		else{
			Toast.makeText(getApplicationContext(), "Error in login,  try again",Toast.LENGTH_LONG).show();
		}
	}

	@override
	protected void onNewIntent(Intent intent) {
		ssm.twitterOnNewIntent(intent);
		super.onNewIntent(intent);
		if(!ssm.getTwitterUserName().equals("null")){
			Toast.makeText(getApplicationContext(), "Twitter Login Successful!", Toast.LENGTH_LONG).show();
			Editor ed=sp.edit();
			ed.putString("twname", ssm.getTwitterUserName());
			ed.commit();
			//twBtn.setClickable(false);
		}
	}

	public void onFacebookPostReceived(String message) {
		Toast.makeText(getApplicationContext(), "Message from facebook: "+message,Toast.LENGTH_LONG).show();
		Log.d(TAG, "Message from facebook: "+message);		
	}

	public void onTwitterPostReceived(String message) {
		Toast.makeText(getApplicationContext(), "Message from twitter: "+message,Toast.LENGTH_LONG).show();
		Log.d(TAG, "Message from twitter: "+message);
	}

	public void onSNUpdateForAllEvents(String message, String accelerometer,
			String bluetooth, String location, String microphone, String wifi) {
		Toast.makeText(getApplicationContext(), "Updated Message: "+message+ ","+accelerometer+ ","+bluetooth+ ","+location+ ","+microphone+ ","+wifi,Toast.LENGTH_LONG).show();
		Log.d(TAG, "Updated Message: "+message+ ","+accelerometer+ ","+bluetooth+ ","+location+ ","+microphone+ ","+wifi);	

		String post,time;
		post=message.substring(1, message.length()-24);
		time=message.substring(message.length()-24);
		time=time.substring(0, 10)+"="+time.substring(11);
		DataBaseMethods dbm=new DataBaseMethods(getApplication());
		dbm.insertIntoTable(post,time.replaceAll(":", "-"),accelerometer, 
				bluetooth.replaceAll("'", "''").replaceAll(" ", "_"), location.replaceAll(" ", "_"), 
				microphone.replaceAll(" ", "_"), wifi.replaceAll("'", "''").replaceAll(" ", "_"));


	}

	public void onSNUpdateWhenUserIsMoving(String message) {
		// TODO Auto-generated method stub

	}

	public void onSNUpdateWhenUserIsNotMoving(String message) {
		// TODO Auto-generated method stub

	}

	public void onSNUpdateWhenUserIsAtSilentPlace(String message) {
		// TODO Auto-generated method stub

	}

	public void onSNUpdateWhenUserIsNotAtSilentPlace(String message) {
		// TODO Auto-generated method stub

	}



}
