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
			//feed: story, statuses: message
			String path="https://graph.facebook.com/"+this.userId+"/"+changedField+"?access_token="+this.token;  //+"&fields=username"
			URL url = new URL(path);
			InputStream Istream=url.openConnection().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
			JSONObject json= new JSONObject(r.readLine());
			json=json.getJSONObject(changedField);
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
			System.out.println(e.toString());
		}
		return data;
	}

	private long timeDifference(){
		Calendar c= Calendar.getInstance();

		return 0;
	}

}
