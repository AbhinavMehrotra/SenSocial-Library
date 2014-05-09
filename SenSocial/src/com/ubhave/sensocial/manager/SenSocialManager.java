/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
package com.ubhave.sensocial.manager;

import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import android.util.Log;

import com.ubhave.sensocial.configuration.FacebookConfiguration;
import com.ubhave.sensocial.configuration.ServerConfiguration;
import com.ubhave.sensocial.configuration.TwitterConfiguration;
import com.ubhave.sensocial.exceptions.IllegalUserAccess;
import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.exceptions.NullPointerException;
import com.ubhave.sensocial.exceptions.ServerException;
import com.ubhave.sensocial.exceptions.UnauthorizedUserException;
import com.ubhave.sensocial.exceptions.XMLFileException;
import com.ubhave.sensocial.http.IdSenderToDisableTrigger;
import com.ubhave.sensocial.privacy.PPDGenerator;
import com.ubhave.sensocial.socialnetworks.AuthenticateFacebook;
import com.ubhave.sensocial.socialnetworks.AuthenticateTwitter;
import com.ubhave.sensocial.tcp.ClientServerCommunicator;

/**
 * SenSocialManager is a singleton class which acts as the manager for all the other classes.
 */
public class SenSocialManager{

	private AuthenticateFacebook AF;
	private AuthenticateTwitter AT;
	private static Context context;
	private String deviceId, TAG="SNnMB", mac;
	private SharedPreferences sp;
	private static SenSocialManager sensocialManager;
	private static Boolean ServerClient, LocationTrackerRequired;


	public static Context getContext(){
		return context;
	}
	public static void setContext(Context app_context){
		context=app_context;
	}

	/**
	 * Returns the object of SenSocialManager class
	 * @param context Application context
	 * @param Boolean Whether this is server-client application
	 * @param Boolean Whether location tracking is required
	 * @return SenSocialManager object
	 * @throws ServerException
	 */
	public static SenSocialManager getSenSocialManager(Context context, Boolean isServerClientApp, Boolean isLocationTrackerRequired) throws ServerException {
		ServerClient=isServerClientApp;
		LocationTrackerRequired=isLocationTrackerRequired;		
		if(ServerClient || LocationTrackerRequired){
			ServerConfiguration sc=new ServerConfiguration(context);
			if(sc.getServerIP().isEmpty() || sc.getServerProjectURL().isEmpty() || sc.getServerPort()==0){
				Log.e("SNnMB", sc.getServerIP()+", "+sc.getServerProjectURL()+", "+sc.getServerPort());
				throw new ServerException("Server configuration missing!! Help: create ServerConfiguration object and " +
						"set port, url, ip.");
			}
		}
		if (sensocialManager == null){
			sensocialManager = new SenSocialManager(context);
		}		
		return sensocialManager;
	}

	/**
	 * Constructor for Sensor Manager.
	 * @param context Application-context
	 * @throws InvalidSensorException Exception for invalid argument in ClientServerConfig.xml 
	 * @throws XMLFileException Exception for ClientServerConfig.xml not found or cannot be parsed.
	 */
	private SenSocialManager(Context context){
		this.context=context;
		sp=context.getSharedPreferences("SSDATA",0);
		Editor ed=sp.edit();
		ed.putBoolean("accelerometerenabled", true);
		ed.putBoolean("microphoneenabled", true);
		ed.putBoolean("wifienabled", true);
		ed.putBoolean("locationenabled", true);
		ed.putBoolean("bluetoothenabled", true);
		ed.commit();
		if(sp.getString("deviceid", "null").equals("null") || sp.getString("bluetoothmac", "null").equals("null")){
			this.deviceId= Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);    //UUID.randomUUID().toString().substring(0, 6);//"abhinav123";//
			BluetoothAdapter a=BluetoothAdapter.getDefaultAdapter();
			mac= a.getAddress();
			ed=sp.edit();
			ed.putString("deviceid", deviceId);
			ed.putString("bluetoothmac", mac);
			ed.commit();
			Log.i(TAG, "Device id in SenSocial Manager is: "+deviceId);
		}
		else{
			deviceId=sp.getString("deviceid", "null");
			mac=sp.getString("bluetoothmac", "null");
		}
		Log.i(TAG, "Device id in SenSocial Manager is: "+sp.getString("deviceid", "null"));

