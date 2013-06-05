package com.ubhave.sensocial.tcp;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.ubhave.sensocial.data.SocialEvent;


public class TCPClient {

	public static  String SERVERIP; 
	public static  int SERVERPORT;
	Socket socket;
	PrintWriter out;

	public TCPClient(String serverIp, int port) {
		SERVERIP=serverIp;
		SERVERPORT=port;
	}

	/**
	 * Sends the stream to the server
	 * @param stream of sensor data
	 */
	public void startSending(SocialEvent se){
		//get stream here
		run();
	}

	public void stopSending(){
		try {
			socket.close();
		} catch (IOException e) {
			Log.e("TCP Client", e.toString());
		}
		Log.e("TCP", "Closed");
	}

	private void run() {
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("TCP Client", "Connecting to the server...");
			socket = new Socket(serverAddr, SERVERPORT);

			try {
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				if (out != null && !out.checkError()) {
					out.println(""); //stream 
					out.flush();
				}
				Log.i("TCP Client", "Stream Sent.");

			} catch (Exception e) {
				Log.e("TCP", "Error"+e.toString());

			} finally {
				//socket.close();  what to do if stream takes a pause to sense data
				//Log.e("TCP", "Closed finally");
			}
		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);
		}
	}
}