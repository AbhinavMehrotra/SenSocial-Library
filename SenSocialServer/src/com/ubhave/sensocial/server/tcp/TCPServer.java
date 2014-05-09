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
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * TCPServer class is responsible to establish a TCP listener.
 * This listener listens to the client connections.
 * It extends Thread class.
 */
public class TCPServer extends Thread{

	public static final int SERVERPORT = 1884;
	private boolean running = true;
	ServerSocket serverSocket =null;
	
	/**
	 * Runs with the thread.
	 */
	@Override
    public void run() {
        super.run(); 
        running =true;
 
            try {
				InetAddress serverAddr = InetAddress.getByName("147.188.197.10");
                System.out.println("Connecting..."+serverAddr );
                System.out.println("Port..."+SERVERPORT );
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e1) {
                System.out.println(e1.toString());
                System.exit(-1);
			}
            
            while(running)
				try {
					new HandleClient(serverSocket.accept()).start();
				} catch (IOException e) {
	                System.out.println(e.toString());
				}
            	try {
					serverSocket.close();
				} catch (IOException e) {
	                System.out.println(e.toString());
				}
 
         
    }

}
