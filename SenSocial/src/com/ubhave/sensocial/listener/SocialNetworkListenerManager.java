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
//package com.ubhave.sensocial.listener;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//
//
//public class SocialNetworkListenerManager {
//	
//	private static ArrayList<SocialNetworkListner> listeners = new ArrayList<SocialNetworkListner>();
//
//	/**
//	 * Method to add an instance of  listener (SocialNetworkListner)
//	 * @param listener SocialNetworkPostListner object
//	 */
//	public static void add(SocialNetworkListner listener) {
//		listeners.add(listener);
//	}
//
//	/**
//	 * Method to remove an instance of  listener (SocialNetworkListner)
//	 * @param listener SocialNetworkPostListner object
//	 */
//	public static void remove(SocialNetworkListner listener) {
//		listeners.remove(listener);
//	}
//	
//	/**
//	 * Method used by the service class to push the data received by the service from server.
//	 * It only be invoked by the service class. <br/>
//	 * It checks for the first two characters if these are 10 then it is from Facebook else 11 means it is from Twitter.
//	 * @param msg Data received by the service class from the server
//	 */
//	public static void newUpdateArrived(Context context, String data){
//		String snName = null, message = null;
//		//logic for data to classify between twitter/facebook
//		if(data.startsWith("10")){
//			snName="FACEBOOK";
//		}
//		else{
//			snName="TWITTER";
//		}
//		message=data.substring(2);
//		fireUpdate(snName, message);
//	}
//
//	/**
//	 * Method to fire social-network updates to all the SocialNetworPostkListener(s).
//	 * @param socialNetworkName  Name of the social-network from which an update has been received.
//	 * @param message Message posted on the social-network by the user.
//	 */
//	private static void fireUpdate(String socialNetworkName, String message) {
//		for (SocialNetworkListner listener:listeners) {
//			if(socialNetworkName.equals("FACEBOOK")){
//				listener.onFacebookPostReceived(message);
//			}
//			else{
//				listener.onTwitterPostReceived(message);
//			}
//		}
//	}
//}
