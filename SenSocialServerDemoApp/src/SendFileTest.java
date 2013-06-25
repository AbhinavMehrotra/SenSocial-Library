import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class SendFileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		recieve();
		//send();
	}


	public static void send(){
		FileInputStream fileInputStream = null;
		OutputStream socketOutputStream = null;
		try{
			File f=new File("filter.xml");
			fileInputStream=new FileInputStream(f);
			InetAddress serverAddr = InetAddress.getByName("10.4.190.245");
			Socket socket = new Socket(serverAddr, 4444);
			byte[] buffer = new byte[65536];
			int number;
			socketOutputStream = socket.getOutputStream();
			while ((number = fileInputStream.read(buffer)) != -1) {
				socketOutputStream.write(buffer, 0, number);
			}
			socketOutputStream.close();
			fileInputStream.close();
			System.out.print("Done !!");
		}
		catch(Exception e){
			System.out.print("Error:"+e.toString());
		}
	}
	
	public static void recieve(){
		try{
		
		ServerSocket serverSocket = new ServerSocket(4444);
		
		System.out.print("Waiting at!!"+ serverSocket.getInetAddress().getHostAddress());
		Socket clientSocket=serverSocket.accept();
		System.out.print("Connected !!");
		byte[] buffer = new byte[65536];

		InputStream socketStream= clientSocket.getInputStream();
		File f=new File("C:\\output.xml");

		OutputStream fileStream=new FileOutputStream(f);
		int number;
		while ((number = socketStream.read(buffer)) != -1) {
		    fileStream.write(buffer,0,number);
		}

		fileStream.close();
		socketStream.close();
		System.out.print("Done !!");
		}
		catch(Exception e){
			System.out.println("Error: "+e.toString());
		}
	}
}
