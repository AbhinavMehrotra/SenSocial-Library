package com.ubhave.sensocial.tcp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.ubhave.sensocial.data.SocialEvent;


public class TCPClient{

	public static  String SERVERIP; 
	public static  int SERVERPORT;
	private Socket socket;
	private PrintWriter out;
	private static TCPClient instance;

	public static TCPClient getInstance(String serverIp, int port){
		if(instance==null){
			instance=new TCPClient(serverIp, port);
		}
		return instance;
	}
	
	private TCPClient(String serverIp, int port) {
		SERVERIP=serverIp;
		SERVERPORT=port;
	}

	/**
	 * Sends the stream to the server
	 * @param stream of sensor data
	 */
	public void startSending(String message){
//		run(se.toString());
		Log.i("SNnMB", "Trying to send: "+message);
		new SendData().execute(message);
	}

	public void stopSending(){
		try {
			socket.close();
		} catch (IOException e) {
			Log.e("TCP Client", e.toString());
		}
		Log.e("TCP", "Closed");
	}

	private class SendData extends AsyncTask<String, Void, Void>{

		protected Void doInBackground(String... data) {
			try {
				InetAddress serverAddr = InetAddress.getByName(SERVERIP);
				Log.e("TCP Client", "Connecting to the server...");
				socket = new Socket(serverAddr, SERVERPORT);

				try {
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					if (out != null && !out.checkError()) {
						System.out.println("send data: "+data[0]);
						out.println(data[0]); //stream 
						out.flush();
					}
					Log.i("TCP Client", "Stream Sent.");

				} catch (Exception e) {
					Log.e("TCP", "Error"+e.toString());

				} finally {
					socket.close();  
					Log.e("TCP", "Closed finally");
				}
			} catch (Exception e) {
				Log.e("TCP", "C: Error", e);
			}
			return null;
		}
		
	}
//	private void run(String data) {
//		try {
//			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
//			Log.e("TCP Client", "Connecting to the server...");
//			socket = new Socket(serverAddr, SERVERPORT);
//
//			try {
//				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//				if (out != null && !out.checkError()) {
//					out.println(""); //stream 
//					out.flush();
//				}
//				Log.i("TCP Client", "Stream Sent.");
//
//			} catch (Exception e) {
//				Log.e("TCP", "Error"+e.toString());
//
//			} finally {
//				socket.close();  
//				Log.e("TCP", "Closed finally");
//			}
//		} catch (Exception e) {
//			Log.e("TCP", "C: Error", e);
//		}
//	}
}