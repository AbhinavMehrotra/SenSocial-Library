package com.sensocialstandalonedemo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
			sm=SenSocialManager.getSenSocialManager(getApplicationContext(), false);
			userId=sm.setUserId("abhinav");
			user=sm.getUser(userId);
			device=user.getMyDevice();
			stream=device.getStream(AllPullSensors.SENSOR_TYPE_BLUETOOTH, "raw");
			f=new Filter();
			ArrayList<Condition> c=new ArrayList<Condition>();
			//c.add(new Condition(ModalityType.noise, Operator.equal_to, ModalValue.silent));
			c.add(new Condition(ModalityType.physical_activity, Operator.equal_to, ModalValue.not_moving));
			Location l=new Location(52.432,-1.924);
			//c.add(new Condition(ModalityType.location, Operator.equal_to, ModalValue.isWithinLocationRange(l, 1)));
			c.add(new Condition(ModalityType.neighbour, Operator.equal_to, ModalValue.isWithUser("A0:6C:EC:89:4C:01")));//"A0:6C:EC:89:4C:01"
			f.addConditions(c);					
			newStream =stream.setFilter(f);        	
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void startbtn(View v){
//    	try {
			l=new SSListener() {
				
				public void onDataSensed(SocialEvent sensor_event) {
					Log.i("SNnMB", "Back in Main Activity \nData sensed: "+sensor_event.getFilteredSensorData().getRawData().toString());
//					Toast.makeText(getApplicationContext(), "Data sensed: "+sensor_event.getFilteredSensorData().getRawData().toString(),
//							Toast.LENGTH_LONG).show();
					
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
