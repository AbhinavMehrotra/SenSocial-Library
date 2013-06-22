package com.ubhave.sensocial.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import android.os.Environment;

import com.ubhave.sensocial.sensormanager.SensorClassifier;

public class ConfigurationHandler {

	/**
	 * Checks for any new/removed configurations in the filter.
	 * It updates the Configuration-Set, Activities-Sets, and Sensor-Set in SharedPreferences. <br>
	 * It then calls SensorHandler to set the OSN and Stream sensors in SharedPreferences,
	 * and start subscription if required.
	 * @param context
	 */
	@SuppressLint("NewApi")
	public static void run(Context context){
		System.out.println("Configuration handler: run");
		//compare the filter configurations which are true and the existing list of configurations in memory
		Set<String> configsFilter= new HashSet<String>();
		Set<String> configsMemory= new HashSet<String>();
		configsFilter=getConfigurations();
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		configsMemory=sp.getStringSet("ConfigurationSet", null);


		System.out.println("configsMemory"+configsMemory);
		System.out.println("configsFilter"+configsFilter);

		if(!configsFilter.equals(configsMemory) ){
			System.out.println("Some changes in filter file");
			//find new configs 
			ArrayList<String> newConfigs= new ArrayList<String>();
			newConfigs=getNewConfigs(configsFilter, configsMemory);

			//find removed configs
			ArrayList<String> removedConfigs= new ArrayList<String>();
			removedConfigs=getRemovedConfigs(configsFilter, configsMemory);

			//set new configs in ConfigurationSet
			Editor ed=sp.edit();
			ed.putStringSet("ConfigurationSet", configsFilter);			
			ed.commit();


			ArrayList<String> blank= new ArrayList<String>();

			//if some configs has been added
			if(newConfigs!=blank && newConfigs!=null){
				System.out.println("New config found:" +newConfigs);
				Set<String> sensors= new HashSet<String>();
				sensors=sp.getStringSet("SensorSet", sensors);
				ArrayList<String> newSensors=new ArrayList<String>();
				//set new activities for new configs
				Set<String> activities= new HashSet<String>();
				for(String config:newConfigs){
					System.out.println("New config: "+config);
					activities.clear();
					activities=getActivities(config);
					ed.putStringSet(config, activities);
					ed.commit();
					System.out.println("Activities: "+activities);

					//find new sensors for new-configs
					for(String activity:activities){
						if(activity.equalsIgnoreCase("ALL")){
							newSensors.add(getRequiredData(config));
							continue;
						}
						if(!sensors.contains(Modality.valueOf(activity).getSensorName())){
							newSensors.add(Modality.valueOf(activity).getSensorName());
						}
					}
				}

				//set new sensors in SensorList
				for(String s:newSensors){
					sensors.add(s);
					System.out.println("New sensors: "+s);
				}
				ed.putStringSet("SensorSet", sensors);
				ed.commit();				
			}
			//if some configs has been removeds
			if(removedConfigs!=blank && removedConfigs!=null){
				System.out.println("Unused config found"+removedConfigs);
				Set<String> sensors= new HashSet<String>();
				sensors=getAllRequiredSensorsByFilter();
				ArrayList<String> unusedSensors=new ArrayList<String>();
				//delete activities for unused configs
				Set<String> activities= new HashSet<String>();
				for(String config:removedConfigs){
					System.out.println("Unused config: "+config);
					activities.clear();
					activities=sp.getStringSet(config, null);
					ed.remove(config);
					ed.commit();

					System.out.println("New activities: "+activities);	
					//find usused sensors
					for(String activity:activities){
						if(activity.equalsIgnoreCase("ALL")){
							unusedSensors.add(getRequiredData(config));
							continue;
						}
						if(!sensors.contains(Modality.valueOf(activity).getSensorName())){
							unusedSensors.add(Modality.valueOf(activity).getSensorName());
						}
					}
				}
				for(String s:unusedSensors){
					sensors.add(s);
					System.out.println("Remove sensors: "+s);
				}
				ed.putStringSet("SensorSet", sensors);
				ed.commit();				
			}



			//subscribe new sensors
			//there can be two sensor-lists
			Map<String, Set<String>> filterConfigs=new HashMap<String, Set<String>>();
			for(String c : getConfigurations()){
				filterConfigs.put(c, getActivities(c));
				System.out.println(c);
				for(String s:getActivities(c))
					System.out.println(s);
			}			
			SensorClassifier.run(context,filterConfigs);

		}
		else{
			System.out.println("No changes found in filter file");
		}

	}

