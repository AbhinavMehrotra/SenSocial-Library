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
package com.ubhave.sensocial.server.configuration;

/**
 * ConfigurationSettings class provides methods to set the configurations for MQTT, Mongodb etc.
 */
public class ConfigurationSettings {

	private static String mqttBrokerUrl;
	private static String tcpIP;
	private static String tcpPort;
	private static String mongoDBIP;
	
	/**
	 * @return the mqttBrokerUrl
	 */
	public static String getMqttBrokerUrl() {
		return mqttBrokerUrl;
	}
	/**
	 * @param mqttBrokerUrl the mqttBrokerUrl to set
	 */
	public static void setMqttBrokerUrl(String mqttBrokerUrl) {
		ConfigurationSettings.mqttBrokerUrl = mqttBrokerUrl;
	}
	/**
	 * @return the tcpIP
	 */
	public static String getTcpIP() {
		return tcpIP;
	}
	/**
	 * @param tcpIP the tcpIP to set
	 */
	public static void setTcpIP(String tcpIP) {
		ConfigurationSettings.tcpIP = tcpIP;
	}
	/**
	 * @return the tcpPort
	 */
	public static String getTcpPort() {
		return tcpPort;
	}
	/**
	 * @param tcpPort the tcpPort to set
	 */
	public static void setTcpPort(String tcpPort) {
		ConfigurationSettings.tcpPort = tcpPort;
	}
	/**
	 * @return the mongoDBIP
	 */
	public static String getMongoDBIP() {
		return mongoDBIP;
	}
	/**
	 * @param mongoDBIP the mongoDBIP to set
	 */
	public static void setMongoDBIP(String mongoDBIP) {
		ConfigurationSettings.mongoDBIP = mongoDBIP;
	}

		
	

	
	
}
