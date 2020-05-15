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
	Set<McallGraph> mcallGraph=new HashSet<McallGraph>();//�洢��������ͼ
	Set<McallGraph> onmcallGraph=new HashSet<McallGraph>();//�洢on��������ͼ
	Set<McallGraph> Heavy_onmcallGraph=new HashSet<McallGraph>();//�洢on��������ͼ
	Set<VH_data> Heavy_method=new HashSet<VH_data>();//�洢on��������ͼ
	Set<String> appclass=new HashSet<String>();//�Է���������м���


	void StartAnalysis(Iterator<SootClass> scIterator,Data data){
		//��ֵ����
		ArrayList<B> bb=new ArrayList<B>();
		bb=data.bb;

		//������б���
		while(scIterator.hasNext()){
			SootClass currentClass = scIterator.next();    //��ǰ��������
			Boolean bool=false;
			//�ж��Ƿ�Ϊ��Ҫ��������
			if((currentClass.isApplicationClass())&&(!(currentClass.isInterface()))){
				//(currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool=true;

			}

			if(bool){//�жϵ�ǰ���Ƿ�Ϊ�����е��࣬�ҷǽӿ���
				String strClassName = currentClass.getName();
				appclass.add(strClassName);
				System.out.println("��ʼ���ࣺ     "+strClassName+"    ���з���");

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);//���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();//��ȡĳ����ķ����б�
				ArrayList<SootMethod> onsmList=new ArrayList<SootMethod>();

		        //��ÿ����ķ������б��������������Ƿ����On��ͷ�ķ�����������Щ��������з�����
				System.out.println("�ܵķ���������"+smList.size());
				for(int i = 0;i<smList.size();i++){
				    if (smList.get(i).getName().startsWith("on")) {//����ƥ�䣬ƥ��android�е���Ҫ�������ѵ�ֻ����Ҫ�ķ������з�����
				        System.out.println("ON start method name:   "+smList.get(i).getName());
				        onsmList.add(smList.get(i));
				      }
				        }
					System.out.println("the number of On_start method: " + onsmList.size());

		        //��"On"��ͷ�ķ������б�����Ȼ�����ÿ��On��ͷ�����ĵ��ù�ϵ����������Ҫ���б����������on�����ļ���һ���Խ��з������ǲ��ǻ�򵥵�
		        //�Ķ࣬Ȼ���callGraph����˫��ѭ���������ҵ����еĵ��ù�ϵ��
				for(int i = 0;i<smList.size();i++){//ֻ��on/���еķ������з���
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());


					//����������
					if(sootmethod.isConcrete()){//�жϷ������Ƿ���ȷ
						System.out.println("The method is concrete");
					    Body body = sootmethod.retrieveActiveBody();//Ϊ��������������
						Iterator<Unit> unitIterator = body.getUnits().iterator();//��ȡ���������ĵ�����
						while(unitIterator.hasNext()){//���������е�ÿ�����
							Unit u = unitIterator.next();    //soot���ýӿ�Unit��ʾstatement
							Stmt stmt=(Stmt) u;

							if (stmt instanceof JInvokeStmt) {
								InvokeExpr ie = stmt.getInvokeExpr();
								SootMethod sm = ie.getMethod();
								String in = sm.getName();

								//��ȡ�к�
								int lineNumber=0;
								List<Tag> tagList = u.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����ѽ��������
								for(int n = 0;n<tagList.size();n++){
									Tag t = tagList.get(n);
									if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
										lineNumber=((LineNumberTag) t).getLineNumber();
									//System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
								}
							}//��ȡ�����Java�ļ��е��к�
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
//				        CHATransformer.v().transform();//����ǲ��ǿ���ע�͵�
//				        PackManager.v().runPacks();
//				        System.out.println("End runPacks");
//				      }
//				      catch (RuntimeException localRuntimeException)
//				      {
//				         System.err.println("[error] " + localRuntimeException.getMessage());
//				      }
//						CallGraph callGraph = Scene.v().getCallGraph();//��ó������ͼ----�����뵱ǰ����������صģ�
//						System.out.println("Call graph size:   "+callGraph.size());//�������ͼ��С,
//
//						//��sootmethodֱ�ӵ��õķ������д洢
//						//�������ͼ���񲢷ǳ�����������ͼ
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
//						QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//��ÿɴ�ķ�������
//		//�ڶ���
//					while (queueReader.hasNext()) {//�Կɴ�ķ������б���
//							SootMethod qReader=(SootMethod) queueReader.next();
//							String str=qReader.getSignature();//Ӧ���Ƿ�����
//							//System.out.println("From Mehtod Name:  "+sootmethod.getSignature()+"TO Method Name:  "+str);
//
//							if (!(qReader.getSignature().equals("")))//�����Ϊ��
//							{
//								onmcallGraph.add(new McallGraph(sootmethod.getSignature(),str));
//								//�����ǿ�����ν�������API�ļ�����
//								for(int k=0;k<bb.size();k++){
//									B HApi=bb.get(k);//�����ҪAPI����
//									String class_name=HApi.getStr1();
//									String method_name=HApi.getStr2();
//
//									//�Ե��ù�ϵ���б���
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
//		                                	Heavy_method.add(new VH_data(sootmethod.getSignature()));//���оͰ����������ͷ�����
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
					System.out.println("�����а������������ϣ��������Ϊ��     "+appclass.size());

	}


}
