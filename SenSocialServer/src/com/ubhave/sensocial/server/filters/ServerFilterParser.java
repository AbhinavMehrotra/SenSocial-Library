/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
package com.ubhave.sensocial.server.filters;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.database.UserRegistrar;
import com.ubhave.sensocial.server.manager.Location;
import com.ubhave.sensocial.server.manager.StreamRegistrar;
import com.ubhave.sensocial.server.manager.User;

/**
 * ServerFilterParser class is the parser used for server filters
 */
public class ServerFilterParser {
	
	/**
	 * Returns whether the stream conditions are satisfied.
	 * @param streamId (String) Stream id
	 * @param Social-Event {@link SocialEvent}
	 * @return
	 */
	public static Boolean isSatisfied(String streamId, SocialEvent se){
		Boolean flag=false;
		String condition=ServerFilterRegistrar.getCondition(streamId);
		Condition c=new Condition(condition);
		if(c.getModalityType().equalsIgnoreCase(ModalityType.facebook_friends_location)){
			double range= Double.parseDouble(c.getModalValue().substring(c.getModalValue().lastIndexOf("_")+1));
			User user=UserRegistrar.getUserByDeviceId(StreamRegistrar.getStreamById(streamId).getDevice().getDeviceId());
			User friend=UserRegistrar.getUserByDeviceId(se.getFilteredSensorData().getDeviceId());
			Location l1=UserRegistrar.getLocation(user.getId());
			Location l2=UserRegistrar.getLocation(friend.getId());
			double distance=calculateDistanceInMiles(l1, l2);
			if(range>=distance){
				flag=true;
			}
		}		
		return flag;
	}
	

	private static double calculateDistanceInMiles(Location StartP, Location EndP) {  
		//Haversine formula-wiki used by google
		//		double Radius=6371; //kms
		double Radius=3963.1676; //miles
		double lat1 = StartP.getLatitude();  
		double lat2 = EndP.getLatitude();  
		double lon1 = StartP.getLongitude();  
		double lon2 = EndP.getLongitude();  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
				Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));  
		return Radius * c;  
	} 

//	public static Boolean isPresent(SocialEvent se ){
//		try
//		{
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			File file = new File("serverfilter.xml");
//			doc = docBuilder.parse(file); 
//			doc.normalize();			
//			Element mainRoot=doc.getDocumentElement();
//			if(mainRoot.getNodeName().equals("Filter")){
//				System.out.println("Filter node found");	
//				NodeList nList = doc.getElementsByTagName("Configuration");
//				System.out.println("Congif nodes found");	
//				for (int temp=0;temp<nList.getLength();temp++) {
//					Node nNode = nList.item(temp);
//					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						System.out.println("it is element");	
//						Element eElement = (Element) nNode;
//						System.out.println("element is fine");	
//						if(eElement.getAttribute("name").equalsIgnoreCase(se.getFilteredSensorData().getStreamId())){
//							return true;
//						}
//					}
//				}
//			}
//			return false;
//		} catch (Exception e) {
//			System.out.println("getActivities:"+e.toString());
//		}		
//		return false;
//	}
	
//	public static void handleData(SocialEvent se){
//		String conditionString;
//		Condition condition;
//		try
//		{
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			File file = new File("serverfilter.xml");
//			doc = docBuilder.parse(file); 
//			doc.normalize();			
//			Element mainRoot=doc.getDocumentElement();
//			if(mainRoot.getNodeName().equals("Filter")){
//				System.out.println("Filter node found");	
//				NodeList nList = doc.getElementsByTagName("Configuration");
//				System.out.println("Congif nodes found");	
//				for (int temp=0;temp<nList.getLength();temp++) {
//					Node nNode = nList.item(temp);
//					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						System.out.println("it is element");	
//						Element eElement = (Element) nNode;
//						System.out.println("element is fine");	
//						if(eElement.getAttribute("name").equalsIgnoreCase(se.getFilteredSensorData().getStreamId())){
//							//get the condition 
//							NodeList nodeList = doc.getElementsByTagName("Condition");
//							for(int i=0;i<nodeList.getLength();i++){
//								Node nNode1 = nodeList.item(i);
//								for(int j=0;j<nNode1.getChildNodes().getLength();j++){
//									Node tempNode=nNode1.getChildNodes().item(j);
//									if(tempNode.getNodeType() == Node.ELEMENT_NODE){	
//										Element e= (Element) tempNode;
//										conditionString=e.getAttribute("name");
//										condition=new Condition(conditionString);
//										filterData(condition, se);									
//									}
//									else{
//										System.out.println("NOOOOO");	
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			System.out.println("getActivities:"+e.toString());
//		}
//	}
	
	
	
//	private static void filterData(Condition condition, SocialEvent se){
//		String userid=condition.getModalValue().substring(condition.getModalValue().lastIndexOf("_"));
//		String modality=condition.getModalityType();
//		switch (modality){
//		case (ModalityType.facebook_friends_location):
//			String str=condition.getModalValue().substring(condition.getModalValue().indexOf("_")+1);
//			str=str.substring(0, str.indexOf("_"));
//			double range=Double.parseDouble(str);
//			Location l1=UserRegistrar.getLocation(userid);
//			Location l2=UserRegistrar.getLocation(UserRegistrar.getUserByDeviceId(se.getFilteredSensorData().getDeviceId()).getId());
//			double distance=DummyClassifier.calculateDistanceInMiles(l1, l2);
//			if(distance<range){
//				//devices are within 1mile range ->> fire data
//				System.out.println("Relational Stream Fired");
//				//find the stream which was created initially and fire data for that stream
//				String id=ServerFilterRegistrar.getFilterId(se.getFilteredSensorData().getStreamId());
//				se.getFilteredSensorData().setStreamId(id); 
//				SensorListenerManager.fireUpdate(se);	
//			}
//			break;
//		case(""):
//			
//		}
//	}

}
