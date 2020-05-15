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
public class Analysis_Insert {

	Set<String> appclass=new HashSet<String>();//对分析的类进行计数
	Set<Insert> insert_store=new HashSet<Insert>();//存储存在问题的insert区域

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{
		//赋值数据
		String[] insert=data.insert;

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

//				for(int i = 0;i<smList.size();i++){
//				    if (smList.get(i).getName().startsWith("on")) {//正则匹配，匹配android中的重要方法，难道只对重要的方法进行分析？
//				    	if(smList.get(i).isConcrete()){//判断方法体是否正确
//				    	System.out.println("ON start method name:   "+smList.get(i).getName());
//				        onsmList.add(smList.get(i));
//				    	}
//				    	else{
//				    		System.out.println("The class:  "+smList.get(i).toString()+"            is no Concrete");
//				    	}
//				    }
//				}

				for(int i = 0;i<smList.size();i++)
				{//对类中的方法进行遍历
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());
					boolean figer[]={false,false,false};
					int insert_lineNumber[]={0,0,0};

					if(sootmethod.isConcrete())
					{//判断方法体是否正确
						figer[0]=false;
						figer[1]=false;
						figer[2]=false;
						insert_lineNumber[0]=0;
						insert_lineNumber[1]=0;
						insert_lineNumber[2]=0;

						System.out.println("The method is concrete："+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

						/*是不是首先判断一个方法体中是否包含了相关的语句，而后对方法体内的语句进行逐行分析，得到具体所在的行号
						 * 等信息。
						 * 也不需要完全匹配，关键字也可以
						 *
						 * 也许是查看调用语句是否包含Insert语句的调用（可以将insert方法的所有参数形式到考虑，这样
						 * 就不是仅仅看insert关键字了，会更准确一些。
						 *
						 * 也许可以这样：
						 * 获得每一个方法体里面的调用关系，而后对这些调用关系进行遍历，看看是否包含了要查找的，
						 * 如果几类都存在，则是正确的，如果不存在，则是存在问题的。
						 *
						 * 另外，如何判断是否被包围，则根据行号来进行确定！）
						 * */
					    Body body = sootmethod.retrieveActiveBody();//为方法建立方法体
					    //System.out.println("Body:   "+body.toString());//输出验证是否存在其他调用方法没有获取

						Iterator<Unit> unitIterator = body.getUnits().iterator();//获取方法中语句的迭代器

						while(unitIterator.hasNext())
						{//遍历方法中的每条语句（一条语句可能拆分为多个unit）
							SootMethod sm=null;
							int lineNumber=0;

							Unit u = unitIterator.next();    //soot中用接口Unit表示statement
							Stmt stmt=(Stmt) u;

							//对其中的调用语句进行分析
							try{
							InvokeExpr invokestmt=stmt.getInvokeExpr();
							sm=invokestmt.getMethod();       //被调用的方法


							//-->进行调用方法的匹配
							for(int j=0;j<insert.length;j++){
								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).equals(insert[j])){
									figer[j]=true;
									System.out.println("Here!");
									//获得行号
									{
										List<Tag> tagList = u.getTags();//获取uint对象的标签列表，unit即为每一行语句
										for(int n = 0;n<tagList.size();n++){
											Tag t = tagList.get(n);
											if(t instanceof LineNumberTag){//找到LineNumberTag
												lineNumber=((LineNumberTag) t).getLineNumber();
												insert_lineNumber[j]=lineNumber;//存储行号
										    }
									    }
									}
								}
							}
							}
							catch (Exception localException2){
							}

//							if ((stmt instanceof JInvokeStmt)||(stmt instanceof InvokeStmt))
//							{
//								System.out.println("Stmt:    "+stmt.toString()+"-----------"+(stmt.getInvokeExprBox()).toString());
//								InvokeExpr ie = stmt.getInvokeExpr();
//								sm = ie.getMethod();
//
//								//获取行号
//								lineNumber=0;
//								List<Tag> tagList = u.getTags();//获取uint对象的标签列表，unit即为每一行语句呀！！！！
//								for(int n = 0;n<tagList.size();n++){
//									Tag t = tagList.get(n);
//									if(t instanceof LineNumberTag){//找到LineNumberTag
//										lineNumber=((LineNumberTag) t).getLineNumber();
//									//System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
//								    }
//								       System.out.println("From:   "+sootmethod.getSignature());
//								       System.out.println("To:     "+sm.getSignature());
//								       System.out.println("LineNumber:    "+lineNumber);
//							    }
//							}
					}
						//方法分析结束，进行信息存储
						System.out.println("figer[1]:"+figer[1]+"   "+figer[0]+"    "+figer[2]);
						if(figer[2]&&!(figer[0]&&figer[1]))
						{
//							insert_store.add(new Insert(sootmethod.getSignature(),insert_lineNumber[1]));
						}
					}else{
						System.out.println("The method is not Concrete");
					}

				}
			}
		}
		System.out.println("The number of method in this application:  "+count_method);
		System.out.println("The number of Insert Problems in this application:  "+insert_store.size());
		//这里是类循环结束

		System.out.println("程序中包含的类分析完毕，类的数量为：     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
