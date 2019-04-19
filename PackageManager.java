import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
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

  /*
   * Package Manager default no-argument constructor.
   */
  public PackageManager() {
    this.graph = new Graph();
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
    String depName;

    try {
      Object obj = parser.parse(new FileReader(jsonFilepath));

      JSONObject jsonObject = (JSONObject) obj;

      // loop array
      JSONArray packages = (JSONArray) jsonObject.get("packages");



      for (int i = 0; i < packages.size(); i++) {
        JSONObject packageItem = (JSONObject) packages.get(i);

        String packageName = (String) packageItem.get("name");
        JSONArray dependencies = (JSONArray) packageItem.get("dependencies");

        System.out.println("Package name: " + packageName);

        this.graph.addVertex(packageName);

        for (int j = 0; j < dependencies.size(); j++) {
          depName = (String) dependencies.get(j);

          this.graph.addEdge(packageName, depName);

          System.out.println("\t" + depName);

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


  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return this.graph.getAllVertices();
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

    Set<String> packageList = getAllPackages();

    if (!packageList.contains(pkg))
      throw new PackageNotFoundException();

    List<String> installationOrder = new Stack<String>();

    try {
      installationOrder = getInstallationOrderHelper(pkg, installationOrder);
      // System.out.println(installationOrder);

    } catch (CycleException e) {
      throw new CycleException();
    }


    return installationOrder;
  }

  private List<String> getInstallationOrderHelper(String pkg, List<String> installationOrder)
    throws CycleException {


    List<String> packageDep = this.graph.getAdjacentVerticesOf(pkg);

    if (packageDep == null) {
      if (!installationOrder.contains(pkg))
        installationOrder.add(pkg);
      return installationOrder;
    } else {
      for (int i = 0; i < packageDep.size(); i++) {
        String packageName = packageDep.get(i);

        List<String> packageDepofDep = this.graph.getAdjacentVerticesOf(packageName);

        if (packageDepofDep != null && packageDepofDep.contains(pkg))
          throw new CycleException();

        installationOrder = getInstallationOrderHelper(packageDep.get(i), installationOrder);

      }
      installationOrder.add(pkg);
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

    Set<String> packageList = getAllPackages();
    List<String> currDep, alreadyInstalled = null;
    String currDepPack = null;

    if (!packageList.contains(newPkg) || !packageList.contains(installedPkg))
      throw new PackageNotFoundException();

    try {
      currDep = this.getInstallationOrder(newPkg);
      alreadyInstalled = this.getInstallationOrder(installedPkg);

      for (int i = 0; i < currDep.size(); i++) {
        currDepPack = currDep.get(i);

        if (alreadyInstalled.contains(currDepPack)) {
          currDep.remove(i);
          i--;
        }


      }

    } catch (CycleException e) {
      throw new CycleException();
    }

    System.out.println(currDep);

    return currDep;
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
   * @throws PackageNotFoundException 
   */
  public List<String> getInstallationOrderForAllPackages() throws CycleException, PackageNotFoundException {

    // union set of all the package installation order
    Set<String> packageList = this.getAllPackages();
    List<String> installOrder, finalInstallOrder = null;
    String packageName, installPack;
    boolean firstRun = false;
    try {
      for (Iterator<String> it = packageList.iterator(); it.hasNext(); ) {
        packageName = it.next();
        installOrder = this.getInstallationOrder(packageName);
        
        if(!firstRun) {
          finalInstallOrder = installOrder;
          firstRun = true;
        } else {
          
          for(int i=0; i <installOrder.size() ; i++ ) {
            installPack = installOrder.get(i);
            
            if(!finalInstallOrder.contains(installPack))
              finalInstallOrder.add(installPack);
          }
        }}
    } catch (CycleException e) {
      throw new CycleException();
    }

System.out.println(finalInstallOrder);
    
    return finalInstallOrder;
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

    Set<String> allPackages = this.graph.getAllVertices();
    List<String> maxDep = null, currDep = null;
    String maxDepPack = "", packageName = "";
    boolean firstRun = false;

    for (Iterator<String> it = allPackages.iterator(); it.hasNext();) {
      packageName = it.next();
      try {
        currDep = this.getInstallationOrder(packageName);

        if (!firstRun) {
          maxDep = currDep;
          maxDepPack = packageName;
          firstRun = true;
        }
      } catch (CycleException e) {
        throw new CycleException();
      } catch (PackageNotFoundException e) {
        // This should never be thrown.
        System.out
          .println("This should never be thrown. Something went wrong with the laws of logic.");
        e.printStackTrace();
      }

      if (maxDep.size() < currDep.size()) {
        maxDep = currDep;
        maxDepPack = packageName;

      }

    }

    System.out.println("Found max dependencies" + maxDep + " for package: " + maxDepPack);


    return maxDepPack;
  }

  public static void main(String[] args) {

    // Your program is not required to handle badly formatted json files, but it must exit gracefully if
    // the input file is incorrect.
    // parse json file path form args

    // String jsonFilePath = "valid.json";
    // String jsonFilePath = "cyclic.json";
    String jsonFilePath = "test.json";
    // String jsonFilePath = "shared_dependencies.json";

    System.out.println("PackageManager.main()");

    PackageManager manager = new PackageManager();

    try {
      manager.constructGraph(jsonFilePath);

      manager.getInstallationOrder("A");
      manager.getPackageWithMaxDependencies();
      manager.toInstall("F", "A");
      manager.getInstallationOrderForAllPackages();

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
      System.out.println("CYCLEEXCEPTION");
      e.printStackTrace();
    } catch (PackageNotFoundException e) {
      System.out.println("PackageNotFoundEXCEPTION");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("LIFENOTFOUNDEXCEPTION: At this point. IDEK.");
      e.printStackTrace();
    }

  }

}
