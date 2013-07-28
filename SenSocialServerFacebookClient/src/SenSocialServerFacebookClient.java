import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.filters.Condition;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.ModalValue;
import com.ubhave.sensocial.server.filters.ModalityType;
import com.ubhave.sensocial.server.filters.Operator;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.SSListener;
import com.ubhave.sensocial.server.manager.SSManager;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.User;


public class SenSocialServerFacebookClient {

	private static SSListener listener;
	private static SSManager sm;
	private static Stream s1,s2,s3,ns1,ns2,ns3;
	private static Map<String, SocialEvent> map=new HashMap<String, SocialEvent>();
	private static int streamSize;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sm=SSManager.getSSManager();
		Set<User> users=new HashSet<User>();
		users=sm.getAllUsers();
		if(users==null){
			System.out.println("NULL");
		}
		else{
			for(User u:users){
				System.out.println("User:"+u.getId());
				ArrayList<Device> devices=u.getDevices();
				for(Device d:devices){
					createStream(d);
					setListener();
					startStream();
				}
			}
		}
	}


	private static void createStream(Device d){
		System.out.println("Device:"+d.getDeviceId());
		try {
			streamSize=3;
			s1=d.getStream(Sensors.SENSOR_TYPE_ACCELEROMETER, "classified");
			s2=d.getStream(Sensors.SENSOR_TYPE_MICROPHONE, "classified");
			s3=d.getStream(Sensors.SENSOR_TYPE_LOCATION, "classified");
			Filter filter=new Filter();
			ArrayList<Condition> conditions=new ArrayList<Condition>();
			conditions.add(new Condition(ModalityType.facebook_activity, Operator.equal_to, ModalValue.active));					
			filter.addConditions(conditions);

			ns1=s1.setFilter(filter);
			ns2=s2.setFilter(filter);
			ns3=s3.setFilter(filter);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void setListener(){
		listener = new SSListener() {

			public void onDataReceived(SocialEvent socialEvent) {
				String message, contextInfo, streamId;
				try{
					message=socialEvent.getSocialData().getOSNFeed();
				}
				catch(Exception e){
					message="unknown";
				}
				contextInfo=socialEvent.getFilteredSensorData().getClassifiedData();
				streamId=socialEvent.getFilteredSensorData().getStreamId();
				System.out.println("Social Event Received! \nStream-id: "+streamId
						+"\nSensor data: "+contextInfo+"\nOSN Message: "+message);
				
				map.put(streamId, socialEvent);
				if(map.size()==streamSize){
					String acc="Disabled", mic="Disabled", location="Disabled";
					try{
						acc = map.get(ns1.getStreamId()).getFilteredSensorData().getClassifiedData();
					}
					catch(Exception e){
						System.out.println(e.toString());
					}
					try{
						mic=map.get(ns2.getStreamId()).getFilteredSensorData().getClassifiedData();
					}
					catch(Exception e){
						System.out.println(e.toString());
					}

					try{
						location=map.get(ns3.getStreamId()).getFilteredSensorData().getClassifiedData();;
					}
					catch(Exception e){
						System.out.println(e.toString());
					}
					//save in db
					String osn_message="unknown", time="unknown", userName="unknown";
					try{
						osn_message=socialEvent.getSocialData().getOSNFeed();
						time=socialEvent.getSocialData().getTime();
						userName=socialEvent.getSocialData().getUserName();
					}
					catch(NullPointerException e1){
						System.out.println("OSN data is unavailable!");
					}
					
					String context= acc +" & "+mic;
					System.out.println("Location data: "+ location);
					double latitude= Double.parseDouble(location.substring(10, location.indexOf(',')));
					location=location.substring(13+location.indexOf(','));
					double longitude=Double.parseDouble(location);
					insertMapData(userName, osn_message, context, time, latitude, longitude);
					insertD3Data(userName, osn_message, context, time, latitude, longitude);
					//clear all entries
					map.clear();

				}	
				
			}
		};
	}

	private static void startStream(){	
		try {	
			sm.registerListener(listener, ns1.getStreamId());
			sm.registerListener(listener, ns2.getStreamId());
			sm.registerListener(listener, ns3.getStreamId());
			ns1.startStream();
			ns2.startStream();
			ns3.startStream();
		} catch (PPDException e) {
			e.printStackTrace();
		} catch (XMLFileException e) {
			e.printStackTrace();
		}

	}
	
	private static void insertMapData(String userName, String message, String context, String time, double latitude, double longitude){
		try {
			File file=new File("MapData.json");
			Boolean flag=file.createNewFile();
			if(flag){
				//new file created, so add the root 
				try {
					JSONObject obj=  new JSONObject();
					obj.put("name", "SenSocial");
					obj.put("value", 100);
					JSONArray arr=new JSONArray();
					obj.put("children", arr);
					FileWriter f= new FileWriter("MapData.json");
					f.write("var data = "+obj.toString());
					f.flush();
					f.close();
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
			//read the file
			BufferedReader br=null;
			String str="";
			try {	 
				String sCurrentLine;
				br = new BufferedReader(new FileReader("MapData.json"));
				while ((sCurrentLine = br.readLine()) != null) {
					str+=sCurrentLine;
				}	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			//append child to json array
			try {		 
				JSONObject json = new JSONObject(str.substring("var data = ".length()));
				JSONArray children=json.getJSONArray("children");
				JSONObject child=new JSONObject();
				child.put("username", userName);
				child.put("message", message);
				child.put("value", 25);
				child.put("time", time);
				child.put("modality", context);
				child.put("latitude", latitude);
				child.put("longitude", longitude);
				children.put(child);
				
				FileWriter f= new FileWriter("MapData.json");
				f.write("var data = "+ json.toString());
				f.flush();
				f.close();
				
		 	} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void insertD3Data(String userName, String message, String context, String time, double latitude, double longitude){
		try {
			File file=new File("D3Data.json");
			Boolean flag=file.createNewFile();
			if(flag){
				//new file created, so add the root 
				try {
					JSONObject obj=  new JSONObject();
					obj.put("name", "SenSocial");
					obj.put("value", 100);
					JSONArray arr=new JSONArray();
					obj.put("children", arr);
					FileWriter f= new FileWriter("D3Data.json");
					f.write(obj.toString());
					f.flush();
					f.close();
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
			//read the file
			BufferedReader br=null;
			String str="";
			try {	 
				String sCurrentLine;
				br = new BufferedReader(new FileReader("D3Data.json"));
				while ((sCurrentLine = br.readLine()) != null) {
					str+=sCurrentLine;
				}	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			//append child to json array
			try {		 
				JSONObject json = new JSONObject(str);
				JSONArray children=json.getJSONArray("children");
				JSONObject child=new JSONObject();
				child.put("username", userName);
				child.put("message", message);
				child.put("value", 25);
				child.put("modality", context);
				child.put("time", time);
				child.put("latitude", latitude);
				child.put("longitude", longitude);
				children.put(child);
				
				FileWriter f= new FileWriter("D3Data.json");
				f.write(json.toString());
				f.flush();
				f.close();
				
		 	} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
