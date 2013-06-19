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
						System.out.println("configuration node found");
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
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

			File file = new File("C:\\testing.xml");
			System.out.println("file found");
			doc = docBuilder.parse(file); 
			

			doc.getDocumentElement().normalize();
			if(doc.getDocumentElement().getNodeName().equals("filter")){
				NodeList nList = doc.getElementsByTagName("configuration");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
							eElement.setAttribute("sense", "false");
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

			File file = new File("C:\\testing.xml");
			System.out.println("file found");
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
							mainRoot.removeChild(nNode);
						}
					}
				}
				System.out.println("Done");
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	
	public static void mergeFilters(String newFilter, String existingFilter){
		//add new filter-config
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(existingFilter);
			System.out.println("file found");
			doc = docBuilder.parse(file); 
			doc.normalize();
			

			Element mainRoot=doc.getDocumentElement();
			if(mainRoot.getNodeName().equals("filter")){
				
				DocumentBuilderFactory docFactoryNew = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilderNew = docFactoryNew.newDocumentBuilder();

				Document docNew = docBuilderNew.newDocument();

				File fileNew = new File(newFilter);
				System.out.println("new filter file found");
				docNew = docBuilderNew.parse(fileNew); 
				docNew.normalize();
				

				Element mainRootNew=docNew.getDocumentElement();
				if(mainRootNew.getNodeName().equalsIgnoreCase("filter")){
					NodeList nList = docNew.getElementsByTagName("configuration");
					for (int temp=0;temp<nList.getLength();temp++) {
							mainRoot.appendChild(nList.item(temp));
					}
				}
				
				System.out.println("Filter appended");
				fileNew.delete();
				System.out.println("File deleted");
				
				
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
//	public static void mergeFilters(String sourceFilter, String destinationFilter){
//		try
//		{
//			Element mainRootS, mainRootD;
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			Document doc = docBuilder.newDocument();
//			Document docSource = docBuilder.newDocument();
//
//			File fileDesination = new File(sourceFilter); 
//			doc = docBuilder.parse(fileDesination); 
//			mainRootD = doc.getDocumentElement(); 
//
//
//			File fileSource = new File(sourceFilter); 
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
//			StreamResult result =  new StreamResult(new File(destinationFilter));
//			transformer.transform(sourceDOM, result);
//
//			Log.i("SNnMB", "New filter successfully merged");
//
//		} catch (Exception e) {
//			Log.e("SNnMB", "Error while merging new filter: "+ e.toString());
//		}
//	}
}
