import java.net.UnknownHostException;

import javax.swing.JFrame;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.ubhave.sensocial.server.manager.SSManager;


public class Testing {
	public final String conKey="vbsG14ISG49JNs0ux0A2g";
	public final String conKeySecret="lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0";
	public final String accessToken ="111082828-bZHnz1qj2iKYPtGMZNyKHtq3EtaYhjMrafZf1V5b";
	public final String accessTokenSecret ="NJLdDIlgwgBPhqxAiyN59G8dgullbQPZ5hESbahLE";

	
	public static void main(String[] args) {
		
//		String facebookToken="access_token=CAAHXrtWdQicBAEU3XgYJoXXJfBFkhqJ5q1iGZBJPHH54vObUw6ctNmz0lNZCSljv8peD53NI1yrLeeIms9RmMAmhULmpIp0pBXI
//		0FqUnvOWEDuCD0hrLAiW3TkvvXZCPcHGV1WcVZBGI5KdZCpSL6&expires=5182338";
//
//String str1=facebookToken.substring(14);
//String str2= str1.substring(0, str1.indexOf("&expires"));
//String str3= str1.substring(str1.indexOf("&expires")+ "&expires=".length());
//System.out.println(str1);
//System.out.println(str2);
//System.out.println(str3);
//		String str1="CAAHXrtWdQicBAJYwLOvhD8RIdv1tYzFLsjgEB6VCSmUXpkyX8WZBBMMoFFX7kp7ldJYRNhoKJcVO7bJwkZAB9cp1XZCv7NkdTz5QyZCWK0kMgg29w2rIR7N86lm5bsmc41NDPL9uWxVMEJaGqEKo";
//		String str2="AAHXrtWdQicBANeUnZBjZBUlAiEJsCZCqSkQBaTdvObHWuPR5RHZCoDpqMughaGFIFa2qZBA0xYbEkyOmrht9Gq3MM9aDwtQ8190Jg6vc6YW50hLyC2e81g3EwzzSORavFikd0buLXHIp33ClasoPmyfUCm73nRgZD";
//		System.out.println(str1.length());
//		System.out.println(str2.length());
		
//		try {
//			MongoClient mongoClient = new MongoClient("147.188.193.205");
//			DB db = mongoClient.getDB( "SenSocialDB");
//			Boolean authenticate= db.authenticate("abhinav", "sensocial".toCharArray());
//			System.out.println(authenticate);			
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}

				SSManager sm=SSManager.getSSManager();
				ServerBoard frame = new ServerBoard();
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.pack();
		        frame.setVisible(true);

		//    	System.out.println("NULL");
		//
		//		new FacebookEventNotifier().start();

	}	
}




