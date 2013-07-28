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
