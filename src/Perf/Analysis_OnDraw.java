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
 * ˼·�� ������Ҫ�Ȼ�����еĶ�����ù�ϵ�����Ҷ���ط������Ƿ���ڶ��󴴽���䣬���ڶ��󴴽��ĵ�ַ���д洢��
 *       ����Ҫʶ����󴴽�����ˣ��ڻ�õ���ͼ��ͬʱ������Ҫ�Ե��÷����е�ÿһ�������з���ʶ����ô
 *       �ܿ�������Ҫͨ�����������������õ�����Ϣ��---���ڷ���ÿһ��������ʱ�򣬼�¼���ù�ϵ��
 *
 *  ���ڷ��������¼���1����������������������а�����Щ�ؼ��ʣ�2�������Ƿ���invoke����������������жϣ��У���
 *
 *  ���ַ���������1����õ���ͼ��������漰�ķ������з����жϣ�
 *               2����ÿһ���������з������ռ����ù�ϵ������δ����ǿ������õģ�
 *
 */
public class Analysis_OnDraw {

	Set<String> appclass=new HashSet<String>();//�Է���������м���
	Set<On_Draw> on_Draw=new HashSet<On_Draw>();//�洢���������insert����
	Set<McallGraph> callGraph=new HashSet<McallGraph>();//�洢����������Ϣ

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{

		//�洢on��ͷ�ķ���
		ArrayList<SootMethod> onsmList=new ArrayList<SootMethod>();
		int count_method=0;

		//������б���
		while(scIterator.hasNext())
		{
			SootClass currentClass = scIterator.next();    //��ǰ��������
			Boolean bool=false;
			//�����ǰ��Ϊ�����е����ҷǽӿ���
			if((currentClass.isApplicationClass())&&(!(currentClass.isInterface())))
			{
				//(currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool=true;
			}
			if(true)
			{
				String strClassName = currentClass.getName();
				appclass.add(strClassName);
				System.out.println("��ʼ���ࣺ     "+strClassName+"    ���з���");

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);//���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();//��ȡĳ����ķ����б��ǰ��ճ���ʵ�ʷ������Ⱥ������е�

		        System.out.println("�ܵķ���������"+smList.size());
				count_method=count_method+smList.size();

				for(int i = 0;i<smList.size();i++)
				{//�����еķ������б���
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());

					if(sootmethod.isConcrete())
					{//�жϷ������Ƿ���ȷ

						System.out.println("The method is concrete��"+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

					    Body body = sootmethod.retrieveActiveBody();//Ϊ��������������
					    //System.out.println("Body:   "+body.toString());//�����֤�Ƿ�����������÷���û�л�ȡ

						Iterator<Unit> unitIterator = body.getUnits().iterator();//��ȡ���������ĵ�����

						while(unitIterator.hasNext())
						{//���������е�ÿ����䣨һ�������ܲ��Ϊ���unit��
							SootMethod sm=null;
							Unit u = unitIterator.next();    //soot���ýӿ�Unit��ʾstatement
							Stmt stmt=(Stmt) u;
							System.out.println("Stmt:   "+u.toString());

							//�����еĵ��������з���
							try{
								int lineNumber=0;
								//����к�
								{
									List<Tag> tagList = u.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
									for(int n = 0;n<tagList.size();n++){
										Tag t = tagList.get(n);
										if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
											lineNumber=((LineNumberTag) t).getLineNumber();

									    }
								    }
								}

								if(stmt.containsInvokeExpr()){
								//�洢��������
							    InvokeExpr invokestmt=stmt.getInvokeExpr();
							    sm=invokestmt.getMethod();       //�����õķ���
							    //callGraph.add(new McallGraph(sootmethod.getSignature(),sm.getSignature(),StringOf(lineNumber)));

								}

							    //���󴴽��¼�ʶ��
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
						//��������������������Ϣ�洢

					}else{
						System.out.println("The method is not Concrete");
					}

				}
			}
		}
		System.out.println("The number of method in this application:  "+count_method);
		System.out.println("The number of On_Draw Problems in this application:  "+on_Draw.size());
		//��������ѭ������

		System.out.println("�����а������������ϣ��������Ϊ��     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
