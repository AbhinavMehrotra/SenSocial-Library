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
//
///**
// * To receive sensordata for each real time updates implement this listener. <br/>
// * To get these updates you should register your listener by using "registerSensorDataListener" 
// * method in SenSocialManager class.
// * @author Abhinav
// */
//public interface SensorDataListener {
//
//	/**
//	 * It will receive the update from social network, with all event classified by the default classifier.
//	 * @param message Message posted on the social-network by the user.
//	 * @param accelerometer Moving or Not moving
//	 * @param bluetooth Bluetooth names
//	 * @param location Latitude and Longitude
//	 * @param microphone Silent or Not
//	 */
//	public void onSNUpdateForAllEvents(String message, String accelerometer, String bluetooth,
//			String location, String microphone, String wifi);
//	
//	/**
//	 * It will receive the update from social network when user is moving(walking/running).
//	 * @param data Message posted on the social-network by the user.
//	 */
//	public void onSNUpdateWhenUserIsMoving(String message);
//	
//	
//	/**
//	 * It will receive the update from social network when user is not moving.
//	 * @param data Message posted on the social-network by the user.
//	 */
//	public void onSNUpdateWhenUserIsNotMoving(String message);
//	
//	/**
//	 * It will receive the update from social network when user is at silent place.
//	 * @param data Message posted on the social-network by the user.
//	 */
//	public void onSNUpdateWhenUserIsAtSilentPlace(String message);
//	
//	
//	/**
//	 * It will receive the update from social network when user is not at silent place.
//	 * @param data Message posted on the social-network by the user.
//	 */
//	public void onSNUpdateWhenUserIsNotAtSilentPlace(String message);
//	
//}
