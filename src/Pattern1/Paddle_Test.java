/**
 * 
 */
package Pattern1;

import java.util.LinkedList;
import java.util.List;

import soot.G;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;

/**
 * @author Dell
 *
 */
public class Paddle_Test {
public final static String projectDir = "D:\\11-Paper-AppSourceSet\\1Comple-result\\APhotoManager-FDroid\\debug";
	
	public static void main(String[] args) {
		
		// reset soot
		Scene.v().releaseActiveHierarchy();
		Scene.v().releaseCallGraph();
		Scene.v().releaseFastHierarchy();
		Scene.v().releasePointsToAnalysis();
		Scene.v().releaseReachableMethods();
		Scene.v().releaseSideEffectAnalysis();
		G.reset();

		// setup soot args
		List<String> sootArgs = new LinkedList<String>();
		
		// analyze entire directory
		sootArgs.add("-process-dir"); 
		sootArgs.add(projectDir);
		
		sootArgs.add("-w"); // enable whole program mode

		// setup spark options
		sootArgs.add("-p");
		sootArgs.add("cg.paddle");
		sootArgs.add("enabled:true,verbose:true");
		
		String[] argsArray1={"-process-dir",projectDir,"-w","-p","cg.paddle","enabled:true,verbose:true"};
		
		// run soot
		try {
			String[] argsArray = sootArgs.toArray(new String[0]);
			soot.Main.main(argsArray1);
	
			// iterate over call graph results
			CallGraph callGraph = Scene.v().getCallGraph();
			System.out.println("Number of edges in callgraph: " + callGraph.size());
			
//			PointsToAnalysis p = Scene.v().getPointsToAnalysis();
		} catch (Throwable t){
			System.out.println("Soot class path " + Scene.v().getSootClassPath());
			t.printStackTrace();
		}
		
	}


}
