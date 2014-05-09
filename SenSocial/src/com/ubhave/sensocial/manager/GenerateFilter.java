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
package com.ubhave.sensocial.manager;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.os.Environment;

import com.ubhave.sensocial.filters.Condition;

/**
 * GenerateFilter class allows creation of filters for the streams, or aggregators.
 */
public class GenerateFilter {
	//used for not exposing
	protected GenerateFilter(){};

	/**
	 * Creates the filter in xml format.
	 * @param context
	 * @param conditions
	 * @param streamConfig
	 * @param sensorName
	 * @param sensorDataType
	 */
	protected static void createXML(Context context,ArrayList<Condition> conditions, String streamConfig, String sensorName, String sensorDataType){
		try
		{
			
			Element rootElement,mainRoot;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			File file = new File(Environment.getExternalStorageDirectory(), "filter.xml"); 
			if (file.exists() && file.length()!=0) 
			{ 
				System.out.println("file found");
				doc = docBuilder.parse(file); 
				mainRoot = doc.getDocumentElement(); 
			} 
			else 
			{ 
				file.createNewFile();
				mainRoot = doc.createElement("Filter");
				doc.appendChild(mainRoot); 
			} 

			rootElement = doc.createElement("Configuration");
			rootElement.setAttribute("name", streamConfig);
			rootElement.setAttribute("sense", "false");
			mainRoot.appendChild(rootElement);

			Element condition = doc.createElement("Condition");
			condition.setAttribute("name", "c1");
			rootElement.appendChild(condition);
			int i=2, j=1;
			Element activity;
			for(Condition c:conditions){

				if(!c.getOperator().equalsIgnoreCase("LogicalOR")){
					activity = doc.createElement("activity"+j++);
					activity.setAttribute("name", c.getConditionString());
					condition.appendChild(activity);
				}
				else{
					condition = doc.createElement("Condition");
					condition.setAttribute("name", "c"+i++);
					rootElement.appendChild(condition);
					j=1;
				}
			}

			Element reqData = doc.createElement("required_data");
			reqData.setAttribute("sensor", sensorName);
			reqData.setAttribute("location", "client");
			reqData.setAttribute("type", sensorDataType);
			rootElement.appendChild(reqData);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result =  new StreamResult(new File(Environment.getExternalStorageDirectory(), "filter.xml"));
			transformer.transform(source, result);

			System.out.println("GenerateFilter Done");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
