import java.io.*;
import java.util.*;

/**
 * Incident matrix implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class IncidenceMatrix extends AbstractAssocGraph {

	/**
	 * Constructs empty graph.
	 */

	private int vertArrayTracker = 0;
	private Map<String, Integer> keyVert = new HashMap<String, Integer>();
	private Map<String, Integer> keyEdge = new HashMap<String, Integer>();
	private Edge incMatrix[][];

	public IncidenceMatrix() {
		// Implement me!
	} // end of IncidentMatrix()

	public void addVertex(String vertLabel) {
		if (keyVert.containsKey(vertLabel) == false) {

			keyVert.put(vertLabel, vertArrayTracker);
			keyEdge.put(vertLabel, vertArrayTracker);

			if (vertArrayTracker != 0) {

				int vertSize = vertArrayTracker + 1;

				Edge[][] temp = new Edge[vertSize - 1][vertSize - 1];

				for (int i = 0; i < temp.length; i++) {

					for (int j = 0; j < temp.length; j++) {

						temp[i][j] = incMatrix[i][j];

					}
				}

				incMatrix = new Edge[vertSize][vertSize];

				for (int i = 0; i < vertSize; i++) {

					for (int j = 0; j < vertSize; j++) {

						if (j < vertSize - 1 && i < vertSize - 1) {

							incMatrix[i][j] = temp[i][j];

						} else {

							String target = "";
							String source = "";

							for (String name : keyEdge.keySet()) {
								String key = name.toString();

								if (keyEdge.get(key) == j) {

									target = key;
								}
								if (keyEdge.get(key) == i) {

									source = key;
								}
							}

							incMatrix[i][j] = new Edge(source, target, 0);
						}

					}
				}

				vertArrayTracker++;

			} else if (vertArrayTracker == 0) {
				incMatrix = new Edge[1][1];

				incMatrix[0][0] = new Edge(vertLabel, vertLabel, 0);

				vertArrayTracker++;
			}

		} else
			System.out.println("The vertex already exists");

	} // end of addVertex()

	public void addEdge(String srcLabel, String tarLabel, int weight) {

		if (keyVert.containsKey(srcLabel) == true && keyEdge.containsKey(tarLabel) == true) {

			updateWeightEdge(srcLabel, tarLabel, weight);

		} else if (keyVert.containsKey(srcLabel) == false || keyEdge.containsKey(tarLabel) == false) {

			System.out.println("one of the vertexes does not exist");
		}
	} // end of addEdge()

	public int getEdgeWeight(String srcLabel, String tarLabel) {

		int weight = 0;

		if (keyVert.containsKey(srcLabel) == true && keyEdge.containsKey(tarLabel) == true) {

			
			
			if (incMatrix[keyVert.get(srcLabel)][keyEdge.get(tarLabel)].getWeight() == 0 ) {
				
				return -1;
			} else weight = incMatrix[keyVert.get(srcLabel)][keyEdge.get(tarLabel)].getWeight();
		}

		// update return value
		return weight;
	} // end of existEdge()

	public void updateWeightEdge(String srcLabel, String tarLabel, int weight) {

		if (keyVert.containsKey(srcLabel) == true && keyEdge.containsKey(tarLabel) == true) {

			
			incMatrix[keyVert.get(srcLabel)][keyEdge.get(tarLabel)].setWeight(weight);

		}

		else
			System.out.println("That Edge does not exist");

	} // end of updateWeightEdge()

	public void removeVertex(String vertLabel) {

		if (keyVert.containsKey(vertLabel) == true) {

			for (int i = 0; i < vertArrayTracker; i++) {

				incMatrix[keyVert.get(vertLabel)][i] = null;

			}

			for (int i = 0; i < vertArrayTracker; i++) {

				incMatrix[i][keyEdge.get(vertLabel)] = null;

			}

			Edge[][] temp = new Edge[vertArrayTracker - 1][vertArrayTracker - 1];

			int countR = 0;
			int countC = 0;

			for (int i = 0; i < vertArrayTracker; i++) {

				for (int j = 0; j < vertArrayTracker; j++) {

					if (incMatrix[i][j] == null) {

					} else {

						temp[countR][countC] = incMatrix[i][j];

						countC++;

					}

				}

				if (countC == vertArrayTracker - 1) {
					countR++;
					countC = 0;
				}

			}

			incMatrix = temp;

			vertArrayTracker--;

			keyVert = new HashMap<String, Integer>();
			keyEdge = new HashMap<String, Integer>();

			for (int i = 0; i < vertArrayTracker; i++) {
      	
				keyVert.put(incMatrix[i][0].getSrc(), i);
				keyEdge.put(incMatrix[0][i].getTar(), i);

			}

		} else if (keyVert.containsKey(vertLabel) == false) {
			
		} else if (vertArrayTracker == 0) {

			incMatrix = null;

		}

	} // end of removeVertex()

	public List<MyPair> inNearestNeighbours(int k, String vertLabel) {

		List<MyPair> neighbours = new ArrayList<MyPair>();

		ArrayList<Edge> edges = new ArrayList<>();

		for (int i = 0; i < vertArrayTracker; i++) {

			for (int j = 0; j < vertArrayTracker; j++) {

				if (incMatrix[i][j] == null) {
					break;
				} else if (incMatrix[i][j].getTar().equals(vertLabel) && incMatrix[i][j].getWeight() != 0) {
					
					edges.add(incMatrix[i][j]);
					
					

				}

			}

		}

		Collections.sort(edges, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return e2.weight - e1.weight; // Descending
			}
		});

		if (keyVert.containsKey(vertLabel)) {

			if (k != -1 && k < edges.size()) {

				for (int i = 0; i < k; i++) {

					neighbours.add(new MyPair(edges.get(i).getSrc(), edges.get(i).getWeight()));

				}

			} else if (k == -1) {

				for (int i = 0; i < edges.size(); i++) {

					neighbours.add(new MyPair(edges.get(i).getSrc(), edges.get(i).getWeight()));

				}

			}

		} 
		return neighbours;
	} // end of inNearestNeighbours()

	public List<MyPair> outNearestNeighbours(int k, String vertLabel) {

		List<MyPair> neighbours = new ArrayList<MyPair>();

		ArrayList<Edge> edges = new ArrayList<>();

		for (int i = 0; i < vertArrayTracker; i++) {

			if (incMatrix[keyVert.get(vertLabel)][i] == null) {

				break;
			}
			else if (incMatrix[keyVert.get(vertLabel)][i].getWeight() != 0)
			edges.add(incMatrix[keyVert.get(vertLabel)][i]);

		}

		Collections.sort(edges, new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return e2.weight - e1.weight; // Descending
			}
		});

		if (keyVert.containsKey(vertLabel)) {

			if (k != -1 && k < edges.size()) {

				for (int i = 0; i < k; i++) {

					neighbours.add(new MyPair(edges.get(i).getTar(), edges.get(i).getWeight()));

				}

			} else if (k == -1) {

				for (int i = 0; i < edges.size(); i++) {

					neighbours.add(new MyPair(edges.get(i).getTar(), edges.get(i).getWeight()));

				}

			}
		} else
			System.out.println("There are only " + edges.size() + " out neighbours for node " + vertLabel);

		return neighbours;
	} // end of outNearestNeighbours()

	public void printVertices(PrintWriter os) {

		for (String name : keyVert.keySet()) {

			String key = name.toString();

			os.printf(key + " ");

		}

		System.out.print("\n");

	} // end of printVertices()

	public void printEdges(PrintWriter os) {

		for (int i = 0; i < vertArrayTracker; i++) {

			for (int j = 0; j < vertArrayTracker; j++) {

				if( incMatrix[i][j] != null && incMatrix[i][j].getWeight() != 0) {

				os.printf(incMatrix[i][j].getSrc() + " " + incMatrix[i][j].tar + " " + incMatrix[i][j].getWeight()
						+ "\n");
				}
			}
		}

	} // end of printEdges()

	protected class Edge {

		private String src;
		private String tar;
		private int weight;

		public Edge(String src, String tar, int weight) {

			this.src = src;
			this.tar = tar;
			this.weight = weight;

		}

		public String getSrc() {
			return src;
		}

		public String getTar() {
			return tar;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

	}

} // end of class IncidenceMatrix
