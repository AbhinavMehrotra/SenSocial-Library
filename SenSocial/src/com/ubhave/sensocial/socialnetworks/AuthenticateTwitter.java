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
package com.ubhave.sensocial.socialnetworks;

import com.ubhave.sensocial.tcp.ClientServerCommunicator;

import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/**
 * Provides the methods to authenticate user with their Twitter account.
 */
public class AuthenticateTwitter {

	private final String consumerKey, consumerKeySecret, callbackURL;
	private final String Tag="SNnMB";
	private Context context;
	private CommonsHttpOAuthConsumer consumer;
	private DefaultOAuthProvider provider;
	private Twitter twitter;
	private SharedPreferences sp;
	private User user;
	private static AuthenticateTwitter instance;
	
	public static AuthenticateTwitter getInstance(Context context, String consumerKey, String consumerKeySecret){
		if(instance==null){
			instance=new AuthenticateTwitter(context, consumerKey, consumerKeySecret);
		}
		return instance;
	}
	
	private AuthenticateTwitter(Context context, String consumerKey, String consumerKeySecret) {
		this.context=context;
		sp=context.getSharedPreferences("SSDATA",0);
		this.consumerKey=consumerKey;
		this.consumerKeySecret=consumerKeySecret;
		this.callbackURL="callback://tweetactivity";
	}

	/**
	 * This method should be called inside the Twitter-Login button's onClickListner. <br>
	 * It authenticates the user via Twitter.
	 * @param currentActivity Activity in which it is called.
	 */
	public void tryLogin(Activity currentActivity) {
		Log.d(Tag, "tryLogin");
		StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerKeySecret);
		provider = new DefaultOAuthProvider("https://twitter.com/oauth/request_token",
				"https://twitter.com/oauth/access_token", "https://twitter.com/oauth/authorize");
		try {
			String oAuthURL = provider.retrieveRequestToken(consumer, callbackURL);
			Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(oAuthURL));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} catch (OAuthMessageSignerException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		StrictMode.setThreadPolicy(old);
	}
	
	/**
	 * This method should be inside onNewIntent. <br/>
	 * Remember to create onNewIntent method in the same activity where tryLogin for Twitter was called.
	 * @param intent Pass the intent received by onNewIntent
	 * @return User object. This contains profile data of authenticated user.
	 */
	public User insideOnNewIntent(Intent intent) {
		Log.d(Tag, "onNewIntent");
		StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(callbackURL)) {
			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			try {
				provider.retrieveAccessToken(consumer, verifier);
				AccessToken accessToken = new AccessToken(consumer.getToken(),consumer.getTokenSecret());
				Log.d(Tag, "Twitter token: "+accessToken);
				Log.d(Tag, "Twitter token: "+accessToken.toString());
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(consumerKey, consumerKeySecret);
				twitter.setOAuthAccessToken(accessToken);
				this.user = twitter.showUser(accessToken.getUserId());
				if(this.user.getScreenName()!=null){
					Log.d(Tag, "Got the twitter name:"+user.getScreenName());
					Editor editor= sp.edit();
					editor.putString("twittertoken", accessToken.toString());
					editor.putString("twitterusername", user.getScreenName());
					editor.putBoolean("twitterlogin", true);
					editor.commit();
					if(sp.getBoolean("useridbytwitter", false)==false){
						ClientServerCommunicator.registerTwitter(context, sp.getString("name", "null"), 
								sp.getString("userid", "null"),	sp.getString("twitterusername", "null"),  sp.getString("twittertoken", "null"));					
					}
					else if(sp.getString("userid", "null").equals("null")){
						String user_id=  generateUserId(sp.getString("twitterusername", "null"));
						Editor ed=sp.edit();
						ed.putString("userid", user_id);
						ed.commit();
						ClientServerCommunicator.registerUser(context,user_id, sp.getString("deviceid", "null"), sp.getString("bluetoothmac", "null"));
						ClientServerCommunicator.registerFacebook(context, sp.getString("name", "null"), 
								user_id, sp.getString("twitterusername", "null"),  sp.getString("twittertoken", "null"));					
					}
				}
				Log.d(Tag, "Twitter name: "+sp.getString("twitterusername", "null"));
				Log.d(Tag, "Twitter token: "+sp.getString("twittertoken", "null"));
			}
			catch (Exception e) {
				Toast.makeText(context, "ERROR: "+e.toString(), Toast.LENGTH_LONG).show();
				Log.e(Tag, "Error:"+e.toString());
			}
			StrictMode.setThreadPolicy(old);
		}
		return this.user;		
	}
	
	private String generateUserId(String id){
		id="ssuid"+ id.trim();
		return id;
	}

}
