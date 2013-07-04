package com.sensocialdemo;

import java.util.ArrayList;

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
import com.ubhave.sensocial.filters.Condition;
import com.ubhave.sensocial.filters.Filter;
import com.ubhave.sensocial.filters.ModalValue;
import com.ubhave.sensocial.filters.ModalityType;
import com.ubhave.sensocial.filters.Operator;
import com.ubhave.sensocial.http.SendSensorDataToServer;
import com.ubhave.sensocial.manager.SSListener;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.manager.Stream;
import com.ubhave.sensocial.manager.User;
import com.ubhave.sensocial.sensormanager.AllPullSensors;

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
	public final String conKey="vbsG14ISG49JNs0ux0A2g";
	public final String conKeySecret="lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0";
	public final String accessToken ="111082828-bZHnz1qj2iKYPtGMZNyKHtq3EtaYhjMrafZf1V5b";
	public final String accessTokenSecret ="NJLdDIlgwgBPhqxAiyN59G8dgullbQPZ5hESbahLE";

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
//			sc.setServerIP("192.168.0.10");
			sc.setServerIP("10.4.71.106");
			sc.setServerProjectURL("http://abhinavtest.net76.net/");
			sc.setMQTTBrokerURL("tcp://broker.mqttdashboard.com:1883");
			sc.setRefreshInterval(60*60);
			sc.setServerPort(4444);
			ssm=SenSocialManager.getSenSocialManager(getApplicationContext(), true);
			String uid=ssm.setUserId("abhinav");
			System.out.println(uid);
			User user=ssm.getUser(uid);
			stream=user.getMyDevice().getStream(AllPullSensors.SENSOR_TYPE_ACCELEROMETER, "classified");
			
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

	public void start_sensing(View v){
		ArrayList<Condition> conditions=new ArrayList<Condition>();
		conditions.add(new Condition(ModalityType.physical_activity, Operator.equal_to, ModalValue.not_moving));
		conditions.add(new Condition(ModalityType.neighbour, Operator.equal_to, ModalValue.isWithNeigbourDevice("A0:6C:EC:89:4C:01")));
		Filter f=new Filter(conditions);
		try {
			stream=stream.setFilter(f);
			stream.startStream();
		} catch (PPDException e) {
			e.printStackTrace();
		}
	}

	public void onDataSensed(SocialEvent sensor_event) {

		Log.e("SNnMB", "Received: "+ sensor_event.getFilteredSensorData().getRawData());
		Toast.makeText(getApplicationContext(), "Social Event: " +sensor_event.getFilteredSensorData().getStreamId(),Toast.LENGTH_LONG).show();

	}

	public void fblogin(View v){
		if(!sp.getString("fbname", "null").equals("null")){
			Toast.makeText(getApplicationContext(), "Already logged-in!", Toast.LENGTH_LONG).show();
			return;			
		}
		fbconfig.setFacebookId("518620884845095", "647777d181d707d4d2993be83c8489d0");
		ssm.authenticateFacebook(MainActivity.this, fbconfig);

//		new SendSensorDataToServer("", "http://10.4.190.245:90/get_file/GetSensorData.php", getApplicationContext()).execute();
	}
//192.168.0.10:90

	public void twlogin(View v){
		tconfig.setTwitterIds(conKey, conKeySecret);
		ssm.authenticateTwitter(MainActivity.this, tconfig);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		ssm.twitterOnNewIntent(intent,tconfig);
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



		
	}
