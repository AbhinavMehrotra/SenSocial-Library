package com.ubhave.sensocial.server.database;

import java.net.UnknownHostException;
import java.util.HashSet;
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

public class DeviceRegistrar {

//	private final static String TAG="SNnMB";
//
//	public static void registerDevice(String deviceName, String uuid, String fbName, String fbToken, String twitterName){
//
//		try {
//			MongoClient mongoClient = new MongoClient();
//			DB db = mongoClient.getDB( "SNnMB" );
//			DBCollection coll = db.getCollection("Device");
//			
//			
//			//Registering the old client who wish to register again after unregistering itself
//			BasicDBObject doc = new BasicDBObject("uuid", uuid);
//			if(coll.find(doc).count()>0){
//				DBObject obj = coll.find(doc).next();
//				obj.removeField("registered");
//				obj.put("registered", true);
//				coll.save(obj);
//			}
//			else{
//				doc = new BasicDBObject("name", deviceName).
//						append("uuid", uuid).
//						append("fbname", fbName).
//						append("fbtoken",fbToken).
//						append("twittername",twitterName).
//						append("registered", true);
//
//				coll.insert(doc);				
//			}
//
//		} catch (UnknownHostException e) {
//			System.out.println(TAG+" error: "+e.toString());
//		}
//	}
//
//	public static void unregisterDevice(String uuid){
//
//		try {
//			MongoClient mongoClient = new MongoClient();
//			DB db = mongoClient.getDB( "SNnMB" );			
//			DBCollection coll = db.getCollection("Device");			
//			BasicDBObject doc = new BasicDBObject("uuid", uuid);
//
//			DBObject obj = coll.find(doc).next();
//			obj.removeField("registered");
//			obj.put("registered", false);
//			coll.save(obj);
//
//		} catch (UnknownHostException e) {
//			System.out.println(TAG+" error: "+e.toString());
//		}
//	}
//
//
//	public static Set<String> getAllRegisteredDevices(){
//		Set<String> devices= new HashSet<String>();
//		try {
//			MongoClient mongoClient = new MongoClient();
//			DB db = mongoClient.getDB( "SNnMB" );			
//			DBCollection coll = db.getCollection("Device");			
//			DBCursor cursor= coll.find();
//			DBObject obj;
//			while(cursor.hasNext()){
//				obj =cursor.next();
//				devices.add(obj.get("name").toString());
//			}
//
//		} catch (UnknownHostException e) {
//			System.out.println(TAG+" error: "+e.toString());
//		}
//		return devices;
//	}
//	
//	public static Device getDevice(String deviceName){
//		String uuid;
//		String fbName;
//		String twitterName;
//		Set<String> fbFriends=new HashSet<String>();
//		Location location;
//		
//		try {
//			MongoClient mongoClient = new MongoClient();
//			DB db = mongoClient.getDB( "SNnMB" );
//			DBCollection coll = db.getCollection("Device");			
//			BasicDBObject doc = new BasicDBObject("name", deviceName);
//			if(coll.find(doc).count()>0){
//				DBObject obj = coll.find(doc).next();
//				uuid=obj.get("name").toString();
//				fbName=obj.get("fbname").toString();
//				twitterName=obj.get("twittername").toString();
//				fbFriends=(Set<String>) obj.get("fbfriends");
//				location=(Location) new Location((double)obj.get("latitude"), (double)obj.get("longitude"));				
//				
//				return new Device(deviceName, uuid, fbName, twitterName, fbFriends, location);
//			}
//
//		} catch (UnknownHostException e) {
//			System.out.println(TAG+" error: "+e.toString());
//		}
//		return null;		
//	}



}
