/**
 *
 */
package Perf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import BaseData.B;
import BaseData.McallGraph;
import BaseData.VH_data;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.queue.QueueReader;

/**
 * @author liwenjie
 *
 */
public class CopyOfLM {
	Set<McallGraph> mcallGraph=new HashSet<McallGraph>();//存储方法调用图
	Set<McallGraph> onmcallGraph=new HashSet<McallGraph>();//存储on方法调用图
	Set<McallGraph> Heavy_onmcallGraph=new HashSet<McallGraph>();//存储on方法调用图
	Set<VH_data> Heavy_method=new HashSet<VH_data>();//存储on方法调用图
	Set<String> appclass=new HashSet<String>();//对分析的类进行计数


	void StartAnalysis(Iterator<SootClass> scIterator,Data data){
		//赋值数据
		ArrayList<B> bb=new ArrayList<B>();
		bb=data.bb;

		//对类进行遍历
		while(scIterator.hasNext()){
			SootClass currentClass = scIterator.next();    //当前分析的类
			Boolean bool=false;
			//判断是否为将要分析的类
			if((currentClass.isApplicationClass())&&(!(currentClass.isInterface()))){
				//(currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool=true;

			}

			if(bool){//判断当前类是否为程序中的类，且非接口类
				String strClassName = currentClass.getName();
				appclass.add(strClassName);
				System.out.println("开始对类：     "+strClassName+"    进行分析");

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);//这是对当前的类进行什么呢？
				List<SootMethod> smList = localSootClass.getMethods();//获取某个类的方法列表
				ArrayList<SootMethod> onsmList=new ArrayList<SootMethod>();

		        //对每个类的方法进行遍历，分析其中是否包含On开头的方法，包含这些方法则进行分析！
				System.out.println("总的方法数量："+smList.size());
				for(int i = 0;i<smList.size();i++){
				    if (smList.get(i).getName().startsWith("on")) {//正则匹配，匹配android中的重要方法，难道只对重要的方法进行分析？
				        System.out.println("ON start method name:   "+smList.get(i).getName());
				        onsmList.add(smList.get(i));
				      }
				        }
					System.out.println("the number of On_start method: " + onsmList.size());

		        //对"On"开头的方法进行遍历，然后分析每个On开头方法的调用关系。在这里需要进行遍历吗？如果将on方法的集合一次性进行分析，是不是会简单的
		        //的多，然后对callGraph进行双层循环分析，找到所有的调用关系。
				for(int i = 0;i<smList.size();i++){//只对on/所有的方法进行分析
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());


					//逐行语句分析
					if(sootmethod.isConcrete()){//判断方法体是否正确
						System.out.println("The method is concrete");
					    Body body = sootmethod.retrieveActiveBody();//为方法建立方法体
						Iterator<Unit> unitIterator = body.getUnits().iterator();//获取方法中语句的迭代器
						while(unitIterator.hasNext()){//遍历方法中的每条语句
							Unit u = unitIterator.next();    //soot中用接口Unit表示statement
							Stmt stmt=(Stmt) u;

							if (stmt instanceof JInvokeStmt) {
								InvokeExpr ie = stmt.getInvokeExpr();
								SootMethod sm = ie.getMethod();
								String in = sm.getName();

								//获取行号
								int lineNumber=0;
								List<Tag> tagList = u.getTags();//获取uint对象的标签列表，unit即为每一行语句呀！！！！
								for(int n = 0;n<tagList.size();n++){
									Tag t = tagList.get(n);
									if(t instanceof LineNumberTag){//找到LineNumberTag
										lineNumber=((LineNumberTag) t).getLineNumber();
									//System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
								}
							}//获取语句在Java文件中的行号
								mcallGraph.add(new McallGraph(sootmethod.getSignature(),sm.getSignature(),String.valueOf(lineNumber)));

							System.out.println("From:   "+sootmethod.getSignature());
							System.out.println("To:     "+sm.getSignature());
							System.out.println("LineNumber:    "+lineNumber);
						}
					}
					}else{
						System.out.println("The method is not Concrete");
					}



//					List<SootMethod> entryPoints=new ArrayList<>();
//					entryPoints.add(sootmethod);
//
//					Scene.v().setEntryPoints(entryPoints);//change the set of entry point methods used to build the call graph
//					try
//				    {
//						System.out.println("Begin runPacks");
//				        CHATransformer.v().transform();//这个是不是可以注释掉
//				        PackManager.v().runPacks();
//				        System.out.println("End runPacks");
//				      }
//				      catch (RuntimeException localRuntimeException)
//				      {
//				         System.err.println("[error] " + localRuntimeException.getMessage());
//				      }
//						CallGraph callGraph = Scene.v().getCallGraph();//获得程序调用图----仅仅与当前分析的类相关的？
//						System.out.println("Call graph size:   "+callGraph.size());//输出调用图大小,
//
//						//对sootmethod直接调用的方法进行存储
//						//这个调用图好像并非程序的整体调用图
//						Iterator<Edge> edgeInto=callGraph.edgesOutOf(sootmethod);//edgesInto(sootmethod)
//						while(edgeInto.hasNext()){
//							Edge to=edgeInto.next();
//							SootMethod src=(SootMethod) to.getTgt();
//							System.out.println(sootmethod.getSignature()+"     call      "+src.getSignature());
//
//							//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
//						}

//				if(sootmethod.getName().startsWith("on")){
//						ArrayList<SootMethod> collectionsootMethod=new ArrayList<SootMethod>();
//						collectionsootMethod.add(sootmethod);
//
//						ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection)collectionsootMethod);
//						reachableMethods.update();
//
//						QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//获得可达的方法集合
//		//第二层
//					while (queueReader.hasNext()) {//对可达的方法进行遍历
//							SootMethod qReader=(SootMethod) queueReader.next();
//							String str=qReader.getSignature();//应该是方法名
//							//System.out.println("From Mehtod Name:  "+sootmethod.getSignature()+"TO Method Name:  "+str);
//
//							if (!(qReader.getSignature().equals("")))//如果不为空
//							{
//								onmcallGraph.add(new McallGraph(sootmethod.getSignature(),str));
//								//下面是考虑如何进行问题API的检测分析
//								for(int k=0;k<bb.size();k++){
//									B HApi=bb.get(k);//获得重要API对象
//									String class_name=HApi.getStr1();
//									String method_name=HApi.getStr2();
//
//									//对调用关系进行遍历
//									if(str.equals(method_name)||str.contains(method_name)){
//										System.out.println("We have find important API:  "+class_name+"-------"+method_name);
//
//										//Heavy_onmcallGraph.add(new McallGraph(sootmethod.getSignature(),str));
//
//		                                boolean figer=true;
//		                                for(VH_data part:Heavy_method){
//		                                	if((part.getMethod_name()).equals(sootmethod.getSignature())){
//		                                		figer=false;
//		                                		break;
//		                                	}
//		                                }
//		                                if(figer){
//		                                	Heavy_method.add(new VH_data(sootmethod.getSignature()));//其中就包含了类名和方法名
//		                                }
//									}
//
//
//								}
//
//							}
//
//						}
//
//					}
					}
					}
		}
					System.out.println("程序中包含的类分析完毕，类的数量为：     "+appclass.size());

	}


}
