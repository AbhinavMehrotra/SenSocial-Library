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
//package com.ubhave.sensocial.configuration;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.ubhave.sensocial.exceptions.ServerException;
//
//public class MQTTServerConfiguration {
//	
//	SharedPreferences sp;
//	private String ip;
//
//	public MQTTServerConfiguration(Context context) {
//		sp=context.getSharedPreferences("SSDATA",0);
//	}
//
//	/**
//	 * Method to set the server IP where the MQTT broker is running. <br/>
//	 * This URL should be the path of the server where all the PHP scripts are hosted. <br/>
//	 * It should not end with "/".
//	 * @param url String
//	 * @throws ServerException
//	 */
//	public void setMQTTServerIP(String IP) throws ServerException{
//		if(IP.endsWith("/")){
//			throw new ServerException(IP+" is not a valid URL. \nURL should not contain / at the end, eg- https//:cs.bham.ac.uk or 10.101.10.101");
//		}
//		else{
//			this.ip=IP;
//		}
//	}
//	
//	/**
//	 * Method to get the server IP where the MQTT broker is running.
//	 * @return String
//	 */
//	public String getMQTTServerIP(){
//		return ip;
//	}
//	
//	
//}
