import javax.swing.JFrame;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.ubhave.sensocial.server.manager.SSManager;
import com.ubhave.sensocial.server.manager.User;
import com.ubhave.sensocial.server.mqtt.MQTTManager;
import com.ubhave.sensocial.server.osn.FacebookEventNotifier;


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
		
//		ServerBoard frame = new ServerBoard();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

    	System.out.println("NULL");

//		new FacebookEventNotifier().start();
        SSManager sm=SSManager.getSSManager();
        for(User u:sm.getAllUsers()){
        	System.out.println("test:"+u.getId());
        }
        if(sm.getAllUsers()==null){
        	System.out.println("NULL");
        }
        
	}

}
