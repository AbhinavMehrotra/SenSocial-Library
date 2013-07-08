package com.sensocialfacebookclient;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewRecordsActivity extends Activity {

	LinearLayout Linear;
	ArrayList<String> entries;
	DataBaseMethods dbm;
	String st;
	int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		Linear  = (LinearLayout)findViewById(R.id.linear);
		dbm=new DataBaseMethods((ContextWrapper) getApplicationContext());
		showTableValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
		return true;
	}

	public void showTableValues(){
		try{
			entries = new ArrayList<String>();
			entries=dbm.getAllTableEntries();
			TextView t0 = new TextView(this);
			t0.setText("Number of entries for are: "+
					entries.size()+"\n...................................................................................................");
			Linear.addView(t0);

			for(i=0;i<entries.size();i++){
				final TextView t = new TextView(this);
				st=entries.get(i)+"\n...................................................................................................";
				System.out.println(st);					
				t.setText(st);
				Linear.addView(t);
			}
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Error encountered: "+e.toString(), Toast.LENGTH_LONG).show();
		}
		Button btn= new Button(this);
		btn.setText("<<< Go Back");
		btn.setWidth(getWallpaperDesiredMinimumWidth());
		btn.setOnClickListener(goBack);
		Linear.addView(btn);
	}

	/**
	 * This is a onClickListener for Back button
	 */
	private OnClickListener goBack = new OnClickListener() {
		public void onClick(View v){
			finish();
		}               
	};
}

