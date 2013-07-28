package com.ubhave.sensocial.filters;

import java.util.ArrayList;

/**
 * This class is used for register all streams requested by server  <Not used now: remove dependency>
 *
 */
public class ServerStreamRegistrar {

	private static ArrayList<String> streamIds=new ArrayList<String>();

	public static void addStreamId(String streamId){
		streamIds.add(streamId);
	}

	public static void removeStreamId(String streamId){
		streamIds.remove(streamId);
	}

	public static Boolean isServerStream(String streamId){
		return streamIds.contains(streamId);
	}

	public static ArrayList<String> getAllServerStreamIds(String streamId){
		return streamIds;
	}
}
