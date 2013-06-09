package com.ubhave.sensocial.server.filters;

import java.util.ArrayList;

public class Filter {

	private ArrayList<Modality> activities;
	
	public Filter(){
	}
	
	public void addConditions(ArrayList<Modality> activity){
		this.activities=activity;
	}
	
	public void addLogicalOR(){
		this.activities.add(Modality.logicalOR);
	}
	
	public ArrayList<Modality> getConditions(){
		return activities;
	}
	
	public Boolean deleteFilter(String filterName){
		Boolean FLAG=true;
		
		//do something
		
		return FLAG;
	}
}

	
