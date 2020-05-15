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

	Set<String> appclass=new HashSet<String>();//�Է���������м���
	Set<Insert> insert_store=new HashSet<Insert>();//�洢���������insert����

	void StartAnalysis(Iterator<SootClass> scIterator,Data data) throws Exception
	{
		//��ֵ����
		String[] insert=data.insert;

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

//				for(int i = 0;i<smList.size();i++){
//				    if (smList.get(i).getName().startsWith("on")) {//����ƥ�䣬ƥ��android�е���Ҫ�������ѵ�ֻ����Ҫ�ķ������з�����
//				    	if(smList.get(i).isConcrete()){//�жϷ������Ƿ���ȷ
//				    	System.out.println("ON start method name:   "+smList.get(i).getName());
//				        onsmList.add(smList.get(i));
//				    	}
//				    	else{
//				    		System.out.println("The class:  "+smList.get(i).toString()+"            is no Concrete");
//				    	}
//				    }
//				}

				for(int i = 0;i<smList.size();i++)
				{//�����еķ������б���
					SootMethod sootmethod=smList.get(i);
					System.out.println("Start to analysis the method: " + sootmethod.getSignature());
					boolean figer[]={false,false,false};
					int insert_lineNumber[]={0,0,0};

					if(sootmethod.isConcrete())
					{//�жϷ������Ƿ���ȷ
						figer[0]=false;
						figer[1]=false;
						figer[2]=false;
						insert_lineNumber[0]=0;
						insert_lineNumber[1]=0;
						insert_lineNumber[2]=0;

						System.out.println("The method is concrete��"+sootmethod.getName());
						System.out.println("The method is :  "+sootmethod.getSignature());

						/*�ǲ��������ж�һ�����������Ƿ��������ص���䣬����Է������ڵ����������з������õ��������ڵ��к�
						 * ����Ϣ��
						 * Ҳ����Ҫ��ȫƥ�䣬�ؼ���Ҳ����
						 *
						 * Ҳ���ǲ鿴��������Ƿ����Insert���ĵ��ã����Խ�insert���������в�����ʽ�����ǣ�����
						 * �Ͳ��ǽ�����insert�ؼ����ˣ����׼ȷһЩ��
						 *
						 * Ҳ�����������
						 * ���ÿһ������������ĵ��ù�ϵ���������Щ���ù�ϵ���б����������Ƿ������Ҫ���ҵģ�
						 * ������඼���ڣ�������ȷ�ģ���������ڣ����Ǵ�������ġ�
						 *
						 * ���⣬����ж��Ƿ񱻰�Χ��������к�������ȷ������
						 * */
					    Body body = sootmethod.retrieveActiveBody();//Ϊ��������������
					    //System.out.println("Body:   "+body.toString());//�����֤�Ƿ�����������÷���û�л�ȡ

						Iterator<Unit> unitIterator = body.getUnits().iterator();//��ȡ���������ĵ�����

						while(unitIterator.hasNext())
						{//���������е�ÿ����䣨һ�������ܲ��Ϊ���unit��
							SootMethod sm=null;
							int lineNumber=0;

							Unit u = unitIterator.next();    //soot���ýӿ�Unit��ʾstatement
							Stmt stmt=(Stmt) u;

							//�����еĵ��������з���
							try{
							InvokeExpr invokestmt=stmt.getInvokeExpr();
							sm=invokestmt.getMethod();       //�����õķ���


							//-->���е��÷�����ƥ��
							for(int j=0;j<insert.length;j++){
								//System.out.println("insert[j]:   "+insert[j]);
								System.out.println("sm.toString():   "+sm.toString());
								if((sm.toString()).equals(insert[j])){
									figer[j]=true;
									System.out.println("Here!");
									//����к�
									{
										List<Tag> tagList = u.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
										for(int n = 0;n<tagList.size();n++){
											Tag t = tagList.get(n);
											if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
												lineNumber=((LineNumberTag) t).getLineNumber();
												insert_lineNumber[j]=lineNumber;//�洢�к�
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
//								//��ȡ�к�
//								lineNumber=0;
//								List<Tag> tagList = u.getTags();//��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����ѽ��������
//								for(int n = 0;n<tagList.size();n++){
//									Tag t = tagList.get(n);
//									if(t instanceof LineNumberTag){//�ҵ�LineNumberTag
//										lineNumber=((LineNumberTag) t).getLineNumber();
//									//System.out.println("uint is "+u +"\nline is "+((LineNumberTag) t).getLineNumber() + "\n");
//								    }
//								       System.out.println("From:   "+sootmethod.getSignature());
//								       System.out.println("To:     "+sm.getSignature());
//								       System.out.println("LineNumber:    "+lineNumber);
//							    }
//							}
					}
						//��������������������Ϣ�洢
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
		//��������ѭ������

		System.out.println("�����а������������ϣ��������Ϊ��     "+appclass.size());
		Do_Write do_writer=new Do_Write();

		//do_writer.wirteToMcallGraph(mcallGraph);


	}


}
