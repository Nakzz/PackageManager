import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Title: PackageManager
 * Course: CS400, Spring 2019 
 * Author: Ajmain Naqib 
 * Email: naqib@wisc.edu 
 * Lecturer's Name: Deb Deppeler
 * 
 */

/**
 * Testing class for HashTable
 */
public class GraphTest {

  Graph testGraph;
  String vertex1 = new String("V1");
  String vertex2 = new String("V2");
  String vertex3 = new String("V3");
  String vertex4 = new String("V4");
  /**
   * Sets the up.
   *
   * @throws Exception the exception
   */

  @Before
  public void setUp() throws Exception {
    testGraph = new Graph();

  }

  /**
   * Tear down.
   *
   * @throws Exception the exception
   */

  @After
  public void tearDown() throws Exception {
    testGraph = null;
  }

  /**
   * Try adding a null vertex
   */
  @Test
  public void test000_insertVertexNull() {

   testGraph.addVertex(null);
    if (testGraph.order() != 0)
      fail("Vertex shouldn't be added");
  }

  /**
   * Try adding a vertex
   */
  @Test
  public void test001_insertVertex() {

   testGraph.addVertex(vertex1);
    if (testGraph.order() != 1)
      fail("Vertex wasn't added");
  }
  
  /**
   * Try removing a vertex
   */
  @Test
  public void test002_removeVertex() {
   
   
   testGraph.addVertex(vertex1);
   testGraph.addVertex(vertex2);
   testGraph.removeVertex(vertex1);
   
   Set<String> allVerticies = testGraph.getAllVertices();
    if (allVerticies.contains(vertex1))
      fail("Vertex wasn't deleted");
  }
 
  /**
   * Try removing a non-exsisting a vertex
   */
  @Test
  public void test003_removeVertexNotExisting() {

    testGraph.addVertex("V2");
   testGraph.removeVertex("V1");
    if (testGraph.order() != 1)
      fail("Vertex size is different");
  }

  /**
   * Try removing a null a vertex
   */
  @Test
  public void test004_removeVertexNull() {

    testGraph.addVertex("V2");
    testGraph.removeVertex(null);
     if (testGraph.order() != 1)
       fail("Vertex size is different");
   }
  
  /**
   * Try adding exsiting vertex edge
   */
  @Test
  public void test005_addEdgeofExsitingVextex() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    
    testGraph.addEdge(vertex1, vertex2);
    
    
    
    testGraph.removeVertex(null);
     if (testGraph.size() != 1)
       fail("Edge size is different");
   }
  
  /**
   * Try adding non-exsiting vertex edge
   */
  @Test
  public void test006_addEdgeofNonExsitingVertex() {

    
    testGraph.addEdge(vertex1, vertex2);
        
    
    testGraph.removeVertex(null);
     if (testGraph.size() != 1)
       fail("Edge size is different");
   }
  
  /**
   * Try adding exsiting vertex edge
   */
  @Test
  public void test007_addEdgeofExsitingEdge() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    
    testGraph.addEdge(vertex1, vertex2);
    
    testGraph.addEdge(vertex1, vertex2);
    
    
    testGraph.removeVertex(null);
     if (testGraph.size() != 1)
       fail("Edge size is different");
   }

  /**
   * Try adding two edges to an exsiting vertex
   */
  @Test
  public void test008_addTwoEdgeofExsitingVertex() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    testGraph.addVertex(vertex3);
    
    testGraph.addEdge(vertex1, vertex2);
    
    testGraph.addEdge(vertex1, vertex3);
    testGraph.addEdge(vertex3, vertex1);
    
    List<String> currentAdjacentVertices = testGraph.getAdjacentVerticesOf(vertex1);
    
     if (!currentAdjacentVertices.contains(vertex2) && !currentAdjacentVertices.contains(vertex3))
       fail("Correct vertexes doesn't exsits");
   }
  
  /**
   * Try adding two edges to an exsiting vertex
   */
  @Test
  public void test009_removeEdgeOfExsitingEdge() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    testGraph.addVertex(vertex3);
    
    testGraph.addEdge(vertex1, vertex2);
    testGraph.addEdge(vertex1, vertex3);
    testGraph.addEdge(vertex1, vertex4);
    testGraph.addEdge(vertex3, vertex1);
    
    testGraph.removeEdge(vertex1, vertex2);
    
    List<String> currentAdjacentVertices = testGraph.getAdjacentVerticesOf(vertex1);
    
     if (currentAdjacentVertices.contains(vertex2) && !currentAdjacentVertices.contains(vertex3)&& !currentAdjacentVertices.contains(vertex4))
       fail("Correct vertexes doesn't exsits");
   }
  
  /**
   * Try removing edges to an exsiting vertex
   */
  @Test
  public void test010_removeEdgeOfNonExsitingEdge() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    testGraph.addVertex(vertex3);
    
    testGraph.addEdge(vertex1, vertex3);
    testGraph.addEdge(vertex1, vertex4);
    testGraph.addEdge(vertex3, vertex1);
    
    testGraph.removeEdge(vertex1, vertex2);
    
    List<String> currentAdjacentVertices = testGraph.getAdjacentVerticesOf(vertex1);
    
     if (currentAdjacentVertices.contains(vertex2) && !currentAdjacentVertices.contains(vertex3)&& !currentAdjacentVertices.contains(vertex4))
       fail("Correct vertexes doesn't exsits");
   }
  
  /**
   * Test correct edgesize
   */
  @Test
  public void test011_edgeSize() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    testGraph.addVertex(vertex3);
    
    testGraph.addEdge(vertex1, vertex3);
    testGraph.addEdge(vertex1, vertex4);
    testGraph.addEdge(vertex3, vertex1);
    
    testGraph.removeVertex(vertex1);
    
     if (testGraph.size() != 0)
       fail("Inncorrect edge size");
   }
  
  
  /**
   * Test correct verticies size
   */
  @Test
  public void test012_vertSize() {

    testGraph.addVertex(vertex1);
    testGraph.addVertex(vertex2);
    testGraph.addVertex(vertex3);
    
    testGraph.addEdge(vertex1, vertex3);
    testGraph.addEdge(vertex1, vertex4);
    testGraph.addEdge(vertex3, vertex1);
    
    testGraph.removeVertex(vertex1);
    
    
     if (testGraph.order() != 3)
       fail("Inncorrect vertex size");
   }
  
  
  
}
