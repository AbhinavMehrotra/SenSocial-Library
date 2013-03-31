package com.ubhave.sensocial.sensordata.classifier;

import java.util.Arrays;

import android.util.Log;

public class AnalyzeMicrophoneData {

	private static final String TAG = "SNnMB";

	protected AnalyzeMicrophoneData(){}
	
	public Boolean isSpeaking(double varianceLevel, double[] arr){
		Boolean flag=false;
		double min,max; 
		Arrays.sort(arr);
		min=arr[0];
		max=arr[arr.length-1];
		if(max-min>varianceLevel){
			flag=true;
		}else{
			flag=false;
		}
		Log.e(TAG, "Microphone min and max values:"+ min+","+max);
		Log.e(TAG, "isSpeaking:"+ flag);
		return flag;	
	}
	
}
