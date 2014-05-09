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
package com.ubhave.sensocial.tcp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.ubhave.sensocial.data.SocialEvent;

/**
 * Creates a TCS socket to send data to the server.
 * TCP socket opened immediately closes after sending the data.
 */
public class TCPClient{

	public static  String SERVERIP; 
	public static  int SERVERPORT;
	private Socket socket;
	private PrintWriter out;
	private static TCPClient instance;

	/**
	 * Returns the instance of TCPClient.
	 * @param serverIp IP address of the server
	 * @param port Port number of the server
	 * @return (Object) Instance of TCPClient
	 */
	protected static TCPClient getInstance(String serverIp, int port){
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
	 * @param message stream of sensor data
	 */
	protected void startSending(String message){
		Log.i("SNnMB", "Trying to send: "+message);
		new SendData().execute(message);
	}

	/**
	 * Closes the TCPSocket.
	 */
	protected void stopSending(){
		try {
			socket.close();
		} catch (IOException e) {
			Log.e("TCP Client", e.toString());
		}
		Log.e("TCP", "Closed");
	}

	/**
	 * Sends data to the server via TCP socket. It extends AsyncTask to perform the work in background.
	 * This class is executed by startSending(String).
	 */
	private class SendData extends AsyncTask<String, Void, Void>{

		protected Void doInBackground(String... data) {
			try {
				InetAddress serverAddr = InetAddress.getByName(SERVERIP);
				Log.e("TCP Client", "Connecting to the server..."+serverAddr +" at PORT: "+ SERVERPORT);
				socket = new Socket(serverAddr, SERVERPORT);

				try {
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					if (out != null && !out.checkError()) {
						System.out.println("send data: "+data[0]);
						out.println(data[0]); //stream 
						out.flush();
					}
					Log.i("TCP Client", "Data Sent.");

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

}