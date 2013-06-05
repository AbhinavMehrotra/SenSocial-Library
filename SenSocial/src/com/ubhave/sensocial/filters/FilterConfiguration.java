package com.ubhave.sensocial.filters;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FilterConfiguration {

	public static void startConfiguration(String configName){
		//set the configuration attribute "sense"="true"
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
							eElement.setAttribute("sense", "true");
						}
					}
				}
				System.out.println("Done");
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
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

	public static void deleteConfiguration(String configName){
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

	
	public static void addConfiguration(String filePath){
		//add new filter-config
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
				
				DocumentBuilderFactory docFactoryNew = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilderNew = docFactoryNew.newDocumentBuilder();

				Document docNew = docBuilderNew.newDocument();

				File fileNew = new File(filePath);
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
}
