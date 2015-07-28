package enron.graph;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Graph class
 * @author ksemertz
 */
public class Graph {
	// fields
	private Map<Integer, Node> nodes;
	
	/**
	 * Constructor
	 */
	public Graph() {
		nodes = new HashMap<>();
	}
	
	/**
	 * Creates a node if it doesn't exist
	 * Returns the node object
	 * @param id
	 * @return
	 */
	public Node getCreateNode(int id) {
		Node n = nodes.get(id);
		
		if (n == null) {
			n = new Node(id);
			nodes.put(id, n);
		}
		
		return n;
	}

	/**
	 * Add edge to n1
	 * @param n2
	 * @param n2
	 * @param timestamp
	 */
	public void addEdge(int n1, int n2, Timestamp timestamp) {
		nodes.get(n1).addEdge(nodes.get(n2), timestamp);
	}

	/**
	 * Return nodes as a collection
	 * @return
	 */
	public Collection<Node> getNodes() {
		return nodes.values();
	}

	/**
	 * Return size of graph
	 * @return
	 */
	public int size() { return nodes.size(); }

	/**
	 * Sort times in edges
	 */
	public void sortTimes() {
		// for all nodes
		for (Node n : getNodes()) {
			// for all edges
			for (Edge e : n.getAdjacency())
				e.sortTimes();
		}		
	}
}