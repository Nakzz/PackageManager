import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Filename: PackageManager.java Project: p4 Authors:
 * 
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json file.
 * 
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * 
 * For example: package A depends upon package B, then package B must be installed before package A.
 * 
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test classes.
 */

public class PackageManager {

  private Graph graph;
  private Set<String> packageList;
  private ArrayList<Package> packageObjectList;

  /*
   * Package Manager default no-argument constructor.
   */
  public PackageManager() {
    this.graph = new Graph();
    this.packageList = new HashSet<String>();
    this.packageObjectList = new ArrayList<Package>();
  }

  /**
   * Takes in a file path for a json file and builds the package dependency graph from it.
   * 
   * @param jsonFilepath the name of json data file with package dependency information
   * @throws FileNotFoundException if file path is incorrect
   * @throws IOException if the give file cannot be read
   * @throws ParseException if the given json cannot be parsed
   */
  public void constructGraph(String jsonFilepath)
    throws FileNotFoundException, IOException, ParseException {

    JSONParser parser = new JSONParser();
    Package dependPackage , newPackage;
    try {
      Object obj = parser.parse(new FileReader(jsonFilepath));

      JSONObject jsonObject = (JSONObject) obj;

      // loop array
      JSONArray packages = (JSONArray) jsonObject.get("packages");



      for (int i = 0; i < packages.size(); i++) {
        JSONObject packageItem = (JSONObject) packages.get(i);

        String name = (String) packageItem.get("name");
        JSONArray dependencies = (JSONArray) packageItem.get("dependencies");

        String[] depenString = new String[100];


        System.out.println("Package name: " + name);

        for (int j = 0; j < dependencies.size(); j++) {
          depenString[j] = (String) dependencies.get(j);
          dependPackage = new Package(depenString[j], null);
          packageObjectList.add(dependPackage);
          this.graph.addVertex(depenString[j]);
          System.out.println("\t" + depenString[j]);

        }
        
        
        for(int k=0; k < this.packageObjectList.size(); k++) {
          if(!packageObjectList.get(k).getName().equals(name)) {
            newPackage = new Package(name, depenString);
            packageObjectList.add(newPackage);
            insertPackagetoGraph(newPackage);
          }
        }

  
  
       

      }

      System.out.println("DONE!");


    } catch (FileNotFoundException e) {
      throw new FileNotFoundException();
    } catch (IOException e) {
      throw new IOException();
    } catch (ParseException e) {
      throw new ParseException(0);
    }

  }

  private void insertPackagetoGraph(Package p) {

    String packageName = p.getName();
    String[] dependencies = p.getDependencies();
Package packageObject = null;
    
    if (packageName != null && !packageList.contains(packageName)) 
      packageList.add(packageName);

    
    this.graph.addVertex(packageName);

    for (int i = 0; i < dependencies.length; i++) {
      String dependPack = dependencies[i];
      
      
      this.graph.addEdge(packageName, dependPack);

      if (dependPack != null && !packageList.contains(dependPack)) {
        packageList.add(dependPack);
        packageObject = new Package(dependPack, null);
        
//        if(!packageObjectList.contains(dependPackage))
//          packageObjectList.add(packageObject);
      
      }
    }
  }

  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return this.packageList;
  }

  /**
   * Given a package name, returns a list of packages in a valid installation order.
   * 
   * Valid installation order means that each package is listed before any packages that depend upon
   * that package.
   * 
   * @return List<String>, order in which the packages have to be installed
   * 
   * @throws CycleException if you encounter a cycle in the graph while finding the installation order
   *         for a particular package. Tip: Cycles in some other part of the graph that do not affect
   *         the installation order for the specified package, should not throw this exception.
   * 
   * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
   */
  public List<String> getInstallationOrder(String pkg)
    throws CycleException, PackageNotFoundException {
    if (!this.packageList.contains(pkg))
      throw new PackageNotFoundException();

    List<String> installationOrder = new Stack<String>();
    
    try {
    installationOrder = getInstallationOrderHelper(pkg, installationOrder);
    System.out.println(installationOrder);
    
    } catch (CycleException e) {
      throw new CycleException();
    }
    
    
    return installationOrder;
  }

  private List<String> getInstallationOrderHelper(String pkg, List<String> installationOrder) throws CycleException{

    Package packageObject = null;

    for (int i = 0; i < packageObjectList.size(); i++) {
      if (packageObjectList.get(i).getName().equals(pkg) ) {
        packageObject = packageObjectList.get(i);
        break;
      }
    }
    
    String[] packageDep = packageObject.getDependencies();
    
    if(packageDep.length == 0) {
      installationOrder.add(pkg);
      return installationOrder;
    } else {
      for(int i=0; i < packageDep.length; i++) {
        
        if(packageDep[i] == pkg)
          throw new CycleException();
        
        installationOrder = getInstallationOrderHelper(packageDep[i], installationOrder);
      }
    }



    return installationOrder;

  }

  /**
   * Given two packages - one to be installed and the other installed, return a List of the packages
   * that need to be newly installed.
   * 
   * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
   * installed and packageB is already installed, return the list ["A", "C"] since D will have been
   * installed when B was previously installed.
   * 
   * @return List<String>, packages that need to be newly installed.
   * 
   * @throws CycleException if you encounter a cycle in the graph while finding the dependencies of
   *         the given packages. If there is a cycle in some other part of the graph that doesn't
   *         affect the parsing of these dependencies, cycle exception should not be thrown.
   * 
   * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
   *         graph.
   */
  public List<String> toInstall(String newPkg, String installedPkg)
    throws CycleException, PackageNotFoundException {
    return null;
  }

  /**
   * Return a valid global installation order of all the packages in the dependency graph.
   * 
   * assumes: no package has been installed and you are required to install all the packages
   * 
   * returns a valid installation order that will not violate any dependencies
   * 
   * @return List<String>, order in which all the packages have to be installed
   * @throws CycleException if you encounter a cycle in the graph
   */
  public List<String> getInstallationOrderForAllPackages() throws CycleException {
    return null;
  }

  /**
   * Find and return the name of the package with the maximum number of dependencies.
   * 
   * Tip: it's not just the number of dependencies given in the json file. The number of dependencies
   * includes the dependencies of its dependencies. But, if a package is listed in multiple places, it
   * is only counted once.
   * 
   * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
   * dependencies - B,C and D.
   * 
   * @return String, name of the package with most dependencies.
   * @throws CycleException if you encounter a cycle in the graph
   */
  public String getPackageWithMaxDependencies() throws CycleException {
    // TODO: IMPLEMENT
    for (int i = 0; i < this.packageObjectList.size(); i++) {

    }

    return "";
  }

  public static void main(String[] args) {

    // Your program is not required to handle badly formatted json files, but it must exit gracefully if
    // the input file is incorrect.
    // parse json file path form args

    String jsonFilePath = "test.json";

    System.out.println("PackageManager.main()");

    PackageManager manager = new PackageManager();

    try {
      manager.constructGraph(jsonFilePath);

      manager.getInstallationOrder("A");

    } catch (FileNotFoundException e) {
      System.out.println("FILE WASN'T FOUND");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("IO EXCEPTION");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("PARSEEXCEPTION");
      e.printStackTrace();
    } catch (CycleException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (PackageNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
