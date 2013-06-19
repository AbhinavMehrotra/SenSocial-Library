package com.ubhave.sensocial.server.tcp;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;


public class TCPServer extends Thread{

	public static final int SERVERPORT = 4444;
	private boolean running = true;
	ServerSocket serverSocket =null;
	
	
	@Override
    public void run() {
        super.run(); 
        running =true;
 
            try {
                System.out.println("S: Connecting...");
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
