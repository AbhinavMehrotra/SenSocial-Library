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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSOND3FileWritter {


	public static void insertData(String message, String context, String time, double latitude, double longitude){
		try {
			File file=new File("D3Data.json");
			Boolean flag=file.createNewFile();
			if(flag){
				//new file created, so add the root 
				try {
					JSONObject obj=  new JSONObject();
					obj.put("name", "SenSocial");
					obj.put("value", 100);
					JSONArray arr=new JSONArray();
					obj.put("children", arr);
					FileWriter f= new FileWriter("D3Data.json");
					f.write(obj.toString());
					f.flush();
					f.close();
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
			//read the file
			BufferedReader br=null;
			String str="";
			try {	 
				String sCurrentLine;
				br = new BufferedReader(new FileReader("D3Data.json"));
				while ((sCurrentLine = br.readLine()) != null) {
					str+=sCurrentLine;
				}	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			//append child to json array
			try {		 
				JSONObject json = new JSONObject(str);
				JSONArray children=json.getJSONArray("children");
				JSONObject child=new JSONObject();
				child.put("message", message);
				child.put("value", 25);
				child.put("modality", context);
				child.put("latitude", latitude);
				child.put("longitude", longitude);
				children.put(child);
				
				FileWriter f= new FileWriter("D3Data.json");
				f.write(json.toString());
				f.flush();
				f.close();
				
		 	} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
