package com.ubhave.sensocial.sensordata.classifier;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensocial.manager.Location;
import com.ubhave.sensocial.sensormanager.AllPullSensors;
import com.ubhave.sensormanager.data.SensorData;

public class DummyClassifier {

	public static String classifyData(SensorData data){
		String result = "";
		JSONFormatter formatter = DataFormatter.getJSONFormatter(data.getSensorType());
		String str=formatter.toJSON(data).toJSONString();
		if(data.getSensorType()==AllPullSensors.SENSOR_TYPE_ACCELEROMETER)
			result=classifyAccelerometer(str);
		if(data.getSensorType()==AllPullSensors.SENSOR_TYPE_MICROPHONE)
			result=classifyMicrophone(str);
		return result;
	}

	private static String classifyMicrophone(String str){
		ArrayList<Double> ar =new ArrayList<Double>();
		String temp;
		double mean=0.0;
		double MEAN=6057, SD=5323;
		int count=0;
		try {
			JSONObject obj=new JSONObject(str);
			temp=obj.getString("amplitude");

			String[] tempAr=temp.split(",");
			for(int i=0;i<tempAr.length;i++){
				ar.add(Double.parseDouble(tempAr[i]));
				mean+=Double.parseDouble(tempAr[i]);
			}
			mean=mean/tempAr.length;
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


	public static double calculateDistanceInMiles(Location StartP, Location EndP) {  
		//Haversine formula-wiki used by google
//		double Radius=6371; //kms
				double Radius=3963.1676; //miles
		double lat1 = StartP.getLatitude();  
		double lat2 = EndP.getLatitude();  
		double lon1 = StartP.getLongitude();  
		double lon2 = EndP.getLongitude(); 
		//		double lat1 = StartP.getLatitude()/1E6;  
		//		double lat2 = EndP.getLatitude()/1E6;  
		//		double lon1 = StartP.getLongitude()/1E6;  
		//		double lon2 = EndP.getLongitude()/1E6;  
		double dLat = Math.toRadians(lat2-lat1);  
		double dLon = Math.toRadians(lon2-lon1);  
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
				Math.sin(dLon/2) * Math.sin(dLon/2);  
		double c = 2 * Math.asin(Math.sqrt(a));  
		return Radius * c;  
	} 
	
	//bluetoothmac
	public static Boolean isBluetoothPresent(Set<String> macs, String mac) {  
		if(macs.contains(mac))
			return true;
		else
			return false;
	}
}
