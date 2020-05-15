/**
 *
 */
package Pattern2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import BaseData.B;
import BaseData.McallGraph;
import BaseData.VH_data;
import Pattern1.Analysis_Bitmap;
import Pattern1.Data1;
import Pattern3.Scene_get;
import Perf.Do_Write;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.queue.QueueReader;

/**
 * @author liwenjie
 *
 */
public class Cheak_UI {

	Set<McallGraph> Heavy_Call = new HashSet<McallGraph>();// 存储检测到的heavy API调用

	//参数是APP的类集合
	public static Scene_get  getCallGraph(Iterator<SootClass> scIterator) throws Exception {
				
		Data2 data = new Data2();
		data.addData();

		List<SootMethod> allmList = new ArrayList<SootMethod>();//保存APP中的所有方法

		// 对类进行遍历
		while (scIterator.hasNext()) {
			SootClass currentClass = scIterator.next(); // 当前分析的类
			System.out.println("Start to Analysis Class-------------->       "
					+ currentClass.getName().toString());

			Boolean bool = false;

			// 如果当前类为程序中的类且非接口类
			if ((currentClass.isApplicationClass())
					&& (!(currentClass.isInterface())) && (true)) {
				// (currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool = true;
			}
			if (bool) {
				SootClass localSootClass = Scene.v().forceResolve(
						currentClass.getName(), 3);// 这是对当前的类进行什么呢？
				List<SootMethod> smList = localSootClass.getMethods();// 获取该类的方法列表，是按照程序实际方法的先后来进行的
				// 对每个类的方法进行遍历
				for (int i = 0; i < smList.size(); i++) {
					if (smList.get(i).isConcrete()) {// 判断方法体是否正确
						allmList.add(smList.get(i));

					} else {
						System.out.println("The method:  "
								+ smList.get(i).toString()
								+ "            is no Concrete");
					}
				}
			}
		}

		List<SootMethod> entryPoints = new ArrayList<>();
		entryPoints = allmList;
		System.out.println("The size of entryPoints:  " + entryPoints.size());

		Scene.v().setEntryPoints(entryPoints);// change the set of entry point methods used to build the call graph
		try {
			System.out.println("Begin runPacks");
			CHATransformer.v().transform();
			PackManager.v().runPacks();
			System.out.println("End runPacks");
		} catch (RuntimeException localRuntimeException) {
			System.err.println("调用图构建失败[error] " + localRuntimeException.getMessage());
			System.out.println("调用图构建失败");
			Analysis_Bitmap.getCallGraph=0;
		}

		CallGraph callGraph = Scene.v().getCallGraph();// 获得程序调用图
		System.out.println("Call graph size:   " + callGraph.size());// 输出调用图大小,
		
		//几个可以利用的地方，需要进一步分析
		PointsToAnalysis pointToAnalysis=Scene.v().getPointsToAnalysis();
		//PointsToSet opintToSet=pointToAnalysis.reachingObjects(arg0);
		Scene_get secne_get=new Scene_get(callGraph,pointToAnalysis);
		
		
		
		
		//Scene.v().getReachableMethods();
		

		return secne_get;
	}


