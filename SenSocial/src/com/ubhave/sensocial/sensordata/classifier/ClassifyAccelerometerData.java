package com.ubhave.sensocial.sensordata.classifier;

import android.util.Log;


public class ClassifyAccelerometerData {
	
	private String TAG="SNnMB";
	protected ClassifyAccelerometerData(){}

	public Boolean isMoving(double varianceLevel, double[] xaxis, double[] yaxis){		
		return (isMoving(varianceLevel,xaxis) && isMoving(varianceLevel,yaxis));		
	}
	
	private Boolean isMoving(double varianceLevel, double[] arr){
		Boolean flag=false;
		int l=arr.length, i=0;
		double min,max;  	
		min=arr[l/2]-varianceLevel;
		max=arr[l/2]+varianceLevel;		
		while(i<l){
			if(arr[i]>max || arr[i]<min){
				flag=true;
				Log.e(TAG, "isMoving:"+ flag);
				break;
			}
			i++;
		}
		Log.e(TAG, "isMoving:"+ flag);
		return flag;	
	}
	
	
	
	
	
}
