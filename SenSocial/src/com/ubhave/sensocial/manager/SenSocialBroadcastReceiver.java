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
package com.ubhave.sensocial.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * SenSocialBroadcastReceiver class is a receiver for the reboot event.
 * This class is not required to be instantiated, but it should be included in the
 * manifest with tag of receiver for reboot.
 * <br>Add this code: <br> 
 * {@code <receiver }<br>&nbsp;{@code 
 *          android:name="com.sensocial.manager.SenSocialServiceReceiver" }<br>&nbsp;{@code 
 *           android:enabled="true" }<br>&nbsp;{@code 
 *           android:exported="false" }<br>&nbsp;{@code 
 *           android:label="BootListener" > }<br>&nbsp;{@code 
 *           <intent-filter> }<br>&nbsp;&nbsp;&nbsp;{@code 
 *               <action android:name="android.intent.action.BOOT_COMPLETED" /> }<br>&nbsp;&nbsp;&nbsp;{@code 
 *               <category android:name="android.intent.category.HOME" /> }<br>&nbsp;{@code 
 *           </intent-filter> }<br>{@code 
 *       </receiver -->
 * }
 */
public class SenSocialBroadcastReceiver extends BroadcastReceiver {   

	/**
	 * Starts the service on device reboot.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myIntent = new Intent(context, com.ubhave.sensocial.mqtt.MQTTService.class);
		context.startService(myIntent);

	}
}
