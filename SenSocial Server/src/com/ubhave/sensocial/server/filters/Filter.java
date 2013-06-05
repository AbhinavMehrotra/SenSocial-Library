package com.ubhave.sensocial.server.filters;

import java.util.ArrayList;

public class Filter {

	private String filterName;
	private ArrayList<Modality> activities;
	
	public Filter(String filterName){
		this.filterName=filterName;
	}
	
	public String getFilterName(){
		return this.filterName;
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

	
