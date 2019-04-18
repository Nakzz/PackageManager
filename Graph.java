import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
 * Filename: Graph.java Project: p4 Authors:
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {

  private int numOfEdges;
  private int numOfVert;
  private Set<String> allVerticies;
  private HashMap<String, LinkedList<String>> graph;

  /**
   * Instantiates a new graph.
   */
  /*
   * Default no-argument constructor
   */
  public Graph() {
    this.numOfEdges = 0;
    this.numOfVert = 0;
    this.allVerticies = new HashSet<String>();
    this.graph = new HashMap<String, LinkedList<String>>();
  }

  /**
   * Add new vertex to the graph.
   * 
   * If vertex is null or already exists, method ends without adding a vertex or throwing an
   * exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   *
   * @param vertex the vertex
   */
  public void addVertex(String vertex) {
    if (vertex == null)
      return;

    Set<String> vertexSet = getAllVertices();

    if (vertexSet.contains(vertex))
      return;

    this.allVerticies.add(vertex);
    this.graph.put(vertex, null);

    this.numOfVert++;
  }

  /**
   * Remove a vertex and all associated edges from the graph.
   * 
   * If vertex is null or does not exist, method ends without removing a vertex, edges, or throwing an
   * exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   *
   * @param vertex the vertex
   */
  public void removeVertex(String vertex) {
    if (vertex == null)
      return;

    Set<String> vertexSet = getAllVertices();

    if (!vertexSet.contains(vertex))
      return;

    // remove all edges from vertex 
    LinkedList<String> currentList = this.graph.get(vertex);
    if (currentList != null) {
      for(int i=0; i < currentList.size();) {
        String e = currentList.get(i);
        this.removeEdge(vertex, e);
      }
    }
    
    //remove vertex from all vertices
    this.allVerticies.remove(vertex);
    
    

    //remove all edges that contains vertex
    for (Iterator<String> it = vertexSet.iterator(); it.hasNext(); ) {
      String f = it.next();
 
      List<String> currentAdjacentVertices = this.getAdjacentVerticesOf(f);
      
      if(currentAdjacentVertices !=null && currentAdjacentVertices.contains(vertex)) {
        System.out.println("FOUND:" + vertex + " edged with " + f);
        
        this.removeEdge(f, vertex);
        
      }
      
      
    }
    


    this.graph.remove(vertex);


    this.numOfVert--;
  }

  /**
   * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and unweighted) If either
   * vertex does not exist, add the non-existing vertex to the graph and then create an edge. If the
   * edge exists in the graph, no edge is added and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. the edge is not in the graph
   *
   *
   * @param vertex1 the vertex 1
   * @param vertex2 the vertex 2
   */
  public void addEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null)
      return;

    // if either vertex does not exist. add the non-existing vertex to the graph
    Set<String> vertexSet = getAllVertices();

    if (!vertexSet.contains(vertex1))
      this.addVertex(vertex1);;

    if (!vertexSet.contains(vertex2))
      this.addVertex(vertex2);



    LinkedList<String> currentList = this.graph.get(vertex1);
    if (currentList == null)
      currentList = new LinkedList<String>();
    else if (currentList.contains(vertex2)) // check if edge exist
      return;



    currentList.add(vertex2);
    this.graph.put(vertex1, currentList);

    this.numOfEdges++;
  }

  /**
   * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed and unweighted) If
   * either vertex does not exist, or if an edge from vertex1 to vertex2 does not exist, no edge is
   * removed and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge from vertex1 to vertex2 is in the graph
   *
   * @param vertex1 the vertex 1
   * @param vertex2 the vertex 2
   */
  public void removeEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null)
      return;

    Set<String> vertexSet = getAllVertices();

    if (!vertexSet.contains(vertex1) && !vertexSet.contains(vertex2))
      return;

    // return if edge doesn't exisits
    List<String> currentAdjacentVertices = this.getAdjacentVerticesOf(vertex1);
    
    if (!currentAdjacentVertices.contains(vertex2))
      return;
    
    currentAdjacentVertices.remove(vertex2);
    this.numOfEdges--;
    
  }

  /**
   * Returns a Set that contains all the vertices.
   *
   * @return the all vertices
   */
  public Set<String> getAllVertices() {
    return this.allVerticies;
  }

  /**
   * Get all the neighbor (adjacent) vertices of a vertex.
   * 
   * 4/9 Clarification of getAdjacentVerticesOf method: For the example graph, A->[B, C], D->[A, B]
   * getAdjacentVerticesOf(A) should return [B, C].
   * 
   * In terms of packages, this list contains the immediate dependencies of A and depending on your
   * graph structure, this could be either the predecessors or successors of A.
   * 
   * @param vertex the specified vertex
   * @return an List<String> of all the adjacent vertices for specified vertex
   */
  public List<String> getAdjacentVerticesOf(String vertex) {

    LinkedList<String> currentList = this.graph.get(vertex);
    if (currentList == null)
      return null;
    else {
      return currentList;
    }
  }

  /**
   * Returns the number of edges in this graph.
   *
   * @return the int
   */
  public int size() {
    return this.numOfEdges;
  }

  /**
   * Returns the number of vertices in this graph.
   *
   * @return the int
   */
  public int order() {
    return this.numOfVert;
  }
}
