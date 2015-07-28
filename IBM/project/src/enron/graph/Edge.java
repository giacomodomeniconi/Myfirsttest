package enron.graph;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Edge class
 * @author ksemertz
 */
public class Edge {
	// fields
	private List<Timestamp> times;
	
	/**
	 * Constructor
	 */
	public Edge() {
		times = new ArrayList<>();
	}

	/**
	 * Add new time
	 */
	public void addTime(Timestamp timestamp) {
		times.add(timestamp);
	}

	/**
	 * Sort the list of times
	 */
	public void sortTimes() {
		Collections.sort(times);
	}

	/**
	 * Return times
	 * @return
	 */
	public List<Timestamp> getTimes() {
		return times;
	}
}