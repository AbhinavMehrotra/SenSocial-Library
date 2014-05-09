/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
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

	
