package com.sensocial.socialnetworks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;


public class AuthenticateFacebook implements AuthenticateFacebookInterface{

	private Context context;
	final private String TAG = "SNnMB";
	private static GraphUser fbuser=null;
	SharedPreferences sp;
	
	public AuthenticateFacebook(Context context) {
		this.context=context;
	}

	public GraphUser tryLogin(Activity currentActivity) {		
		Session.openActiveSession(currentActivity, true, new Session.StatusCallback() {
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								AuthenticateFacebook.fbuser=user;
								sp=context.getSharedPreferences("snmbData",0);
								SharedPreferences.Editor editor= sp.edit();
								editor.putString("fbusername", user.getUsername());
								editor.putBoolean("fblogin", true);								
								editor.commit();
								Log.d(TAG, sp.getString("fbusername", "NO")+" user logged in to facebook.");
							}
						}
					});
				}
			}
		});
		return fbuser;		
	}

	public void insideOnActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(currentActivity, requestCode, resultCode, data);
	}

}
