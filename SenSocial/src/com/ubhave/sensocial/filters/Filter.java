package com.ubhave.sensocial.filters;

import java.util.ArrayList;


public class Filter {

	//	private ArrayList<Modality> activities;
	//	
	//	public Filter(){
	//	}
	//
	//	
	//	public void addConditions(ArrayList<Modality> activity){
	//		this.activities=activity;
	//	}
	//	
	//	public void addLogicalOR(){
	//		this.activities.add(Modality.logicalOR);
	//	}
	//	
	//	public ArrayList<Modality> getConditions(){
	//		return activities;
	//	}

	ArrayList<Condition> conditions= new ArrayList<Condition>();

	public Filter(){
		//do nothing
	}

	public void addConditions(ArrayList<Condition> condition){
		this.conditions=condition;
	}

	public Filter(ArrayList<Condition> condotions){
		this.conditions=condotions;
	}

	public ArrayList<Condition> getConditions(){
		return this.conditions;
	}

}
