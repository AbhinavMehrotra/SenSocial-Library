package com.sensocial.mqtt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.simple.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;

import com.sensocial.configuration.ServerConfiguration;
import com.sensocial.sensormanager.StartPullSensors;
import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.csv.CSVFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;


public class PushCallback implements MqttCallback {

	final private String TAG = "SNnMB";
    private ContextWrapper context;
    private String BROKER_URL;
	public String clientId;                  
    public static final String TOPIC = "sensocial";
    private MqttClient mqttClient;

    public PushCallback(ContextWrapper context, String clientId) {
        this.context = context;
        ServerConfiguration sc= new ServerConfiguration(context);
		BROKER_URL= "tcp://"+sc.getMQTTServerIP()+":1883";
		this.clientId=clientId;
    }

    public void connectionLost(Throwable cause) {
    	Log.d(TAG,"Connection lost. Reason: "+ cause);
    }

    /**
     * This method is called when the message is arrived. 
     * It starts sensing process.
     */
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
    	if(sp.getBoolean("sensing", false)==false){
			Log.d(TAG, "Found a new update");
			Editor ed=sp.edit();
			ed.putBoolean("sensing", true);
			ed.commit();
			StartPullSensors sps=new StartPullSensors(context);
			sps.StartSensing();
		}
    }

    public void deliveryComplete(MqttDeliveryToken token) {
        //We do not need this because we do not publish    	
    }
    
}
