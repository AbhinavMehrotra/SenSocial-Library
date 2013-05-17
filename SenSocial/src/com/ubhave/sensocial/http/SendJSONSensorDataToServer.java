package com.ubhave.sensocial.http;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.ubhave.sensormanager.data.SensorData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class SendJSONSensorDataToServer extends AsyncTask<String,Void,String>{
	final private String TAG = "SNnMB";
	private HttpURLConnection connection = null;
	private DataOutputStream outputStream = null;
	private String serverUrl;
	private String serverResponseMessage;
	private Context context;
	SensorData sensorData;
	
	/**
	 * Constructor
	 * @param context Application context
	 * @param sensorData SensorData received after completion of sensing task
	 */
	public SendJSONSensorDataToServer( Context context, SensorData sensorData){
		this.context=context;
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		this.serverUrl=sp.getString("sever", null);
	}

	/**
	 * Sends the sensor data in JSON format to the server. 
	 */
	@Override
	protected String doInBackground(String... arg0) {
		try{
			Log.d(TAG, "Sending JSON data to server");
			URL url = new URL(serverUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(sensorData.toString());
			
			int serverResponseCode = connection.getResponseCode();
			serverResponseMessage = connection.getResponseMessage();
			Log.d("response",""+serverResponseCode);
			Log.d("serverResponseMessage",""+serverResponseMessage);
			outputStream.flush();
			outputStream.close();

		}
		catch (Exception ex){
			Log.e(TAG, ex.toString());
			serverResponseMessage=ex.toString();
		}
		return ("Bytes read: "+serverResponseMessage);
	}
	
	@Override
	protected void onPostExecute(String result){
		Log.d(TAG, "Sending JSON sensor data complete");
		Log.d(TAG, "Server response: "+result);
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		Editor ed=sp.edit();
		ed.putBoolean("sensing", false);
		ed.commit();
	}

}
