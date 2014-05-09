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
package com.ubhave.sensocial.server.osn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;

public class FacebookGetters {

	String userId;
	String token; 
	
	/**
	 * Constructor
	 * @param userId(String) Facebook id 
	 */
	public FacebookGetters(String userId) {
		this.userId=userId;
		this.token=UserRegistrar.getFacebookToken(userId); 
		System.out.println("Token: "+ this.token);
	}

	/**
	 * Gets List of Facebook friends of user from the Facebook server
	 * @return ArrayList<String> Facebook friends
	 */
	public ArrayList<String> getFriends(){
		ArrayList<String> friends = new ArrayList<String>();
		try {
			String path="https://graph.facebook.com/me/friends?access_token="+this.token+"&fields=username";
			URL url = new URL(path);
			InputStream Istream=url.openConnection().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
			JSONObject json= new JSONObject(r.readLine());
			JSONArray ar= new JSONArray();
			ar=json.getJSONArray("data");
			JSONObject obj;
			for(int i=0;i<ar.length();i++){
				obj=ar.getJSONObject(i);
				if(obj.has("username"))
					friends.add(obj.get("username").toString());
			}			
		} catch (Exception e) {
			System.out.println(e.toString());
		}		
		return friends;
	}

	/**
	 * Gets the dats updated by any user on Facebook.
	 * Request is sent to the Facebook server to provide the data.
	 * @param changedField (String) Changed field on users account
	 * @param updationTime (String) Time of update
	 * @return (String) Data updated by the user on Facebook.
	 */
	public String getUpdatedData(String changedField, long updationTime){
		String data="";
		try {
			//feed: story, statuses: message
			String path="https://graph.facebook.com/"+this.userId+"/"+changedField+"?access_token="+this.token;  //+"&fields=username"
			URL url = new URL(path);

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("webcache.cs.bham.ac.uk", 3128));
			HttpsURLConnection conn = (HttpsURLConnection) new URL(path).openConnection(proxy);
			
			//HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.connect();
			InputStream Istream=conn.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
			JSONObject json= new JSONObject(r.readLine());
			//json=json.getJSONObject(changedField);
			JSONArray ar= new JSONArray();
			ar=json.getJSONArray("data");	
			JSONObject obj;
			Calendar c= Calendar.getInstance();
			int hr=c.get(Calendar.HOUR);
			int min=c.get(Calendar.MINUTE);
			int yr=c.get(Calendar.YEAR);
			int mnth=(1+c.get(Calendar.MONTH));
			int dt=c.get(Calendar.DATE);
			//"updated_time": "2013-07-03T19:43:39+0000"
			if(changedField.equalsIgnoreCase("feed")){
				for(int i=0;i<5;i++){  // checking only first 5 entries, check all b'coz there might be instant likes or comments
					obj=ar.getJSONObject(i);
					String uid= ((JSONObject)obj.get("from")).getString("id");
					//we check data b'coz message might be deleted
					if(uid.equalsIgnoreCase(this.userId)){
						String str=obj.getString("created_time").toString();
						int year=Integer.parseInt(str.substring(0,4));
						int month=Integer.parseInt(str.substring(5,7));
						int date=Integer.parseInt(str.substring(8,10));
						int hour=Integer.parseInt(str.substring(11, 13));
						int minute=Integer.parseInt(str.substring(14, 16));
						//check message is not older than 3 mins
						if( timeDifference()< 3*60){
							try{
								data=obj.getString("message");	// status_update comes as "message" under feed							
							}
							catch(Exception e){
								data=obj.getString("story");								
							}
							return data;
						}
						else{
							break;
						}
					}
				}
			}
			else if(changedField.equalsIgnoreCase("statuses")){
				for(int i=0;i<5;i++){  // checking only first 5 entries, check all b'coz there might be instant likes or comments
					obj=ar.getJSONObject(i);
					String uid= ((JSONObject)obj.get("from")).getString("id");
					//we check data b'coz message might be deleted
					if(uid.equalsIgnoreCase(this.userId)){
						String str=obj.getString("updated_time").toString();
						int year=Integer.parseInt(str.substring(0,4));
						int month=Integer.parseInt(str.substring(5,7));
						int date=Integer.parseInt(str.substring(8,10));
						int hour=Integer.parseInt(str.substring(11, 13));
						int minute=Integer.parseInt(str.substring(14, 16));
						//check message is not older than 3 mins
						if( timeDifference()< 3*60){
							data=obj.getString("message");
							return data;
						}
						else{
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Facebook connection timed-out. Check your internet connection.\n"+ e.toString());
			return "message unavailable";
		}
		return data;
	}

	private long timeDifference(){
		Calendar c= Calendar.getInstance();

		return 0;
	}

}
