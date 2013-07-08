package com.ubhave.sensocial.sensordata.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.manager.Location;
import com.ubhave.sensocial.manager.SenSocialManager;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.data.SensorData;

public class DummyClassifier {
	public static String getClassifiedData(SensorData data){
		String str=null;
		if(data.getSensorType()!=AllPullSensors.SENSOR_TYPE_ACCELEROMETER && data.getSensorType()!=AllPullSensors.SENSOR_TYPE_MICROPHONE){
			str="Data from this sensor cannot be classified"; 
		}
		else{
			str=classifyData(data);
		}

		return str;
	}

	private static String classifyData(SensorData data){
		String result = "";
		JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), data.getSensorType());
		String str=formatter.toJSON(data).toString();
		Log.i("SNnMB", "Data(string) to be classified: "+str);
		if(data.getSensorType()==AllPullSensors.SENSOR_TYPE_ACCELEROMETER)
			result=classifyAccelerometer(str);
		if(data.getSensorType()==AllPullSensors.SENSOR_TYPE_MICROPHONE)
			result=classifyMicrophone(str);
		return result;
	}

	public static Boolean isSatisfied(ArrayList<SensorData> data,String sensorName, String operator,String value){
		if(sensorName.equalsIgnoreCase("null"))
			return true;
		return classifyWithModality(data, sensorName, operator, value);
	}

	private static SensorData getData(ArrayList<SensorData> data, int sensorId){
		for(SensorData s:data){
			if(s.getSensorType()==sensorId){
				return s;
			}
		}
		return null;
	}

	private static Boolean classifyWithModality(ArrayList<SensorData> data, String sensorName, String operator,String value){
		SensorData acc=getData(data, AllPullSensors.SENSOR_TYPE_ACCELEROMETER);
		SensorData mic=getData(data, AllPullSensors.SENSOR_TYPE_MICROPHONE);
		SensorData wifi=getData(data, AllPullSensors.SENSOR_TYPE_WIFI);
		SensorData bt=getData(data, AllPullSensors.SENSOR_TYPE_BLUETOOTH);
		SensorData loc=getData(data, AllPullSensors.SENSOR_TYPE_LOCATION);
		Boolean isTrue=false;
		if(sensorName.equals("accelerometer")){
			String activity= value;
			if(classifyData(acc).equalsIgnoreCase(activity)){
				return true;
			}
			return false;
		}
		else if(sensorName.equals("microphone")){
			String sound= value;
			if(classifyData(mic).equalsIgnoreCase(sound)){
				return true;
			}
			return false;
		}
		else if(sensorName.equals("bluetooth")){
			System.out.println("Modal-Value: "+value);
			String string=value.substring("neighbour_".length());
			System.out.println("Looking for bluetooth MAC: "+string);
			return isBluetoothPresent(bt, string);
		}
		else if(sensorName.equals("location")){
			try{
				//String str="latitude_"+location.getLatitude()+"_longitude_"+location.getLongitude()+"_range_"+rangeInMiles;
				String str=value.substring(9);
				String lat=str.substring(0, str.indexOf("_"));
				str=str.substring(str.indexOf("_")+1);
				str=str.substring(str.indexOf("_")+1);
				String lon=str.substring(0, str.indexOf("_"));
				str=str.substring(str.indexOf("_")+1);
				str=str.substring(str.indexOf("_")+1);			
				String range=str;				
				JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), loc.getSensorType());
				String currentLoc=formatter.toJSON(loc).toString();
				System.out.print("Sensed location string:"+currentLoc);
				JSONObject obj=new JSONObject(currentLoc);			
				Location l1=new Location(Double.parseDouble(lat),Double.parseDouble(lon));
				Location l2=new Location(obj.getDouble("latitude"),obj.getDouble("longitude"));
				System.out.print("Sensed location:"+ l2.getLatitude()+","+l2.getLongitude());
				if(Double.parseDouble(range)>calculateDistanceInMiles(l1, l2)){
					System.out.println("Range "+range+", Distance: "+ calculateDistanceInMiles(l1, l2));
					return true;
				}
				return false;
			}
			catch(Exception e){
				Log.e("SNnMB", "Error"+e.toString());
			}
		}
		else if(sensorName.equals("wifi")){


		}
		return isTrue;
	}





	private static String classifyMicrophone(String str){
		ArrayList<Double> ar =new ArrayList<Double>();
		String temp;
		double mean=0.0;
		double MEAN=6057, SD=5323;
		int count=0;
		try {
			JSONObject obj=new JSONObject(str);
//			temp=obj.getString("amplitude");
//			Log.e("SNnMB", "1 Microphone Amplitude: "+ temp);
//			temp.replace('[', '0');
//			temp.replace(']', '0');
//			Log.e("SNnMB", "2 Microphone Amplitude: "+ temp);
//			String[] tempAr=temp.split(",");
			
//			for(int i=0;i<tempAr.length;i++){
//				ar.add(Double.parseDouble(tempAr[i]));
//				mean+=Double.parseDouble(tempAr[i]);
//			}
//			mean=mean/tempAr.length;
			
			JSONArray tempAr=obj.getJSONArray("amplitude");
			Log.e("SNnMB", "Microphone Amplitude: "+ tempAr);
			for(int i=0;i<tempAr.length();i++){
				ar.add(Double.parseDouble(tempAr.getString(i)));
				mean+=Double.parseDouble(tempAr.getString(i));
			}
			mean=mean/tempAr.length();
			if(mean > MEAN+1000){
				System.out.println("Talking");
				return "talking";
			}

			System.out.println("mean: "+mean);

			for(double d:ar){
				if(d>mean+1000+SD){
					count++;
				}
				else{
					count=0;
				}
				if(count>=5){
					System.out.println("Talking");	
					return "talking";				
				}
			}
			System.out.println("Silent");	
			return "silent";	

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "error";
	}


	private static String classifyAccelerometer(String str){
		final double sittingMeanX=-0.287153537028424;
		final double sittingMeanY=-0.617490947002584;
		final double sittingMeanZ=9.825690578811388;
		final double sittingSDX=0.03375924640762579;
		final double sittingSDY=0.03393656221897022;
		final double sittingSDZ=0.03841632568708566;
		final double standingMeanX=-6.081962846233765;
		final double standingMeanY=-0.40719533187792234;
		final double standingMeanZ=8.288671538961028;
		final double standingSDX=0.3278253053843241;
		final double standingSDY=0.37105022703594853;
		final double standingSDZ=0.17450479643656341;
		final double movingMeanX=-10.596419360439276;
		final double movingMeanY=-3.6381814051705454;
		final double movingMeanZ=3.558113458444448;
		final double movingSDX=3.580298938417277;
		final double movingSDY=2.1721840121922487;
		final double movingSDZ=2.243726150548793;	
		double meanx=0, meany=0, meanz=0, sdx=0, sdy=0, sdz=0;
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray x=obj.getJSONArray("xAxis");
			JSONArray y=obj.getJSONArray("yAxis");
			JSONArray z=obj.getJSONArray("zAxis");

			for(int i=0;i<x.length();i++){
				meanx+=x.getDouble(i);
			}

			for(int i=0;i<y.length();i++){
				meany+=y.getDouble(i);
			}

			for(int i=0;i<z.length();i++){
				meanz+=z.getDouble(i);
			}
			meanx=meanx/x.length();
			meany=meany/y.length();
			meanz=meanz/z.length();

			for(int i=0;i<x.length();i++){
				sdx+=Math.pow((x.getDouble(i)-meanx),2);
			}

			for(int i=0;i<y.length();i++){
				sdy+=Math.pow((y.getDouble(i)-meany),2);
			}

			for(int i=0;i<z.length();i++){
				sdz+=Math.pow((z.getDouble(i)-meanz),2);
			}

			sdx=Math.sqrt(sdx/(x.length()-1));
			sdy=Math.sqrt(sdy/(y.length()-1));
			sdz=Math.sqrt(sdz/(z.length()-1));

			if(sdx>Math.floor(movingSDX) || sdy>Math.floor(movingSDY) || sdz>Math.floor(movingSDZ)){
				System.out.println("Moving");
				return "moving";
			}
			else{
				System.out.println("Not Moving");
				return "not_moving";
			}



		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "error";
	}


	private static double calculateDistanceInMiles(Location StartP, Location EndP) {  
		//Haversine formula-wiki used by google
		//		double Radius=6371; //kms
		double Radius=3963.1676; //miles
		double lat1 = StartP.getLatitude();  
		double lat2 = EndP.getLatitude();  
		double lon1 = StartP.getLongitude();  
		double lon2 = EndP.getLongitude();  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
				Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));  
		return Radius * c;  
	} 

	//bluetoothmac
	private static Boolean isBluetoothPresent(SensorData data, String mac) { 
		try{
		JSONFormatter formatter = DataFormatter.getJSONFormatter(SenSocialManager.getContext(), data.getSensorType());
		String json_string=formatter.toJSON(data).toString();
		JSONObject obj=new JSONObject(json_string);
		JSONArray devices= obj.getJSONArray("devices");
		Set<String> macs=new HashSet<String>();
		Set<String> names=new HashSet<String>();
		for(int i=0;i<devices.length();i++){
			JSONObject temp=new JSONObject(devices.getString(i));
			macs.add(temp.getString("address"));
			names.add(temp.getString("name"));
		}
		if(macs.contains(mac) || names.contains(mac))
			return true;
		else
			return false;
		}
		catch(Exception e){
			System.out.println("Error in isBLuetoothPresent: "+ e.toString());
		}
		return false;
	}
}
