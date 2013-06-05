package com.ubhave.sensocial.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigurationHandler {

	@SuppressLint("NewApi")
	public static void run(Context context){
		//compare the filter configurations which are true and the existing list of configurations in memory
		Set<String> configsFilter= new HashSet<String>();
		Set<String> configsMemory= new HashSet<String>();
		configsFilter=getConfigurations();
		
		SharedPreferences sp=context.getSharedPreferences("SNnMB", 0);
		configsMemory=sp.getStringSet("ConfigurationSet", null);
		
		if(!configsFilter.equals(configsMemory)){
			//find new configs 
			ArrayList<String> newConfigs= new ArrayList<String>();
			newConfigs=getNewConfigs(configsFilter, configsMemory);
			
			//set new configs in ConfigurationSet
			Editor ed=sp.edit();
			ed.putStringSet("ConfigurationSet", configsFilter);			
			ed.commit();
			
			
			Set<String> sensors= new HashSet<String>();
			sensors=sp.getStringSet("SensorSet", null);
			ArrayList<String> newSensors=new ArrayList<String>();
			//set new activities for new configs
			Set<String> activities= new HashSet<String>();
			for(String config:newConfigs){
				activities.clear();
				activities=getActivities(config);
				ed.putStringSet(config, activities);
				
				//find new sensors for new-configs
				for(String activity:activities){
					if(!sensors.contains(Modality.valueOf(activity).getSensorName())){
						newSensors.add(Modality.valueOf(activity).getSensorName());
					}
				}
			}
			
			//set new sensors in SensorList
			for(String s:newSensors)
				sensors.add(s);
			ed.putStringSet("SensorSet", sensors);
			ed.commit();
			
			//subscribe new sensors
				//there can be three sensor-lists 
			
			
		}
		
	}
	
	private static Set<String> getConfigurations(){
		Set<String> configs= new HashSet<String>();
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File("C:\\testing.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("filter")){
				NodeList nList = doc.getElementsByTagName("configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("sense").equalsIgnoreCase("true")){
							configs.add(eElement.getAttribute("name"));
						}
					}
				}
				System.out.println("Done");
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return configs;
	}

	private static ArrayList<String> getNewConfigs(Set<String> filter, Set<String> mem){
		ArrayList<String> configs=new ArrayList<String>();
		for(String f : filter){
			if(!mem.contains(f)){
				configs.add(f);
			}
	    }		
		return configs;
	}

	private static Set<String> getActivities(String configName){
		Set<String> activities= new HashSet<String>();
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File("C:\\testing.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("filter")){
				NodeList nList = doc.getElementsByTagName("configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							for(int j=0;j<eElement.getChildNodes().getLength();j++){
								if(eElement.getChildNodes().item(j).getNodeType()==Node.ELEMENT_NODE){
									activities.add(((Element)eElement.getChildNodes().item(j)).getAttribute("name"));
								}
							}
						}
					}
				}
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}		
		return activities;
	}
	
}
