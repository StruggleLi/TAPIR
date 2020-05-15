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
import BaseData.Insert;
import BaseData.McallGraph;
import BaseData.On_Draw;
import BaseData.VH_data;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
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
import soot.util.queue.QueueReader;

/**
 * @author liwenjie
 * 思路： 还是需要先获得所有的对象调用关系，并且对相关方法中是否存在对象创建语句，存在对象创建的地址进行存储。
 *       由于要识别对象创建，因此，在获得调用图的同时，还需要对调用方法中的每一条语句进行分析识别，那么
 *       很可能是需要通过语句逐条分析来获得调用信息。---》在分析每一个方法的时候，记录调用关系。
 *
 *  关于方法调用事件：1）逐条语句分析，看看语句中包含哪些关键词；2）看看是否如invoke那样，有语句类型判断（有）。
 *
 *  两种分析方法：1）获得调用图，而后对涉及的方法进行分析判断，
 *               2）对每一个方法进行分析，收集调用关系；（这段代码是可以重用的）
 *
 */
public class Analysis_OnDraw {

	Set<String> appclass=new HashSet<String>();//对分析的类进行计数
	Set<On_Draw> on_Draw=new HashSet<On_Draw>();//存储存在问题的insert区域
	Set<McallGraph> callGraph=new HashSet<McallGraph>();//存储方法调用信息

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{

		//存储on开头的方法
		ArrayList<SootMethod> onsmList=new ArrayList<SootMethod>();
		int count_method=0;

		//对类进行遍历
		while(scIterator.hasNext())
		{
			SootClass currentClass = scIterator.next();    //当前分析的类
			Boolean bool=false;
			//如果当前类为程序中的类且非接口类
			if((currentClass.isApplicationClass())&&(!(currentClass.isInterface())))
			{
				//(currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool=true;
			}
			if(true)
			{
				String strClassName = currentClass.getName();
				appclass.add(strClassName);
				System.out.println("开始对类：     "+strClassName+"    进行分析");

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);//这是对当前的类进行什么呢？
				List<SootMethod> smList = localSootClass.getMethods();//获取某个类的方法列表，是按照程序实际方法的先后来进行的

		        System.out.println("总的方法数量："+smList.size());
				count_method=count_method+smList.size();

				for(int i = 0;i<smList.size();i++)
				{//对类中的方法进行遍历
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());

					if(sootmethod.isConcrete())
					{//判断方法体是否正确

						System.out.println("The method is concrete："+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

					    Body body = sootmethod.retrieveActiveBody();//为方法建立方法体
					    //System.out.println("Body:   "+body.toString());//输出验证是否存在其他调用方法没有获取

						Iterator<Unit> unitIterator = body.getUnits().iterator();//获取方法中语句的迭代器

						while(unitIterator.hasNext())
						{//遍历方法中的每条语句（一条语句可能拆分为多个unit）
							SootMethod sm=null;
							Unit u = unitIterator.next();    //soot中用接口Unit表示statement
							Stmt stmt=(Stmt) u;
							System.out.println("Stmt:   "+u.toString());

							//对其中的调用语句进行分析
							try{
								int lineNumber=0;
								//获得行号
								{
									List<Tag> tagList = u.getTags();//获取uint对象的标签列表，unit即为每一行语句
									for(int n = 0;n<tagList.size();n++){
										Tag t = tagList.get(n);
										if(t instanceof LineNumberTag){//找到LineNumberTag
											lineNumber=((LineNumberTag) t).getLineNumber();

									    }
								    }
								}

								if(stmt.containsInvokeExpr()){
								//存储方法调用
							    InvokeExpr invokestmt=stmt.getInvokeExpr();
							    sm=invokestmt.getMethod();       //被调用的方法
							    //callGraph.add(new McallGraph(sootmethod.getSignature(),sm.getSignature(),StringOf(lineNumber)));

								}

							    //对象创建事件识别
								/*
								 *
								 * */
							    NewExpr newExpr=(NewExpr) ((DefinitionStmt) stmt).getRightOp();
								SootClass instanceClass=newExpr.getBaseType().getSootClass();
								if(newExpr!=null){
									System.out.println("Here is object create event!    "+instanceClass.getName());
									on_Draw.add(new On_Draw(sootmethod.getSignature(),instanceClass.getName(),lineNumber));

								}

							}
							catch (Exception localException2){
							}
					}
						//方法分析结束，进行信息存储

					}else{
						System.out.println("The method is not Concrete");
					}

				}
			}
		}
		System.out.println("The number of method in this application:  "+count_method);
		System.out.println("The number of On_Draw Problems in this application:  "+on_Draw.size());
		//这里是类循环结束

		System.out.println("程序中包含的类分析完毕，类的数量为：     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
