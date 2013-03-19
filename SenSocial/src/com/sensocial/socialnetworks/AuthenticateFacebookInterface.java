package com.sensocial.socialnetworks;

import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;

public interface AuthenticateFacebookInterface {
	
	/**
	 * This method should be called inside the Facebook-Login button's onClickListner. <br>
	 * It authenticates the user via Facebook.
	 * @param currentActivity Activity in which it is called.
	 * @return GraphUser object. This contains profile data of authenticated user.
	 */
	public GraphUser tryLogin(Activity currentActivity);
	
	/**
	 * This method should be inside onActivityResult. <br/>
	 * Remember to create onActivityResult method in the same activity where tryLogin for Facebook was called. <br/>
	 * Pass all the argument which are received by onActivityResult.
	 * @param currentActivity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void insideOnActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data);
}
