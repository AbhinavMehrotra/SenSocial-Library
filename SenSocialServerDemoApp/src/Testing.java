import javax.swing.JFrame;

import com.ubhave.sensocial.server.manager.SSManager;


public class Testing {
	public final String conKey="vbsG14ISG49JNs0ux0A2g";
	public final String conKeySecret="lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0";
	public final String accessToken ="111082828-bZHnz1qj2iKYPtGMZNyKHtq3EtaYhjMrafZf1V5b";
	public final String accessTokenSecret ="NJLdDIlgwgBPhqxAiyN59G8dgullbQPZ5hESbahLE";

	//	 public static void main(String args[]) throws Exception{
	//		 File file = new File("twitter4j.properties");
	//	        Properties prop = new Properties();
	//	        InputStream is = null;
	//	        OutputStream os = null;
	//	        try {
	//	            if (file.exists()) {
	//	                is = new FileInputStream(file);
	//	                prop.load(is);
	//	            }
	//	            if (args.length < 2) {
	//	                if (null == prop.getProperty("vbsG14ISG49JNs0ux0A2g")
	//	                        && null == prop.getProperty("lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0")) {
	//	                    // consumer key/secret are not set in twitter4j.properties
	//	                    System.out.println(
	//	                            "Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
	//	                    System.exit(-1);
	//	                }
	//	            } else {
	//	                prop.setProperty("vbsG14ISG49JNs0ux0A2g", args[0]);
	//	                prop.setProperty("lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0", args[1]);
	//	                os = new FileOutputStream("twitter4j.properties");
	//	                prop.store(os, "twitter4j.properties");
	//	            }
	//	        } catch (IOException ioe) {
	//	            ioe.printStackTrace();
	//	            System.exit(-1);
	//	        } finally {
	//	            if (is != null) {
	//	                try {
	//	                    is.close();
	//	                } catch (IOException ignore) {
	//	                }
	//	            }
	//	            if (os != null) {
	//	                try {
	//	                    os.close();
	//	                } catch (IOException ignore) {
	//	                }
	//	            }
	//	        }
	//	        try {
	//	            Twitter twitter = new TwitterFactory().getInstance();
	//	            RequestToken requestToken = twitter.getOAuthRequestToken();
	//	            System.out.println("Got request token.");
	//	            System.out.println("Request token: " + requestToken.getToken());
	//	            System.out.println("Request token secret: " + requestToken.getTokenSecret());
	//	            AccessToken accessToken = null;
	//
	//	            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	//	            while (null == accessToken) {
	//	                System.out.println("Open the following URL and grant access to your account:");
	//	                System.out.println(requestToken.getAuthorizationURL());
	//	                try {
	//	                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
	//	                } catch (UnsupportedOperationException ignore) {
	//	                } catch (IOException ignore) {
	//	                } catch (URISyntaxException e) {
	//	                    throw new AssertionError(e);
	//	                }
	//	                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
	//	                String pin = br.readLine();
	//	                try {
	//	                    if (pin.length() > 0) {
	//	                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	//	                    } else {
	//	                        accessToken = twitter.getOAuthAccessToken(requestToken);
	//	                    }
	//	                } catch (TwitterException te) {
	//	                    if (401 == te.getStatusCode()) {
	//	                        System.out.println("Unable to get the access token.");
	//	                    } else {
	//	                        te.printStackTrace();
	//	                    }
	//	                }
	//	            }
	//	            System.out.println("Got access token.");
	//	            System.out.println("Access token: " + accessToken.getToken());
	//	            System.out.println("Access token secret: " + accessToken.getTokenSecret());
	//
	//	            try {
	//	                prop.setProperty("oauth.accessToken", accessToken.getToken());
	//	                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
	//	                os = new FileOutputStream(file);
	//	                prop.store(os, "twitter4j.properties");
	//	                os.close();
	//	            } catch (IOException ioe) {
	//	                ioe.printStackTrace();
	//	                System.exit(-1);
	//	            } finally {
	//	                if (os != null) {
	//	                    try {
	//	                        os.close();
	//	                    } catch (IOException ignore) {
	//	                    }
	//	                }
	//	            }
	//	            System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
	//	            System.exit(0);
	//	        } catch (TwitterException te) {
	//	            te.printStackTrace();
	//	            System.out.println("Failed to get accessToken: " + te.getMessage());
	//	            System.exit(-1);
	//	        } catch (IOException ioe) {
	//	            ioe.printStackTrace();
	//	            System.out.println("Failed to read the system input.");
	//	            System.exit(-1);
	//	        }
	//	    }
	//}
	//	/**
	//	 * @param args
	//	 */
	public static void main(String[] args) {

		//		try {
		//			MQTTManager m=new MQTTManager("abhinav");
		//			m.connect();
		//			m.publishToDevice("hello??");
		//			m.subscribeToDevice();
		//		} catch (MqttException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
//		String value="latitude_5"+"_longitude_7"+"_range_3";
//		String str=value.substring(9);
//		System.out.println(str);
//		String lat=str.substring(0, str.indexOf("_"));
//		str=str.substring(str.indexOf("_")+1);
//		str=str.substring(str.indexOf("_")+1);
//		System.out.println(str);
//		String lon=str.substring(0, str.indexOf("_"));
//		str=str.substring(str.indexOf("_")+1);
//		str=str.substring(str.indexOf("_")+1);
//		System.out.println(str);			
//		String range=str;
//		
//		System.out.println(lat);
//		System.out.println(lon);
//		System.out.println(range);
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

//	public static void startConfiguration(String configName){
//		//set the configuration attribute "sense"="true"
//		try
//		{
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			Document doc = docBuilder.newDocument();
//
//			File file = new File("filter.xml");
//			System.out.println("file found");
//			doc = docBuilder.parse(file); 
//			
//
//			doc.getDocumentElement().normalize();
//			if(doc.getDocumentElement().getNodeName().equalsIgnoreCase("filter")){
//				System.out.println("filter node found");
//				NodeList nList = doc.getElementsByTagName("Configuration");
//				for (int temp=0;temp<nList.getLength();temp++) {
//					Node nNode = nList.item(temp);
//					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						System.out.println("configuration node found");
//						Element eElement = (Element) nNode;
//						if(eElement.getAttribute("name").equalsIgnoreCase(configName)){
//							eElement.setAttribute("sense", "true");
//							System.out.print("config found:"+eElement.getAttribute("name"));
//							//do we need to write it again?
//							TransformerFactory transformerFactory = TransformerFactory.newInstance();
//							Transformer transformer = transformerFactory.newTransformer();
//							DOMSource source = new DOMSource(doc);
//
//							StreamResult result =  new StreamResult("filter.xml");
//							transformer.transform(source, result);
//						}
//						else{
//							System.out.println("config not found: "+configName);
//						}
//					}
//				}
//				System.out.println("Start-configuration: Done");
//			}	
//			else{
//				System.out.println("Start-configuration: Filter not found. It is: "+doc.getDocumentElement().getNodeName());
//			}
//		} catch (Exception e) {
//			System.out.println("Filter-setting: "+e.toString());
//		}
//	}



