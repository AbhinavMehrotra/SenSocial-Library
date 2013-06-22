package com.ubhave.sensocial.filters;

import java.io.File;

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

import android.os.Environment;
import android.util.Log;

public class FilterSettings {

	public static void startConfiguration(String configName){
		//set the configuration attribute "sense"="true"
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			System.out.println("file found");
			doc = docBuilder.parse(file); 
			doc.getDocumentElement().normalize();
			if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("filter")){
				System.out.println("filter node found");
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("configuration nodes found");
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equals(configName)){
							eElement.setAttribute("sense", "true");
							System.out.print("config found:"+eElement.getAttribute("name"));
							//do we need to write it again?
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource source = new DOMSource(doc);

							StreamResult result =  new StreamResult(new File(Environment.getExternalStorageDirectory(), "filter.xml"));
							transformer.transform(source, result);
						}
						else{
							System.out.println("config not found: "+configName);
							System.out.println("config name is: "+eElement.getAttribute("name"));
						}
					}
				}
				System.out.println("Start-configuration: Done");
			}	
			else{
				System.out.println("Start-configuration: Filter not found. It is: "+doc.getDocumentElement().getNodeName());
			}
		} catch (Exception e) {
			System.out.println("Filter-setting: "+e.toString());
		}
	}

	
	public static void stopConfiguration(String configName){
		//set the configuration attribute "sense"="false"
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			System.out.println("file found");
			doc = docBuilder.parse(file); 
			doc.getDocumentElement().normalize();
			if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("filter")){
				System.out.println("filter node found");
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println("configuration node found");
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							eElement.setAttribute("sense", "false");
							Log.i("SNnMB", "Config sensing false(pause)");
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource source = new DOMSource(doc);

							StreamResult result =  new StreamResult(new File(Environment.getExternalStorageDirectory(), "filter.xml"));
							transformer.transform(source, result);
						}
						else{
							Log.i("SNnMB", "Config not found");
						}
					}
				}
				System.out.println("Done");
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void removeConfiguration(String configName){
		//delete filter-config
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
			System.out.println("file found");
			doc = docBuilder.parse(file); 
			doc.normalize();
			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equalsIgnoreCase("filter")){
				NodeList nList = doc.getElementsByTagName("Configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							mainRoot.removeChild(nNode);
							Log.i("SNnMB", "Config deleted");
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource source = new DOMSource(doc);

							StreamResult result =  new StreamResult(new File(Environment.getExternalStorageDirectory(), "filter.xml"));
							transformer.transform(source, result);
						}
						else{
							Log.i("SNnMB", "Config not found");
						}
					}
				}
				System.out.println("Done");
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	
	public static void mergeFilters(String newFilter){
		//add new filter-config
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml");
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
				File fileNew = new File(Environment.getExternalStorageDirectory(), newFilter);
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

					StreamResult result =  new StreamResult(new File(Environment.getExternalStorageDirectory(), "filter.xml"));
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
