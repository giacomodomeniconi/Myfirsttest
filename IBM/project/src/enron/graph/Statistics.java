package enron.graph;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TreeMap;

public class Statistics {
	// fields
	private Graph g;

	/**
	 * Constructor
	 * @param g
	 */
	public Statistics(Graph g) {
		this.g = g;
	}
	
	public void showGeneralStats() {
		System.out.println("Nodes size: " + g.size());
		
		degree();
		degreeByTime();
	}

	private void degreeByTime() {
		int max = -1, min = Integer.MAX_VALUE, edges = 0, num = 0, t1 = 0;
		Calendar cal = Calendar.getInstance();
		TreeMap<Integer, Integer> years = new TreeMap<>();

		int[][] nodeStats = new int[g.size()][2];
		
		for (Node n : g.getNodes()) {
			for (Edge e : n.getAdjacency()) {
				for (Timestamp t : e.getTimes()) {
					cal.setTimeInMillis(t.getTime());
					cal.get(Calendar.YEAR);
				}

			}
			nodeStats[num][0]  = n.getID();
			
			nodeStats[num][1] = n.getAdjacency().size();
			for (Edge e : n.getAdjacency()) {
				t1+= e.getTimes().size();
			}
			
			edges+= nodeStats[num][1];
			
			if (min > nodeStats[num][1])
				min = nodeStats[num][1];
			
			if (max < nodeStats[num][1])
				max = nodeStats[num][1];
			num++;
		}
		
		Arrays.sort(nodeStats, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(b[1], a[1]);
		    }
		});
		
		TreeMap<Integer, Integer> st = new TreeMap<>();
		for (int i = 0; i < nodeStats.length; i++) {
			if (st.containsKey(nodeStats[i][1])) {
				st.put(nodeStats[i][1], st.get(nodeStats[i][1]) + 1);
			} else {
				st.put(nodeStats[i][1], 1);
			}
		}

		FileWriter w;
		try {
			w = new FileWriter("degree", false);
			for (int key : st.descendingKeySet()) {
				w.write(key + "\t" + st.get(key) + "\n");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Edges size: " + edges);
		System.out.println("Average edges: " + ((double) edges / (double) g.size()));
		System.out.println("Max: " + max);
		System.out.println("Min: " + min);
		System.out.println("Total timestamps in edges: " + t1);		
	}

	private void degree() {
		int max = -1, min = Integer.MAX_VALUE, edges = 0, num = 0, t = 0;
		int[][] nodeStats = new int[g.size()][2];
		
		for (Node n : g.getNodes()) {
			nodeStats[num][0]  = n.getID();
			
			nodeStats[num][1] = n.getAdjacency().size();
			
			for (Edge e : n.getAdjacency()) {
				t+= e.getTimes().size();
			}
			
			edges+= nodeStats[num][1];
			
			if (min > nodeStats[num][1])
				min = nodeStats[num][1];
			
			if (max < nodeStats[num][1])
				max = nodeStats[num][1];
			num++;
		}
		
		Arrays.sort(nodeStats, new Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(b[1], a[1]);
		    }
		});
		
		TreeMap<Integer, Integer> st = new TreeMap<>();
		for (int i = 0; i < nodeStats.length; i++) {
			if (st.containsKey(nodeStats[i][1])) {
				st.put(nodeStats[i][1], st.get(nodeStats[i][1]) + 1);
			} else {
				st.put(nodeStats[i][1], 1);
			}
		}

		FileWriter w;
		try {
			w = new FileWriter("degree", false);
			for (int key : st.descendingKeySet()) {
				w.write(key + "\t" + st.get(key) + "\n");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Edges size: " + edges);
		System.out.println("Average edges: " + ((double) edges / (double) g.size()));
		System.out.println("Max: " + max);
		System.out.println("Min: " + min);
		System.out.println("Total timestamps in edges: " + t);		
	}
}
