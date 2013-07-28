package com.ubhave.sensocial.privacy;

import java.io.File;
import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class PPDParser {

	/**
	 * Returns whether the sensor is allowed for the location and with given granularity.
	 * @param ppdSensorName
	 * @param ppdLocation
	 * @param ppdDataType
	 * @return
	 */
	public static Boolean isAllowed(String ppdSensorName, String ppdLocation, String ppdDataType){
		try{
			Boolean flag=false;
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				PPDGenerator.createDefaultPPD();
				return true;
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			System.out.print("Getting PPD file from mobile device");
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			System.out.print("JSON array for PPD: "+ ppd);
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					System.out.println("Checking sensor: "+ppdSensorName);
					String dataType=((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase());
					if(ppdDataType.equalsIgnoreCase(PPDDataType.CLASSIFIED) && !dataType.equalsIgnoreCase(PPDDataType.NULL)){
						flag = true;
						break;
					}
					else if(((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase()).equalsIgnoreCase(ppdDataType)){
						flag = true;
						break;
					}
					else{
						System.out.println("PPD settings for: "+ppdSensorName+", at- "+ppdLocation+" is- "+ ((JSONObject)ppd.get(i)).getString(ppdLocation.toLowerCase()));
					}
				}
				else{
					System.out.println("Sensor: "+ppdSensorName+", not found!!");
				}
			}
			fis.close();
			return flag;			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
			return false;
		}
	}
	
	/**
	 * Returns PPD in JSON string format
	 * @return
	 */
	public static String getPPDJSONString(){
		try{
			File file=new File(Environment.getExternalStorageDirectory(), "ppd.txt");
			if(!file.exists()){
				PPDGenerator.createDefaultPPD();
			}
			FileInputStream fis=new FileInputStream(file);
			int content;
			String data="";
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			return data;			
		}
		catch(Exception e){
			Log.e("SNnMB", "Error caused by start-sensing in PPD file. "+e.toString());
			return "Error while parsing the file!!";
		}
	}
}