		//create a default ppd
		PPDGenerator.createDefaultPPD();

		if(LocationTrackerRequired){
			Log.i(TAG, "Strating Location tracking service!");
			context.startService(new Intent(context, com.ubhave.sensocial.client.tracker.LocationTrackerService.class));
		}	
		if(ServerClient){
			Log.i(TAG, "Strating MQTT push notification service!");
			context.startService(new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class));
		}
		ed=sp.edit();
		ed.putBoolean("locationtrackerrequired", LocationTrackerRequired);
		ed.putBoolean("serverclient", ServerClient);
		ed.commit();
	}

	/**
	 * Method to authenticate and enable Facebook triggers. Before calling this method "FacebookConfiguration" should be initialized and facebook appIds should be configured
	 * by calling its method named as "subscribeToFacebook". <br/>
	 * Please also use facebookOnActivityResult in the same activity with this method.
	 * @param activity Activity from which it is called.
	 * @param FBConfig FacebookConfiguration object. 
	 */
	public void authenticateFacebook(Activity activity, FacebookConfiguration FBConfig)throws IllegalUserAccess{
		if(!sp.getString("userid", "null").equals("null") && sp.getBoolean("useridbyfacebook", false)==false && sp.getBoolean("useridbytwitter", false)==false){
			throw new IllegalUserAccess("Please set the user id before using any method.");
		}
		AF=new AuthenticateFacebook(context, FBConfig.getFacebookClientId(), FBConfig.getFacebookClientSecretId());
		AF.tryLogin(activity);
	}

	/**
	 * This method should be called inside the onActivityResult method of the activity from which "authenticateAndEnableFacebookTriggers"
	 * is called. Pass the same "FacebookConfiguration" object which was passed in "authenticateAndEnableFacebookTriggers".
	 * @param currentActivity Activity from which it is called.
	 * @param requestCode Request  code received by onActivityResult method.
	 * @param resultCode Result code received by onActivityResult method.
	 * @param data Data received by onActivityResult method.
	 * @param FBConfig FacebookConfiguration object.
	 */
	public void facebookOnActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data, FacebookConfiguration FBConfig){
		//AF=new AuthenticateFacebook(context, FBConfig.getFacebookClientId(), FBConfig.getFacebookClientSecretId());
		AF.insideOnActivityResult(currentActivity, requestCode, resultCode, data);
	}

	/**
	 * Getter for Facebook user name
	 * @return
	 */
	public String getFacebookUserName(){
		return sp.getString("name", null);
	}

	/**
	 * This method disables Facebook triggers. After calling this method trigger will not be received from Faceboook.
	 * To enable the trigger again, call authenticateAndEnableFacebookTriggers and facebookOnActivityResult.
	 * @throws UnauthorizedUserException If the user is not authorized to receive Facebook triggers or if the Facebook triggers
	 * for the user is already disabled.
	 */
	public void disableFacebookTriggers() throws UnauthorizedUserException{
		String userName= sp.getString("fbusername", "null");
		if(userName.equals("null")){
			throw new UnauthorizedUserException("FACEBOOK");
		}else{
			new IdSenderToDisableTrigger(userName, deviceId, context).sendIdToServer();			
		}
	}

	/**
	 * Method to authenticate and enable Twitter triggers. Before calling this method "TwitterConfiguration" should be initialized and facebook appIds should be configured
	 * by calling its method named as "subscribeToTwitter". <br/>
	 * Please also use twitterOnNewIntent in the same activity with this method.
	 * @param activity Activity from which it is called.
	 * @param TwitterConfig TwitterConfiguration object.
	 */
	public void authenticateTwitter(Activity activity, TwitterConfiguration TwitterConfig){
		AT=AuthenticateTwitter.getInstance(context, TwitterConfig.getTwitterConsumerKey(), TwitterConfig.getTwitterConsumerSecretKey());
		AT.tryLogin(activity);
	}

	/**
	 * This method should be called inside the onNewIntent method of the activity from which "authenticateAndEnableTwitterTriggers"
	 * is called. Pass the same "TwitterConfiguration" object which was passed in "authenticateAndEnableTwitterTriggers".
	 * @param intent Intent received by onNewIntent.
	 * @param TwitterConfig TwitterConfiguration Object.
	 * @return User Object with information of user.
	 */
	public User twitterOnNewIntent(Intent intent, TwitterConfiguration TwitterConfig){
		AT=AuthenticateTwitter.getInstance(context, TwitterConfig.getTwitterConsumerKey(), TwitterConfig.getTwitterConsumerSecretKey());
		return AT.insideOnNewIntent(intent);
	}

	/**
	 * This method disables Twitter triggers. After calling this method trigger will not be received from Twitter.
	 * To enable the trigger again, call authenticateAndEnableTwitterTriggers and twitterOnNewIntent.
	 * @throws UnauthorizedUserException If the user is not authorized to receive Twitter triggers or if the Twitter triggers
	 * for the user is already disabled.
	 */
	public void disableTwitterTriggers() throws UnauthorizedUserException{
		String userName= sp.getString("twitterusername", "null");
		if(userName.equals("null")){
			throw new UnauthorizedUserException("TWITTER");
		}else{
			new IdSenderToDisableTrigger(userName, deviceId, context).sendIdToServer();			
		}
	}

	/**
	 * Getter for user-id
	 * @return String user-id
	 */
	public String getUserId(){
		return sp.getString("userid", null);
	}

	/**
	 * Setter for user-id. <br>
	 * After setting the user-id the client registers itself on the server, if the application is client-server application.
	 * @return String user-id
	 */
	public void setUserId(String userId) throws IllegalUserAccess{
		String user_id=  generateUserId(userId);
		if(!sp.getString("userid", "null").equals("null") &&  !sp.getString("userid", "null").equals(user_id)){
			throw new IllegalUserAccess("User id already set once. Cannot set a new id again");
		}

		if(ServerClient && !sp.getBoolean("userregistered", false)){
			ClientServerCommunicator.registerUser(context,user_id, deviceId, mac);
			context.startService(new Intent(context, com.ubhave.sensocial.client.tracker.LocationTrackerService.class));
		}
		Editor ed=sp.edit();
		ed.putString("userid", user_id);
		ed.putBoolean("useridbyparam", true);
		ed.putBoolean("userregistered", true);
		ed.commit();
	}

	/**
	 * Setter for user-id. via facebook.
	 */
	public void setUserIdByFacebook(){
		Editor ed=sp.edit();
		ed.putBoolean("useridbyfacebook", true);
		ed.putBoolean("userregistered", false);
		ed.commit();
	}

	/**
	 * Setter for user-id. via twitter.
	 */
	public void setUserIdByTwitter(){
		Editor ed=sp.edit();
		ed.putBoolean("useridbytwitter", true);
		ed.putBoolean("userregistered", false);
		ed.commit();
	}


	/**
	 * Generates unique userid 
	 * @return String user-id
	 */
	private String generateUserId(String id){
		id="ssuid"+ id.trim();
		return id;
	}

	/**
	 * Getter for use.
	 * @param userId It is the id(String) which is returned by setUserId methods..
	 * @return User object
	 */
	public com.ubhave.sensocial.manager.User getUser(String userId){
		com.ubhave.sensocial.manager.User user=new com.ubhave.sensocial.manager.User(context, sp.getString("name", null), sp.getString("facebookusername", null), sp.getString("twitterusername", null), 
				sp.getStringSet("facebookfriends", null), sp.getStringSet("twitterfollowers", null));
		return user;
	}

	/**
	 * Method to register a listener(SensorListener) for the specific configuration to receive sensor data.
	 * @param listener SensorListener Object
	 * @param configuration Configuration name for which which listener will receive data
	 */
	public void registerListener(SSListener listener, String streamId){
		SSListenerManager.add(listener, streamId);
	}

	/**
	 * Method to unregister a listener(SensorDataListener) to receive sensor data.
	 * @param listener SensorDataListener object
	 */
	public void unregisterListener(SSListener listener) {
		SSListenerManager.remove(listener);
	}



	//	<<<Removed this method as it uses the SensorConfiguration, which is now automated (See the method below).>>>
	//	/**
	//	 * Method to start the android background-service. It will automatically start HTTP or MQTT service according to the
	//	 * configuration set in the ServerConfiguration. It is mandatory to set 
	//	 * @param sensorConfig SensorConfiguration object.
	//	 * @param serverConfig ServerConfiguration object.
	//	 * @throws NullPointerException If SensorConfiguration or ServerConfiguration is not initialized and configured.
	//	 */
	//	public void startService(SensorConfiguration sensorConfig,	ServerConfiguration serverConfig) throws NullPointerException{
	//		if(sensorConfig==null || serverConfig==null ){
	//			Log.e(TAG, "Service can not be started");
	//			throw new NullPointerException("Objects are not initialized");
	//		}
	//		else{
	//			Log.d(TAG, "Starting the service");
	//			new SenSocialService().startService(context);			
	//		}
	//	}

	//	/**
	//	 * Method to start the android background-service. It will automatically start HTTP or MQTT service according to the
	//	 * configuration set in the ServerConfiguration. It is mandatory to set ServerConfiguration.
	//	 * @param serverConfig ServerConfiguration object.
	//	 * @throws NullPointerException If ServerConfiguration is not configured.
	//	 */
	//	public void startService(ServerConfiguration serverConfig) throws NullPointerException{
	//		if(serverConfig==null ){
	//			Log.e(TAG, "Service can not be started");
	//			throw new NullPointerException("Objects are not initialized");
	//		}
	//		else{
	//			Log.d(TAG, "Starting the service");
	//			SenSocialService.startService(context);			
	//		}
	//	}
	//
	//	/**
	//	 * Method to stop the background service.
	//	 * It will stop whichever service is running in the background (MQTT or HTTP).
	//	 */
	//	public void stopService(){
	//		SenSocialService.stopService(context);
	//	}
	//
	//
	//	/**
	//	 * Method to unsubscribe any sensor for the configured sensors.
	//	 * @param sensor String Sensor Name: accelerometer, bluetooth, wifi, microphone, location.
	//	 * @throws InvalidSensorNameException Caused when the sensor name is invalid.
	//	 */
	//	public void stopSensing(String sensor) throws InvalidSensorNameException{
	//		new StartPullSensors(context).stopIndependentContinuousStreamSensing(sensor);
	//		//		Editor ed=sp.edit();
	//		//		if(sensor.equals("accelerometer")) ed.putBoolean("accelerometer", false);
	//		//		else if(sensor.equals("bluetooth")) ed.putBoolean("bluetooth", false);
	//		//		else if(sensor.equals("wifi")) ed.putBoolean("wifi", false);
	//		//		else if(sensor.equals("microphone")) ed.putBoolean("microphone", false);
	//		//		else if(sensor.equals("location")) ed.putBoolean("location", false);
	//		//		else throw new InvalidSensorNameException();
	//		//		//if OSN independent sensing is On for this sensor then stop it
	//		//		if(sp.getBoolean("streamsensing", false)){
	//		//			new StartPullSensors(context).stopIndependentContinuousStreamSensing(sensor);
	//		//		}
	//	}
	//
	//	/**
	//	 * Method to subscribe any sensor for the configured sensors.
	//	 * @param sensor String Sensor Name: accelerometer, bluetooth, wifi, microphone, location.
	//	 * @throws InvalidSensorNameException Caused when the sensor name is invalid.
	//	 */
	//	public void startSensing(String sensor) throws InvalidSensorNameException{
	//		new StartPullSensors(context).startIndependentContinuousStreamSensing(sensor);
	//		//		Editor ed=sp.edit();
	//		//		if(sensor.equals("accelerometer")) ed.putBoolean("accelerometer", true);
	//		//		else if(sensor.equals("bluetooth")) ed.putBoolean("bluetooth", true);
	//		//		else if(sensor.equals("wifi")) ed.putBoolean("wifi", true);
	//		//		else if(sensor.equals("microphone")) ed.putBoolean("microphone", true);
	//		//		else if(sensor.equals("location")) ed.putBoolean("location", true);
	//		//		else throw new InvalidSensorNameException();
	//		//		//if configured for OSN independent sensing the start it for this sensor
	//		//		if(sp.getBoolean("streamsensing", false)){
	//		//			new StartPullSensors(context).startIndependentContinuousStreamSensing(sensor);
	//		//		}
	//	}



	//	public String setUserIdByFacebook() throws NullPointerException, IllegalUserAccess{
	//		if(sp.getString("fbusername", "null").equals("null")){
	//			throw new NullPointerException("Facebook object is null. Cannot set user-id by facebook account before their authentication.");
	//		}
	//		else{
	//			String user_id=  generateUserId(sp.getString("fbusername", "null"));	
	//			if(!sp.getString("userid", "null").equals("null") &&  !sp.getString("userid", "null").equals(user_id)){
	//				throw new IllegalUserAccess("User id already set once. Cannot set a new id again");
	//			}
	//			if(ServerClient && !sp.getBoolean("userregistered", false)){
	//				ClientServerCommunicator.registerUser(context,user_id, deviceId, mac);
	//				ClientServerCommunicator.registerFacebook(context, sp.getString("name", "null"), user_id,
	//						sp.getString("fbusername", "null"),  sp.getString("fbtoken", "null"));
	//				context.startService(new Intent(context, com.ubhave.sensocial.client.tracker.LocationTrackerService.class));
	//			}
	//			Editor ed=sp.edit();
	//			ed.putString("userid", user_id);
	//			ed.putBoolean("useridbyfacebook", true);
	//			ed.putBoolean("userregistered", true);
	//			ed.commit();	
	//			return user_id;	
	//		}
	//	}
	//
	//	public String setUserIdByTwitter() throws NullPointerException, IllegalUserAccess{
	//		if(sp.getString("twitterusername", "null").equals("null")){
	//			throw new NullPointerException("Twitter object is null. Cannot set user-id by twitter account before their authentication.");
	//		}
	//		else{
	//			String user_id= generateUserId(sp.getString("twitterusername", "null"));	
	//			if(!sp.getString("userid", "null").equals("null") &&  !sp.getString("userid", "null").equals(user_id)){
	//				throw new IllegalUserAccess("User id already set once. Cannot set a new id again");
	//			}
	//			if(ServerClient && !sp.getBoolean("userregistered", false)){
	//				ClientServerCommunicator.registerUser(context,user_id, deviceId, mac);
	//				ClientServerCommunicator.registerTwitter(context, sp.getString("name", "null"), user_id,
	//						sp.getString("twitterusername", "null"),  sp.getString("twittertoken", "null"));
	//				context.startService(new Intent(context, com.ubhave.sensocial.client.tracker.LocationTrackerService.class));
	//			}
	//			Editor ed=sp.edit();
	//			ed.putString("userid", user_id);
	//			ed.putBoolean("useridbytwitter", true);
	//			ed.putBoolean("userregistered", true);
	//			ed.commit();
	//			return user_id;
	//		}
	//	}



}
