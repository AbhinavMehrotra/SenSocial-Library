package com.ubhave.sensocial.server.manager;

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

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Used only for testing
		//		MQTTClientNotifier.sendStreamNotification("abhinav", MQTTNotifitions.start_stream, "stream");

		mergeFilters("C.xml","B.xml");
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
			if(mainRoot.getNodeName().equalsIgnoreCase("filter")){				
				DocumentBuilderFactory docFactoryNew = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilderNew = docFactoryNew.newDocumentBuilder();
				Document docNew = docBuilderNew.newDocument();
				File fileNew = new File(newFilter);
				System.out.println("new filter file found");
				docNew = docBuilderNew.parse(fileNew); 
				docNew.normalize();

				Element mainRootNew=docNew.getDocumentElement();
				if(mainRootNew.getNodeName().equalsIgnoreCase("Filter")){
					
					NodeList nList = docNew.getElementsByTagName("Configuration");
					for (int temp=0;temp<nList.getLength();temp++) {
						Node n= nList.item(temp);
//						System.out.println("new config:"+n);
//						mainRootNew.removeChild(n);
//						mainRoot.appendChild(n);
						mainRoot.appendChild(doc.importNode(n, true));
					}
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);

					StreamResult result =  new StreamResult(new File(existingFilter));
					transformer.transform(source, result);
					System.out.println("Filter appended");
//					fileNew.delete();
//					System.out.println("File deleted");
				}				
			}		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
}
