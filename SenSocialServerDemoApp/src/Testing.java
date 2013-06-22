import java.util.ArrayList;

import javax.swing.JFrame;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.exception.PPDException;
import com.ubhave.sensocial.server.exception.SensorDataTypeException;
import com.ubhave.sensocial.server.exception.XMLFileException;
import com.ubhave.sensocial.server.filters.Filter;
import com.ubhave.sensocial.server.filters.Modality;
import com.ubhave.sensocial.server.manager.Device;
import com.ubhave.sensocial.server.manager.SSManager;
import com.ubhave.sensocial.server.manager.SensorListener;
import com.ubhave.sensocial.server.manager.Sensors;
import com.ubhave.sensocial.server.manager.Stream;
import com.ubhave.sensocial.server.manager.User;


public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		try {
//			MQTTManager m=new MQTTManager("abhinav");
//			m.connect();
//			m.publishToDevice("hello??");
//			m.subscribeToDevice();
//		} catch (MqttException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		SSManager sm=SSManager.getSSManager();
		ServerBoard frame = new ServerBoard();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

//    	System.out.println("NULL");
//
//		new FacebookEventNotifier().start();
        
		
	}
	
//	public static void startConfiguration(String configName){
//		//set the configuration attribute "sense"="true"
//		try
//		{
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			Document doc = docBuilder.newDocument();
//
//			File file = new File("filter.xml");
//			System.out.println("file found");
//			doc = docBuilder.parse(file); 
//			
//
//			doc.getDocumentElement().normalize();
//			if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("filter")){
//				System.out.println("filter node found");
//				NodeList nList = doc.getElementsByTagName("Configuration");
//				for (int temp=0;temp<nList.getLength();temp++) {
//					Node nNode = nList.item(temp);
//					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						System.out.println("configuration node found");
//						Element eElement = (Element) nNode;
//						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
//							eElement.setAttribute("sense", "true");
//							System.out.print("config found:"+eElement.getAttribute("name"));
//							//do we need to write it again?
//							TransformerFactory transformerFactory = TransformerFactory.newInstance();
//							Transformer transformer = transformerFactory.newTransformer();
//							DOMSource source = new DOMSource(doc);
//
//							StreamResult result =  new StreamResult("filter.xml");
//							transformer.transform(source, result);
//						}
//						else{
//							System.out.println("config not found: "+configName);
//						}
//					}
//				}
//				System.out.println("Start-configuration: Done");
//			}	
//			else{
//				System.out.println("Start-configuration: Filter not found. It is: "+doc.getDocumentElement().getNodeName());
//			}
//		} catch (Exception e) {
//			System.out.println("Filter-setting: "+e.toString());
//		}
//	}


}
