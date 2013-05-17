package com.ubhave.sensocial.manager;

import java.util.UUID;

import twitter4j.User;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ubhave.sensocial.configuration.FacebookConfiguration;
import com.ubhave.sensocial.configuration.ServerConfiguration;
import com.ubhave.sensocial.configuration.TwitterConfiguration;
import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.exceptions.InvalidSensorNameException;
import com.ubhave.sensocial.exceptions.NullPointerException;
import com.ubhave.sensocial.exceptions.UnauthorizedUserException;
import com.ubhave.sensocial.exceptions.XMLFileException;
import com.ubhave.sensocial.filters.PrivacyPolicyDescriptorParser;
import com.ubhave.sensocial.filters.SensorConfiguration;
import com.ubhave.sensocial.http.HTTPService;
import com.ubhave.sensocial.http.IdSenderToDisableTrigger;
import com.ubhave.sensocial.listener.SensorDataListener;
import com.ubhave.sensocial.listener.SensorDataListenerManager;
import com.ubhave.sensocial.listener.SocialNetworkListner;
import com.ubhave.sensocial.mqtt.PushCallback;
import com.ubhave.sensocial.socialnetworks.AuthenticateFacebook;
import com.ubhave.sensocial.socialnetworks.AuthenticateTwitter;
import com.ubhave.sensocial.listener.SocialNetworkListenerManager;;

public class SenSocialManager{

	private AuthenticateFacebook AF;
	private AuthenticateTwitter AT;
	private Context context;
	private String uuId, TAG="SNnMB";
	private SharedPreferences sp;

	/**
	 * Constructor for Sensor Manager.
	 * @param context Application-context
	 * @throws InvalidSensorException Exception for invalid argument in ClientServerConfig.xml 
	 * @throws XMLFileException Exception for ClientServerConfig.xml not found or cannot be parsed.
	 */
	public SenSocialManager(Context context) throws InvalidSensorException, XMLFileException{
		this.context=context;
		sp=context.getSharedPreferences("snmbData",0);
		if(sp.getString("uuid", "null").equals("null")){
			uuId=UUID.randomUUID().toString();
			Editor ed=sp.edit();
			ed.putString("uuid", uuId);
			ed.commit();
		}
		else{
			uuId=sp.getString("uuid", "null");
		}
		new PrivacyPolicyDescriptorParser(context).refineXML();
	}

	/**
	 * Method to authenticate and enable Facebook triggers. Before calling this method "FacebookConfiguration" should be initialized and facebook appIds should be configured
	 * by calling its method named as "subscribeToFacebook". <br/>
	 * Please also use facebookOnActivityResult in the same activity with this method.
	 * @param activity Activity from which it is called.
	 * @param FBConfig FacebookConfiguration object. 
	 */
	public void authenticateAndEnableFacebookTriggers(Activity activity, FacebookConfiguration FBConfig){
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
	 * Method to retreive Facebook user name, after successful login.
	 * It returns null if user is not logged-in.
	 * @return String Facebook userName
	 */
	public String getFacebookUserName(){
		return sp.getString("name", "null");
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
			new IdSenderToDisableTrigger(userName, uuId, context).sendIdToServer();			
		}
	}

	/**
	 * Method to authenticate and enable Twitter triggers. Before calling this method "TwitterConfiguration" should be initialized and facebook appIds should be configured
	 * by calling its method named as "subscribeToTwitter". <br/>
	 * Please also use twitterOnNewIntent in the same activity with this method.
	 * @param activity Activity from which it is called.
	 * @param TwitterConfig TwitterConfiguration object.
	 */
	public void authenticateAndEnableTwitterTriggers(Activity activity, TwitterConfiguration TwitterConfig){
		AT=new AuthenticateTwitter(context, TwitterConfig.getTwitterConsumerKey(), TwitterConfig.getTwitterConsumerSecretKey());
		AT.tryLogin(activity);
	}

	/**
	 * This method should be called inside the onNewIntent method of the activity from which "authenticateAndEnableTwitterTriggers"
	 * is called. Pass the same "TwitterConfiguration" object which was passed in "authenticateAndEnableTwitterTriggers".
	 * @param intent Intent received by onNewIntent.
	 * @param TwitterConfig TwitterConfiguration Object.
	 * @return User Object with information of user.
	 */
	public User twitterOnNewIntent(Intent intent){
		return AT.insideOnNewIntent(intent);
	}
	
	/**
	 * Method to retreive Twitter user name, after successful login.
	 * It returns null if user is not logged-in.
	 * @return String Twitter userName
	 */
	public String getTwitterUserName(){
		return sp.getString("twitterusername", "null");
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
			new IdSenderToDisableTrigger(userName, uuId, context).sendIdToServer();			
		}
	}

//	<<<Removed this method as it uses the ServerConfiguration, which is now automated (See the method below).>>>
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
	
	/**
	 * Method to start the android background-service. It will automatically start HTTP or MQTT service according to the
	 * configuration set in the ServerConfiguration. It is mandatory to set ServerConfiguration.
	 * @param serverConfig ServerConfiguration object.
	 * @throws NullPointerException If ServerConfiguration is not configured.
	 */
	public void startService(ServerConfiguration serverConfig) throws NullPointerException{
		if(serverConfig==null ){
			Log.e(TAG, "Service can not be started");
			throw new NullPointerException("Objects are not initialized");
		}
		else{
			Log.d(TAG, "Starting the service");
			new SenSocialService().startService(context);			
		}
	}

	/**
	 * Method to stop the background service.
	 * It will stop whichever service is running in the background (MQTT or HTTP).
	 */
	public void stopService(){
		new SenSocialService().stopService(context);
	}

	public boolean serviceIsRunning() {
		return new SenSocialService().isRunning(context);
	}
	
	/**
	 * Method to unsubscribe any sensor for the configured sensors.
	 * @param sensor String Sensor Name: accelerometer, bluetooth, wifi, microphone, location.
	 * @throws InvalidSensorNameException Caused when the sensor name is invalid.
	 */
	public void stopSensingFromThisSensor(String sensor) throws InvalidSensorNameException{
		Editor ed=sp.edit();
		if(sensor.equals("accelerometer")) ed.putBoolean("accelerometer", false);
		else if(sensor.equals("bluetooth")) ed.putBoolean("bluetooth", false);
		else if(sensor.equals("wifi")) ed.putBoolean("wifi", false);
		else if(sensor.equals("microphone")) ed.putBoolean("microphone", false);
		else if(sensor.equals("location")) ed.putBoolean("location", false);
		else throw new InvalidSensorNameException();
	}

	/**
	 * Method to register a listener(SocialNetworkListner) to receive updates from social network.
	 * @param listener SocialNetworkPostListner object
	 */
	public void registerSocialNetworkListener(SocialNetworkListner listener){
		SocialNetworkListenerManager.add(listener);
	}

	/**
	 * Method to unregister a listener(SocialNetworkListner) to receive updates from social network.
	 * @param listener SocialNetworkPostListner object
	 */
	public void unregisterSocialNetworkListener(SocialNetworkListner listener) {
		SocialNetworkListenerManager.remove(listener);
	}

	/**
	 * Method to register a listener(SensorDataListener) to receive sensor data.
	 * @param listener SensorDataListener object
	 */
	public void registerSensorDataListener(SensorDataListener listener){
		SensorDataListenerManager.add(listener);
	}

	/**
	 * Method to unregister a listener(SensorDataListener) to receive sensor data.
	 * @param listener SensorDataListener object
	 */
	public void unregisterSensorDataListener(SensorDataListener listener) {
		SensorDataListenerManager.remove(listener);
	}

}
