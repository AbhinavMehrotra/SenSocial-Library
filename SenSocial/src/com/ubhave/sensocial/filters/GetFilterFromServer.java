package com.ubhave.sensocial.filters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.util.Log;

public class GetFilterFromServer {

//	final private String TAG = "SNnMB";
//	private Context context;
//
//	public void downloadFilter(Context context){
//		this.context=context;
//		//make these paths generic
//		final String dwnload_file_path = "http://abhinavtest.net76.net/temp/filter.xml";
//		final String dest_file_path = "/mnt/sdcard/dwn1.xml";
//		new Thread(new Runnable() {
//			public void run() {
//				downloadFile(dwnload_file_path, dest_file_path);
//			}
//		}).start();
//	}
//
//	private void downloadFile(String url, String dest_file_path) {
//		try {
//			File dest_file = new File(dest_file_path);
//			URL u = new URL(url);
//			URLConnection conn = u.openConnection();
//			int contentLength = conn.getContentLength();
//			DataInputStream stream = new DataInputStream(u.openStream());
//			byte[] buffer = new byte[contentLength];
//			stream.readFully(buffer);
//			stream.close();
//			DataOutputStream fos = new DataOutputStream(new FileOutputStream(dest_file));
//			fos.write(buffer);
//			fos.flush();
//			fos.close();
//			
//			//FilterSettings.mergeFilters(dest_file_path);
//			new FilterParser(context).parseXML();
//
//		} catch(FileNotFoundException e) {
//			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
//		} catch (IOException e) {
//			Log.e(TAG, "Error while getting new filter!!\n"+e.toString());
//		}
//	}
}

