package com.sensocial.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class SendSensorDataToServer extends AsyncTask<String,Void,String>{
	final private String TAG = "SNnMB";
	private HttpURLConnection connection = null;
	private DataOutputStream outputStream = null;
	private DataInputStream inputStream = null;
	private String pathToOurFile = "/mnt/sdcard/";
	private String serverUrl;
	private String lineEnd= "\r\n", twoHyphens= "--", boundary=  "*****";
	private int bytesRead, bytesAvailable, bufferSize;
	private byte[] buffer;
	private int maxBufferSize = 1*1024*1024;
	private String serverResponseMessage;
	private Context context;
	
	/**
	 * Constructor to set file attributes.
	 * @param folderName Folder where file exists
	 * @param fileName Name of sensor data file
	 * @param context Application context
	 */
	public SendSensorDataToServer(String folderName, String fileName, Context context){
		this.pathToOurFile=this.pathToOurFile+folderName+"/"+fileName;
		this.context=context;
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		this.serverUrl=sp.getString("sever", null);
	}

	/**
	 * Sends the sensor data file to the server. 
	 */
	@Override
	protected String doInBackground(String... arg0) {
		try{
			Log.d(TAG, "trying Sending File");
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
			URL url = new URL(serverUrl);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0){
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			serverResponseMessage = connection.getResponseMessage();
			Log.d("response",""+serverResponseCode);
			Log.d("serverResponseMessage",""+serverResponseMessage);
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			serverResponseMessage+="\n and try end";
		}
		catch (Exception ex){
			Log.e(TAG, ex.toString());
			serverResponseMessage=ex.toString();
		}
		return ("Bytes read: "+serverResponseMessage);
	}
	
	/**
	 * Delete the file of sensor data after it has been sent to the server. 
	 * It also set sensing as false, so that any new trigger can now start sensing.
	 */
	@Override
	protected void onPostExecute(String result){
		Log.d(TAG, "File Sending Complete");
		Log.d(TAG, result);
		File file = new File(pathToOurFile);
		boolean deleted = file.delete();
		Log.d(TAG, "File Deleted:" + deleted);
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		Editor ed=sp.edit();
		ed.putBoolean("sensing", false);
		ed.commit();
	}

}
