package com.ubhave.sensocial.http;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class IdSenderToDisableTrigger {

	final private String TAG = "SNnMB";
	private String serverUrl;
	private String uuId;
	private String userName;
	private SharedPreferences sp;
	
	public IdSenderToDisableTrigger(String userName, String uuId, Context context) {
		sp=context.getSharedPreferences("SNnMB", 0);
		this.userName=userName;
		this.uuId=uuId;
		this.serverUrl= sp.getString("server", "");
	}

	/**
	 * Method to send the ScreenName for Social Network  and the Unique Id.
	 */
	public void sendIdToServer(){
		Thread th= new Thread(){
    		public void run(){
    			try{
    				HttpClient httpclient = new DefaultHttpClient();
    				String uri=serverUrl+"DeleteUser.php?uuid="+uuId;
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
