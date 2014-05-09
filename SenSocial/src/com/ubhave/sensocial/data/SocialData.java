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
