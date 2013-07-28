package com.ubhave.sensocial.server.tcp;

import org.json.JSONException;
import org.json.JSONObject;

import com.ubhave.sensocial.server.data.SocialEvent;
import com.ubhave.sensocial.server.filters.ServerFilterParser;
import com.ubhave.sensocial.server.filters.ServerFilterRegistrar;
import com.ubhave.sensocial.server.manager.AggregatorRegistrar;
import com.ubhave.sensocial.server.manager.SSListenerManager;

public class StreamReceiver {
	public static void onReceive(String userId, String deviceId, String socialEventString){
		try {
			JSONObject obj=new JSONObject(socialEventString);
			String streamId=obj.getString("streamid");
			
			
			SocialEvent socialEvent= SocialEvent.getSocialEvent(obj);	
			SSListenerManager.fireUpdate(socialEvent);	
			
			if(AggregatorRegistrar.isAggregated(streamId)){				
				streamId=AggregatorRegistrar.getAggregatedStreamId(streamId);
				System.out.println("This is aggregated stream: "+streamId);
				obj.put("streamid", streamId);
				socialEvent= SocialEvent.getSocialEvent(obj);
				SSListenerManager.fireUpdate(socialEvent);	
				
				if(ServerFilterRegistrar.isPresent(streamId)){
					System.out.println("Stream present in server filter");
					String id=ServerFilterRegistrar.getStreamId(streamId);
					if(ServerFilterParser.isSatisfied(id, socialEvent)){
						obj.put("streamid", id);
						socialEvent= SocialEvent.getSocialEvent(obj);
						SSListenerManager.fireUpdate(socialEvent);
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
//SensorData rawData, String classifiedData, String streamId, String deviceId,
//String oSNFeed, String oSNName, String feedType