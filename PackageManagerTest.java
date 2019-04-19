import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.json.simple.parser.ParseException;
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
public class PackageManagerTest {

  PackageManager manager; 
  String jsonFilePathV = "valid.json";
 String jsonFilePathC = "cyclic.json";
 String jsonFilePathS = "shared_dependencies.json";
  /**
   * Sets the up.
   *
   * @throws Exception the exception
   */

  @Before
  public void setUp() throws Exception {
    manager = new PackageManager();

  }

  /**
   * Tear down.
   *
   * @throws Exception the exception
   */

  @After
  public void tearDown() throws Exception {
    manager = null;
  }

  /**
   * Construct graph with valid
   */
  @Test
  public void test000_constructValid() {

   
    try {
      manager.constructGraph(jsonFilePathV);

      Set<String> expected = new HashSet<String>();
      
      expected.add("A");
      expected.add("B");
      expected.add("C");
      expected.add("D");
      expected.add("E");
      
      Set<String> actual = manager.getAllPackages();
      
      for(String e : expected) {
        if(!actual.contains(e))
          fail("Doesn't contain all as expected");
      }
      
      expected= new HashSet<String>();
      
      expected.add("C");
      expected.add("D");
      expected.add("B");
      expected.add("A");
      
      List<String> actualList = manager.getInstallationOrder("A");
      
      for(String e : expected) {
        if(!actualList.contains(e))
          fail("Doesn't contain all as expected");
      }
      
 expected= new HashSet<String>();
      
      expected.add("A");
      
      actualList = manager.toInstall("A", "B");
      
      for(String e : expected) {
        if(!actualList.contains(e))
          fail("Doesn't contain all as expected");
      }
      
    } catch (FileNotFoundException e) {
      System.out.println("FILE WASN'T FOUND");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("IO EXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("PARSEEXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("LIFENOTFOUNDEXCEPTION: At this point. IDEK.");
      fail("Should not throw this error");
      e.printStackTrace();
    }
  }

  /**
   * Construct graph with valid
   */
  @Test
  public void test001_constructCyclic() {

   
    try {
      manager.constructGraph(jsonFilePathC);

      Set<String> expected = new HashSet<String>();
      
      expected.add("A");
      expected.add("B");
    
      
      Set<String> actual = manager.getAllPackages();
      
      for(String e : expected) {
        if(!actual.contains(e))
          fail("Doesn't contain all as expected");
      }
      
      expected= new HashSet<String>();
      
      expected.add("C");
      expected.add("D");
      expected.add("B");
      expected.add("A");
      
      try {
      List<String> actualList = manager.getInstallationOrder("A");
      } catch (CycleException e) {
        System.out.println("Pass");
      } catch (Exception e) {
        fail("Should receive cyclic error now");
      }
  
    } catch (FileNotFoundException e) {
      System.out.println("FILE WASN'T FOUND");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("IO EXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("PARSEEXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("LIFENOTFOUNDEXCEPTION: At this point. IDEK.");
      fail("Should not throw this error");
      e.printStackTrace();
    }
  }
  
  /**
   * Construct graph with valid
   */
  @Test
  public void test002_constructShared() {

   
    try {
      manager.constructGraph(jsonFilePathS);

      Set<String> expected = new HashSet<String>();
      
      expected.add("A");
      expected.add("B");
      expected.add("C");
      expected.add("D");
      
      Set<String> actual = manager.getAllPackages();
      
      for(String e : expected) {
        if(!actual.contains(e))
          fail("Doesn't contain all as expected");
      }
      
      expected= new HashSet<String>();
      
      expected.add("C");
      expected.add("D");
      expected.add("B");
      expected.add("A");
      
      List<String> actualList = manager.getInstallationOrder("A");
      
      for(String e : expected) {
        if(!actualList.contains(e))
          fail("Doesn't contain all as expected");
      }
      
      expected= new HashSet<String>();
      
      expected.add("C");
      expected.add("A");
      
      actualList = manager.toInstall("A", "B");
      
      for(String e : expected) {
        if(!actualList.contains(e))
          fail("Doesn't contain all as expected");
      }
      
      
      
    } catch (FileNotFoundException e) {
      System.out.println("FILE WASN'T FOUND");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("IO EXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("PARSEEXCEPTION");
      fail("Should not throw this error");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("LIFENOTFOUNDEXCEPTION: At this point. IDEK.");
      fail("Should not throw this error");
      e.printStackTrace();
    }
  }
  
  
  
}
