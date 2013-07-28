//package com.ubhave.sensocial.http;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.util.Log;
//
//public class IdSenderToEnableTrigger {
//
//	final private String TAG = "SNnMB";
//	private String fbName;
//	private String fbToken;
//	private String twitterName;
//	private String serverUrl;
//	private String uuId;
//
//	/**
//	 * Method to set the FacebookUserName, TwitterScreenName and the Unique Android Id. <br/>
//	 * These can be send to the server by calling sendIdToServer.
//	 */
//	public IdSenderToEnableTrigger(String fbName, String fbToken, String twitterName, String uuId, String serverUrl){
//		this.fbName=fbName;
//		this.fbToken=fbToken;
//		this.twitterName=twitterName;
//		this.uuId=uuId;
//		this.serverUrl=serverUrl;
//	}
//
//	/**
//	 * Method to send the FacebookUserName, TwitterScreenName and the Unique Android Id.
//	 */
//	public void sendIdToServer(){
//		Thread th= new Thread(){
//    		public void run(){
//    			try{
//    				HttpClient httpclient = new DefaultHttpClient();
//    				String uri=serverUrl+"getemailid.php?fb_id="+fbName+"&fbtoken="+fbToken+"&twitter="+twitterName+"&uuid="+uuId;
//    				Log.d(TAG, "Sending names to: "+uri);
//    				HttpPost httppost = new   HttpPost(uri);  
//    				HttpResponse response = httpclient.execute(httppost);
//    				Log.d(TAG, "Success"+response.getParams());
//    			} catch (MalformedURLException e) {
//    				Log.e(TAG, e.toString());
//    			} catch (IOException e) {
//    				Log.e(TAG, e.toString());
//    			}
//    		}
//    	};
//    	th.start();		
//	}
//}