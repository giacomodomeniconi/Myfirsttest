package processing.graph.enron;

import java.sql.*;

public class Main {
	/***********************************************/
	// JDBC driver name, database URL, user, pass
	static final String DB_URL = "jdbc:mysql://shadow.cs.uoi.gr:3306/enron_clean";
	static final String USER = "ksemer";
	static final String PASS = "10101990";
	
	// for debugging purpose
	static final boolean debug = false;

	// Graph instance
	static Graph g;
	/***********************************************/

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		
		// create graph instance
		g = new Graph();
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			// get connections
			getConnections(conn, stmt);
			
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		
		g.sortTimes();
		new Statistics(g).showGeneralStats();
	}

	private static void getConnections(Connection conn, Statement stmt) throws SQLException {
	      System.out.println("Creating statement...");
	      stmt = conn.createStatement();
	      String sql = "SELECT * FROM connections";
	      ResultSet rs = stmt.executeQuery(sql);
	      int sender, recipient;
	      Timestamp timestamp;
	      
	      while(rs.next()){
	         //Retrieve by column name
	         sender  = rs.getInt("sender_id");
	         recipient = rs.getInt("recipient_id");
	         timestamp = rs.getTimestamp("time");
	         
	         // create nodes
	         g.getCreateNode(sender);
	         g.getCreateNode(recipient);
	         
	         // create edge
	         g.addEdge(sender, recipient, timestamp);

	         if (debug) {
		         // Display values
		         System.out.println("Sender: " + sender);
		         System.out.println("Recipient: " + recipient);
		         System.out.println("Timestamp: " + timestamp);
		         System.out.println("--------");
	         }
	      }
	      // clean up
	      rs.close();
	      stmt.close();		
	}
}