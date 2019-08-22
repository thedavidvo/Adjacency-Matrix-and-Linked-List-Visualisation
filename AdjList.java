import java.io.*;
import java.util.*;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjList extends AbstractAssocGraph {

	private Node[] adjList = new Node[10000];
	private int numOfHead = 0;

	public AdjList() {

	}

	public void addVertex(String vertLabel) {
		if (!findNode(vertLabel)) {
			Node n = new Node(vertLabel);
			adjList[numOfHead] = n;
			numOfHead++;
		} else {
			System.err.println("Node exists");
		}
	}

	public void addEdge(String srcLabel, String tarLabel, int weight) {
		if (findNode(srcLabel) && findNode(tarLabel)) {
			if (getNode(srcLabel).findEdge(srcLabel, tarLabel)) {
				System.err.println("Edge already exists between the two nodes.");
			} else {
				getNode(srcLabel).addEdge(srcLabel, tarLabel, weight);
			}
		}
	}

	public int getEdgeWeight(String srcLabel, String tarLabel) {
		int w = getNode(srcLabel).getWeight(srcLabel, tarLabel);

		// if the weight is non existent, then it is denoted as -1
		if (w <= 0) {
			w = -1;
		}

		return w;
	}

	public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {
		int w = weight;

		if (w > 0) {
			getNode(srcLabel).updateWeight(srcLabel, tarLabel, weight);
		} else {
			getNode(srcLabel).removeEdge(srcLabel, tarLabel);
		}

	}

	public void removeVertex(String vertLabel) {

		if (findNode(vertLabel)) {
			// removes out neighbours
			getNode(vertLabel).removeAllEdges();

			// removes in neighbours
			for (int i = 0; i < numOfHead; i++) {
				for (int j = 0; j < adjList[i].getNumEdges(); j++) {
					if (adjList[i].getEdgeArray()[j].getTarget().equals(vertLabel)) {
						adjList[i].removeEdge(adjList[i].getName(), vertLabel);
					}
				}
			}

			for (int i = 0; i < numOfHead; i++) {
				if (adjList[i].getName().equals(vertLabel)) {
					adjList[i] = null;
					break;
				}
			}

			Node temp[] = new Node[10000];
  
			int count = 0;

			for (int i = 0; i < numOfHead; i++) 
			{
				if (adjList[i] != null) 
				{
					temp[count] = adjList[i];
					count++;
				}
			}

			adjList = new Node[10000];

			adjList = temp;

			numOfHead--;
		}

	}

	public List<MyPair> inNearestNeighbours(int k, String vertLabel) {
		List<MyPair> neighbours = new ArrayList<MyPair>();

		ArrayList<Edge> myEdge = new ArrayList<>();

		for (int i = 0; i < numOfHead; i++) {
			for (int j = 0; j < adjList[i].getNumEdges(); j++) {
				if (adjList[i].getEdgeArray()[j].getTarget().equals(vertLabel)) {
					myEdge.add(adjList[i].getEdgeArray()[j]);
				}
			}
		}

		Collections.sort(myEdge, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return e2.weight - e1.weight;
			}
		});

		if (k <= -1) {
			for (int i = 0; i < myEdge.size(); i++) {
				neighbours.add(new MyPair(myEdge.get(i).getSource(), myEdge.get(i).getWeight()));
			}
		} else {
			if (k <= myEdge.size()) {
				for (int i = 0; i < k; i++) {
					neighbours.add(new MyPair(myEdge.get(i).getSource(), myEdge.get(i).getWeight()));
				}
			} else {
				System.err.println("There are only " + myEdge.size() + "in neighbours for node " + vertLabel);
			}
		}
		return neighbours;
	}

	public List<MyPair> outNearestNeighbours(int k, String vertLabel) {
		List<MyPair> neighbours = new ArrayList<MyPair>();

		ArrayList<Edge> myEdge = new ArrayList<>();

		for (int i = 0; i < getNode(vertLabel).getNumEdges(); i++) {
			myEdge.add(getNode(vertLabel).getEdgeArray()[i]);
		}

		Collections.sort(myEdge, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return e2.weight - e1.weight;
			}
		});

		if (k <= -1) {
			for (int i = 0; i < myEdge.size(); i++) {
				neighbours.add(new MyPair(myEdge.get(i).getTarget(), myEdge.get(i).getWeight()));
			}
		} else {
			if (k <= myEdge.size()) {
				for (int i = 0; i < k; i++) {
					neighbours.add(new MyPair(myEdge.get(i).getTarget(), myEdge.get(i).getWeight()));
				}
			} else {
				System.err.println("There are only " + myEdge.size() + "out neighbours for node " + vertLabel);
			}
		}

		return neighbours;
	}

	public void printVertices(PrintWriter os) {
		for (int i = 0; i < numOfHead; i++) {
			if (adjList[i] != null)
				os.printf(adjList[i].getName() + " ");
		}
	}

	public void printEdges(PrintWriter os) {
		for (int i = 0; i < numOfHead; i++) {
			Edge[] myEdge = adjList[i].getEdgeArray();

			for (int j = 0; j < adjList[i].getNumEdges(); j++) {
				os.println(adjList[i].getName() + " " + myEdge[j].getTarget() + " " + myEdge[j].getWeight());
			}
		}
	}

	// returns true if found
	public boolean findNode(String vertLabel) {
		boolean found = false;

		for (int i = 0; i < numOfHead; i++) {
			if (adjList[i].getName().equals(vertLabel)) {
				found = true;
			}
		}

		return found;
	}

	// returns the node
	public Node getNode(String vertLabel) {
		for (int i = 0; i < numOfHead; i++) {
			if (adjList[i].getName().equals(vertLabel)) {
				return adjList[i];
			}
		}
		return null;
	}

	protected class Node {

		private String name;
		private Edge edges[] = new Edge[10000];
		private int numEdges = 0;

		public Node(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getNumEdges() {
			return numEdges;
		}

		public Edge[] getEdgeArray() {
			return edges;
		}

		public void addEdge(String source, String target, int weight) {
			edges[numEdges] = new Edge(source, target, weight);
			numEdges++;
		}

		// to be called if updating edge 0, or removing the in neighbors
		public void removeEdge(String source, String target) {
			for (int i = 0; i < numEdges; i++) {
				if (edges[i].getTarget().equals(target)) {
					edges[i] = null;
				}
			}

			Edge tempEdge[] = new Edge[10000];
			int count = 0;

			for (int i = 0; i < numEdges; i++) {
				if (edges[i] != null) {
					tempEdge[count] = edges[i];
					count++;
				}
			}

			edges = new Edge[1000];

			edges = tempEdge;

			numEdges--;
		}

		// to be called if deleting node so it removes all out neighbors
		public void removeAllEdges() {
			// remove all the edges of vertex.

			for (int i = 0; i < numEdges; i++) {
				if (edges[i] != null) {
					edges[i] = null;
				}
			}

			numEdges = 0;
		}

		// checks to see if there is an existing edge between the two nodes
		public boolean findEdge(String source, String target) {
			boolean found = false;

			for (int i = 0; i < numEdges; i++) {
				if (edges[i] != null) {
					if (edges[i].getSource().equals(source) && edges[i].getTarget().equals(target)) {
						found = true;
						break;
					}
				}
			}
			return found;
		}

		// gets the weight of the edge
		public int getWeight(String source, String target) {
			int w = 0;

			for (int i = 0; i < numEdges; i++) {
				if (edges[i] != null) {
					if (edges[i].getSource().equals(source) && edges[i].getTarget().equals(target)) {
						w = edges[i].getWeight();
					}
				} else {
					w = 0;
				}
			}

			return w;
		}

		// updates the weight of the edge
		public void updateWeight(String source, String target, int weight) {
			for (int i = 0; i < numEdges; i++) {
				if (edges[i].getSource().equals(source) && edges[i].getTarget().equals(target)) {
					edges[i].setWeight(weight);
				}
			}
		}
	}

	protected class Edge {
		private String source;
		private String target;
		private int weight;

		public Edge(String source, String target, int weight) {
			this.source = source;
			this.target = target;
			this.weight = weight;
		}

		public String getSource() {
			return source;
		}

		public String getTarget() {
			return target;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
	}

}
