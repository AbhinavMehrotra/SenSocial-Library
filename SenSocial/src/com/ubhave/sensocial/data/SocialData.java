package com.ubhave.sensocial.data;

public class SocialData {

	private String OSNFeed;
	
	private String OSNName;

	private String feedType;
	
	private String time;
	
	private String userName;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		try{
			this.time = SocialEvent.convertTime(Long.parseLong(time));
		}
		catch(Exception e){
			this.time=time;
		}
	}

	/**
	 * @return the oSNFeed
	 */
	public String getOSNFeed() {
		return OSNFeed;
	}

	/**
	 * @param oSNFeed the oSNFeed to set
	 */
	public void setOSNFeed(String oSNFeed) {
		this.OSNFeed = oSNFeed;
	}

	/**
	 * @return the oSNName
	 */
	public String getOSNName() {
		return OSNName;
	}

	/**
	 * @param oSNName the oSNName to set
	 */
	public void setOSNName(String oSNName) {
		this.OSNName = oSNName;
	}

	/**
	 * @return the feedType
	 */
	public String getFeedType() {
		return feedType;
	}

	/**
	 * @param feedType the feedType to set
	 */
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	
	
}
