package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ubhave.sensocial.exceptions.PPDException;


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
