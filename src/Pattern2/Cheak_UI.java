/**
 *
 */
package Pattern2;

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
import Pattern1.Data1;
import Pattern3.Scene_get;
import Perf.Do_Write;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PointsToAnalysis;
import soot.PointsToSet;
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

/**
 * @author liwenjie
 *
 */
public class Cheak_UI {

	Set<McallGraph> Heavy_Call = new HashSet<McallGraph>();// �洢��⵽��heavy API����

	//������APP���༯��
	public static Scene_get  getCallGraph(Iterator<SootClass> scIterator) throws Exception {
				
		Data2 data = new Data2();
		data.addData();

		List<SootMethod> allmList = new ArrayList<SootMethod>();//����APP�е����з���

		// ������б���
		while (scIterator.hasNext()) {
			SootClass currentClass = scIterator.next(); // ��ǰ��������
			System.out.println("Start to Analysis Class-------------->       "
					+ currentClass.getName().toString());

			Boolean bool = false;

			// �����ǰ��Ϊ�����е����ҷǽӿ���
			if ((currentClass.isApplicationClass())
					&& (!(currentClass.isInterface())) && (true)) {
				// (currentClass.isApplicationClass())&&(!(currentClass.isInterface()))
				bool = true;
			}
			if (bool) {
				SootClass localSootClass = Scene.v().forceResolve(
						currentClass.getName(), 3);// ���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();// ��ȡ����ķ����б��ǰ��ճ���ʵ�ʷ������Ⱥ������е�
				// ��ÿ����ķ������б���
				for (int i = 0; i < smList.size(); i++) {
					if (smList.get(i).isConcrete()) {// �жϷ������Ƿ���ȷ
						allmList.add(smList.get(i));

					} else {
						System.out.println("The method:  "
								+ smList.get(i).toString()
								+ "            is no Concrete");
					}
				}
			}
		}

		List<SootMethod> entryPoints = new ArrayList<>();
		entryPoints = allmList;
		System.out.println("The size of entryPoints:  " + entryPoints.size());

		Scene.v().setEntryPoints(entryPoints);// change the set of entry point methods used to build the call graph
		try {
			System.out.println("Begin runPacks");
			CHATransformer.v().transform();
			PackManager.v().runPacks();
			System.out.println("End runPacks");
		} catch (RuntimeException localRuntimeException) {
			System.err.println("����ͼ����ʧ��[error] " + localRuntimeException.getMessage());
			System.out.println("����ͼ����ʧ��");
			Analysis_Bitmap.getCallGraph=0;
		}

		CallGraph callGraph = Scene.v().getCallGraph();// ��ó������ͼ
		System.out.println("Call graph size:   " + callGraph.size());// �������ͼ��С,
		
		//�����������õĵط�����Ҫ��һ������
		PointsToAnalysis pointToAnalysis=Scene.v().getPointsToAnalysis();
		//PointsToSet opintToSet=pointToAnalysis.reachingObjects(arg0);
		Scene_get secne_get=new Scene_get(callGraph,pointToAnalysis);
		
		
		
		
		//Scene.v().getReachableMethods();
		

		return secne_get;
	}


