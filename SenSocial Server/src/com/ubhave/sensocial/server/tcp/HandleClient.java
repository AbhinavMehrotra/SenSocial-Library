package com.ubhave.sensocial.server.tcp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class HandleClient extends Thread {
    Socket socket = null;

    HandleClient(Socket socket) {
        super("HansleClientThread");  //KKMultiServerThread
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(
                                  new InputStreamReader(socket.getInputStream()));
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
            	//fire the stream to the listener or filter it
            	if(inputLine==null || inputLine.isEmpty()){
    				System.out.println("It is blank");
    				continue;            		
            	}
            		
				System.out.println(inputLine);
				MessageParser.run(inputLine);
            }
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}