package com.sensocialfacebookclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.dataformatter.json.pull.LocationFormatter;
import com.ubhave.sensocial.configuration.FacebookConfiguration;
import com.ubhave.sensocial.configuration.ServerConfiguration;
import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.exceptions.ServerException;
import com.ubhave.sensocial.filters.Condition;
import com.ubhave.sensocial.filters.Filter;
import com.ubhave.sensocial.filters.ModalValue;
import com.ubhave.sensocial.filters.ModalityType;
import com.ubhave.sensocial.filters.Operator;
import com.ubhave.sensocial.manager.SSListener;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.manager.Stream;
import com.ubhave.sensocial.manager.User;
import com.ubhave.sensocial.privacy.PrivacySettings;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.data.SensorData;

public class MainActivity extends Activity implements SSListener{
	private SharedPreferences sp;
	private Stream s1,s2,s3;
	private Stream ns1,ns2,ns3;
	private SenSocialManager manager;
	private User user;
	private FacebookConfiguration config;
	private String clientId = "518620884845095";
	private String clientSecretId = "647777d181d707d4d2993be83c8489d0";
	private Map<String, SocialEvent> map=new HashMap<String, SocialEvent>();
	private int streamSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp=getSharedPreferences("FBCLIENT",0);
		try {
			ServerConfiguration sc=new ServerConfiguration(getApplicationContext());
			sc.setServerIP("192.168.0.4"); //10.4.71.106
			sc.setServerProjectURL("http://abhinavtest.net76.net/");
			sc.setMQTTBrokerURL("tcp://broker.mqttdashboard.com:1883");
			sc.setRefreshInterval(60*60);
			sc.setServerPort(4444);
			manager=SenSocialManager.getSenSocialManager(getApplicationContext(), true, false);
			createStream();
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}

