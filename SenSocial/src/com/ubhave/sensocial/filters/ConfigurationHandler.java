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
import android.util.Log;

import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.privacy.PPDDataType;
import com.ubhave.sensocial.privacy.PPDLocation;
import com.ubhave.sensocial.privacy.PPDParser;
import com.ubhave.sensocial.privacy.PPDSensor;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensocial.sensormanager.SensorClassifier;

public class ConfigurationHandler {

	/**
	 * Checks for any new/removed configurations in the filter.
	 * It updates the Configuration-Set, Activities-Sets, and Sensor-Set in SharedPreferences. <br>
	 * It then calls SensorHandler to set the OSN and Stream sensors in SharedPreferences,
	 * and start subscription if required.
	 * @param context
	 */
	//ConfigurationSet SensorSet
	@SuppressLint("NewApi")
	public static void run(Context context){
		System.out.println("Configuration handler: run");
		//compare the filter configurations which are true and the existing list of configurations in memory
		Set<String> configsFilter= new HashSet<String>();
		Set<String> configsMemory= new HashSet<String>();
		configsFilter=getConfigurations();
		SharedPreferences sp=context.getSharedPreferences("SSDATA", 0);
		configsMemory=sp.getStringSet("ConfigurationSet", null);

		System.out.println("Before PPD parsing- ConfigsMemory"+configsMemory);
		System.out.println("ConfigsFilter"+configsFilter);

		//		check for PPD
		configsFilter=checkForPPD(configsFilter);

		System.out.println("After PPD parsing- ConfigsMemory"+configsMemory);
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
			if(newConfigs!=blank && newConfigs!=null && newConfigs.size()>0){
				System.out.println("New configs found:" +newConfigs);
				Set<String> sensors= new HashSet<String>();
				sensors=sp.getStringSet("SensorSet", sensors);
				ArrayList<String> newSensors=new ArrayList<String>();
				//set new activities for new configs
				Set<String> conditions= new HashSet<String>();
				for(String config:newConfigs){
					System.out.println("New config: "+config);
					conditions.clear();
					conditions=getConditionString(config);
					ed.putStringSet(config, conditions);
					ed.commit();
					System.out.println("Conditions: "+conditions);

					//find new sensors for new-configs
					AllPullSensors aps=new AllPullSensors(context);
					for(String condition:conditions){
						if(condition.equalsIgnoreCase(ModalityType.null_condition)){
							newSensors.add(getRequiredData(config));
							continue;
						}
						Condition con=new Condition(condition);
						if(!sensors.contains(aps.getSensorNameById(ModalityType.getSensorId(con.getModalityType())))){
							newSensors.add(aps.getSensorNameById(ModalityType.getSensorId(con.getModalityType())));
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
			if(removedConfigs!=blank && removedConfigs!=null && removedConfigs.size()>0){
				System.out.println("Unused config found "+removedConfigs);
				Set<String> sensors= new HashSet<String>();
				sensors=getAllRequiredSensorsByFilter();
				ArrayList<String> unusedSensors=new ArrayList<String>();
				//delete activities for unused configs
				Set<String> conditions= new HashSet<String>();
				for(String config:removedConfigs){
					System.out.println("Unused config: "+config);
					conditions.clear();
					conditions=sp.getStringSet(config, null);
					ed.remove(config);
					ed.commit();

					System.out.println("Dependent modalities: "+conditions);	
					//find usused sensors
					AllPullSensors aps=new AllPullSensors(SenSocialManager.getContext());
					for(String condition:conditions){

						if(condition.equalsIgnoreCase("null")){
							unusedSensors.add(getRequiredData(config));
							continue;
						}
						Condition con=new Condition(condition);
						if(!sensors.contains(aps.getSensorNameById(ModalityType.getSensorId(con.getModalityType())))){
							unusedSensors.add(aps.getSensorNameById(ModalityType.getSensorId(con.getModalityType())));
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
			System.out.println("subscribing new sensors");
			for(String c : configsFilter){
				filterConfigs.put(c, getConditionString(c));
				System.out.println("Condition: "+c);
				for(String s:getConditionString(c))
					System.out.println(s);
			}			
			SensorClassifier.run(context,filterConfigs);

		}
		else{
			System.out.println("No changes found in filter file");
		}

	}

	private static Set<String> checkForPPD(Set<String> configsFilter){
		String sName, reqData;
		Map<String, String> lnt;
		String lName;
		Boolean flag;
		Set<String> sensors= new HashSet<String>();
		Set<String> configsPPD= new HashSet<String>();	
		AllPullSensors aps=new AllPullSensors(SenSocialManager.getContext());
		for(String con:configsFilter){
			flag=false;
			sensors.clear();
			for(String conditions: getConditionString(con)){
				Condition c=new Condition(conditions);
				if(!c.getModalityType().equalsIgnoreCase(ModalityType.null_condition) &&
						!c.getModalityType().equalsIgnoreCase(ModalityType.facebook_activity))
					sensors.add(aps.getSensorNameById(ModalityType.getSensorId(c.getModalityType())));
			}
			reqData=getRequiredData(con);
			if(reqData!=null){
				sensors.add(reqData);
			}
			lnt=getRequiredDataLocationNType(con);
			if(lnt.containsKey("server"))
				lName=PPDLocation.SERVER;
			else
				lName=PPDLocation.CLIENT;

			for(String sen: sensors){	
				System.out.println("Ckecking for sensor: "+sen);
				if(!PPDParser.isAllowed(sen, lName, PPDDataType.CLASSIFIED)){
					Log.e("SNnMB", "Sensor: "+sen +", is not allowed");
					flag=true;
					break;
				}
			}
			if(flag==true){
				configsPPD.add(con);
			}
		}
		for(String configs:configsPPD){
			System.out.println("Config not allowed by PPD: "+configs);			
			configsFilter.remove(configs);
		}

		return configsFilter;
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
				System.out.println("getConfigurations in C-Handler");
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

	private static Set<String> getConditionString(String configName){
		Set<String> conditions= new HashSet<String>();
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
						Element eElement = (Element) nNode;
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
										Element e= (Element) tempNode;
										conditions.add(e.getAttribute("name"));
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
		return conditions;
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

						Element eElement = (Element) nNode;
						System.out.println("Config nodes found"+eElement.getAttribute("name"));	
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							System.out.println("Config found: "+configName);		

							NodeList configChilds=nNode.getChildNodes();
							for(int j=0;j<configChilds.getLength();j++){
								if(configChilds.item(j).getNodeName().equalsIgnoreCase("required_data")){
									System.out.println("Required-data node found");	
									sensor=((Element)configChilds.item(j)).getAttribute("sensor");
									System.out.println("Required-data found: "+sensor);										
								}
								else{
									System.out.println("Node Name: "+configChilds.item(j).getNodeName());										
								}
							}

							//							NodeList nodeList = doc.getElementsByTagName("required_data");
							//							
							//							
							//							System.out.println("Required-data node found");	
							//							for(int i=0;i<nodeList.getLength();i++){
							//								Node nNode1 = nodeList.item(i);
							//								sensor=((Element)nNode1).getAttribute("sensor");
							//								System.out.println("Required-data found: "+sensor);		
							//							}

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

							System.out.println("Config found: "+configName);		

							NodeList configChilds=nNode.getChildNodes();
							for(int j=0;j<configChilds.getLength();j++){
								if(configChilds.item(j).getNodeName().equalsIgnoreCase("required_data")){
									System.out.println("Required-data node found");	
									Element eElement1=(Element)configChilds.item(j);
									String location=eElement1.getAttribute("location");
									String data=eElement1.getAttribute("type");
									map.put(location, data);
									System.out.println("Required-data location and type: "+location+", "+data);										
								}
								else{
									System.out.println("Node Name: "+configChilds.item(j).getNodeName());										
								}
							}
//
//							NodeList nodeList = doc.getElementsByTagName("required_data");
//							for(int i=0;i<nodeList.getLength();i++){
//								Node nNode1 = nodeList.item(i);
//								Element eElement1 = (Element) nNode1;
//								String location=eElement1.getAttribute("location");
//								String data=eElement1.getAttribute("type");
//								map.put(location, data);
//							}
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
		AllPullSensors aps=new AllPullSensors(SenSocialManager.getContext());
		for(String c:configs){
			for(String s:getConditionString(c)){
				if(s.equalsIgnoreCase("null")){
					sensors.add(getRequiredData(c));
					break;
				}
				Condition con=new Condition(s);
				sensors.add(aps.getSensorNameById(ModalityType.getSensorId(con.getModalityType())));
			}
		}
		return sensors;
	}

}
