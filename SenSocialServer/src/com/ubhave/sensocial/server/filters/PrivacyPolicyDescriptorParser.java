package com.ubhave.sensocial.server.filters;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ubhave.sensocial.server.exception.InvalidSensorException;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.XMLFileException;


public class PrivacyPolicyDescriptorParser {

	private final String TAG="SNnMB";
	private String sensor, sConfig,cConfig;


	public Boolean isAllowed(String deviceId,Map<String, String> sensorNameAndType) throws PPDException, XMLFileException{
		Boolean flag=true, present;
		String key, value;
		Map<String,String> ppd= new HashMap<String, String>();
		try {
			ppd=parseXML(deviceId);
		} catch (Exception e) {
			System.out.println("PPD parser error. "+e.toString());
		}
		for (Map.Entry<String, String> stream : sensorNameAndType.entrySet()) {
		    key = stream.getKey();
		    value = stream.getValue();
		    present=false;
			for (Map.Entry<String, String> ppdEntry : sensorNameAndType.entrySet()) {
				if(ppdEntry.getKey().equalsIgnoreCase(key) && ppdEntry.getValue().equalsIgnoreCase(value)){
					present=true;
					break;
				}
			}
		    if(present==false){
		    	flag=false;
		    	break;
		    }
		}
		System.out.println("is Allowed: "+flag);
		return flag;		
	}

	/**
	 * Method to parse the XML file called Privacy Policy Descriptor
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws PPDException 
	 * @throws InvalidSensorException
	 * @throws XMLFileException
	 */
	private Map<String, String> parseXML(String deviceId) throws ParserConfigurationException, SAXException, IOException, PPDException, XMLFileException{
		Map<String,String> ppd= new HashMap<String, String>();		
		File file = new File("PPD\\ppd"+deviceId+".xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);

		doc.getDocumentElement().normalize();
		if(doc.getDocumentElement().getNodeName().equals("ppd")){
			NodeList nList = doc.getElementsByTagName("sensor");
			for (int temp=0;temp<nList.getLength();temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					sensor=eElement.getAttribute("name");
					if(!isSensor(sensor)){
						throw new PPDException(sensor+" is not a valid sensor name. Please check the PPD for this device.");
					}
					if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("required").equals("true")){
						if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("data").equals("both")){
							sConfig="ALL";
						}else if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("data").equals("raw")){
							sConfig="ALL";
						}else{
							sConfig="CLASSIFIED";
						}
					}
					else{
						sConfig="NO";
					}
					ppd.put(sensor,sConfig);
				}
			}
		}
		else{
			System.out.println(TAG+" No root node found as: <ppd> .....s </ppd>");
			//If no Descriptor found then set all as NO (no data will be provided)
			throw new XMLFileException("Check your PPD file. No root node found as: <ppd> .....s </ppd>");
		}
		return ppd;
	}


	private Boolean isSensor(String sName){
		if(sName.equals("accelerometer") ||sName.equals("bluetooth") ||sName.equals("wifi") ||sName.equals("location") ||sName.equals("microphone")){
			return true;
		}
		return false;
	}




}