	private void createStream(){
		String uid;
		try {
			ArrayList<Condition> conditions=new ArrayList<Condition>();
			Condition c=new Condition(ModalityType.facebook_activity, Operator.equal_to, ModalValue.active);
			conditions.add(c);
			Filter f=new Filter(conditions);
			uid = manager.setUserId("fbuser");
			user=manager.getUser(uid);
			s1=user.getMyDevice().getStream(AllPullSensors.SENSOR_TYPE_ACCELEROMETER, "classified");
			s2=user.getMyDevice().getStream(AllPullSensors.SENSOR_TYPE_MICROPHONE, "classified");
			//s3=user.getMyDevice().getStream(AllPullSensors.SENSOR_TYPE_LOCATION, "raw");
			ns1=s1.setFilter(f);
			ns2=s2.setFilter(f);
			//ns3=s3.setFilter(f);
			streamSize=2;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void viewRecordsBtn(View v){
		startActivity(new Intent(this, ViewRecordsActivity.class));
	}

	public void startBtn(View v){
		manager.registerListener(this, ns1.getStreamId());
		manager.registerListener(this, ns2.getStreamId());
		//manager.registerListener(this, ns3.getStreamId());
		ns1.startStream();
		ns2.startStream();
		//ns3.startStream();
	}

	public void stopBtn(View v){
		manager.unregisterListener(this);
		user.getMyDevice().removeStream(ns1);
		user.getMyDevice().removeStream(ns2);
		user.getMyDevice().removeStream(ns3);
	}

	public void pauseBtn(View v){
		ns1.pauseStream();
		ns2.pauseStream();
		ns3.pauseStream();
	}

	public void unpauseBtn(View v){
		ns1.unpauseStream();
		ns2.unpauseStream();
		ns3.unpauseStream();
	}

	public void loginBtn(View v){
		if(sp.getBoolean("login", false)){
			Toast.makeText(getApplicationContext(), "You are already logged in!", Toast.LENGTH_LONG).show();
		}
		else{
			Editor ed=sp.edit();
			ed.putBoolean("login", true);
			ed.commit();
			config=new FacebookConfiguration(getApplicationContext());
			config.setFacebookId(clientId, clientSecretId);
			manager.authenticateFacebook(MainActivity.this, config);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		manager.facebookOnActivityResult(MainActivity.this, requestCode, resultCode, data, config);
	}

	@Override
	public void onDataSensed(SocialEvent sensor_event) {

		Log.e("FacebookClient", "Data recieved: "+sensor_event.toJSONString());
		map.put(sensor_event.getFilteredSensorData().getStreamId(), sensor_event);
		if(map.size()==streamSize){
			String acc="Disabled", mic="Disabled", location="Disabled";
			try{
				acc=map.get(ns1.getStreamId()).getFilteredSensorData().getClassifiedData();
			}
			catch(Exception e){
				Log.e("FacebookClient",e.toString());
			}
			try{
				mic=map.get(ns2.getStreamId()).getFilteredSensorData().getClassifiedData();
			}
			catch(Exception e){
				Log.e("FacebookClient",e.toString());
			}
			try{
				SensorData loc=map.get(ns3.getStreamId()).getFilteredSensorData().getRawData();
				String lat,lon;
				JSONFormatter formatter = DataFormatter.getJSONFormatter(getApplicationContext(), loc.getSensorType());
				String str=formatter.toJSON(loc).toString();
				JSONObject obj=new JSONObject(str);
				lat=obj.getString("latitude");
				lon=obj.getString("longitude");
				location="Latitude- "+lat+"& Longitude- "+lon;
			}
			catch(Exception e){
				Log.e("FacebookClient",e.toString());
			}
				//save in db
				String message="unknown", time="unknown";
				try{
					message=sensor_event.getSocialData().getOSNFeed();
					time=sensor_event.getSocialData().getTime();
				}
				catch(NullPointerException e1){
					Log.e("FacebookClient","OSN data is unavailable!");
				}
				DataBaseMethods dbm=new DataBaseMethods((ContextWrapper) getApplicationContext());
				dbm.insertIntoTable(message,time,acc, mic, location);
				//clear all entries
				map.clear();
			
		}		
	}	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Boolean check;
		try {
			switch (item.getItemId()) 
			{
			case R.id.saccelerometer:
				check=PrivacySettings.enableSensing(getApplicationContext(), "accelerometer", "client", "raw");
				if(check){
					Toast.makeText(getApplicationContext(), "Enable accelerometer", Toast.LENGTH_LONG).show();
					++streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsaccelerometer:
				check=PrivacySettings.disableSensing(getApplicationContext(), "accelerometer", "client");
				if(check){
					Toast.makeText(getApplicationContext(), "Unsubscribed to accelerometer", Toast.LENGTH_LONG).show();
					--streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			case R.id.smicrophone:
				check=PrivacySettings.enableSensing(getApplicationContext(), "microphone", "client", "raw");
				if(check){
					Toast.makeText(getApplicationContext(), "Enable microphone", Toast.LENGTH_LONG).show();
					++streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsmicrophone:
				check=PrivacySettings.disableSensing(getApplicationContext(), "microphone", "client");
				if(check){
					Toast.makeText(getApplicationContext(), "Unsubscribed to microphone", Toast.LENGTH_LONG).show();
					--streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			case R.id.slocation:
				check=PrivacySettings.enableSensing(getApplicationContext(), "location", "client", "raw");
				if(check){
					Toast.makeText(getApplicationContext(), "Subscribed to location", Toast.LENGTH_LONG).show();
					++streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unslocation:
				check=PrivacySettings.disableSensing(getApplicationContext(), "location", "client");
				if(check){
					Toast.makeText(getApplicationContext(), "Unsubscribed to location", Toast.LENGTH_LONG).show();
					--streamSize;
				}
				else
					Toast.makeText(getApplicationContext(), "Already Set!!", Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
			Log.e("Facebook Client", "Error: "+ e.toString());
		}
		return false;
	}
}




