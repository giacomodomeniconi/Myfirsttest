import java.io.IOException;
import java.sql.SQLException;

import construction.bc3.BC3;
import construction.enron.Enron;

/**
 * @author ksemer
 */
public class Main {
	private static boolean BC3_CREATE = false;
	private static boolean ENRON_CREATE = false;
	private static boolean BC3_RUN = true;
	
	public static void main(String[] args) {
		if (BC3_CREATE) {
			try {
				new BC3();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} 
		}
		
		if (ENRON_CREATE) {
			try {
				new Enron();
			} catch (ClassNotFoundException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (BC3_RUN) {
//			new BC3GraphSimilarity();
		}
		
		//TODO
	}
}