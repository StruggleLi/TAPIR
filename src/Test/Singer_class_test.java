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
 * һ���򵥵�sootʹ��ʾ����
 * @author wang
 *
 */
public class Singer_class_test {
	public static void main(String [] args){
//		Options.v().set_allow_phantom_refs(true);//soot������ѡ�������
//		Options.v().set_app(true);
//		Options.v().set_whole_program(true);
//		Options.v().set_keep_line_number(true);
		//��������
				Options.v().set_allow_phantom_refs(true);
				Options.v().set_app(true);
				Options.v().set_whole_program(true);
				Options.v().set_keep_line_number(true);
				Options.v().set_output_format(Options.output_format_jimp);//���м�������趨Ϊjimp
			
		
		
       String[] aa={"Example","MyRunnable","ThreadTest"};

       //�����������ķ�������
       List<SootMethod> MethodList=new ArrayList<SootMethod>();

       for(int i1=0;i1<aa.length;i1++){
			String arg=aa[i1];
		SootClass sootClass = Scene.v().loadClassAndSupport(arg);//���ش���������
		//SootClass sootClass = Scene.v().loadClassAndSupport("Example2");//���ش���������
		Scene.v().loadNecessaryClasses();//���ر�Ҫ���࣬Java�����

		List<SootMethod> smList = sootClass.getMethods();//��ȡĳ����ķ����б�
       SootMethod main_method=null;

		//�Է����б���б���
		for(int i = 0;i<smList.size();i++){
			SootMethod smMethod = smList.get(i);  //��ñ������ķ���
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
		SootClass sootClass = Scene.v().loadClassAndSupport(arg);//���ش���������
		//SootClass sootClass = Scene.v().loadClassAndSupport("Example2");//���ش���������
		Scene.v().loadNecessaryClasses();//���ر�Ҫ���࣬Java�����

		List<SootMethod> smList = sootClass.getMethods();//��ȡĳ����ķ����б�
        SootMethod main_method=null;

		//�Է����б���б���
		for(int i = 0;i<smList.size();i++){
			SootMethod smMethod = smList.get(i);  //��ñ������ķ���
			if(smMethod.getName().startsWith("getSum")){
				main_method=smMethod;
				break;
			}
		}
		if(main_method!=null){
			Body body = main_method.retrieveActiveBody();//Ϊ��������������

			UnitGraph graph = new BriefUnitGraph(body);//Ϊ��������������ͼ
			CombinedAnalysis duAnalysis = CombinedDUAnalysis.v(graph);// ���������������÷���

			Iterator<Unit> unitIterator = body.getUnits().iterator();//��ȡ���������ĵ�����
			while(unitIterator.hasNext()){//���������е�ÿ�����
				Unit u = unitIterator.next();    //soot���ýӿ�Unit��ʾstatement
				try{
				Stmt stmt=(Stmt) u;

				stmt.branches();//�ж�����Ƿ�Ϊ��֧
				stmt.containsInvokeExpr();//�ж�����Ƿ�Ϊ��������
				if(stmt.containsInvokeExpr()){
				InvokeExpr invoke=stmt.getInvokeExpr();//��õ�����䣻

				System.out.println("invoke from:   "+main_method.getSignature());
				System.out.println("invoke to:   "+invoke.getMethod().getSignature());//�����÷���


				//�ж��Ƿ�Ϊ���󴴽����
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


				//ͨ��Unit���ǿ��Լ���ʹ�ù���ֵ��getUseBoxes()�����������ֵ(getDefBoxes())��������(getUseAndDefBoxes())��
				//UnitͬʱҲ�ṩ�˸��ֲ�ͬ��ѯ��֧��Ϊ�ķ��������磺fallsThrough()�� branches().
				List<Tag> tagList = u.getTags();//��ȡuint����ı�ǩ�б�
				for(int n = 0;n<tagList.size();n++){
					Tag t = tagList.get(n);
					if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
						System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
					}
				}//��ȡ�����Java�ļ��е��к�
			}
		}



//����callgraph
		List<SootMethod> entryPoints=new ArrayList<>();
		//System.out.println("Main_method_name:    "+main_method.getName().toString());
		entryPoints.add(main_method);


//����Ӱ�����ͼ�����Ե�����
		entryPoints=MethodList;
		System.out.println("The size of entryPoints:  "+entryPoints.size());

		//����ͼ��entryPoints�����кܴ�Ĺ�ϵ����ֻ������ָ���ķ������ڲ��Ĵ��룬���������÷������ⲿ�ġ�
		Scene.v().setEntryPoints(entryPoints);//change the set of entry point methods used to build the call graph
		try
	    {
			System.out.println("Begin runPacks");
	        CHATransformer.v().transform();//����ǲ��ǿ���ע�͵�
	        PackManager.v().runPacks();
	        System.out.println("End runPacks");
	      }
	      catch (RuntimeException localRuntimeException)
	      {
	         System.err.println("[error] " + localRuntimeException.getMessage());
	      }
			CallGraph callGraph = Scene.v().getCallGraph();//��ó������ͼ----�����뵱ǰ����������صģ�

//���Ա���
			System.out.println("Start to run traversal: ");//�������ͼ��С,
			traversal(main_method,main_method,callGraph);


			System.out.println("Call graph size:   "+callGraph.size());//�������ͼ��С,

			PegCallGraph pecg = new PegCallGraph(callGraph);//���һ��������lib��native�ĵ���ͼ
			System.out.println("Call PegCallGraph size:   "+pecg.size());//�������ͼ��С,
			//System.out.println("Call PegCallGraph :   "+pecg.);//�������ͼ��С,

			//��CallGraph�����ĵ�����Ϣ�������
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


				Stmt stmt=to.srcStmt();//������
				Unit u=(Unit)stmt;
				int lineNumber=0;
				//System.out.println("edgeInto stmt:   "+stmt.toString());


						List<Tag> tagList = stmt.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
						for(int n = 0;n<tagList.size();n++){
							Tag t = tagList.get(n);
							if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
								lineNumber=((LineNumberTag) t).getLineNumber();
						    }
					    }

					System.out.println("LineNumber:    "+lineNumber);



				//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
			}

			//�ڶ���
			System.out.println("The second layer of call:");
			Iterator<Edge> edgeOutOf1=callGraph.edgesOutOf(main_method1);//edgesInto(sootmethod)
			//System.out.println("edgeOutOf size:   "+edgeOutOf.);
			while(edgeOutOf1.hasNext()){
				Edge to=edgeOutOf1.next();
				SootMethod src=(SootMethod) to.getTgt();
				System.out.println(main_method1.getSignature()+"     call      "+src.getSignature());





				Stmt stmt=to.srcStmt();//������
				Unit u=(Unit)stmt;
				int lineNumber=0;
				//System.out.println("edgeInto stmt:   "+stmt.toString());


						List<Tag> tagList = stmt.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
						for(int n = 0;n<tagList.size();n++){
							Tag t = tagList.get(n);
							if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
								lineNumber=((LineNumberTag) t).getLineNumber();
						    }else{
						    	lineNumber=9999;
						    }
					    }

					System.out.println("LineNumber:    "+lineNumber);

				//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
			}



			//��PegCallGraph�����ĵ�����Ϣ�������
			List edgeInto=pecg.getSuccsOf(main_method);//��ú�̽ڵ㼯��
			int size=edgeInto.size();
			System.out.println("edgeInto size:   "+size);
			for(int j=0;j<edgeInto.size();j++){
				Object object=edgeInto.get(j);
				System.out.println("ToString:   "+object.toString());

			}




//����Reachable()�Ŀɴ���
			ArrayList<SootMethod> collectionsootMethod=new ArrayList<SootMethod>();
			collectionsootMethod.add(main_method);//����ָ��reachable�ķ����������Խ��������֤��
			System.out.println("THE CURRENT METHOD IS:  "+main_method.getSignature());

			ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection)collectionsootMethod);
			reachableMethods.update();

			QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//��ÿɴ�ķ�������
			while (queueReader.hasNext()) {//�Կɴ�ķ������б���
				SootMethod qReader=(SootMethod) queueReader.next();
				String str=qReader.getSignature();//Ӧ���Ƿ�����
				//System.out.println("From Mehtod Name:  "+sootmethod.getSignature()+"TO Method Name:  "+str);
				System.out.println("Reachable method  TO Method Name:  "+str);
			}



			System.out.println("�������ڷ���"+main_method.getSignature()+"��forѭ����");

	}//��������������� for



	}

