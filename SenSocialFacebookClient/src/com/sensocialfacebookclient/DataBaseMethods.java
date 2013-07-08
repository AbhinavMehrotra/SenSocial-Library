package com.sensocialfacebookclient;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DataBaseMethods {

	public final static String TAG="SNnMB";
	SQLiteDatabase mydb;
	private static String DBNAME = "PERSONAL.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
	private static String TABLE = "SSDATA";       // THIS IS THE TABLE NAME
	Point p;
	Context context;
	ContextWrapper cw;

	public DataBaseMethods(ContextWrapper cw){
		this.cw=cw;
		createTable();
	}


	public void createTable(){
		mydb = cw.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (Message TEXT, PresrentTime TEXT, Accelerometer TEXT, Microphone TEXT, Location TEXT);");
		mydb.close();
	}

	public void insertIntoTable(String msg, String time, String acc, String mic, String loc){
		try{
			mydb = cw.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
			mydb.execSQL("INSERT INTO " + TABLE + "(Message,PresrentTime,Accelerometer,Microphone,Location) VALUES('"+msg+"','"+time+"','"+acc+"','"+mic+"','"+loc+"');");
			mydb.close();
		}catch(Exception e){
			Log.e(TAG, "Error Inserting Into Table!!"+e.toString());
		}
	}

	public ArrayList<String> getAllTableEntries(){
		ArrayList<String> entries = new ArrayList<String>();
		int i=0;
		mydb =cw.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  TABLE , null);
		//allrows  = mydb.rawQuery("SELECT * FROM "+  TABLE , null);
		if(allrows.moveToFirst()){
			do{
				String msg = "\nMessage: "+allrows.getString(0)+"\nPresrentTime: "+allrows.getString(1)+"\nAccelerometer: "+
						allrows.getString(2)+"\nMicrophone: "+allrows.getString(3)+"\nLocation: "+allrows.getString(4);
				entries.add(msg);
			}
			while(allrows.moveToNext());
		}
		mydb.close();
		return entries;		
	}

	public void deleteAll(){
		mydb = cw.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		mydb.execSQL("DELETE FROM " + TABLE);
		mydb.close();
	}
	
	
}
