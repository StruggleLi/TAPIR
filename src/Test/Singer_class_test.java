package Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.CombinedAnalysis;
import soot.toolkits.scalar.CombinedDUAnalysis;
import soot.util.queue.QueueReader;

/**
 * 一个简单的soot使用示例。
 * @author wang
 *
 */
public class Singer_class_test {
	public static void main(String [] args){
//		Options.v().set_allow_phantom_refs(true);//soot命令行选项解析器
//		Options.v().set_app(true);
//		Options.v().set_whole_program(true);
//		Options.v().set_keep_line_number(true);
		//环境设置
				Options.v().set_allow_phantom_refs(true);
				Options.v().set_app(true);
				Options.v().set_whole_program(true);
				Options.v().set_keep_line_number(true);
				Options.v().set_output_format(Options.output_format_jimp);//对中间码进行设定为jimp
			
		
		
       String[] aa={"Example","MyRunnable","ThreadTest"};

       //获得整个程序的方法集合
       List<SootMethod> MethodList=new ArrayList<SootMethod>();

       for(int i1=0;i1<aa.length;i1++){
			String arg=aa[i1];
		SootClass sootClass = Scene.v().loadClassAndSupport(arg);//加载待分析的类
		//SootClass sootClass = Scene.v().loadClassAndSupport("Example2");//加载待分析的类
		Scene.v().loadNecessaryClasses();//加载必要的类，Java库类等

		List<SootMethod> smList = sootClass.getMethods();//获取某个类的方法列表
       SootMethod main_method=null;

		//对方法列表进行遍历
		for(int i = 0;i<smList.size();i++){
			SootMethod smMethod = smList.get(i);  //获得遍历到的方法
			MethodList.add(smMethod);
			if(smMethod.getName().startsWith("main")){
				main_method=smMethod;
				break;
			}
		}
       }






//for(int i1=0;i1<aa.length;i1++){
		for(int i1=0;i1<0;i1++){
			String arg=aa[i1];
		SootClass sootClass = Scene.v().loadClassAndSupport(arg);//加载待分析的类
		//SootClass sootClass = Scene.v().loadClassAndSupport("Example2");//加载待分析的类
		Scene.v().loadNecessaryClasses();//加载必要的类，Java库类等

		List<SootMethod> smList = sootClass.getMethods();//获取某个类的方法列表
        SootMethod main_method=null;

		//对方法列表进行遍历
		for(int i = 0;i<smList.size();i++){
			SootMethod smMethod = smList.get(i);  //获得遍历到的方法
			if(smMethod.getName().startsWith("getSum")){
				main_method=smMethod;
				break;
			}
		}
		if(main_method!=null){
			Body body = main_method.retrieveActiveBody();//为方法建立方法体

			UnitGraph graph = new BriefUnitGraph(body);//为方法建立控制流图
			CombinedAnalysis duAnalysis = CombinedDUAnalysis.v(graph);// 建立变量定义引用分析

			Iterator<Unit> unitIterator = body.getUnits().iterator();//获取方法中语句的迭代器
			while(unitIterator.hasNext()){//遍历方法中的每条语句
				Unit u = unitIterator.next();    //soot中用接口Unit表示statement
				try{
				Stmt stmt=(Stmt) u;

				stmt.branches();//判断语句是否为分支
				stmt.containsInvokeExpr();//判断语句是否为方法调用
				if(stmt.containsInvokeExpr()){
				InvokeExpr invoke=stmt.getInvokeExpr();//获得调用语句；

				System.out.println("invoke from:   "+main_method.getSignature());
				System.out.println("invoke to:   "+invoke.getMethod().getSignature());//被调用方法


				//判断是否为对象创建语句
//				NewExpr newExpr=(NewExpr) ((DefinitionStmt) stmt).getRightOp();
//				SootClass instanceClass=newExpr.getBaseType().getSootClass();
//				System.out.println("Here is object create event!");
//
//				if(stmt instanceof DefinitionStmt){
//					System.out.println("Here is object create event!");
//
//				}


				}

				NewExpr newExpr=(NewExpr) ((DefinitionStmt) stmt).getRightOp();
				SootClass instanceClass=newExpr.getBaseType().getSootClass();
				//System.out.println("Here is object create event!");
				//if(stmt instanceof DefinitionStmt){
				if(newExpr!=null){
					System.out.println("Here is object create event!    "+instanceClass.getName());

				}

				}catch(Exception localException2){

				}







				//System.out.println("Stmt:    "+stmt.toString());


				//通过Unit我们可以检索使用过的值（getUseBoxes()），定义过的值(getDefBoxes())或这两种(getUseAndDefBoxes())。
				//Unit同时也提供了各种不同查询分支行为的方法，例如：fallsThrough()和 branches().
				List<Tag> tagList = u.getTags();//获取uint对象的标签列表
				for(int n = 0;n<tagList.size();n++){
					Tag t = tagList.get(n);
					if(t instanceof LineNumberTag){//找到LineNumberTag
						System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
					}
				}//获取语句在Java文件中的行号
			}
		}



//测试callgraph
		List<SootMethod> entryPoints=new ArrayList<>();
		//System.out.println("Main_method_name:    "+main_method.getName().toString());
		entryPoints.add(main_method);


//测试影响调用图完整性的因素
		entryPoints=MethodList;
		System.out.println("The size of entryPoints:  "+entryPoints.size());

		//调用图与entryPoints集合有很大的关系，其只分析所指定的方法体内部的代码，而不分析该方法体外部的。
		Scene.v().setEntryPoints(entryPoints);//change the set of entry point methods used to build the call graph
		try
	    {
			System.out.println("Begin runPacks");
	        CHATransformer.v().transform();//这个是不是可以注释掉
	        PackManager.v().runPacks();
	        System.out.println("End runPacks");
	      }
	      catch (RuntimeException localRuntimeException)
	      {
	         System.err.println("[error] " + localRuntimeException.getMessage());
	      }
			CallGraph callGraph = Scene.v().getCallGraph();//获得程序调用图----仅仅与当前分析的类相关的？

//测试遍历
			System.out.println("Start to run traversal: ");//输出调用图大小,
			traversal(main_method,main_method,callGraph);


			System.out.println("Call graph size:   "+callGraph.size());//输出调用图大小,

			PegCallGraph pecg = new PegCallGraph(callGraph);//获得一个不包含lib和native的调用图
			System.out.println("Call PegCallGraph size:   "+pecg.size());//输出调用图大小,
			//System.out.println("Call PegCallGraph :   "+pecg.);//输出调用图大小,

			//对CallGraph包含的调用信息进行输出
			Iterator<Edge> edgeOutOf=callGraph.edgesOutOf(main_method);//edgesInto(sootmethod)
			//System.out.println("edgeOutOf size:   "+edgeOutOf.);
			SootMethod main_method1=null;
			int count=0;
			while(edgeOutOf.hasNext()){
				Edge to=edgeOutOf.next();
				SootMethod src=(SootMethod) to.getTgt();
				System.out.println(main_method.getSignature()+"     call      "+src.getSignature());

				count++;
				if(count==1){
				main_method1=src;
				}


				Stmt stmt=to.srcStmt();//获得语句
				Unit u=(Unit)stmt;
				int lineNumber=0;
				//System.out.println("edgeInto stmt:   "+stmt.toString());


						List<Tag> tagList = stmt.getTags();//获取uint对象的标签列表，unit即为每一行语句
						for(int n = 0;n<tagList.size();n++){
							Tag t = tagList.get(n);
							if(t instanceof LineNumberTag){//找到LineNumberTag
								lineNumber=((LineNumberTag) t).getLineNumber();
						    }
					    }

					System.out.println("LineNumber:    "+lineNumber);



				//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
			}

			//第二层
			System.out.println("The second layer of call:");
			Iterator<Edge> edgeOutOf1=callGraph.edgesOutOf(main_method1);//edgesInto(sootmethod)
			//System.out.println("edgeOutOf size:   "+edgeOutOf.);
			while(edgeOutOf1.hasNext()){
				Edge to=edgeOutOf1.next();
				SootMethod src=(SootMethod) to.getTgt();
				System.out.println(main_method1.getSignature()+"     call      "+src.getSignature());





				Stmt stmt=to.srcStmt();//获得语句
				Unit u=(Unit)stmt;
				int lineNumber=0;
				//System.out.println("edgeInto stmt:   "+stmt.toString());


						List<Tag> tagList = stmt.getTags();//获取uint对象的标签列表，unit即为每一行语句
						for(int n = 0;n<tagList.size();n++){
							Tag t = tagList.get(n);
							if(t instanceof LineNumberTag){//找到LineNumberTag
								lineNumber=((LineNumberTag) t).getLineNumber();
						    }else{
						    	lineNumber=9999;
						    }
					    }

					System.out.println("LineNumber:    "+lineNumber);

				//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
			}



			//对PegCallGraph包含的调用信息进行输出
			List edgeInto=pecg.getSuccsOf(main_method);//获得后继节点集合
			int size=edgeInto.size();
			System.out.println("edgeInto size:   "+size);
			for(int j=0;j<edgeInto.size();j++){
				Object object=edgeInto.get(j);
				System.out.println("ToString:   "+object.toString());

			}




//测试Reachable()的可达性
			ArrayList<SootMethod> collectionsootMethod=new ArrayList<SootMethod>();
			collectionsootMethod.add(main_method);//这是指定reachable的方法？（可以进行输出验证）
			System.out.println("THE CURRENT METHOD IS:  "+main_method.getSignature());

			ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection)collectionsootMethod);
			reachableMethods.update();

			QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//获得可达的方法集合
			while (queueReader.hasNext()) {//对可达的方法进行遍历
				SootMethod qReader=(SootMethod) queueReader.next();
				String str=qReader.getSignature();//应该是方法名
				//System.out.println("From Mehtod Name:  "+sootmethod.getSignature()+"TO Method Name:  "+str);
				System.out.println("Reachable method  TO Method Name:  "+str);
			}



			System.out.println("结束关于方法"+main_method.getSignature()+"的for循环！");

	}//对类遍历分析结束 for



	}

//测试循环遍历
		public static void traversal(SootMethod start,SootMethod sootmethod,CallGraph callGraph){

				//获得每个from方法调用的to方法集合       edgesInto(m)--->returns an iterator over all edges that have m as their target method
				Iterator<Edge> edgeInto=callGraph.edgesOutOf(sootmethod);//returns an iterator over all edges that have u as their source method
				//遍历集合
				System.out.println("The number of edgeInto of method--->"+sootmethod.getName().toString()+"---->"+edgeInto.hasNext());
				if(edgeInto!=null){//如果集合不为空
				while(edgeInto.hasNext()){
					int lineNumber=0;
					Edge to=edgeInto.next();
					Stmt stmt=to.srcStmt();//获得语句
					Unit u=(Unit)stmt;
					//System.out.println("edgeInto stmt:   "+stmt.toString());
					SootMethod src=(SootMethod) to.getTgt();
					System.out.println(sootmethod.getSignature()+"     call      "+src.getSignature());
					//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
					try{

							List<Tag> tagList = stmt.getTags();//获取uint对象的标签列表，unit即为每一行语句
							for(int n = 0;n<tagList.size();n++){
								Tag t = tagList.get(n);
								if(t instanceof LineNumberTag){//找到LineNumberTag
									lineNumber=((LineNumberTag) t).getLineNumber();
							    }
						    }

						System.out.println("LineNumber:    "+lineNumber);
						}
						catch (Exception localException2){
							System.out.println("Wrong on stmt.getTags()");
						}


						//排除子线程，AsyncTask
//						if(!(src.getSignature().contains("start"))){


						if((!(src.getSignature().contains("void run()")))&&(!(src.getSignature().contains("void start()")))&&(lineNumber!=0)&&(!(sootmethod.toString().equals(src.toString())))){
						traversal(start,src,callGraph);
						}

						}
//					}

				}


	  }

}

























