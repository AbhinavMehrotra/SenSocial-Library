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
package com.ubhave.sensocial.server.tcp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * HandleClient is responsible for handling clients'connection via TCP socket.
 * It extends Thread class.
 */
class HandleClient extends Thread {
	Socket socket = null;

	/**
	 * Constructor
	 * @param (Socket) TCP socket for accepted client
	 */
	protected HandleClient(Socket socket) {
		super("HandleClientThread"); 
		this.socket = socket;
	}

	/**
	 * Runs the thread to receive data via TCP socket
	 */
	public void run() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				//fire the stream to the listener or filter it
				if(inputLine==null || inputLine.isEmpty()){
					System.out.println("Stream is blank");
					continue;            		
				}
				System.out.println("TCP Client class: "+inputLine);
				MessageParser.run(inputLine);
			}
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//	public void run(){
	//		try{
	//			System.out.println("run");
	//			DataInputStream input=new DataInputStream(socket.getInputStream());
	//			File f=new File("newfilter.xml");
	//			f.createNewFile();
	//			FileOutputStream fileOut = new FileOutputStream(f);
	//			byte[] buf = new byte[Short.MAX_VALUE];
	//			int bytesSent;        
	//			while( (bytesSent = input.read()) != -1 ) {
	//				System.out.print(bytesSent+",");
	//				fileOut.write(bytesSent);
	//			}
	//			fileOut.close();
	//		}
	//		catch(Exception e){
	//			System.out.println("Error:"+e.toString());
	//		}
	//	}
}