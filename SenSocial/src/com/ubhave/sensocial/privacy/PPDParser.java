package com.ubhave.sensocial.privacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class PPDParser {

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
			while ((content = fis.read()) != -1) {
				System.out.print((char) content);
				data+=(char)content;
			}
			JSONObject obj=new JSONObject(data);
			JSONArray ppd=obj.getJSONArray("ppd");
			for(int i=0; i<ppd.length();i++){
				if(((JSONObject)ppd.get(i)).getString("name").equals(ppdSensorName)){
					if(((JSONObject)ppd.get(i)).getString(ppdLocation).equals(ppdDataType)){
						flag = true;
						break;
					}
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
