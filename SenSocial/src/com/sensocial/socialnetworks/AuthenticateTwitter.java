package com.sensocial.socialnetworks;

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

public class AuthenticateTwitter implements AuthenticateTwitterInterface{

	private final String consumerKey, consumerKeySecret, callbackURL;
	private final String Tag="SNnMB";
	private Context context;
	private CommonsHttpOAuthConsumer consumer;
	private DefaultOAuthProvider provider;
	private Twitter twitter;
	private SharedPreferences sp;
	private User user;

	public AuthenticateTwitter(Context context, String consumerKey, String consumerKeySecret, Boolean newCallBackURL, String URL) {
		this.context=context;
		this.consumerKey=consumerKey;
		this.consumerKeySecret=consumerKeySecret;
		if(newCallBackURL){
			this.callbackURL=URL;
		}else{
			this.callbackURL="callback://tweetactivity";
		}
	}

	public void tryLogin(Activity currentActivity) {
		StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerKeySecret);
		provider = new DefaultOAuthProvider("https://twitter.com/oauth/request_token",
				"https://twitter.com/oauth/access_token", "https://twitter.com/oauth/authorize");
		try {
			String oAuthURL = provider.retrieveRequestToken(consumer, callbackURL);
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(oAuthURL)));
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

	public User insideOnNewIntent(Intent intent) {
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
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(consumerKey, consumerKeySecret);
				twitter.setOAuthAccessToken(accessToken);
				this.user = twitter.showUser(accessToken.getUserId());
				if(this.user.getScreenName()!=null){
					Log.d(Tag, "Got the twitter name:"+user.getScreenName());
					Editor editor= sp.edit();
					editor.putString("twitterusername", user.getScreenName());
					editor.putBoolean("twitterlogin", true);
					editor.commit();
				}
			}
			catch (Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
				Log.d(Tag, "Error:"+e.toString());
			}
			StrictMode.setThreadPolicy(old);
		}
		return this.user;		
	}

}