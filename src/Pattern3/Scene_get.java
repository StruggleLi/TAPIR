/**
 * 
 */
package Pattern3;

import soot.PointsToAnalysis;
import soot.jimple.toolkits.callgraph.CallGraph;

/**
 * @author Dell
 *
 */
public class Scene_get {
	CallGraph callGraph;
	PointsToAnalysis pointToAnalysis;
	
	/**
	 * @param callGraph
	 * @param pointToAnalysis
	 */
	public Scene_get(CallGraph callGraph, PointsToAnalysis pointToAnalysis) {
		super();
		this.callGraph = callGraph;
		this.pointToAnalysis = pointToAnalysis;
	}
	/**
	 * @return the callGraph
	 */
	public CallGraph getCallGraph() {
		return callGraph;
	}
	/**
	 * @param callGraph the callGraph to set
	 */
	public void setCallGraph(CallGraph callGraph) {
		this.callGraph = callGraph;
	}
	/**
	 * @return the pointToAnalysis
	 */
	public PointsToAnalysis getPointToAnalysis() {
		return pointToAnalysis;
	}
	/**
	 * @param pointToAnalysis the pointToAnalysis to set
	 */
	public void setPointToAnalysis(PointsToAnalysis pointToAnalysis) {
		this.pointToAnalysis = pointToAnalysis;
	}
	
	

}
