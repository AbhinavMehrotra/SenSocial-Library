package com.ubhave.sensocial.server.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.manager.Aggregator;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.StreamCollection;
import com.ubhave.sensocial.server.manager.StreamRegistrar;
import com.ubhave.sensocial.server.manager.User;
import com.ubhave.sensocial.server.manager.UserRelation;
import com.ubhave.sensocial.server.mqtt.MQTTClientNotifier;
import com.ubhave.sensocial.server.mqtt.MQTTNotifitions;

public class GenerateFilter {

	public static void createXML(User user, String deviceId, ArrayList<String> modalities, String streamId, String sensorName, String sensorDataType){
		//check for filter (server or client)
		//filter types: device specific, relational, and combinational(two streams
		//requires similar data with different frequency

		if(isDeviceSpecific(deviceId, modalities)){
			if(isCombinational(deviceId, sensorName, modalities, streamId)!=null){
				String s=isCombinational(deviceId, sensorName, modalities, streamId);
				if(!s.equalsIgnoreCase(streamId)){
					//set it on server
					createServerFilter(modalities, streamId, sensorName, sensorDataType);
				}
				else{
					// delete existing client filter and set new.
					File file = new File("ClientFilters\\Filter"+streamId+".xml"); 
					if (file.exists()){
						mergeFilters("ClientFilters\\Filter"+streamId+".xml", "serverfilter.xml");
						file.delete();
						//map the stream id of deleted filter so that when it comes it should be fired to right listener


						createClientFilter(modalities, streamId, sensorName, sensorDataType);
					}
					else{
						//delete from client and create it for server.
						//create a new for client
						MQTTClientNotifier.sendStreamNotification(deviceId, MQTTNotifitions.stop_stream, s);
						Set<Stream> stream=new HashSet<Stream>();
						stream=StreamRegistrar.getStream(deviceId);
						for(Stream st : stream){
							if(st.getStreamId().equalsIgnoreCase(s)){
								ArrayList<String> m=new ArrayList<String>();
								for(Modality mod:st.getFilter().getConditions()){
									m.add(mod.getActivityName());
								}
								createServerFilter(m, st.getStreamId(), ""+st.getSensorId(), st.getDataType());
								createClientFilter(modalities, streamId, sensorName, sensorDataType);
								MQTTClientNotifier.sendStreamNotification(deviceId, MQTTNotifitions.start_stream, streamId);
							}
						}
					}
				}
			}
			else{
				createClientFilter(modalities, streamId, sensorName, sensorDataType);				
			}
		}
		else if(isRelational(modalities)){
			ArrayList<String> modalitiesNew= new ArrayList<String>();
			modalitiesNew.add("friends_within_1_mile"+deviceId);
			createServerFilter(modalitiesNew, streamId, sensorName, sensorDataType);			
			
			Filter f=new Filter();
			ArrayList<Modality> modality= new ArrayList<Modality>();
			for(String m:modalities)
				modality.add(Modality.every_ten_minutes.getModaalityByName(m));
			f.addConditions(modality);
			Set<Stream> streams=new HashSet<Stream>();
			StreamCollection sc=new StreamCollection(Sensors.SENSOR_TYPE_LOCATION, "raw");
			try {
				streams=sc.getStreamSet(user, UserRelation.facebook_friends);
				Aggregator ag=new Aggregator(streams);
				Stream stream=ag.createStream();
				stream.setFilter(f);
				stream.startStream();
				ServerFilterRegistrar.add(streamId, stream.getStreamId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Boolean isDeviceSpecific(String deviceId, ArrayList<String> modalities){
		//does not require data from other device
		for(String m:modalities){
			if(m.startsWith("friends_within_1_mile"))
				return false;
		}		
		return true;
	}

	private static Boolean isRelational(ArrayList<String> modalities){
		//require data from other device
		for(String m:modalities){
			if(m.startsWith("friends_within_1_mile")){
				return true;
			}
		}		
		return false;
	}

	/**
	 * Returns the streamId of the similar stream and high frequency.
	 * It returns back the same streams(passed by arg) if a low frequency stream is present on client.
	 * If such stream is not found then it returns null.
	 * @param deviceId
	 * @param sensorName
	 * @param modalities
	 * @return
	 */
	private static String isCombinational(String deviceId, String sensorName, ArrayList<String> modalities, String streamid){
		String streamId=null, existingTime ="", newTime="";
		Boolean flag;
		Set<Stream> streams = new HashSet<Stream>();
		streams=StreamRegistrar.getStream(deviceId);
		for(Stream s:streams){
			// check: stream for same sensor is already present
			if(Sensors.getSensorNameById(s.getSensorId()).equalsIgnoreCase(sensorName)){
				// check: stream has any filer
				if(s.getFilter()==null){
					//no filer for existing stream, return the existing stream id
					streamId=s.getStreamId();
				}
				else{
					//filter present, now check for frequency
					flag=true;
					ArrayList<Modality> existingMod=new ArrayList<Modality>();
					existingMod=s.getFilter().getConditions();
					ArrayList<String> existingModalities=new ArrayList<String>();
					for(Modality m:existingMod)  //modality to string
						existingModalities.add(m.getActivityName()); 

					if(existingModalities.size()==modalities.size()){
						for(String m:modalities){
							if(!existingModalities.contains(m)){
								flag=false;
								break;
							}
						}
						if(flag){
							for(String m:modalities)
								if(m.equalsIgnoreCase("every_ten_mins") || m.equalsIgnoreCase("every_thirty_mins")){
									newTime=m;
									break;
								}
							for(String m:existingModalities)
								if(m.equalsIgnoreCase("every_ten_mins") || m.equalsIgnoreCase("every_thirty_mins")){
									existingTime=m;
									break;
								}

						}
					}

					if(flag && (existingTime.equalsIgnoreCase(newTime) || (existingTime.equalsIgnoreCase("every_ten_mins"))))
						streamId=s.getStreamId();  //already a high freq stream coming from client
					else if(flag)
						streamId=streamid;  //already a low freq stream coming from client
				}
			}
		}
		return streamId;
	}


	private static void createClientFilter(ArrayList<String> modalities, String streamId, String sensorName, String sensorDataType){
		try
		{
			//			System.out.println("Working Directory = " +
			//		              System.getProperty("user.dir"));

			Element rootElement,mainRoot;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File("filter.xml"); 
			if (file.exists() && file.length()!=0) 
			{ 
				System.out.println("file found");
				doc = docBuilder.parse(file); 
				mainRoot = doc.getDocumentElement(); 
			} 
			else 
			{ 
				mainRoot = doc.createElement("Filter");
				doc.appendChild(mainRoot); 
			} 

			rootElement = doc.createElement("Configuration");
			rootElement.setAttribute("name", streamId);
			rootElement.setAttribute("sense", "true");
			mainRoot.appendChild(rootElement);

			Element condition = doc.createElement("Condition");
			condition.setAttribute("name", "c1");
			rootElement.appendChild(condition);
			int i=2, j=1;
			Element activity;
			for(String s:modalities){
				if(!s.equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", s);
					condition.appendChild(activity);
				}
				else{
					condition = doc.createElement("Condition");
					condition.setAttribute("name", "c"+i++);
					rootElement.appendChild(condition);
					j=1;
				}		
			}

			Element reqData = doc.createElement("required_data");
			reqData.setAttribute("sensor", sensorName);
			reqData.setAttribute("location", "server");
			reqData.setAttribute("type", sensorDataType);
			rootElement.appendChild(reqData);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result =  new StreamResult(new File("ClientFilters\\Filter"+streamId+".xml"));
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void createServerFilter(ArrayList<String> modalities, String streamId, String sensorName, String sensorDataType){
		try
		{
			Element rootElement,mainRoot;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File("serverfilter.xml"); 
			if (file.exists() && file.length()!=0) 
			{ 
				System.out.println("file found");
				doc = docBuilder.parse(file); 
				mainRoot = doc.getDocumentElement(); 
			} 
			else 
			{ 
				mainRoot = doc.createElement("Filter");
				doc.appendChild(mainRoot); 
			} 

			rootElement = doc.createElement("Configuration");
			rootElement.setAttribute("name", streamId);
			rootElement.setAttribute("sense", "true");
			mainRoot.appendChild(rootElement);

			Element condition = doc.createElement("Condition");
			condition.setAttribute("name", "c1");
			rootElement.appendChild(condition);
			int i=2, j=1;
			Element activity;
			for(String s:modalities){

				if(!s.equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", s);
					condition.appendChild(activity);
				}
				else{
					condition = doc.createElement("Condition");
					condition.setAttribute("name", "c"+i++);
					rootElement.appendChild(condition);
					j=1;
				}				

			}

			Element reqData = doc.createElement("required_data");
			reqData.setAttribute("sensor", sensorName);
			reqData.setAttribute("location", "server");
			reqData.setAttribute("type", sensorDataType);
			rootElement.appendChild(reqData);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result =  new StreamResult(new File("serverfilter.xml"));
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void mergeFilters(String source, String destination){
		try
		{
			Element mainRootS, mainRootD;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Document docSource = docBuilder.newDocument();

			File fileDesination = new File(source); 
			doc = docBuilder.parse(fileDesination); 
			mainRootD = doc.getDocumentElement(); 


			File fileSource = new File(source); 
			docSource = docBuilder.parse(fileSource); 
			mainRootS = docSource.getDocumentElement(); 

			mainRootD.appendChild(mainRootS.getChildNodes().item(0));


			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource sourceDOM = new DOMSource(doc);

			StreamResult result =  new StreamResult(new File(destination));
			transformer.transform(sourceDOM, result);

			System.out.println("Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
