package com.ubhave.sensocial.filters;

import java.util.ArrayList;


public class Filter {
	
	ArrayList<Condition> conditions= new ArrayList<Condition>();

	public Filter(){
		//do nothing
	}

	/**
	 * Adds given list of conditions to the filter.
	 * @param ArrayList of Condition
	 */
	public void addConditions(ArrayList<Condition> condition){
		this.conditions=condition;
	}

	/**
	 * Constructor
	 * @param ArrayList of Condition
	 */
	public Filter(ArrayList<Condition> condotions){
		this.conditions=condotions;
	}

	/**
	 * Returns the list of condition in the filter
	 * @return ArrayList of Condition
	 */
	public ArrayList<Condition> getConditions(){
		return this.conditions;
	}

}
