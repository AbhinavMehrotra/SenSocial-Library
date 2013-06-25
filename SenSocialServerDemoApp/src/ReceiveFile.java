
import java.lang.*;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class ReceiveFile implements Runnable {

	private static final int port = 4444;

	private Socket socket;

	public static void main(String[] _) {
		try {
			System.out.println("Running");
			ServerSocket listener = new ServerSocket(port);

			while (true) {
				ReceiveFile file_rec = new ReceiveFile();
				file_rec.socket = listener.accept();  

				new Thread(file_rec).start();
			}
		}
		catch (java.lang.Exception ex) {
			System.out.println("Error:" + ex.toString());
		}
	}

	public void run() {
		//		String s="ThisFile";
		//		System.out.println("Count: "+ );

		try {
			System.out.println("connected");
			InputStream in = socket.getInputStream();

			//			int nof_files = ByteStream.toInt(in);
			//
			//			for (int cur_file=0;cur_file < nof_files; cur_file++) {
			int l= (int) in.read();
			System.out.println("Length: "+ l);
			String file_name = ByteStream.toString(in,l);
			System.out.println("File name: "+ file_name);
			System.out.println("Bytes "+in);

			File file=new File("newFilter.xml");

			ByteStream.toFile(in, file);
			//			}
		}
		catch (java.lang.Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}