	// 循环遍历--递归的方式
	public static int traversal(SootMethod sootmethod,CallGraph callGraph,int count) {
		Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);
		// 遍历into方法集合
		if (edgeInto != null) {
			while (edgeInto.hasNext()) {
				int lineNumber = 0;
				Edge to = edgeInto.next();
				Stmt stmt = to.srcStmt();// 获得语句
				Unit u = (Unit) stmt;
				SootMethod src = (SootMethod) to.getTgt();
				System.out.println(sootmethod.getSignature()+"     called by      "+src.getSignature());
				try {
					List<Tag> tagList = stmt.getTags();// 获取uint对象的标签列表，unit即为每一行语句
					for (int n = 0; n < tagList.size(); n++) {
						Tag t = tagList.get(n);
						if (t instanceof LineNumberTag) {// 找到LineNumberTag
							lineNumber = ((LineNumberTag) t).getLineNumber();
						}
					}
					System.out.println("LineNumber:    "+lineNumber+"      "+stmt.getClass());
				} catch (Exception localException2) {
					System.out.println("Wrong on stmt.getTags()");
				}
					// 判断是否在UI线程
					// if((lineNumber!=0)&&(!(sootmethod.toString().equals(src.toString())))&&(!(src.getSignature().contains("void run()")))&&(!(src.getSignature().contains("void start()")))){
					if ((src.toString().contains("doInBackground"))||(src.toString().contains("run"))) {
						System.out.println("The opearition is not on UI thread");//分支遍历结束
					}else{
						if(src.toString().startsWith("on")){
							System.out.println("The opearition is on UI thread");//分支遍历结束
							count++;
						}else{
							count=traversal(src, callGraph,count);//继续遍历分支
						}
					}
			}
		}
		return count;
	}

	
	/**
	 * 逆向分析是否位于UI线程中
	 * **/
	public static Result_Pattern2 traverse_UI_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<String> UI_method=new ArrayList<String>();  //存储每个decode相关的UI方法
		ArrayList<String> child_method=new ArrayList<String>();  //存储每个decode相关的child方法
		int UI_count=0;
		int Child_count=0;
		
		System.out.println("The start method is: "+start.getName());
		ArrayList<SootMethod> store= new ArrayList<SootMethod>();//存储已经分析过的方法
		store.add(start);
				
		boolean figer=true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		
		//判断当前start方法是否就处于子线程或UI线程
				if((start.getName().toString().contains("doInBackground"))||(start.getName().toString().equals("run"))){
					System.out.println("The opearition is on child thread");//分支遍历结束
					child_method.add(start.getSignature());
					Child_count++;
					figer=false;
				}
				if((start.getName().toString().startsWith("on"))||(start.getName().equals("getView"))){
					UI_method.add(start.getSignature());
					System.out.println("The opearition is on UI thread");
					UI_count++;
					figer=false;
				}
				
		//如果目前就处于frequency方法中，就不用入栈了。
		if(figer){		
		stack.add(start);// 初始入栈
		while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
			int size = stack.size();
			System.out.println("traverse_UI_analysis the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); //get()是从0 开始的,所以要减去1，获得ArrayList中的最后的一个元素
			store.add(sootmethod);//表示该方法已经遍历分析过
			System.out.println("get the method from stack:  "+ sootmethod.getName());
			stack.remove(size - 1);
			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //获得调用当前方法的集合
			if (edgeInto!= null) {
				// 遍历集合
				while (edgeInto.hasNext()) {
					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();
					System.out.println("UI_analysis_Invoke statement:   "+sootmethod.getSignature()+"            "+from.getSignature());
					boolean add_figer=true;
					//判断是否位于子线程
					String str=from.getName();
					boolean one=str.startsWith("on");
					boolean one1=str.equals("getView");
					boolean two1=str.equals("doInBackground");
					boolean three1=str.equals("run");
					
					if(one||one1){
						UI_method.add(from.getSignature());
						System.out.println("The opearition is on UI thread");//分支遍历结束
						UI_count++;
						add_figer=false;
					}
				
					if(two1 || three1){
						child_method.add(from.getSignature());
						System.out.println("The opearition is on Child thread");//分支遍历结束
						Child_count++;
						add_figer=false;
					}

					if((add_figer)&&(!(store.contains(from)))&&(!(stack.contains(from)))){//排除循环调用--》避免重复添加，没有必要进行考虑
							stack.add(from);// 添加到栈中
							System.out.println("Add to stack");
					}
				}
			}
			
		}
		}

		System.out.println("count_UI:  "+UI_count);
		return new Result_Pattern2(UI_method,child_method,UI_count,Child_count);
	}
	

	

}
