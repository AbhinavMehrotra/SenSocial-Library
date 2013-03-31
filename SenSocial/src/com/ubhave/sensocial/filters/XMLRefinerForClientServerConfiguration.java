package com.ubhave.sensocial.filters;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.sensocial.R;
import com.ubhave.sensocial.exceptions.InvalidSensorException;
import com.ubhave.sensocial.exceptions.XMLFileException;


public class XMLRefinerForClientServerConfiguration {

	private final String TAG="SNnMB";
	private SharedPreferences sp;
	private Editor ed;
	private Context context;
	private String sensor, sConfig,cConfig;

	public XMLRefinerForClientServerConfiguration(Context context){
		this.context=context;
		sp=context.getSharedPreferences("snmbData",0);
		ed=sp.edit();
	}
	public void refineXML() throws InvalidSensorException, XMLFileException{
		try {
			
//			File fXmlFile = new File("clientserverconfig.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
			
			InputStream in=context.getResources().openRawResource(R.raw.clientserverconfig);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			
			doc.getDocumentElement().normalize();
			if(doc.getDocumentElement().getNodeName().equals("sensorconfiguration")){
				NodeList nList = doc.getElementsByTagName("sensor");
				for (int temp=0;temp<nList.getLength();temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						sensor=eElement.getAttribute("name");
						if(!isSensor(sensor)){
							throw new InvalidSensorException(sensor+" is not a valid sensor name.");
						}
						if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("required").equals("true")){
							if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("data").equals("raw")){
								sConfig="str";
							}else{
								sConfig="stc";
							}
						}
						else{
							sConfig="sf";
						}
						if(((Element) eElement.getElementsByTagName("client").item(0)).getAttribute("required").equals("true")){
							if(((Element) eElement.getElementsByTagName("client").item(0)).getAttribute("data").equals("raw")){
								cConfig="ctr";
							}else{
								cConfig="ctc";
							}
						}
						else{
							cConfig="cf";
						}
						insertConfig(sensor,cConfig+sConfig);
					}
				}
			}
			else{
				Log.e(TAG,"No root node found as: <sensorconfiguration> .....s </sensorconfiguration>");
			}
		} catch (Exception e) {
			throw new XMLFileException(e.toString());
		}
	}

	private void insertConfig(String sensorName, String config){
		if(sensorName.equals("accelerometer")) ed.putString("accelerometerconfig", config);
		if(sensorName.equals("bluetooth")) ed.putString("bluetoothconfig", config);
		if(sensorName.equals("wifi")) ed.putString("wificonfig", config);
		if(sensorName.equals("location")) ed.putString("locationconfig", config);
		if(sensorName.equals("microphone")) ed.putString("microphoneconfig", config);
		ed.commit();
	}
	
	private Boolean isSensor(String sName){
		if(sName.equals("accelerometer") ||sName.equals("bluetooth") ||sName.equals("wifi") ||sName.equals("location") ||sName.equals("microphone")){
			return true;
		}
		return false;
	}

}
