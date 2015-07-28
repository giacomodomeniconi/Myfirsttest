package enron.graph;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Node class
 * @author ksemertz
 */
public class Node {
	// fields
	private int id;
	private Map<Integer, Edge> adjacency;
	
	/**
	 * Constructor
	 * @param id
	 */
	public Node(int id) {
		this.id = id;
		this.adjacency = new HashMap<>();
	}

	/**
	 * Return id
	 * @return
	 */
	public int getID() { return id; }

	/**
	 * Add edge
	 * @param node
	 * @param timestamp 
	 */
	public void addEdge(Node node, Timestamp timestamp) {
		Edge e;
		
		e = adjacency.get(node.id);
		
		if (e == null) {
			e = new Edge();
			
			adjacency.put(node.getID(), e);
		}
		
		e.addTime(timestamp);
	}

	/**
	 * Return all edges as a collection
	 * @return
	 */
	public Collection<Edge> getAdjacency() {
		return adjacency.values();
	}
}