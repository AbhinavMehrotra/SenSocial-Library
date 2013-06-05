package com.ubhave.sensocial.server.osn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ubhave.sensocial.server.database.UserRegistrar;

public class FacebookGetters {

	String userId;
	String token="CAACEdEose0cBAJxzdE8OA4XG5oVNnhjE9eBT04AgM7FCReTpGhZBIQBK6ntmT2GDxY8jMKYVDJ4IZBQVtZAZB24VL105X4IRnLi4hV4XKZBbakjq6W7oZBeFKlbi7lllFEFoDfKEMcAPfbARhqCcVlGwiORbjDeYsZD";
	
	public FacebookGetters(String userId) {
		this.userId=userId;
		this.token=UserRegistrar.getFacebookToken(userId); 
	}
	
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
	
	public String getUpdatedData(String changedField, long updationTime){
		String data="";
		try {
			String path="https://graph.facebook.com/"+this.userId+"/"+changedField+"?access_token="+this.token+"&fields=username";
			URL url = new URL(path);
			InputStream Istream=url.openConnection().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
			JSONObject json= new JSONObject(r.readLine());
			json=json.getJSONObject(changedField);
			JSONArray ar= new JSONArray();
			ar=json.getJSONArray("data");
			JSONObject obj;
			Calendar c= Calendar.getInstance();
			String hr=""+c.get(Calendar.HOUR);
			String min=""+c.get(Calendar.MINUTE);
			String date=""+c.get(Calendar.YEAR)+"-"+(1+c.get(Calendar.MONTH))+"-"+ c.get(Calendar.DATE);
			for(int i=0;i<5;i++){  // checking only first 5 entries
				obj=ar.getJSONObject(i);
				if(obj.get("username").toString().equalsIgnoreCase(this.userId) && obj.get("created_time").toString().startsWith(date)){
					String hour=obj.get("created_time").toString().substring(11, 13);
					String minute=obj.get("created_time").toString().substring(14, 16);
					if(hr.equalsIgnoreCase(hour) && (Integer.parseInt(minute)-Integer.parseInt(min))<10){
						data=obj.getString("message");
						return data;
					}
					
				}
			}			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return data;
	}

}
