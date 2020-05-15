/**
 *
 */
package Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import BaseData.B;
import BaseData.McallGraph;
import BaseData.VH_data;
import Pattern1.Analysis_Bitmap;

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
import soot.util.Chain;
import soot.util.queue.QueueReader;

import util.ParametersGet;

/**
 * @author liwenjie
 *
 */
public class Test {

	public static void TestAnalysis(Iterator<SootClass> scIterator) throws Exception{

		//������б���
		while(scIterator.hasNext()){
			SootClass currentClass = scIterator.next();
			System.out.println("Start to Analysis Class:       "+currentClass.getName().toString());

			Boolean bool=false;
                                                                                     //verify_super_class(interface_class,a)
			if((currentClass.isApplicationClass())&&(!(currentClass.isInterface()))&&(true)){
				//(currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
		
				bool=true;
		
			}
			if(bool){
				Chain<SootClass> interface_class=currentClass.getInterfaces();
				String strClassName = currentClass.getName();

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);//���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();//��ȡ����ķ����б��ǰ��ճ���ʵ�ʷ������Ⱥ������е�

				//��ÿ����ķ������б���
				for(int i = 0;i<smList.size();i++){
					if(smList.get(i).isConcrete()){//�жϷ������Ƿ���ȷ
						//�Է������з���
						SootMethod sootmethod=smList.get(i);
						System.out.println("Start to Analysis the method:      "+sootmethod.getSignature());
						Body body = sootmethod.retrieveActiveBody();
						System.out.println("Body:               -----------------");
						System.out.println(body.toString());
						System.out.println("Body End-----------------------------");
						Iterator<Unit> unitIterator = body.getUnits().iterator();

						// ���������е����
						while (unitIterator.hasNext()) {
							Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement

							Stmt stmt = (Stmt) u;
							int lineNumber = Analysis_Bitmap.getLinenumber(u);//����к�
							System.out.println("LineNumber:      "+lineNumber);
							System.out.println("u.toString():    "+u.toString());

							//����
							//ParametersGet.getInvokePara(stmt);
						}
				    }else{
			    		System.out.println("The method:  "+smList.get(i).toString()+"        is no Concrete");
			    	}
				}
			}
		}

		}

}
