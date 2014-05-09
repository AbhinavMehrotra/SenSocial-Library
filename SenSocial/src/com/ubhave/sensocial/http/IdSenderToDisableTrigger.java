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
		sp=context.getSharedPreferences("SSDATA", 0);
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
