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
