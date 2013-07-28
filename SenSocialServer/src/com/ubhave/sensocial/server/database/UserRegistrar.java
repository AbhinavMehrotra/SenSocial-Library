package com.ubhave.sensocial.server.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.User;
import com.ubhave.sensocial.server.manager.UserRegistrationListenerManager;

public class UserRegistrar {

	private static String TAG="SNnMB";

	/**
	 * Registers user with his/her device settings.
	 * This methods is called whenever a new user registers with application.
	 * @param userId (String) user-id
	 * @param deviceId (String) device-id
	 * @param bluetoothMAC (String) Bluetooth MAC address
	 * @param ppd (String) Privacy Policy Descriptor
	 */
	public static void registerUser(String userId, String deviceId, String bluetoothMAC, String ppd){
		//String userId=UUID.randomUUID().toString();
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			ArrayList<String> deviceid=new ArrayList<String>();
			deviceid.add(deviceId);
			Map<String,String> bluetooth=new HashMap<String,String>();
			bluetooth.put(deviceId,bluetoothMAC);
			BasicDBObject doc=new BasicDBObject();
			doc.append("userid", userId).
			append("deviceids", deviceid).
			append("bluetoothmac", bluetooth).
			append("ppd", ppd);
			if(!(coll.find(doc).count()>0))
				coll.save(doc);
			User user = getUserById(userId);
			UserRegistrationListenerManager.fireUpdates(user);
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}		
	}

	
	/**
	 * Registers users' Facebook settings
	 * @param userId (String) user-id
	 * @param deviceId (String) device-id
	 * @param userName (String) user-name
	 * @param facebookId (String) Facebook-id of user
	 * @param facebookToken (String) Facebook token of user for the application
	 */
	public static void registerFacebook(String userId, String deviceId, String userName, String facebookId, String facebookToken){
		try {
			System.out.println("Trying to register Facebook user!");
			String str1=facebookToken.substring(13);
			String token= str1.substring(0, str1.indexOf("&expires"));
			String expires= str1.substring(str1.indexOf("&expires")+ "&expires=".length());	
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			BasicDBObject doc = new BasicDBObject("userid", userId);
			
			if(coll.find(doc).hasNext()){
				DBObject obj = coll.find(doc).next();
				ArrayList<String> fbFriends= new ArrayList<String>();
				//TODO: create logic to update friends
				fbFriends = getFacebookFriends(facebookId, token);
				System.out.println("Facebook user friendlist:"+fbFriends);
				obj.put("username", userName);
				obj.put("facebookid", facebookId);
				obj.put("facebooktoken", token);
				obj.put("facebookexpires", expires);
				obj.put("facebookfriends", fbFriends);
				coll.save(obj);	
				System.out.println("Facebook user registered!");
			}
			else{
				System.out.println("Error: Facebook user not registered! Trying again!!");	
				long time= System.currentTimeMillis();
				long exitTime= time+2000;
				while(time<exitTime){
					time= System.currentTimeMillis();
				}
				registerFacebook(userId, deviceId, userName, facebookId, facebookToken);
			}
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	
	/**
	 * Registers users' Twitter settings
	 * @param userId (String) user-id
	 * @param deviceId (String) device-id
	 * @param userName (String) user-name
	 * @param twitterId (String) Twitter-id of user
	 * @param twitterToken (String) Twitter token of user for the application
	 */
	public static void registerTwitter(String userId, String deviceId, String userName, String twitterId, String twitterToken){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			BasicDBObject doc = new BasicDBObject("userid", userId);
			DBObject obj = coll.find(doc).next();
			ArrayList<String> twitterFollowers= new ArrayList<String>();
			//TODO: create logic to update friends
			twitterFollowers = getTwitterFollowers(twitterId);
			obj.put("username", userName);
			obj.put("facebookid", twitterId);
			obj.put("facebooktoken", twitterToken);
			obj.put("facebookfriends", twitterFollowers);
			coll.save(obj);		
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	
	/**
	 * Updates location of the user. This method is called only for applications which uses 
	 * location tracking service by SenSocial.
	 * @param userId (String) user-id
	 * @param deviceId (String) device-id
	 * @param lat (String) latitude
	 * @param lon (String) longitude
	 */
	public static void updateLocation(String userId, String deviceId, String lat, String lon){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			DBCursor cursor =coll.find();

			ArrayList<Double> loc=new ArrayList<Double>();
			loc.add(Double.parseDouble(lat));
			loc.add(Double.parseDouble(lon));
			BasicDBObject doc = new BasicDBObject("userid", userId);
			DBObject obj = coll.find(doc).next();
			Map<String,ArrayList<Double>> location=new HashMap<String,ArrayList<Double>>();	
			location = (Map<String, ArrayList<Double>>) obj.get("location");
			location.remove(deviceId);
			location.put(deviceId,loc);
			obj.put("location", location);
			coll.save(obj);
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	
	/**
	 * Returns the latest location of the user
	 * @param userId (String) user-id of the user for whom location is required
	 * @return Location object
	 */
	public static Location getLocation(String userId){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			DBCursor cursor =coll.find();
			BasicDBObject doc = new BasicDBObject("userid", userId);
			DBObject obj = coll.find(doc).next();
			Map<String,ArrayList<Double>> location=new HashMap<String,ArrayList<Double>>();	
			ArrayList<Double> l=new ArrayList<Double>();
			for(Map.Entry<String, ArrayList<Double>> e: location.entrySet()){
				l=e.getValue();
			}
			return new Location(l.get(0), l.get(1));
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		return new Location(0, 0);
	}

	/**
	 * Registers user with his/her complete settings, including OSN settings
	 * @param userName
	 * @param deviceId
	 * @param bluetoothMAC
	 * @param fbName
	 * @param fbId
	 * @param fbToken
	 * @param twitterName
	 * @param latitude
	 * @param longitude
	 */
	public static void registerUser(String userName, String deviceId, String bluetoothMAC, String fbName, String fbId, String fbToken, String twitterName, double latitude, double longitude){

		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		//query facebook  friends- same as client
		//query twitter   followers- same as client
		//how to update these?- thread but it might be killed if server restarts
		//TODO: create logic to update friends
		fbFriends = getFacebookFriends(fbId, fbToken);
		twFollowers = getTwitterFollowers(twitterName);

		String userId=generateUserId(fbName, twitterName);
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
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

	/**
	 * Unregisters the user associated with the given device-id
	 * @param deviceId (String) Device-id
	 */
	public static void unregisterDevice(String deviceId){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());			
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

	/**
	 * Returns User class object associated with any OSN name or id
	 * @param osn_name_or_id (String) OSN name or id
	 * @return User Object
	 */
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
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
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
				DBObject obj = coll.find(doc2).next();
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
				DBObject obj = coll.find(doc3).next();
				if(obj==null){
					System.out.println("DBObject is null!!");
					return null;		
				}
				try{ name=obj.get("name").toString(); } catch(Exception e){name="unknown";}
				try{ id=obj.get("userid").toString(); } catch(Exception e){id="unknown";}
				try{ fbName=obj.get("facebookname").toString(); } catch(Exception e){fbName="unknown";}
				try{ twName=obj.get("twittername").toString(); } catch(Exception e){twName="unknown";}
				try{ fbFriends=(ArrayList<String>) obj.get("facebookfriends"); } catch(Exception e){fbFriends= new ArrayList<String>();}
				try{ twFollowers=(ArrayList<String>) obj.get("twitterfollowers"); } catch(Exception e){twFollowers= new ArrayList<String>();}
				try{ deviceIds=(ArrayList<String>) obj.get("deviceids"); } catch(Exception e){deviceIds= new ArrayList<String>();}
				try{ location=(HashMap<String, ArrayList<Double>>) obj.get("location");		 } catch(Exception e){HashMap<String, ArrayList<Double>> l= new HashMap<String, ArrayList<Double>>(); location=l;}
				return new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);				
			}
			else if(coll.find(doc4).count()>0){
				DBObject obj = coll.find(doc4).next();
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

	/**
	 * Returns User class object associated with given device id
	 * @param deviceId (String) Device-id
	 * @return User Object
	 */
	public static User getUserByDeviceId(String deviceId){
		String name;
		String fbName;
		String twName;
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");				
			DBCursor cursor=coll.find();
			while( cursor.hasNext()) {
				DBObject obj = cursor.next();
				if(((ArrayList<String>) obj.get("deviceids")).contains(deviceId)){
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
			}
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		//if user not found then return null
		return null;
	}

	/**
	 * Returns the Device class object of the given Bluetooth MAC address
	 * @param bluetoothMAC (String) Bluetooth MAC
	 * @return Device Object
	 */
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
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
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

	/**
	 * Returns the Bluetooth MAC of the device
	 * @param deviceId (String) Device id
	 * @return (String) Bluetooth MAC
	 */
	public static String getBluetooth(String deviceId){
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
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

	/**
	 * Returns the Set of all User class object 
	 * @return Set<User> Object
	 */
	public static Set<User> getAllUsers(){
		Set<User> users= new HashSet<User>();
		String name="";
		String fbName="";
		String twName="";
		String id;
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");			
			DBCursor cursor= coll.find();
			while(cursor.hasNext()){
				System.out.println("user found");
				DBObject obj = cursor.next();
				if(obj.get("username")!=null)
					name=obj.get("username").toString();
				id=obj.get("userid").toString();
				if(obj.get("facebookname")!=null)
					fbName=obj.get("facebookname").toString();
				if(obj.get("twittername")!=null)
					twName=obj.get("twittername").toString();
				if(obj.get("facebookfriends")!=null)
					fbFriends=(ArrayList<String>) obj.get("facebookfriends");
				if(obj.get("twitterfollowers")!=null)
					twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
				deviceIds=(ArrayList<String>) obj.get("deviceids");
				if(obj.get("location")!=null)
					location=(HashMap<String, ArrayList<Double>>) obj.get("location");		
				users.add( new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location) );
			}

		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}

		return users;
	}

	/**
	 * Returns the Set of User class object with the given user-name
	 * @param userName (String) User-name
	 * @return Set<User> Object
	 */
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
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");			
			BasicDBObject doc = new BasicDBObject("username", userName);				
			DBCursor cursor = coll.find(doc);
			while(cursor.hasNext()){
				DBObject obj = cursor.next();
				name=obj.get("username").toString();
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

	/**
	 * Returns the User class object of the given user-id
	 * @param userId (String) User-id
	 * @return User Object
	 */
	public static User getUserById(String userId){
		User user=null;

		String name="";
		String fbName="";
		String twName="";
		String id="";
		ArrayList<String> deviceIds= new ArrayList<String>();
		ArrayList<String> fbFriends= new ArrayList<String>();
		ArrayList<String> twFollowers= new ArrayList<String>();
		HashMap<String, ArrayList<Double>> location = new HashMap<String, ArrayList<Double>>();

		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");			
			BasicDBObject doc = new BasicDBObject("userid", userId);
			if(!coll.find(doc).hasNext()){
				return null;
			}
			DBObject obj = coll.find(doc).next();
			if(obj.get("username")!=null)
				name=obj.get("username").toString();
			if(obj.get("userid")!=null)
				id=obj.get("userid").toString();
			if(obj.get("facebookname")!=null)
				fbName=obj.get("facebookname").toString();
			if(obj.get("twittername")!=null)
				twName=obj.get("twittername").toString();
			if(obj.get("facebookfriends")!=null)
				fbFriends=(ArrayList<String>) obj.get("facebookfriends");
			if(obj.get("twitterfollowers")!=null)
				twFollowers=(ArrayList<String>) obj.get("twitterfollowers");
			deviceIds=(ArrayList<String>) obj.get("deviceids");
			if(obj.get("location")!=null)
				location=(HashMap<String, ArrayList<Double>>) obj.get("location");
			
			user=new User(name, id, fbName, twName, deviceIds, fbFriends, twFollowers, location);


		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
		return user;
	}

	/**
	 * Generates unique user-id of the user
	 * @param fbName (String) Facebook name of the user
	 * @param twitterName (String) Twitter name of the user
	 * @return (String) Unique user-id
	 */
	private static String generateUserId(String fbName, String twitterName){
		if(fbName==null || fbName.isEmpty())
			fbName="facebooknull";
		if(twitterName==null || twitterName.isEmpty())
			twitterName="twitternull";
		return UUID.randomUUID().toString().substring(3, 8)+fbName+twitterName;
	}

	/**
	 * Returns the Facebook token of the user
	 * @param userId (String) Facebook id of the user
	 * @return (String) Facebook token of the user
	 */
	public static String getFacebookToken(String userId){
		String token;
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
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

	/**
	 * Gets the friendlist of the user from Facebook server
	 * @param id (String) Facebook id of the user
	 * @param token (String) Facebook token of the user
	 * @return ArrayList<String> Twitter followers of the user
	 */
	private static ArrayList<String> getFacebookFriends(String id, String token){
		System.out.println("Inside getFacebookFriends, id: "+id+", token: "+token);

		ArrayList<String> friends = new ArrayList<String>();
		try {
			String path="https://graph.facebook.com/"+id+"/friends?access_token="+token+"&fields=username";
			System.out.println("PATH: "+path);			
			URL url = new URL(path);

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("webcache.cs.bham.ac.uk", 3128));
			HttpsURLConnection conn = (HttpsURLConnection) new URL(path).openConnection(proxy);

			//HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.connect();
			InputStream Istream=conn.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
			JSONObject json= new JSONObject(r.readLine());
			r.close();
			conn.disconnect();

			JSONArray ar= new JSONArray();
			ar=json.getJSONArray("data");
			JSONObject obj;
			for(int i=0;i<ar.length();i++){
				obj=ar.getJSONObject(i);
				if(obj.has("id"))
					friends.add(obj.get("id").toString());
			}			
		} catch (Exception e) {
			System.out.println("Check your internet connectivity \n"+e.toString());
		}		
		System.out.println("Inside getFacebookFriends, friend-ids: "+friends);
		return friends;
	}

	/**
	 * Returns the list of all Twitter followers of a user.
	 * @param name (String) user-id or user-name on Twitter
	 * @return ArrayList<String> Twitter followers of the user
	 */
	private static ArrayList<String> getTwitterFollowers(String name){
		ArrayList<String> friends = new ArrayList<String>();
		//		try {
		//			String path="https://graph.facebook.com/me/friends?access_token="+this.token+"&fields=username";
		//			URL url = new URL(path);
		//			InputStream Istream=url.openConnection().getInputStream();
		//			BufferedReader r = new BufferedReader(new InputStreamReader(Istream));
		//			JSONObject json= new JSONObject(r.readLine());
		//			JSONArray ar= new JSONArray();
		//			ar=json.getJSONArray("data");
		//			JSONObject obj;
		//			for(int i=0;i<ar.length();i++){
		//				obj=ar.getJSONObject(i);
		//				if(obj.has("username"))
		//					friends.add(obj.get("username").toString());
		//			}			
		//		} catch (Exception e) {
		//			System.out.println(e.toString());
		//		}		
		return friends;
	}

	/**
	 * Updates the Users' Facebook friends list.
	 * This method is called when FBserver notifies for friend list change
	 * @param facebookId
	 */
	public static void updateFacebookFriendList(String facebookId){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			BasicDBObject doc = new BasicDBObject("facebookid", facebookId);
			DBObject obj = coll.find(doc).next();
			String token=obj.get("facebooktoken").toString();
			ArrayList<String> fbFriends= new ArrayList<String>();
			fbFriends = getFacebookFriends(facebookId, token);
			obj.removeField("facebookfriends"); 
			obj.put("facebookfriends", fbFriends);
			coll.save(obj);
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
		}
	}

	
	/**
	 * Returns whether the Facebook id is associated with any user.
	 * @param facebookId (String) Facebook-id which needs to be searched
	 * @return Boolean
	 */
	public static Boolean isFacebookIdPresent(String facebookId){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("User");
			BasicDBObject doc = new BasicDBObject("facebookid", facebookId);
			if(coll.find(doc).hasNext())
				return true;
			else
				return false;
		} catch (UnknownHostException e) {
			System.out.println(TAG+" error: "+e.toString());
			return false;
		}
	}
}
