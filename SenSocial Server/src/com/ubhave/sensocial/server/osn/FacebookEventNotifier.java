package com.ubhave.sensocial.server.osn;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;

import net.spy.memcached.MemcachedClient;

public class FacebookEventNotifier extends Thread{

	protected void checkEvent() {
		Timer timer =new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkMemcached("localhost", 11211);				
			}
		}, 10*1000,10*1000);

	}

	private void checkMemcached(String host, int port){
		try {
			InetSocketAddress ia= new InetSocketAddress(host, port);
			MemcachedClient c = new MemcachedClient(ia);
			if(c.get("FBINFO")!=null){
				JSONObject json=new JSONObject(c.get("FBINFO").toString());
				System.out.println("Received event notification from Facebook: "+c.get("FBINFO").toString());
				parseJSON(json);
				Future<Boolean> fo=c.delete("FBINFO");
				System.out.println("Is the event deleted from Memcached: "+fo.get());
				c.shutdown();
			}
		}
		catch(Exception e){
			System.out.println("Error with facebook updates: "+e.toString());
		}
	}
	
	private void parseJSON(JSONObject json){
		try {
			JSONArray entries= json.getJSONArray("entry");
			JSONObject entry;
			String id,message,deviceId;
			long time;
			JSONArray fields;
			for(int i=0; i<entries.length();i++){
				entry=entries.getJSONObject(i);
				id=entry.get("uid").toString();
				time=entry.getLong("time");
				fields=entry.getJSONArray("changed_fields");
				for(int j=0;j<fields.length();j++){
					System.out.println("Entry "+ j+1 +": "+id+","+time+","+fields.getString(j));
					FacebookGetters fg=new FacebookGetters(id);
					message=fg.getUpdatedData(fields.get(j).toString(), time);
					MQTTClientNotifier.sendFacebookUpdate(id, message,time);
				}
				
			}
		} catch (JSONException e) {
			System.out.println("Error with parsing facebook updates: "+e.toString());
		}
	}

}
