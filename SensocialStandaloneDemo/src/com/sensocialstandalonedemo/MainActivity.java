package com.sensocialstandalonedemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ubhave.sensocial.data.SocialEvent;
import com.ubhave.sensocial.filters.Condition;
import com.ubhave.sensocial.filters.Filter;
import com.ubhave.sensocial.filters.ModalValue;
import com.ubhave.sensocial.filters.ModalityType;
import com.ubhave.sensocial.filters.Operator;
import com.ubhave.sensocial.manager.Location;
import com.ubhave.sensocial.manager.MyDevice;
import com.ubhave.sensocial.manager.SSListener;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.manager.Stream;
import com.ubhave.sensocial.manager.User;
import com.ubhave.sensocial.privacy.PrivacySettings;
import com.ubhave.sensocial.sensormanager.AllPullSensors;

public class MainActivity extends Activity {

	SenSocialManager sm;
	String userId;
	User user;
	MyDevice device;
	Stream stream, newStream;
	Filter f;
	SSListener l;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try{
			sm=SenSocialManager.getSenSocialManager(getApplicationContext(), false, false);
			SharedPreferences sp=getSharedPreferences("SSdemo", 0);
			if(sp.getString("userid", "null").equals("null")){
				userId=sm.setUserId("abhinav");
				Editor ed=sp.edit();
				ed.putString("userid", userId);
				ed.commit();
			}
			
			user=sm.getUser(userId);
			device=user.getMyDevice();
			stream=device.getStream(AllPullSensors.SENSOR_TYPE_ACCELEROMETER, "classified");
			f=new Filter();
			ArrayList<Condition> c=new ArrayList<Condition>();
			//c.add(new Condition(ModalityType.noise, Operator.equal_to, ModalValue.silent));
			c.add(new Condition(ModalityType.physical_activity, Operator.equal_to, ModalValue.not_moving));
			Location l=new Location(52.432,-1.924);
			//			c.add(new Condition(ModalityType.location, Operator.equal_to, ModalValue.isWithinLocationRange(l, 1)));
			//			c.add(new Condition(ModalityType.neighbour, Operator.equal_to, ModalValue.isWithNeigbourDevice("A0:6C:EC:89:4C:01")));//"A0:6C:EC:89:4C:01"
			f.addConditions(c);					
			newStream =stream.setFilter(f);        	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		try {
			switch (item.getItemId()) 
			{
			case R.id.saccelerometer:
				PrivacySettings.enableSensing(getApplicationContext(), "accelerometer", "client", "raw");
				Toast.makeText(getApplicationContext(), "Enable accelerometer", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsaccelerometer:
				PrivacySettings.disableSensing(getApplicationContext(), "accelerometer", "client");
				Toast.makeText(getApplicationContext(), "Unsubscribed to accelerometer", Toast.LENGTH_LONG).show();
				return true;
			case R.id.sbluetooth:
				PrivacySettings.enableSensing(getApplicationContext(), "bluetooth", "client", "raw");
				Toast.makeText(getApplicationContext(), "Enable bluetooth", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsbluetooth:
				PrivacySettings.disableSensing(getApplicationContext(), "bluetooth", "client");
				Toast.makeText(getApplicationContext(), "Unsubscribed to bluetooth", Toast.LENGTH_LONG).show();
				return true;
			case R.id.smicrophone:
				PrivacySettings.enableSensing(getApplicationContext(), "microphone", "client", "raw");
				Toast.makeText(getApplicationContext(), "Enable microphone", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unsmicrophone:
				PrivacySettings.disableSensing(getApplicationContext(), "microphone", "client");
				Toast.makeText(getApplicationContext(), "Unsubscribed to microphone", Toast.LENGTH_LONG).show();
				return true;
			case R.id.slocation:
				PrivacySettings.enableSensing(getApplicationContext(), "location", "client", "raw");
				Toast.makeText(getApplicationContext(), "Subscribed to location", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unslocation:
				PrivacySettings.disableSensing(getApplicationContext(), "location", "client");
				Toast.makeText(getApplicationContext(), "Unsubscribed to location", Toast.LENGTH_LONG).show();
				return true;
			case R.id.swifi:
				PrivacySettings.enableSensing(getApplicationContext(), "wifi", "client", "raw");
				Toast.makeText(getApplicationContext(), "Subscribed to wifi", Toast.LENGTH_LONG).show();
				return true;
			case R.id.unswifi:
				PrivacySettings.disableSensing(getApplicationContext(), "wifi", "client");
				Toast.makeText(getApplicationContext(), "Unsubscribed to wifi", Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
			Log.e("Demo app", "Error: "+ e.toString());
		}
		return false;
	}


	public void startbtn(View v){
		//    	try {
		l=new SSListener() {

			public void onDataSensed(SocialEvent sensor_event) {
				Log.i("SNnMB", "Back in Main Activity \nData sensed: "+sensor_event.getFilteredSensorData().getClassifiedData());
				Toast.makeText(getApplicationContext(), "Data sensed: "+sensor_event.getFilteredSensorData().getClassifiedData(),Toast.LENGTH_LONG).show();
			}
		};
		sm.registerListener(l, newStream.getStreamId());
		newStream.startStream();
		//		} catch (ServerException e) {
		//			e.printStackTrace();
		//		} 

	}


	public void stopbtn(View v){
		device.removeStream(newStream);
	}


	public void pausebtn(View v){
		newStream.pauseStream();
	}


	public void unpausebtn(View v){
		newStream.unpauseStream();
	}
}
