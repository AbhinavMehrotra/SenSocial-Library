package com.sensocial.socialnetworks;

import twitter4j.User;
import android.app.Activity;
import android.content.Intent;

public interface AuthenticateTwitterInterface {
	
	/**
	 * This method should be called inside the Twitter-Login button's onClickListner. <br>
	 * It authenticates the user via Twitter.
	 * @param currentActivity Activity in which it is called.
	 */
	public void tryLogin(Activity currentActivity);
	
	/**
	 * This method should be inside onNewIntent. <br/>
	 * Remember to create onNewIntent method in the same activity where tryLogin for Twitter was called.
	 * @param intent Pass the intent received by onNewIntent
	 * @return User object. This contains profile data of authenticated user.
	 */
	public User insideOnNewIntent(Intent intent);
}
