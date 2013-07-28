import com.ubhave.sensocial.server.manager.SSManager;


public class RunOnce {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Run this to create TCP listener and activate MQTT service
		SSManager.getSSManager();
		
	}

}
