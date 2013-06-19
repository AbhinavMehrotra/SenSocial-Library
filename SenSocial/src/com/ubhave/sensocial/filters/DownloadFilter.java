package com.ubhave.sensocial.filters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.ubhave.sensormanager.data.SensorData;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadFilter extends AsyncTask<Void,Void,Void>{
	private Context context;
	private String url;
	private String dest_file_path;
	private String TAG="SNnMB";
	public DownloadFilter(Context context, String url, String dest_file_path) {
		this.context=context;
		this.url=url;
		this.dest_file_path=dest_file_path;
	}

	protected Void doInBackground(Void... arg0) {
//		final String dwnload_file_path = "http://abhinavtest.net76.net/temp/filter.xml";
//		final String dest_file_path = "/mnt/sdcard/dwn1.xml";
		
		try {
			File dest_file = new File(dest_file_path);
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			int contentLength = conn.getContentLength();
			DataInputStream stream = new DataInputStream(u.openStream());
			byte[] buffer = new byte[contentLength];
			stream.readFully(buffer);
			stream.close();
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(dest_file));
			fos.write(buffer);
			fos.flush();
			fos.close();
			
			FilterSettings.mergeFilters(dest_file_path,"/res/raw/filter.xml");

		} catch(FileNotFoundException e) {
			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
		}
		
		return null;
	}
	
	public void onPostExecute(Void... arg){
		Log.d(TAG,"DownloadFilter-onPostExecute: Set new configuration and sensor list");
		
		//new FilterParser(context).parseXML();
		ConfigurationHandler.run(context);		
	}
}
