package com.ubhave.sensocial.server.filters;

import java.util.ArrayList;

public class Filter {

//	private ArrayList<Modality> activities;
//	
//	public Filter(){
//	}
//	
//	public void addConditions(ArrayList<Modality> activity){
//		this.activities=activity;
//	}
//	
//	
//	public ArrayList<Modality> getConditions(){
//		return activities;
//	}
//	
//	public Boolean deleteFilter(String filterName){
//		Boolean FLAG=true;
//		
//		//do something
//		
//		return FLAG;
//	}
	
	ArrayList<Condition> conditions= new ArrayList<Condition>();
	
	public Filter(){
		//do nothing
	}
	
	/**
	 * Adds List of conditions in the filter
	 * @param condition (ArrayList) {@link Condition}
	 */
	public void addConditions(ArrayList<Condition> condition){
		this.conditions=condition;
	}
		
	/**
	 * Constructor
	 * @param condition (ArrayList) {@link Condition}
	 */
	public Filter(ArrayList<Condition> condotions){
		this.conditions=condotions;
	}
	
	/**
	 * Getter for the list of conditions
	 * @return (ArrayList) {@link Condition}
	 */
	public ArrayList<Condition> getConditions(){
		return this.conditions;
	}
}

	
