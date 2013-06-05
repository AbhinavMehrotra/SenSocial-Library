package com.ubhave.sensocial.server.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.User;

public class UserRegistrar {

	private static String TAG="SNnMB";
	private String name;
	private String userId;
	private String fbName;
	private String twName;
	private ArrayList<Device> devices;
	private ArrayList<String> fbFriends;
	private ArrayList<String> twFollowers;
	private Location deviceLocation;

	public static void registerUser(String userName, String deviceId, String bluetoothMAC, String fbName, String fbId, String fbToken, String twitterName, double latitude, double longitude){

		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		//query facebook  friends- same as client
		//query twitter   followers- same as client
		//how to update these?- thread but it might be killed if server restarts

		String userId=generateUserId(fbName, twitterName);
		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");

			//Registering the existing user with new device
			BasicDBObject doc = new BasicDBObject("facebookname", fbName);
			BasicDBObject doc1=new BasicDBObject("twittername", twitterName);
			if(coll.find(doc).count()>0 || coll.find(doc1).count()>0){
				DBObject obj = coll.find(doc).next();
				//update the device list
				ArrayList<String> deviceid=new ArrayList<String>();
				deviceid= (ArrayList<String>) obj.get("deviceids");
				deviceid.add(deviceId);
				obj.removeField("deviceids");
				obj.put("deviceids", deviceid);
				
				//update the bluetooth MAP
				Map<String,String> bluetooth=new HashMap<String,String>();
				bluetooth= (Map<String, String>) obj.get("bluetoothmac");
				bluetooth.put(deviceId, bluetoothMAC);
				obj.removeField("bluetoothmac");
				obj.put("bluetoothmac", bluetooth);

				//update the location map
				ArrayList<Double> loc=new ArrayList<Double>();
				loc.add(latitude);
				loc.add(longitude);
				HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");
				location.put(deviceId, loc);
				obj.removeField("location");
				obj.put("location", location);
				coll.save(obj);
			}

			else{
				ArrayList<String> deviceid=new ArrayList<String>();
				deviceid.add(deviceId);

				Map<String,String> bluetooth=new HashMap<String,String>();
				bluetooth.put(deviceId,bluetoothMAC);

				ArrayList<Double> loc=new ArrayList<Double>();
				loc.add(latitude);
				loc.add(longitude);
				HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();
				location.put(deviceId, loc);

				doc = new BasicDBObject("username", userName).
						append("userid", userId).
						append("deviceids", deviceid).
						append("bluetoothmac", bluetooth).
						append("facebookname", fbName).
						append("facebooktoken",fbToken).
						append("facebookid",fbId).
						append("twittername",twitterName).
						append("facebookfriends", fbFriends).
						append("twitterfollowers", twFollowers).
						append("location", loc);

				coll.insert(doc);				
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	public static void unregisterDevice(String deviceId){
		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );			
			DBCollection coll = db.getCollection("User");		
			DBObject obj;
			DBCursor cursor=coll.find();
			ArrayList<String> deviceid=new ArrayList<String>();
			Map<String,String> bluetooth=new HashMap<String,String>();
			while(cursor.hasNext()){
				obj=cursor.next();
				deviceid= (ArrayList<String>) obj.get("deviceid");
				bluetooth= (Map<String, String>) obj.get("bluetoothmac");
				if(deviceid.contains(deviceId)){
					deviceid.remove(deviceId);
					obj.removeField("deviceid");
					obj.put("deviceid", deviceid);					
					bluetooth.remove(deviceId);
					obj.removeField("bluetoothmac");
					obj.put("bluetoothmac", bluetooth);					
					coll.save(obj);
					break;
				}				
			}
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	public static User getUser(String osn_name_or_id){
		String name;
		String fbName;
		String twName;
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");			
			BasicDBObject doc1 = new BasicDBObject("facebookname", osn_name_or_id);
			BasicDBObject doc2 = new BasicDBObject("twittername", osn_name_or_id);	
			BasicDBObject doc3 = new BasicDBObject("facebookid", osn_name_or_id);
			BasicDBObject doc4 = new BasicDBObject("twitterid", osn_name_or_id);			
			if(coll.find(doc1).count()>0){
				DBObject obj = coll.find(doc1).next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				return new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);
			}
			else if(coll.find(doc2).count()>0){
				DBObject obj = coll.find(doc1).next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				return new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);				
			}
			else if(coll.find(doc3).count()>0){
				DBObject obj = coll.find(doc1).next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				return new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);				
			}
			else if(coll.find(doc4).count()>0){
				DBObject obj = coll.find(doc1).next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				return new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);				
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		//if user not found then return null
		return null;
	}
	

	public static Device getDeviceWithBluetooth(String bluetoothMAC){
		String name;
		String fbName;
		String twName;
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");		
			DBCursor cursor = coll.find();
			DBObject obj;
			Map<String,String> bluetooths=new HashMap<String,String>();
			ArrayList<Device> devices=new ArrayList<Device>();
			while(cursor.hasNext()){
				obj=cursor.next();
				bluetooths=(Map<String, String>) obj.get("bluetoothmac");
				for(Map.Entry<String, String> b:bluetooths.entrySet()){
					if(b.getValue().contains(bluetoothMAC)){
						name=obj.get("name").toString();
						id=obj.get("userid").toString();
						fbName=obj.get("facebookname").toString();
						twName=obj.get("twittername").toString();
						fbFriends=(ArrayList<String>) obj.get("facebookfriends");
						twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
						deviceIds=(ArrayList<String>) obj.get("deviceids");
						location=(HashMap<String, ArrayList<Double>>) obj.get("location");	
						User user = new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);
						devices= user.getDevices();
						for(Device d:devices){
							if(d.getDeviceId().equalsIgnoreCase(b.getKey())){
								return d;
							}
						}
					}
				}
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		//if user not found then return null
		return null;
	}
	

	public static String getBluetooth(String deviceId){
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");		
			DBCursor cursor = coll.find();
			DBObject obj;
			Map<String,String> bluetooths=new HashMap<String,String>();
			while(cursor.hasNext()){
				obj=cursor.next();
				bluetooths=(Map<String, String>) obj.get("bluetoothmac");
				for(Map.Entry<String, String> b:bluetooths.entrySet()){
					if(b.getKey().contains(deviceId)){
						return b.getValue();
					}
				}
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		//if device not found then return null
		return null;
	}
	
	public static Set<User> getAllUsers(){
		Set<User> users= new HashSet<User>();
		String name;
		String fbName;
		String twName;
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");			
			DBCursor cursor= coll.find();
			while(cursor.hasNext()){
				DBObject obj = cursor.next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				users.add( new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location) );
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		
		return users;
	}

	public static Set<User> getAllUsers(String userName){
		Set<User> users= new HashSet<User>();

		String name;
		String fbName;
		String twName;
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");			
			BasicDBObject doc = new BasicDBObject("username", userName);				
			DBCursor cursor = coll.find(doc);
			while(cursor.hasNext()){
				DBObject obj = cursor.next();
				name=obj.get("name").toString();
				id=obj.get("userid").toString();
				fbName=obj.get("facebookname").toString();
				twName=obj.get("twittername").toString();
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				users.add( new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location) );
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		return users;
	}

	private static String generateUserId(String fbName, String twitterName){
		if(fbName==null || fbName.isEmpty())
			fbName="facebooknull";
		if(twitterName==null || twitterName.isEmpty())
			twitterName="twitternull";
		return UUID.randomUUID().toString().substring(3, 8)+fbName+twitterName;
	}

	public static String getFacebookToken(String userId){
		String token;
		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");			
			BasicDBObject doc = new BasicDBObject("facebookid", userId);			
			if(coll.find(doc).count()>0){
				DBObject obj = coll.find(doc).next();
				token=obj.get("facebooktoken").toString();	
				return token;
			}
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		//if user not found then return null
		return null;
	}
	
}
