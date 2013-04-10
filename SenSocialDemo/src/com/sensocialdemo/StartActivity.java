package com.sensocialdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class StartActivity extends Activity {

	SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		sp=getSharedPreferences("ssdemo", 0);
		if(!sp.getBoolean("consent", false)){
			showContent();
		}
		final Timer timer = new Timer();;
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Log.i("ssdemo", "Started");
				SharedPreferences sp=getSharedPreferences("ssdemo", 0);
				if(!sp.getBoolean("started", false)){
					Editor ed=sp.edit();
					ed.putBoolean("started", true);
					ed.commit();
				}
				else{
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					StartActivity.this.finish();
					timer.cancel();
				}
			}
		}, 2*1000,2*1000);
	}


	private void showContent(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("SenSocial Demo");
		alertDialogBuilder
		.setMessage("This application will collect sensor data and public data from your social network accounts. " +
				" Your data will be used by the members of this project for this project's research only." +
				"If you agree to share your contextual information and social network data, please click CONTINUE.")
				.setCancelable(false)
				.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						StartActivity.this.finish();
					}

				})
				.setNeutralButton("CONTINUE",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						SharedPreferences.Editor editor= sp.edit();
						editor.putBoolean("consent", true);
						editor.commit();
						dialog.cancel();					
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
}
