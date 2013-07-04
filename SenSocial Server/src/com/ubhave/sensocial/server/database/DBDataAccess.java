package com.ubhave.sensocial.server.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.data.SocialEvent;


public class DBDataAccess {

	public void saveStream(String deviceName){
		
		//save stream to DB
	}
	
	public static SocialEvent getAllSavedStreams(){
		try {
			MongoClient mongoClient = new MongoClient();
			DB db = mongoClient.getDB( "SenSocial" );
			DBCollection coll = db.getCollection("User");
			DBCursor cursor=coll.find();
			while(cursor.hasNext()){
				System.out.println("Element: "+cursor.next());
			}
		} catch (UnknownHostException e) {
			System.out.println("SNnMB error: "+e.toString());
		}
		return null;
	}
}
