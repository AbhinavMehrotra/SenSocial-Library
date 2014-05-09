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
package com.ubhave.sensocial.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FacebookConfiguration {

	SharedPreferences sp;

	public FacebookConfiguration(Context context){
		sp=context.getSharedPreferences("SSDATA",0);
	}
	
	/**
	 * Getter for Facebook subscription.
	 * @return Boolean True if user has subscribed to facebook else False.
	 */
	public Boolean getFacebook() {
		return sp.getBoolean("facebook", false);
	}
	
	/**
	 * 'Getter for Facebook app's client id
	 * @return String Facebook app's client id
	 */
	public String getFacebookClientId() {
		return sp.getString("fbClientId", "");
	}
	
	/**
	 * Getter for Facebook app's client secret id
	 * @return String Facebook app's client secret id
	 */
	public String getFacebookClientSecretId() {
		return sp.getString("fbClientSecretId", "");
	}

	/**
	 * Method to set configuration for Facebook  app
	 * @param clientId String Facebook app's client id
	 * @param clientSecretId String Facebook app's client secret id
	 */
	public void setFacebookId(String clientId, String clientSecretId) {
		Editor ed=sp.edit();
		ed.putBoolean("facebook", true);
		ed.putString("fbClientId", clientId);
		ed.putString("fbClientSecretId", clientSecretId);
		ed.commit();
	}

}
