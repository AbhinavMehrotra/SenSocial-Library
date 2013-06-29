package com.ubhave.sensocial.server.manager;

public enum UserRelation {
	facebook_friends("facebook_friends"),
	twitter_followers("twitter_followers"),
	people_near_the_user("people_near_the_user");
	
	private final String relation;
	
	UserRelation(String message){
		this.relation=message;
	}
	
	public String getMessage(){
		return relation;
	}

}
