import javax.swing.JFrame;

import com.ubhave.sensocial.server.manager.SSManager;


public class Testing {

	
	public static void main(String[] args) {

				SSManager sm=SSManager.getSSManager();
				ServerBoard frame = new ServerBoard();
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.pack();
		        frame.setVisible(true);

	}	
}




