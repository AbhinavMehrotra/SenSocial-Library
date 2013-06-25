import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ubhave.sensocial.server.data.SensorData;
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
 
 
public class ServerBoard extends JFrame {
    private JTextArea messagesArea;
    private JButton sendButton;
    private JTextField message;
    private JButton startServer;
 
    public ServerBoard() {
 
        super("ServerBoard");
 
        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields,BoxLayout.X_AXIS));
 
        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2,BoxLayout.X_AXIS));
 
        //here we will have the text messages screen
        messagesArea = new JTextArea();
        messagesArea.setColumns(30);
        messagesArea.setRows(10);
        messagesArea.setEditable(false);
 
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                // get the message from the text view
//                String messageText = message.getText();
//                // add message to the message area
//                messagesArea.append("\n" + messageText);
//                // send the message to the client
//
//                MQTTManager m;
//				try {
//					m = new MQTTManager("abhinav");
//	    			m.connect();
//	    			m.publishToDevice("hello??");
//	    			m.subscribeToDevice();
//				} catch (MqttException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//                // clear text
//                message.setText("");
            	
            	SSManager sm=SSManager.getSSManager();
                for(User u:sm.getAllUsers()){
                	System.out.println("User:"+u.getId());
                	ArrayList<Device> devices=u.getDevices();
                	for(Device d:devices){
                    	System.out.println("Device:"+d.getDeviceId());
                		try {
        					Stream stream=d.getStream(Sensors.SENSOR_TYPE_WIFI, "raw");
        					Filter filter=new Filter();
        					ArrayList<Modality> activity=new ArrayList<Modality>();
        					activity.add(Modality.Not_Moving);
        					filter.addConditions(activity);
        					Stream s=stream.setFilter(filter);
        					SensorListener l=new SensorListener() {
        						
        						public void onDataReceived(SocialEvent socialEvent) {
        							System.out.println("Social Event received");
        							System.out.println("Stream id: "+socialEvent.getFilteredSensorData().getStreamId());
        							System.out.println("Raw data: "+socialEvent.getFilteredSensorData().getRawData());
        							SensorData d=socialEvent.getFilteredSensorData().getRawData();
        							
        							
        						}
        					};
        					sm.registerListener(l, s.getStreamId());
        					s.startStream();
        					
        				} catch (PPDException | SensorDataTypeException | XMLFileException e1) {
        					e1.printStackTrace();
        				}
                	}
                }
                if(sm.getAllUsers()==null){
                	System.out.println("NULL");
                }
        		
            }
        });
 
        startServer = new JButton("Start");
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable the start button
                startServer.setEnabled(false);
 
            }
        });
 
        //the box where the user enters the text (EditText is called in Android)
        message = new JTextField();
        message.setSize(200, 20);
 
        //add the buttons and the text fields to the panel
        panelFields.add(messagesArea);
        panelFields.add(startServer);
 
        panelFields2.add(message);
        panelFields2.add(sendButton);
 
        getContentPane().add(panelFields);
        getContentPane().add(panelFields2);
 
 
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
 
        setSize(300, 170);
        setVisible(true);
    }
}