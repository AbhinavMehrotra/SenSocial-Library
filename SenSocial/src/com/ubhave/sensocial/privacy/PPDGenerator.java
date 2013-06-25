package com.ubhave.sensocial.privacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class PPDGenerator {


	//we can create PPD via java code
	public static void createDefaultPPD(){
		try{
			String server="raw";
			String client="raw";
			JSONObject accelerometer=new JSONObject();
			accelerometer.put("name", "accelerometer");
			accelerometer.put("server", server);
			accelerometer.put("client", client);
			JSONObject microphone=new JSONObject();
			microphone.put("name", "microphone");
			microphone.put("server", server);
			microphone.put("client", client);
			JSONObject wifi=new JSONObject();
			wifi.put("name", "wifi");
			wifi.put("server", server);
			wifi.put("client", client);
			JSONObject location=new JSONObject();
			location.put("name", "location");
			location.put("server", server);
			location.put("client", client);
			JSONObject bluetooth=new JSONObject();
			bluetooth.put("name", "bluetooth");
			bluetooth.put("server", server);
			bluetooth.put("client", client);

			JSONArray ppd=new JSONArray();
			ppd.put(accelerometer);
			ppd.put(microphone);
			ppd.put(wifi);
			ppd.put(location);
			ppd.put(bluetooth);	

			JSONObject obj=new JSONObject();
			obj.put("ppd",ppd);
			String str=obj.toString();

			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(str);
			out.close();
			os.close();

		}
		catch(Exception e){
			Log.e("SNnMB", "Error while creating a PPD file. "+e.toString());
		}
	}

	public static void startSensing(String ppdSensorName, String ppdLocation, String ppdDataType){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					((JSONObject)ppd.get(i)).put(ppdLocation, ppdDataType);
					break;
				}
			}
			fis.close();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(obj.toString());
			out.close();
			os.close();		
			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
		}

	}

	public static void stopSensing(String ppdSensorName, String ppdLocation){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					((JSONObject)ppd.get(i)).put(ppdLocation, PPDDataType.NULL);
					break;
				}
			}
			fis.close();
			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(obj.toString());
			out.close();
			os.close();		
			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
		}
	}

}

//{'ppd':[{'name':'accelerometer', 'server':'raw', 'client':'raw'}, .....]}
