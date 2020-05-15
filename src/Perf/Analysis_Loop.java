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
import BaseData.VH_data;

import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
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
import soot.util.queue.QueueReader;

/**
 * @author liwenjie
 *
 */
public class Analysis_Loop {

	Set<String> appclass=new HashSet<String>();//对分析的类进行计数
	Set<Insert> insert_store=new HashSet<Insert>();//存储存在问题的insert区域

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{
		//赋值数据
		String[] loop1=data.Loop1;
		String[] loop2=data.Loop2;


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

//对当前类中的方法进行遍历分析
				int line_number_figer=0;//行号标记
				for(int i = 0;i<smList.size();i++)
				{
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());
					boolean figer1[]={false,false};
					boolean figer2[]={false,false};
					int[] insert_lineNumber1={0,0};//记录行号
					int[] insert_lineNumber2={0,0};//记录行号

					if(sootmethod.isConcrete())
					{//判断方法体是否正确
						figer1[0]=false;
						figer1[1]=false;
						figer2[0]=false;
						figer2[1]=false;

						insert_lineNumber1[0]=0;
						insert_lineNumber1[1]=0;
						insert_lineNumber2[0]=0;
						insert_lineNumber2[1]=0;


						System.out.println("The method is concrete："+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

					    Body body = sootmethod.retrieveActiveBody();//为方法建立方法体
					    //System.out.println("Body:   "+body.toString());//输出验证是否存在其他调用方法没有获取

						Iterator<Unit> unitIterator = body.getUnits().iterator();//获取方法中语句的迭代器

						while(unitIterator.hasNext())
						{//遍历方法中的每条语句（一条语句可能拆分为多个unit）
							/*
							 * 判断每一条指令unit中是否包含了相关的语句信息。
							 * */
							SootMethod sm=null;
							int lineNumber=0;

							Unit u = unitIterator.next();    //soot中用接口Unit表示statement
							Stmt stmt=(Stmt) u;

							//对其中的调用语句进行分析（一个是new，一个是对图形处理API的调用。）
							try{
							InvokeExpr invokestmt=stmt.getInvokeExpr();
							sm=invokestmt.getMethod();       //被调用的方法


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

							//-->进行调用方法的匹配
							for(int j=0;j<loop1.length;j++){

								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).contains(loop1[j])){
									figer1[lineNumber]=true;//标记行号所在为true

								}
							}

							//-->进行调用方法的匹配
							for(int j=0;j<loop2.length;j++){

								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).contains(loop2[j])){
									figer2[lineNumber]=true;

								}
							}
							//进行判断(1)同时包含调用和转换；（2）行号要相同
							if(figer1[lineNumber]&&figer2[lineNumber])
							{
//								insert_store.add(new Insert(sootmethod.getSignature(),lineNumber));
							}


							}
							catch (Exception localException2){
							}
					}

					}else{
						System.out.println("The method is not Concrete");
					}

				}
			}
		}
		System.out.println("The number of method in this application:  "+count_method);
		System.out.println("The number of Loop Problems in this application:  "+insert_store.size());
		//这里是类循环结束

		System.out.println("程序中包含的类分析完毕，类的数量为：     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