	private static Set<String> getConfigurations(){
		Set<String> configs= new HashSet<String>();
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("Filter")){
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("sense").equalsIgnoreCase("true")){
							configs.add(eElement.getAttribute("name"));
						}
					}
				}
				System.out.println("Get-Config in C-Handler: Done");
			}		
		} catch (Exception e) {
			System.out.println("C-Handler getConfig: "+e.toString());
		}
		return configs;
	}

	private static ArrayList<String> getNewConfigs(Set<String> filter, Set<String> mem){
		ArrayList<String> configs=new ArrayList<String>();
		for(String f : filter){
			if(mem==null || !mem.contains(f)){
				configs.add(f);
			}
		}		
		return configs;
	}

	private static ArrayList<String> getRemovedConfigs(Set<String> filter, Set<String> mem){
		ArrayList<String> configs=new ArrayList<String>();
		if(mem!=null){
			for(String x : mem){
				if(!filter.contains(x)){
					configs.add(x);
				}
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

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
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
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							//							for(int j=0;j<eElement.getChildNodes().getLength();j++){
							//								if(eElement.getChildNodes().item(j).getNodeType()==Node.ELEMENT_NODE){
							//									activities.add(((Element)eElement.getChildNodes().item(j)).getAttribute("name"));
							//								}
							//							}						


							NodeList nodeList = doc.getElementsByTagName("Condition");
							for(int i=0;i<nodeList.getLength();i++){
								Node nNode1 = nodeList.item(i);
								for(int j=0;j<nNode1.getChildNodes().getLength();j++){
									Node tempNode=nNode1.getChildNodes().item(j);
									if(tempNode.getNodeType() == Node.ELEMENT_NODE){
										System.out.println("Is it??");	
										Element e= (Element) tempNode;
										System.out.println("Yessss");	
										activities.add(e.getAttribute("name"));
									}
									else{
										System.out.println("NOOOOO");	
									}
//									activities.add(((Element)nNode1.getChildNodes().item(j)).getAttribute("name"));
								}
							}
						}
					}
				}
			}		
		} catch (Exception e) {
			System.out.println("getActivities:"+e.toString());
		}		
		return activities;
	}


	public static String getRequiredData(String configName){
		String sensor=null;
		System.out.println("getRequiredData: " + configName);
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("Filter")){
				System.out.println("Filter node found");				
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("Config nodes found");		
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							System.out.println("Config found: "+configName);		
							NodeList nodeList = doc.getElementsByTagName("required_data");
							System.out.println("Required-data node found");	
							for(int i=0;i<nodeList.getLength();i++){
								Node nNode1 = nodeList.item(i);
								sensor=((Element)nNode1).getAttribute("sensor");
								System.out.println("Required-data found: "+sensor);		
//								for(int j=0;j<nNode1.getChildNodes().getLength();j++){
//									sensor=((Element)nNode1.getChildNodes().item(j)).getAttribute("name");
//								}
							}
						}
					}
				}
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}		
		return sensor;
	}

	public static Map<String,String> getRequiredDataLocationNType(String configName){
		Map<String,String> map= new HashMap<String,String>();

		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			doc = docBuilder.parse(file); 
			doc.normalize();			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("Filter")){
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							NodeList nodeList = doc.getElementsByTagName("required_data");
							for(int i=0;i<nodeList.getLength();i++){
								Node nNode1 = nodeList.item(i);
								Element eElement1 = (Element) nNode1;
								String location=eElement1.getAttribute("location");
								String data=eElement1.getAttribute("type");
								map.put(location, data);
//								for(int j=0;j<nNode1.getChildNodes().getLength();j++){
//									String location=((Element)nNode1.getChildNodes().item(j)).getAttribute("location");
//									String data=((Element)nNode1.getChildNodes().item(j)).getAttribute("data");
//									map.put(location, data);
//								}
							}
						}
					}
				}
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
		return map;
	}

	private static Set<String> getAllRequiredSensorsByFilter(){
		Set<String> sensors= new HashSet<String>();
		Set<String> configs= new HashSet<String>();
		configs=getConfigurations();
		for(String c:configs){
			for(String s:getActivities(c)){
				if(s.equalsIgnoreCase("ALL")){
					sensors.add(getRequiredData(c));
					break;
				}
				sensors.add(Modality.valueOf(s).getSensorName());
			}
		}
		return sensors;
	}

}
