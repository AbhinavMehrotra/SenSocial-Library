package com.ubhave.sensocial.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.content.Context;

import com.ubhave.sensocial.exceptions.PPDException;


public class Filter {

	private String filterName;
	private Context context;
	private ArrayList<Activity> activities;
	
	public Filter(String filterName, Context context){
		this.filterName=filterName;
		this.context=context;
	}
	
	public void addCondition(Activity activity){
		this.activities.add(activity);
	}
	
	public Stream requiredData(){ //can we give sensor name as arg, but then what methods should be placed in Stream class?
		Stream stream = new Stream(this);
		
		return stream;
	}
	
	/**
	 * 
	 * @return 
	 * @throws PPDException If there exists any activity of which the associated sensor 
	 * (or SensorData from this sensor on client) is not declared in PPD.
	 */
	public Boolean setFilter() throws PPDException{		//this can be in Stream? And what about deleting filter?
		Boolean FLAG=true;
		PrivacyPolicyDescriptorParser ppd= new PrivacyPolicyDescriptorParser(context);
		Set<String> sensors = new HashSet<String>();
		//check PPD for the sensors associated to activities 
		for(int i=0;i<this.activities.size();i++){			
			if(ppd.isAllowed(activities.get(i).getSensorName(), null, "classified")){
				throw new PPDException(activities.get(i).getSensorName()); 
				// shall I throw exception or just drop this filter by returning false ??
			}
			


			try {				
				String filepath = "res\raw\filter.xml";
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(filepath);
				
				// create config
				Node filter = doc.createAttribute(this.filterName);
		 
				doc.appendChild(filter);
				Node staff = doc.getElementsByTagName("staff").item(0);
				
				//create XML
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return FLAG; 
	}
	
	
	
	public Boolean deleteFilter(String filterName){
		Boolean FLAG=true;
		
		//do something
		
		return FLAG;
	}
	
}