//����ѭ������
		public static void traversal(SootMethod start,SootMethod sootmethod,CallGraph callGraph){

				//���ÿ��from�������õ�to��������       edgesInto(m)--->returns an iterator over all edges that have m as their target method
				Iterator<Edge> edgeInto=callGraph.edgesOutOf(sootmethod);//returns an iterator over all edges that have u as their source method
				//��������
				System.out.println("The number of edgeInto of method--->"+sootmethod.getName().toString()+"---->"+edgeInto.hasNext());
				if(edgeInto!=null){//������ϲ�Ϊ��
				while(edgeInto.hasNext()){
					int lineNumber=0;
					Edge to=edgeInto.next();
					Stmt stmt=to.srcStmt();//������
					Unit u=(Unit)stmt;
					//System.out.println("edgeInto stmt:   "+stmt.toString());
					SootMethod src=(SootMethod) to.getTgt();
					System.out.println(sootmethod.getSignature()+"     call      "+src.getSignature());
					//mcallGraph.add(new McallGraph(sootmethod.getSignature(),src.getSignature()));
					try{

							List<Tag> tagList = stmt.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
							for(int n = 0;n<tagList.size();n++){
								Tag t = tagList.get(n);
								if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
									lineNumber=((LineNumberTag) t).getLineNumber();
							    }
						    }

						System.out.println("LineNumber:    "+lineNumber);
						}
						catch (Exception localException2){
							System.out.println("Wrong on stmt.getTags()");
						}


						//�ų����̣߳�AsyncTask
//						if(!(src.getSignature().contains("start"))){


						if((!(src.getSignature().contains("void run()")))&&(!(src.getSignature().contains("void start()")))&&(lineNumber!=0)&&(!(sootmethod.toString().equals(src.toString())))){
						traversal(start,src,callGraph);
						}

						}
//					}

				}


	  }

}

























