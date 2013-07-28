package com.ubhave.sensocial.mqtt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.ubhave.sensocial.filters.ConfigurationHandler;
import com.ubhave.sensocial.filters.FilterSettings;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * DownloadFilter class downloads the filter from the server.
 * It extends AsyncTask to carry this process in background thread.
 */
public class DownloadFilter extends AsyncTask<Void,Void,Integer>{
	private Context context;
	private String url;
	private String dest_file_name;
	private String TAG="SNnMB";
	
	/**
	 * Constructor 
	 * @param context Application context
	 * @param String url 
	 * @param String dest_file_name
	 */
	protected DownloadFilter(Context context, String url, String dest_file_name) {
		this.context=context;
		this.url=url;
		this.dest_file_name=dest_file_name;
	}
	@Override
	public void onPreExecute()
	{
		Log.i("SNnMB", "Download filter file:  onPreExecute");
	}
	@Override
	protected Integer doInBackground(Void... arg0) {
		
		try {
			File dest_file = new File(Environment.getExternalStorageDirectory(), dest_file_name);
			dest_file.createNewFile();
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
			
			FilterSettings.mergeFilters(dest_file_name);

			Log.d(TAG,"Download Complete");
			ConfigurationHandler.run(context);
			
		} catch(FileNotFoundException e) {
			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
		} catch (IOException e) {
			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
		}
		return 1;
		
	}

	public Void onPostExecute(Integer... arg){
		Log.d(TAG,"DownloadFilter-onPostExecute: Set new configuration and sensor list");
		return null;		
	}
}
