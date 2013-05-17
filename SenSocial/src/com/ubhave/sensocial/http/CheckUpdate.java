package com.ubhave.sensocial.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class CheckUpdate {
	
	final private String TAG = "SNnMB";
	private String serverUrl;

	protected CheckUpdate(String serverUrl){
		this.serverUrl=serverUrl;
	}
	
	/**
	 * Method to check the trigger from SN. <br/>
	 * It checks the server for any new update.
	 * @return String '1' for new update, else '0'. 
	 */
	public String checkNow(){
		String myString="0";
		try{
            URL myURL = new URL(serverUrl+"checktrigger1.php");   
            URLConnection ucon = myURL.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while((current=bis.read())!=-1){
                baf.append((byte)current);
            }
            myString = new String (baf.toByteArray());
            return myString;
        }catch(Exception e){
    		Log.e(TAG, "Error while checking for update!!\n"+e.toString());
        }
		return myString;
	}
}