	// ѭ������--�ݹ�ķ�ʽ
	public static int traversal(SootMethod sootmethod,CallGraph callGraph,int count) {
		Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);
		// ����into��������
		if (edgeInto != null) {
			while (edgeInto.hasNext()) {
				int lineNumber = 0;
				Edge to = edgeInto.next();
				Stmt stmt = to.srcStmt();// ������
				Unit u = (Unit) stmt;
				SootMethod src = (SootMethod) to.getTgt();
				System.out.println(sootmethod.getSignature()+"     called by      "+src.getSignature());
				try {
					List<Tag> tagList = stmt.getTags();// ��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
					for (int n = 0; n < tagList.size(); n++) {
						Tag t = tagList.get(n);
						if (t instanceof LineNumberTag) {// �ҵ�LineNumberTag
							lineNumber = ((LineNumberTag) t).getLineNumber();
						}
					}
					System.out.println("LineNumber:    "+lineNumber+"      "+stmt.getClass());
				} catch (Exception localException2) {
					System.out.println("Wrong on stmt.getTags()");
				}
					// �ж��Ƿ���UI�߳�
					// if((lineNumber!=0)&&(!(sootmethod.toString().equals(src.toString())))&&(!(src.getSignature().contains("void run()")))&&(!(src.getSignature().contains("void start()")))){
					if ((src.toString().contains("doInBackground"))||(src.toString().contains("run"))) {
						System.out.println("The opearition is not on UI thread");//��֧��������
					}else{
						if(src.toString().startsWith("on")){
							System.out.println("The opearition is on UI thread");//��֧��������
							count++;
						}else{
							count=traversal(src, callGraph,count);//����������֧
						}
					}
			}
		}
		return count;
	}

	
	/**
	 * ��������Ƿ�λ��UI�߳���
	 * **/
	public static Result_Pattern2 traverse_UI_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<String> UI_method=new ArrayList<String>();  //�洢ÿ��decode��ص�UI����
		ArrayList<String> child_method=new ArrayList<String>();  //�洢ÿ��decode��ص�child����
		int UI_count=0;
		int Child_count=0;
		
		System.out.println("The start method is: "+start.getName());
		ArrayList<SootMethod> store= new ArrayList<SootMethod>();//�洢�Ѿ��������ķ���
		store.add(start);
				
		boolean figer=true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		
		//�жϵ�ǰstart�����Ƿ�ʹ������̻߳�UI�߳�
				if((start.getName().toString().contains("doInBackground"))||(start.getName().toString().equals("run"))){
					System.out.println("The opearition is on child thread");//��֧��������
					child_method.add(start.getSignature());
					Child_count++;
					figer=false;
				}
				if((start.getName().toString().startsWith("on"))||(start.getName().equals("getView"))){
					UI_method.add(start.getSignature());
					System.out.println("The opearition is on UI thread");
					UI_count++;
					figer=false;
				}
				
		//���Ŀǰ�ʹ���frequency�����У��Ͳ�����ջ�ˡ�
		if(figer){		
		stack.add(start);// ��ʼ��ջ
		while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
			int size = stack.size();
			System.out.println("traverse_UI_analysis the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); //get()�Ǵ�0 ��ʼ��,����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
			store.add(sootmethod);//��ʾ�÷����Ѿ�����������
			System.out.println("get the method from stack:  "+ sootmethod.getName());
			stack.remove(size - 1);
			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //��õ��õ�ǰ�����ļ���
			if (edgeInto!= null) {
				// ��������
				while (edgeInto.hasNext()) {
					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();
					System.out.println("UI_analysis_Invoke statement:   "+sootmethod.getSignature()+"            "+from.getSignature());
					boolean add_figer=true;
					//�ж��Ƿ�λ�����߳�
					String str=from.getName();
					boolean one=str.startsWith("on");
					boolean one1=str.equals("getView");
					boolean two1=str.equals("doInBackground");
					boolean three1=str.equals("run");
					
					if(one||one1){
						UI_method.add(from.getSignature());
						System.out.println("The opearition is on UI thread");//��֧��������
						UI_count++;
						add_figer=false;
					}
				
					if(two1 || three1){
						child_method.add(from.getSignature());
						System.out.println("The opearition is on Child thread");//��֧��������
						Child_count++;
						add_figer=false;
					}

					if((add_figer)&&(!(store.contains(from)))&&(!(stack.contains(from)))){//�ų�ѭ������--�������ظ���ӣ�û�б�Ҫ���п���
							stack.add(from);// ��ӵ�ջ��
							System.out.println("Add to stack");
					}
				}
			}
			
		}
		}

		System.out.println("count_UI:  "+UI_count);
		return new Result_Pattern2(UI_method,child_method,UI_count,Child_count);
	}
	

	

}
