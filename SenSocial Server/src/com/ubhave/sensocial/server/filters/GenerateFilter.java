package com.ubhave.sensocial.server.filters;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateFilter {

	public static void createXML(String deviceId, ArrayList<String> activities, String streamId, String sensorName, String sensorDataType){
		try
		{
			Element rootElement,mainRoot;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File("C:\\Filter"+deviceId+".xml"); 
			if (file.exists() && file.length()!=0) 
			{ 
				System.out.println("file found");
				doc = docBuilder.parse(file); 
				mainRoot = doc.getDocumentElement(); 
			} 
			else 
			{ 
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
			for(String s:activities){

				if(!s.equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", s);
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

			StreamResult result =  new StreamResult(new File("C:\\Filter"+deviceId+".xml"));
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
