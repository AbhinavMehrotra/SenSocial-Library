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