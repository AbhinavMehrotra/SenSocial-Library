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
//package com.ubhave.sensocial.http;
//
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.net.URLConnection;
//
//import org.apache.http.util.ByteArrayBuffer;
//
//import android.util.Log;
//
//public class CheckUpdate {
//	
//	final private String TAG = "SNnMB";
//	private String serverUrl;
//
//	protected CheckUpdate(String serverUrl){
//		this.serverUrl=serverUrl;
//	}
//	
//	/**
//	 * Method to check the trigger from SN. <br/>
//	 * It checks the server for any new update.
//	 * @return String '1' for new update, else '0'. 
//	 */
//	public String checkNow(){
//		String myString="0";
//		try{
//            URL myURL = new URL(serverUrl+"checktrigger1.php");   
//            URLConnection ucon = myURL.openConnection();
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//            ByteArrayBuffer baf = new ByteArrayBuffer(50);
//            int current = 0;
//            while((current=bis.read())!=-1){
//                baf.append((byte)current);
//            }
//            myString = new String (baf.toByteArray());
//            return myString;
//        }catch(Exception e){
//    		Log.e(TAG, "Error while checking for update!!\n"+e.toString());
//        }
//		return myString;
//	}
//}
