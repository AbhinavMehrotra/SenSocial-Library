package com.ubhave.sensocial.server.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.filters.ServerFilterParser;
import com.ubhave.sensocial.server.manager.AggregatorRegistrar;
import com.ubhave.sensocial.server.manager.SensorListenerManager;

public class StreamReceiver {
	public static void onReceive(String userId, String deviceId, String socialEventString){
		try {
			JSONObject obj=new JSONObject(socialEventString);
			String streamId=obj.getString("streamid");
			
			if(AggregatorRegistrar.isAggregated(streamId)){
				streamId=AggregatorRegistrar.getAggregatedStreamId(streamId);
				obj.put("streamid", streamId);
			}
			SocialEvent socialEvent= SocialEvent.getSocialEvent(obj);	
			
			if(ServerFilterParser.isPresent(socialEvent)){
				System.out.println("Stream present is server filter");
				ServerFilterParser.handleData(socialEvent);
			}
			else{
				System.out.println("Stream present is server filter");
				SensorListenerManager.fireUpdate(socialEvent);				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
//SensorData rawData, String classifiedData, String streamId, String deviceId,
//String oSNFeed, String oSNName, String feedType