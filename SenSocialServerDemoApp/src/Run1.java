import com.ubhave.sensocial.server.manager.SSManager;


public class Run1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Run this to create TCP listener and activate MQTT service
		SSManager.getSSManager();
	}

}
