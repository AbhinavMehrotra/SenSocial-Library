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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ubhave.sensocial.server.manager.Aggregator;
import com.ubhave.sensocial.server.manager.AggregatorRegistrar;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.StreamCollection;
import com.ubhave.sensocial.server.manager.StreamRegistrar;
import com.ubhave.sensocial.server.manager.User;
import com.ubhave.sensocial.server.manager.UserRelation;

public class GenerateFilter {

	public static void createXML(User user, String deviceId, ArrayList<Condition> conditions, String streamId, String sensorName, String sensorDataType){
		//check for filter (server or client)
		//filter types: device specific, relational, and combinational(two streams
		//on a device requires similar data with different frequency)
		if(isRelational(conditions)){ //example- get activity of all friends who are in the same location
			System.out.println("Relational FILTER");	
			for(Condition c:conditions){
				if(c.getModalityType().equals(ModalityType.facebook_friends_location)){
					conditions.remove(c);
					if(conditions.size()<1){
						conditions.add(new Condition("Null", "", ""));
					}
					createClientFilter(conditions, streamId, sensorName, sensorDataType);
					conditions.clear();
					conditions.add(c);
					createServerFilter(conditions, streamId, sensorName, sensorDataType);
					break;
				}
			}
			Filter f=new Filter();
			f.addConditions(conditions);
			Set<Stream> streams=new HashSet<Stream>();
			StreamCollection sc=new StreamCollection(Sensors.getSensorIdByName(sensorName), sensorDataType);
			try {
				streams=sc.getStreamSet(user, UserRelation.facebook_friends);
				if(streams!=null)
				System.out.println("Streams for UserRelation.facebook_friends: "+ streams.size());
				else
					System.out.println("Streams for UserRelation.facebook_friends: No streams");
					
				Aggregator ag=new Aggregator(streams);
				Stream stream=ag.createStream();
				//AggregatorRegistrar.addIfExists(stream.getStreamId(), streamId);
				Stream newstream=stream.setFilter(f);
				ServerFilterRegistrar.add(streamId, newstream.getStreamId());
				newstream.startStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(isDeviceSpecific(conditions)){
			if(isCombinational(conditions, deviceId, sensorName)){
				//there exists a stream with similar filter settings 
				String sid=getHighFrequencyStream(deviceId, sensorName, conditions, streamId);
				if(sid!=streamId){
					//new stream has low frequency, so add it in server filter
					FrequencyFilterRegistrar.add(sid, streamId);
					createServerFilter(conditions, streamId, sensorName, sensorDataType);

				}
				else{
					//new stream has high frequency, so send the new filter to the device, 
					//delete the existing filter on device and add the same in server-filter


				}				
			}
			//				if(isCombinational(deviceId, sensorName, conditions, streamId)!=null){
			//				System.out.println("NEW COMBINATIONAL FILTER");
			//				String s=isCombinational(deviceId, sensorName, modalities, streamId);
			//				if(!s.equalsIgnoreCase(streamId)){
			//					//set it on server as this stream has less frequency
			//					FrequencyFilterRegistrar.add(clientStreamId, serverStreamIds);
			//					createServerFilter(modalities, streamId, sensorName, sensorDataType);
			//				}
			//				else{
			//					// delete existing client filter and set new.
			//					File file = new File("ClientFilters\\Filter"+streamId+".xml"); 
			//					if (file.exists()){
			//						mergeFilters("ClientFilters\\Filter"+streamId+".xml", "serverfilter.xml");
			//						file.delete();
			//						//map the stream id of deleted filter so that when it comes it should be fired to right listener
			//						
			//
			//						createClientFilter(modalities, streamId, sensorName, sensorDataType);
			//					}
			//					else{
			//						//delete from client and create it for server.
			//						//create a new for client
			//						MQTTClientNotifier.sendStreamNotification(deviceId, MQTTNotifitions.stop_stream, s);
			//						Set<Stream> stream=new HashSet<Stream>();
			//						stream=StreamRegistrar.getStream(deviceId);
			//						for(Stream st : stream){
			//							if(st.getStreamId().equalsIgnoreCase(s)){
			//								ArrayList<String> m=new ArrayList<String>();
			//								for(Modality mod:st.getFilter().getConditions()){
			//									m.add(mod.getActivityName());
			//								}
			//								createServerFilter(m, st.getStreamId(), ""+st.getSensorId(), st.getDataType());
			//								createClientFilter(modalities, streamId, sensorName, sensorDataType);
			//								MQTTClientNotifier.sendStreamNotification(deviceId, MQTTNotifitions.start_stream, streamId);
			//							}
			//						}
			//					}
			//				}
			else{
				System.out.println("NEW DEVICE SPECIFIC FILTER");
				createClientFilter(conditions, streamId, sensorName, sensorDataType);				
			}
		}
		
	}

	private static Boolean isDeviceSpecific(ArrayList<Condition> conditions){
		//does not require data from other device
		for(Condition c:conditions){
			if(c.getModalityType().equals(ModalityType.facebook_friends_location))
				return false;
		}		
		return true;
	}

	private static Boolean isRelational(ArrayList<Condition> conditions){
		//does not require data from other device
		for(Condition c:conditions){
			if(c.getModalityType().equals(ModalityType.facebook_friends_location)){
				return true;
			}
		}		
		return false;
	}

	/**
	 * Returns the streamId of the similar stream with high frequency.
	 * It returns back the same streams(passed by arg) if a low frequency stream is present on client.
	 * If such stream is not found then it returns null.
	 * @param deviceId
	 * @param sensorName
	 * @param conditions
	 * @return
	 */
	private static Boolean isCombinational(ArrayList<Condition> conditions, String deviceId, String sensorName){
		for(Condition c:conditions){
			if(c.getModalityType().equals(ModalityType.time) && c.getModalValue().startsWith("frequency")){
				Set<Stream> streams = new HashSet<Stream>();
				streams=StreamRegistrar.getStream(deviceId);
				for(Stream s : streams){
					if(Sensors.getSensorNameById(s.getSensorId()).equalsIgnoreCase(sensorName)){
						ArrayList<Condition> ar = s.getFilter().getConditions();
						for(Condition con: ar){
							if(con.getModalityType().equals(ModalityType.time) && con.getModalValue().startsWith("frequency") 
									&& isSameConditionSet(conditions, ar))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static Boolean isSameConditionSet(ArrayList<Condition> conditions1, ArrayList<Condition> conditions2){
		if(conditions1.size()!=conditions2.size())
			return false;
		for(Condition c: conditions1){
			if(!conditions2.contains(c))
				return false;
		}
		return true;
	}
	private static String getHighFrequencyStream(String deviceId, String sensorName, ArrayList<Condition> conditions, String streamid){
		double existingTime, newTime;
		Boolean flag;
		for(Condition c:conditions){
			if(c.getModalityType().equals(ModalityType.time) && c.getModalValue().startsWith("frequency")){
				newTime=Double.parseDouble(c.getModalValue().substring(10));
				Set<Stream> streams = new HashSet<Stream>();
				streams=StreamRegistrar.getStream(deviceId);
				for(Stream s : streams){
					if(Sensors.getSensorNameById(s.getSensorId()).equalsIgnoreCase(sensorName)){
						ArrayList<Condition> ar = s.getFilter().getConditions();
						for(Condition con: ar){
							if(con.getModalityType().equals(ModalityType.time) && con.getModalValue().startsWith("frequency") 
									&& isSameConditionSet(conditions, ar)){
								existingTime=Double.parseDouble(con.getModalValue().substring(10));//eg: frequency_20
								if(existingTime>newTime){
									streamid=s.getStreamId();
								}
							}
						}
					}
				}
			}
		}
		return streamid;
	}


	private static void createClientFilter(ArrayList<Condition> conditions, String streamId, String sensorName, String sensorDataType){
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
				file.createNewFile();
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
			for(Condition c:conditions){
				if(!c.getOperator().equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", c.getConditionString());
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

			File newFile=new File("ClientFilters\\Filter"+streamId+".xml");
			System.out.println("File path: "+newFile.getAbsolutePath());

			if(newFile.createNewFile())
				System.out.println("New file created: "+newFile.getName());
			else
				System.out.println("File exists: "+newFile.getName());
			StreamResult result =  new StreamResult(newFile);
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void createServerFilter(ArrayList<Condition> conditions, String streamId, String sensorName, String sensorDataType){
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
				file.createNewFile();
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
			for(Condition c:conditions){

				if(!c.getOperator().equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", c.getConditionString());
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

	//	private static void mergeFilters(String source, String destination){
	//		try
	//		{
	//			Element mainRootS, mainRootD;
	//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	//
	//			Document doc = docBuilder.newDocument();
	//			Document docSource = docBuilder.newDocument();
	//
	//			File fileDesination = new File(source); 
	//			doc = docBuilder.parse(fileDesination); 
	//			mainRootD = doc.getDocumentElement(); 
	//
	//
	//			File fileSource = new File(source); 
	//			docSource = docBuilder.parse(fileSource); 
	//			mainRootS = docSource.getDocumentElement(); 
	//
	//			mainRootD.appendChild(mainRootS.getChildNodes().item(0));
	//
	//
	//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	//			Transformer transformer = transformerFactory.newTransformer();
	//			DOMSource sourceDOM = new DOMSource(doc);
	//
	//			StreamResult result =  new StreamResult(new File(destination));
	//			transformer.transform(sourceDOM, result);
	//
	//			System.out.println("Done");
	//
	//		} catch (Exception e) {
	//			System.out.println(e.toString());
	//		}
	//	}

	public static void mergeFilters(String newFilter){
		//add new filter-config
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File("serverfilter.xml");
			Element mainRoot;
			if(!file.exists()){
				file.createNewFile();
				mainRoot = doc.createElement("Filter");
				doc.appendChild(mainRoot);
			}
			else{
				doc = docBuilder.parse(file); 
				doc.normalize(); 
				mainRoot=doc.getDocumentElement();				
			}
			if(mainRoot.getNodeName().equalsIgnoreCase("filter")){				
				DocumentBuilderFactory docFactoryNew = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilderNew = docFactoryNew.newDocumentBuilder();
				Document docNew = docBuilderNew.newDocument();
				File fileNew = new File(newFilter);
				docNew = docBuilderNew.parse(fileNew); 
				docNew.normalize();
				Element mainRootNew=docNew.getDocumentElement();
				if(mainRootNew.getNodeName().equalsIgnoreCase("Filter")){					
					NodeList nList = docNew.getElementsByTagName("Configuration");
					for (int temp=0;temp<nList.getLength();temp++) {
						Node n= nList.item(temp);
						mainRoot.appendChild(doc.importNode(n, true));
					}
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);

					StreamResult result =  new StreamResult(new File("serverfilter.xml"));
					transformer.transform(source, result);
					System.out.println("Filter appended");
					fileNew.delete();
					System.out.println("New Filter-File deleted");
				}				
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
