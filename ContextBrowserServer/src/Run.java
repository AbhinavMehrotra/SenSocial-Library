import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.SSListener;
import com.ubhave.sensocial.server.manager.SSManager;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.User;
import com.ubhave.sensocial.server.manager.UserRegistrationListener;
import com.ubhave.sensocial.server.manager.UserRegistrationListenerManager;

public class Run {

	private static SSManager sm;
	private static UserRegistrationListener listener;
	private static Stream s1,s2;
	private static SSListener ssListener;
	private static Map<String, Set<String>> user_stream_map=new HashMap<String, Set<String>>();
	private static Map<String, String> sensor_name_stream_map=new HashMap<String, String>();
	private static Set<Stream> streamSet;
	private static Set<String> streamSetIds;

	public static void main(String[] args) {
		sm=SSManager.getSSManager();
		setSSListener();
		setUserRegistrationListener();
	}

	private static void setUserRegistrationListener(){
		listener=new UserRegistrationListener() {			
			@Override
			public void onNewUserRegistered(User user) {
				streamSetIds=new HashSet<String>();
				System.out.println("New user registered: "+ user.getId());
				for(Device d: user.getDevices()){
					System.out.println("Device: " + d.getDeviceId());
					streamSet =new HashSet<Stream>();
					streamSet = createStream(d);
					System.out.println("Stream created!!");
					startStream(streamSet);
					System.out.println("Stream started!!");
					
					for(Stream stream : streamSet){
						System.out.println("Stream id: "+ stream.getStreamId());
						streamSetIds.add(stream.getStreamId());
						sensor_name_stream_map.put(stream.getStreamId(), Sensors.getSensorNameById(stream.getSensorId()));
					}
					user_stream_map.put(user.getId(), streamSetIds);
				}
			}
		};
		UserRegistrationListenerManager.addListener(listener);
	}


	private static Set<Stream> createStream(Device d){
		System.out.println("Create stream on device: "+d.getDeviceId());
		Set<Stream> streams =new HashSet<Stream>();
		try {
			s1=d.getStream(Sensors.SENSOR_TYPE_ACCELEROMETER, "classified");
			s2=d.getStream(Sensors.SENSOR_TYPE_MICROPHONE, "classified");
			streams.add(s1);
			streams.add(s2);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return streams;
	}

	private static void startStream(Set<Stream> streamSet){	
		try {	
			for(Stream s: streamSet){
				sm.registerListener(ssListener, s.getStreamId());
				s.startStream();				
			}
		} catch (PPDException e) {
			e.printStackTrace();
		} catch (XMLFileException e) {
			e.printStackTrace();
		}
	}

	private static void setSSListener(){
		ssListener = new SSListener(){
			@Override
			public void onDataReceived(SocialEvent event) {
				String sensor_name = getSensorNameByStreamId(event.getFilteredSensorData().getStreamId());
				String data = event.getFilteredSensorData().getClassifiedData();
				insertDataIntoDB(getUserId(event.getFilteredSensorData().getStreamId()), sensor_name, data);
			}

		};
	}

	private static void insertDataIntoDB(String userId, String sensor_name, String data){
		try {
			MongoClient mongoClient = new MongoClient("147.188.193.205");
			DB db = mongoClient.getDB( "SenSocialDB" );
			db.authenticate("abhinav", "sensocial".toCharArray());
			DBCollection coll = db.getCollection("ContextBrowser");
			BasicDBObject doc=new BasicDBObject("userid", userId);

			if(coll.find(doc).hasNext()){
				//update the current activity of the user
				System.out.println("Update the current activity of the user");
				DBObject obj = coll.find(doc).next();
				obj.removeField(sensor_name);
				obj.put(sensor_name, data);
				coll.findAndModify(doc, obj);
			}
			else{
				//new user
				System.out.println("New User");
				doc.append(sensor_name, data).
				append(sensor_name.equalsIgnoreCase("accelerometer")? "microphone" : "accelerometer", "unknown");
				coll.save(doc);
			}
		} catch (UnknownHostException e) {
			System.out.println("Error with data injection: "+e.toString());
		}		
	}

	/*	
	   //We are using map to store in ram instead of db
		private static void insertUserStreamIntoDB(String userId, Set<String> streams){
			try {
				MongoClient mongoClient = new MongoClient("147.188.193.205");
				DB db = mongoClient.getDB( "SenSocialDB" );
				db.authenticate("abhinav", "sensocial".toCharArray());
				DBCollection coll = db.getCollection("UserStreamRegister");
				BasicDBObject doc=new BasicDBObject("userid", userId);
				int i=1;
				for(String s: streams)
					doc.append("stream"+ i++, s);
				coll.save(doc);
			} catch (UnknownHostException e) {
				System.out.println("Error with data injection: "+e.toString());
			}
		}
	*/

	private static String getUserId(String streamId){
		System.out.println("Inside getUserId, looking for : "+ streamId+" in map: "+ user_stream_map);
		for(Map.Entry<String, Set<String>> e : user_stream_map.entrySet()){
			for(String s: e.getValue()){
				if(s.equals(streamId)){
					return e.getKey();
				}
			}
		}
		return null;
	}

	private static String getSensorNameByStreamId(String streamId){
		for(Map.Entry<String, String> e : sensor_name_stream_map.entrySet()){
			if(e.getKey().equals(streamId))
				return e.getValue();
		}
		return null;
	}

}



