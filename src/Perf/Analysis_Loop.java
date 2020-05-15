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

	Set<String> appclass=new HashSet<String>();//�Է���������м���
	Set<Insert> insert_store=new HashSet<Insert>();//�洢���������insert����

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{
		//��ֵ����
		String[] loop1=data.Loop1;
		String[] loop2=data.Loop2;


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

//�Ե�ǰ���еķ������б�������
				int line_number_figer=0;//�кű��
				for(int i = 0;i<smList.size();i++)
				{
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());
					boolean figer1[]={false,false};
					boolean figer2[]={false,false};
					int[] insert_lineNumber1={0,0};//��¼�к�
					int[] insert_lineNumber2={0,0};//��¼�к�

					if(sootmethod.isConcrete())
					{//�жϷ������Ƿ���ȷ
						figer1[0]=false;
						figer1[1]=false;
						figer2[0]=false;
						figer2[1]=false;

						insert_lineNumber1[0]=0;
						insert_lineNumber1[1]=0;
						insert_lineNumber2[0]=0;
						insert_lineNumber2[1]=0;


						System.out.println("The method is concrete��"+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

					    Body body = sootmethod.retrieveActiveBody();//Ϊ��������������
					    //System.out.println("Body:   "+body.toString());//�����֤�Ƿ�����������÷���û�л�ȡ

						Iterator<Unit> unitIterator = body.getUnits().iterator();//��ȡ���������ĵ�����

						while(unitIterator.hasNext())
						{//���������е�ÿ����䣨һ�������ܲ��Ϊ���unit��
							/*
							 * �ж�ÿһ��ָ��unit���Ƿ��������ص������Ϣ��
							 * */
							SootMethod sm=null;
							int lineNumber=0;

							Unit u = unitIterator.next();    //soot���ýӿ�Unit��ʾstatement
							Stmt stmt=(Stmt) u;

							//�����еĵ��������з�����һ����new��һ���Ƕ�ͼ�δ���API�ĵ��á���
							try{
							InvokeExpr invokestmt=stmt.getInvokeExpr();
							sm=invokestmt.getMethod();       //�����õķ���


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

							//-->���е��÷�����ƥ��
							for(int j=0;j<loop1.length;j++){

								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).contains(loop1[j])){
									figer1[lineNumber]=true;//����к�����Ϊtrue

								}
							}

							//-->���е��÷�����ƥ��
							for(int j=0;j<loop2.length;j++){

								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).contains(loop2[j])){
									figer2[lineNumber]=true;

								}
							}
							//�����ж�(1)ͬʱ�������ú�ת������2���к�Ҫ��ͬ
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
		//��������ѭ������

		System.out.println("�����а������������ϣ��������Ϊ��     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
