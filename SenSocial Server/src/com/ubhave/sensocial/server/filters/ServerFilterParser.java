package com.ubhave.sensocial.server.filters;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.SensorListenerManager;

public class ServerFilterParser {

	public static Boolean isPresent(SocialEvent se ){
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File("serverfilter.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			
			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("Filter")){
				System.out.println("Filter node found");	
				NodeList nList = doc.getElementsByTagName("Configuration");
				System.out.println("Congif nodes found");	
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("it is element");	
						Element eElement = (Element) nNode;
						System.out.println("element is fine");	
						if(eElement.getAttribute("name").equalsIgnoreCase(se.getFilteredSensorData().getStreamId())){
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			System.out.println("getActivities:"+e.toString());
		}		
		return false;
	}
	
	public static void handleData(SocialEvent se){
		String conditionString;
		Condition condition;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File("serverfilter.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			
			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("Filter")){
				System.out.println("Filter node found");	
				NodeList nList = doc.getElementsByTagName("Configuration");
				System.out.println("Congif nodes found");	
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("it is element");	
						Element eElement = (Element) nNode;
						System.out.println("element is fine");	
						if(eElement.getAttribute("name").equalsIgnoreCase(se.getFilteredSensorData().getStreamId())){
							//get the condition 
							NodeList nodeList = doc.getElementsByTagName("Condition");
							for(int i=0;i<nodeList.getLength();i++){
								Node nNode1 = nodeList.item(i);
								for(int j=0;j<nNode1.getChildNodes().getLength();j++){
									Node tempNode=nNode1.getChildNodes().item(j);
									if(tempNode.getNodeType() == Node.ELEMENT_NODE){	
										Element e= (Element) tempNode;
										conditionString=e.getAttribute("name");
										condition=new Condition(conditionString);
										filterData(condition, se);									
									}
									else{
										System.out.println("NOOOOO");	
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("getActivities:"+e.toString());
		}
	}
	
	private static void filterData(Condition condition, SocialEvent se){
		String userid=condition.getModalValue().substring(condition.getModalValue().lastIndexOf("_"));
		String modality=condition.getModalityType();
		switch (modality){
		case (ModalityType.facebook_friends_location):
			String str=condition.getModalValue().substring(condition.getModalValue().indexOf("_")+1);
			str=str.substring(0, str.indexOf("_"));
			double range=Double.parseDouble(str);
			Location l1=UserRegistrar.getLocation(userid);
			Location l2=UserRegistrar.getLocation(UserRegistrar.getUserByDeviceId(se.getFilteredSensorData().getDeviceId()).getId());
			double distance=DummyClassifier.calculateDistanceInMiles(l1, l2);
			if(distance<range){
				//devices are within 1mile range ->> fire data
				System.out.println("Relational Stream Fired");
				//find the stream which was created initially and fire data for that stream
				String id=ServerFilterRegistrar.getFilterId(se.getFilteredSensorData().getStreamId());
				se.getFilteredSensorData().setStreamId(id); 
				SensorListenerManager.fireUpdate(se);	
			}
			break;
		case(""):
			
		}
	}

}
