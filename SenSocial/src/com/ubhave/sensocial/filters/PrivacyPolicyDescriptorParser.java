//package com.ubhave.sensocial.filters;
//import java.io.InputStream;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.util.Log;
//
//import com.sensocial.R;
//import com.ubhave.sensocial.exceptions.InvalidSensorException;
//import com.ubhave.sensocial.exceptions.XMLFileException;
//
//
//public class PrivacyPolicyDescriptorParser {
//
//	private final String TAG="SNnMB";
//	private SharedPreferences sp;
//	private Editor ed;
//	private Context context;
//	private String sensor, sConfig,cConfig;
//
//	public PrivacyPolicyDescriptorParser(Context context){
//		this.context=context;
//		sp=context.getSharedPreferences("SSDATA",0);
//		ed=sp.edit();
//	}
//	
//	/**
//	 * Method to parse the XML file called Privacy Policy Descriptor
//	 * @throws InvalidSensorException
//	 * @throws XMLFileException
//	 */
//	public void refineXML() throws InvalidSensorException, XMLFileException{
//		try {
//			
//			
//			InputStream in=context.getResources().openRawResource(R.raw.ppd);
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document doc = builder.parse(in);
//			
//			doc.getDocumentElement().normalize();
//			if(doc.getDocumentElement().getNodeName().equals("ppd")){
//				NodeList nList = doc.getElementsByTagName("sensor");
//				for (int temp=0;temp<nList.getLength();temp++) {
//					Node nNode = nList.item(temp);
//					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						Element eElement = (Element) nNode;
//						sensor=eElement.getAttribute("name");
//						if(!isSensor(sensor)){
//							throw new InvalidSensorException(sensor+" is not a valid sensor name.");
//						}
//						if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("required").equals("true")){
//							if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("data").equals("both")){
//								sConfig="SYA";
//							}else if(((Element) eElement.getElementsByTagName("server").item(0)).getAttribute("data").equals("raw")){
//								sConfig="SYR";
//							}else{
//								sConfig="SYC";
//							}
//						}
//						else{
//							sConfig="SNO";
//						}
//						if(((Element) eElement.getElementsByTagName("client").item(0)).getAttribute("required").equals("true")){
//							if(((Element) eElement.getElementsByTagName("client").item(0)).getAttribute("data").equals("both")){
//								cConfig="CYA";
//							}else if(((Element) eElement.getElementsByTagName("client").item(0)).getAttribute("data").equals("raw")){
//								cConfig="CYR";
//							}else{
//								cConfig="CYC";
//							}
//						}
//						else{
//							cConfig="CNO";
//						}
//						insertConfig(sensor,cConfig+sConfig);
//					}
//				}
//			}
//			else{
//				Log.e(TAG,"No root node found as: <sensorconfiguration> .....s </sensorconfiguration>");
//				//If no Descriptor found then set all as NO (no data will be provided)
//				deactivateAll();
//			}
//		} catch (Exception e) {
//			//If no Descriptor found then set all as NO (no data will be provided)
//			deactivateAll();
//			throw new XMLFileException(e.toString());
//		}
//	}
//
//	private void insertConfig(String sensorName, String config){
//		if(sensorName.equals("accelerometer")) ed.putString("accelerometerconfig", config);
//		if(sensorName.equals("bluetooth")) ed.putString("bluetoothconfig", config);
//		if(sensorName.equals("wifi")) ed.putString("wificonfig", config);
//		if(sensorName.equals("location")) ed.putString("locationconfig", config);
//		if(sensorName.equals("microphone")) ed.putString("microphoneconfig", config);
//		ed.commit();
//	}
//	
//	private void deactivateAll(){
//		String config="SNOCNO";
//		ed.putString("accelerometerconfig", config);
//		ed.putString("bluetoothconfig", config);
//		ed.putString("wificonfig", config);
//		ed.putString("locationconfig", config);
//		ed.putString("microphoneconfig", config);
//		ed.commit();
//	}
//	
//	private Boolean isSensor(String sName){
//		if(sName.equals("accelerometer") ||sName.equals("bluetooth") ||sName.equals("wifi") ||sName.equals("location") ||sName.equals("microphone")){
//			return true;
//		}
//		return false;
//	}
//	
//	public Boolean isAllowed(String sensorName, String serverDataType, String clientDataType ){
//		try {
//			refineXML();
//		} catch (InvalidSensorException e) {
//			Log.e(TAG, "Is allowed 1: "+e.toString());
//		} catch (XMLFileException e) {
//			Log.e(TAG, "Is Allowed 2: "+e.toString());
//		}
//		Boolean flag=false;
//		String config=sp.getString(sensorName+"config", "");
//		Log.e(TAG, "Config in ppd: "+config);
//		if(config!=null && serverDataType!=null){
//			char serverType=config.charAt(5);
//			if((serverDataType.toLowerCase()).charAt(0)==serverType){
//				flag=true;
//			}
//		}
//		if(config!=null && clientDataType!=null){
//			char clientType=config.charAt(2);
//			if((clientDataType.toUpperCase()).charAt(0)==clientType){
//				flag=true;
//			}
//		}
//		return flag;		
//	}
//	
//	protected Boolean isAllowed(String sensorName, String config ){
//		return(config.equalsIgnoreCase(sp.getString(sensorName+"config", "")));		
//	}
//	
//
//}
