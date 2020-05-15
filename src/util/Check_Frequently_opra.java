/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.queue.QueueReader;
import Pattern1.Analysis_Bitmap;
import Pattern2.Result_Pattern2_back;
import Pattern2.Thread_info;
import Pattern3.Pattern34_result;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.callgraph.Targets;


/**
 * @author Dell GridView�Ĵ��ڱ�ʾ��Ҫʹ�õ�������ͼƬ
 *
 */
public class Check_Frequently_opra {

	public static String[] feature = { "onDraw(android.graphics.Canvas)",
			"getView(int,android.view.View,android.view.ViewGroup)", };

	/**
	 * 
	 * @param sootmethod
	 */
	public static boolean find_frequen(SootMethod sootmethod) {
		boolean result = false;
		for (int i = 0; i < feature.length; i++) {
			if (sootmethod.getSignature().contains(feature[i])) {
				System.out.println("Frequen Method:" + i + "    "+ sootmethod.getSignature());
				Analysis_Bitmap.freq_count[i]++;
				result = true;

			}
		}
		return result;

	}

	/**
	 * ���ظ�decode����������frequent �����ļ���
	 * �ؼ���ע���漰���߳�ʱ��ν��з�����
	 * **/
	public static ArrayList<String> traverse_frequent_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// �洢�Ѿ��������ķ���
		store.add(start);
		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean add_figer = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		// �жϵ�ǰstart�����Ƿ�ʹ���frequency�������ڵķ���
		for (int i = 0; i < feature.length; i++) {
			if ((start.getSignature().contains(feature[i]))) {
				System.out.println("The decode method related with frequency:"+ i + "----------" + start.getSignature() + "   "	+ "   " + feature[i]);// ��֧��������
				figer = false;
				result.add(start.getSignature());
				Analysis_Bitmap.freq_decode[i]++;
			}
		}

		if (figer) {
			stack.add(start);// ��ʼ��ջ
			while (!(stack.isEmpty())) {
				int size = stack.size();
				System.out.println("traverse_frequent_analysis the size of stack:  "+ size);
				SootMethod sootmethod = stack.get(size - 1); //get()�Ǵ�0 ��ʼ��,����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
				store.add(sootmethod);// ��ʾ�÷����Ѿ�����������
				System.out.println("get the method from stack:  "+ sootmethod.getName());
				stack.remove(size - 1);
				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod); // ���õ�ǰ�����ļ���
				if (edgeInto != null) {
					// ��������
					while (edgeInto.hasNext()) {
						add_figer=true;
						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// ���from
						System.out.println("Invoke statement:   "+ sootmethod.getSignature() + "            "+ from.getSignature());
						// �ж��Ƿ���frequent method����
						for (int i = 0; i < feature.length; i++) {
							String method_name = feature[i];
							if (from.getSignature().toString().contains(method_name)) {
								System.out.println("The decode method related with frequency:"+ i+ "-----"+ start.getSignature()+ "   "+ from.getSignature());
								if(!(result.contains(from.getSignature()))){
									result.add(from.getSignature());
								}
								add_figer = false;
							}
						}
						if (add_figer) {
							if ((!(store.contains(from)))&&(!(stack.contains(from)))) {// �ų�ѭ������--��û�б�Ҫ���п���
								stack.add(from);
								System.out.println("Add to stack");
							} 
						}
					}
				}

			}
		}

		return result;
	}
	
	/**
	 * ���ظ�decode����������frequent �����ļ���
	 * **/
	public static ArrayList<String> Forward_traverse_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// �洢�Ѿ��������ķ���
		store.add(start);
		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean add_figer = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		// �жϵ�ǰstart�����Ƿ�ʹ���frequency�������ڵķ���
		for (int i = 0; i < feature.length; i++) {
			if ((start.getSignature().contains(feature[i]))) {
				System.out.println("The decode method related with frequency:"+ i + "----------" + start.getSignature() + "   "	+ "   " + feature[i]);// ��֧��������
				figer = false;
				result.add(start.getSignature());
				Analysis_Bitmap.freq_decode[i]++;
			}
		}

		if (figer) {
			stack.add(start);// ��ʼ��ջ
			while (!(stack.isEmpty())) {
				int size = stack.size();
				System.out.println("traverse_frequent_analysis the size of stack:  "+ size);
				SootMethod sootmethod = stack.get(size - 1); //get()�Ǵ�0 ��ʼ��,����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
				store.add(sootmethod);// ��ʾ�÷����Ѿ�����������
				System.out.println("get the method from stack:  "+ sootmethod.getName());
				stack.remove(size - 1);
				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod); // ���õ�ǰ�����ļ���
				if (edgeInto != null) {
					// ��������
					while (edgeInto.hasNext()) {
						add_figer=true;
						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// ���from
						System.out.println("Invoke statement:   "+ sootmethod.getSignature() + "            "+ from.getSignature());
						// �ж��Ƿ���frequent method����
						for (int i = 0; i < feature.length; i++) {
							String method_name = feature[i];
							if (from.getSignature().toString().contains(method_name)) {
								System.out.println("The decode method related with frequency:"+ i+ "-----"+ start.getSignature()+ "   "+ from.getSignature());
								if(!(result.contains(from.getSignature()))){
									result.add(from.getSignature());
								}
								add_figer = false;
							}
						}
						if (add_figer) {
							if ((!(store.contains(from)))&&(!(stack.contains(from)))) {// �ų�ѭ������--��û�б�Ҫ���п���
								stack.add(from);
								System.out.println("Add to stack");
							} 
						}
					}
				}

			}
		}

		return result;
	}
	
	/**
	 * ���������Ƚϴ󣬽����ʺ϶��ض������������֤����ʱʹ��
	 * 
	 * start: ·��������㷽��
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> forward_path_analysis_ArrayList(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
		for (int i = 0; i < end.size(); i++) {
			if ((start.getSignature().contains(end.get(i)))) {
				System.out.println("Find the end method");
				figer = false;//���ټ�������
				result1.add(start.getSignature());
				result_set.add(result1);
			}
		}
		System.out.println("Start to perform forward_path_analysis(start):    "+start.getSignature());

		if (figer) {
			while(true){
				end_figer=true;
				next_method=start;//�ع����
				//��ʼ������
				ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
				ArrayList<String> result = new ArrayList<String>();//�洢��·����������
				System.out.println("Enter while-1");
				while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
					System.out.println("Enter while-2");
					store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
					Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
					Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
					
					//��Ϣ���
					int edge_count=0;
					System.out.println("next_method:   "+next_method.getSignature());
					while (edgeOut_temp.hasNext()) {//���ڵ����
						Edge to = edgeOut_temp.next();
						SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
						edge_count++;
						System.out.println("edge:    "+to_meth.getSignature());
					}
					System.out.println("The count of edge:   "+edge_count);
					
					if (edgeOut != null) {
						leaf_figer=true;//��ԭ��ʼֵ
						while (edgeOut.hasNext()) {
							Edge to = edgeOut.next();
							SootMethod to_meth = (SootMethod) to.getTgt();// ���from
							System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
							System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
							if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
									(!(store_leaf.contains(to_meth)))){
								store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
								leaf_figer=false;
								System.out.println("forward_path_analysis:    "+to_meth.getSignature());
								boolean temp=find_target_ArrayList(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
								if(temp){//���λ��target����
									next_method=null;//�ٴδ�start������ʼ����
									//�洢�����Ϣ
									result.add(to_meth.getSignature());
									result_set.add(result);
									//��ʾ���Ҷ�ڵ��Ѿ�����
									store_leaf.add(to_meth);
									break;
								}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
									next_method=to_meth;
									break;
								}									
								
							}
						}//edgeOut.hasNext()��������
						//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
						System.out.println("leaf_figer:  "+leaf_figer);
						if(leaf_figer){
							System.out.println("store_leaf.add:    "+next_method.getSignature());
							store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
							next_method=null;
						}
					}else{
						System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
						store_leaf.add(next_method);
					}

				}
				//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
				Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
				if (edgeOut_start != null) {
					while (edgeOut_start.hasNext()) {
						Edge to = edgeOut_start.next();
						SootMethod to_meth = (SootMethod) to.getSrc();// ���from
						//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
						if(to_meth.getDeclaringClass().isApplicationClass()){
							if(!(store_leaf.contains(to_meth))){
								end_figer=false;
							}
						}
					}
				}
				
				if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
					break;
				}
				
			}
			
		}

		
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ󣬽����ʺ϶��ض������������֤����ʱʹ��
	 * 
	 * start: ·��������㷽��
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> forward_path_analysis_Set(SootMethod start, Set<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
		for (String str: end) {
			if ((start.getSignature().contains(str))) {
				System.out.println("Find the end method");
				figer = false;//���ټ�������
				result1.add(start.getSignature());
				result_set.add(result1);
			}
		}
		System.out.println("Start to perform forward_path_analysis(start):    "+start.getSignature());

		if (figer) {
			while(true){
				end_figer=true;
				next_method=start;//�ع����
				//��ʼ������
				ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
				ArrayList<String> result = new ArrayList<String>();//�洢��·����������
				System.out.println("Enter while-1");
				while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
					System.out.println("Enter while-2");
					store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
					Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
					Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
					
					//��Ϣ���
					int edge_count=0;
					System.out.println("next_method:   "+next_method.getSignature());
					while (edgeOut_temp.hasNext()) {//���ڵ����
						Edge to = edgeOut_temp.next();
						SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
						edge_count++;
						System.out.println("edge:    "+to_meth.getSignature());
					}
					System.out.println("The count of edge:   "+edge_count);
					
					if (edgeOut != null) {
						leaf_figer=true;//��ԭ��ʼֵ
						while (edgeOut.hasNext()) {
							Edge to = edgeOut.next();
							SootMethod to_meth = (SootMethod) to.getTgt();// ���from
							System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
							System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
							if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
									(!(store_leaf.contains(to_meth)))){
								store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
								leaf_figer=false;
								System.out.println("forward_path_analysis:    "+to_meth.getSignature());
								boolean temp=find_target_Set(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
								if(temp){//���λ��target����
									next_method=null;//�ٴδ�start������ʼ����
									//�洢�����Ϣ
									result.add(to_meth.getSignature());
									result_set.add(result);
									//��ʾ���Ҷ�ڵ��Ѿ�����
									store_leaf.add(to_meth);
									break;
								}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
									next_method=to_meth;
									break;
								}									
								
							}
						}//edgeOut.hasNext()��������
						//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
						System.out.println("leaf_figer:  "+leaf_figer);
						if(leaf_figer){
							System.out.println("store_leaf.add:    "+next_method.getSignature());
							store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
							next_method=null;
						}
					}else{
						System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
						store_leaf.add(next_method);
					}

				}
				//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
				Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
				if (edgeOut_start != null) {
					while (edgeOut_start.hasNext()) {
						Edge to = edgeOut_start.next();
						SootMethod to_meth = (SootMethod) to.getSrc();// ���from
						//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
						if(to_meth.getDeclaringClass().isApplicationClass()){
							if(!(store_leaf.contains(to_meth))){
								end_figer=false;
							}
						}
					}
				}
				
				if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
					break;
				}
				
			}
			
		}

		
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ󣬶�һ��decode�����й����������з���
	 * 
	 * start_set: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_Set(ArrayList<SootMethod> start_set, Set<String> end, CallGraph callGraph) {
		String args_check="android.graphics.Bitmap";
		String args_check1="android.graphics.drawable.BitmapDrawable";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//���ټ�������
					result1.add("In ownself:     ");
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		}
		
		System.out.println("Start to perform forward_path_analysis(start_set):    "+start_set.toString());
		
		if(true){
			for(int i=0;i<start_set.size();i++){
				SootMethod start=start_set.get(i);
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						ArrayList<String> result = new ArrayList<String>();//�洢��·����������
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//���ڵ����
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// ���from
									System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									List args_type=to_meth.getParameterTypes();
									boolean args_bitmap=false;
									for(int jj=0;jj<args_type.size();jj++){
										if((args_type.get(jj).toString().contains(args_check))||(args_type.get(jj).toString().contains(args_check1))){
											System.out.println("to_meth:   "+to_meth.getSignature());
											System.out.println("Args is bitmap or BitmapDrawable:   "+args_type.get(jj).toString());
											args_bitmap=true;
										}
									}
									if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))&&args_bitmap){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_Set(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(temp){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//�洢�����Ϣ
											result.add(to_meth.getSignature());
											result_set.add(result);
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							break;
						}
						
					}			
			}
		}
		

		

		
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ󣬶�һ��decode�����й����������з���
	 * 
	 * start_set: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_Set_inbitmap(ArrayList<SootMethod> start_set, Set<String> end, CallGraph callGraph) {
		String args_check="android.graphics.BitmapFactory$Options";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//���ټ�������
					result1.add("In ownself:     ");
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		}
		
		System.out.println("Start to perform forward_path_analysis(start_set):    "+start_set.toString());
		
		if(true){
			for(int i=0;i<start_set.size();i++){
				SootMethod start=start_set.get(i);
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						ArrayList<String> result = new ArrayList<String>();//�洢��·����������
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						System.out.println();
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//���ڵ����
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// ���from
									System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									List args_type=to_meth.getParameterTypes();
									boolean args_bitmap=false;
									for(int jj=0;jj<args_type.size();jj++){
										if((args_type.get(jj).toString().contains(args_check))){
											System.out.println("to_meth:   "+to_meth.getSignature());
											System.out.println("Args is opintions:   "+args_type.get(jj).toString());
											args_bitmap=true;
										}
									}
									if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))&&args_bitmap){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_Set(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(temp){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//�洢�����Ϣ
											result.add(to_meth.getSignature());
											result_set.add(result);
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							break;
						}
						
					}			
			}
		}
		
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ󣬶�һ��decode�����й����������з���
	 * 
	 * start_set: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_ArrayList(ArrayList<SootMethod> start_set, ArrayList<String> end, CallGraph callGraph) {
		String args_check="android.graphics.Bitmap";
		String args_check1="android.graphics.drawable.BitmapDrawable";
		String args_check2="android.graphics.drawable.Drawable";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//���ټ�������
					result1.add("In ownself:     ");
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		}
		
		System.out.println("Start to perform forward_path_analysis(start_set):    "+start_set.toString());
		
		if(figer){
			for(int i=0;i<start_set.size();i++){
				SootMethod start=start_set.get(i);
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						ArrayList<String> result = new ArrayList<String>();//�洢��·����������
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//���ڵ����
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// ���from
									System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									List args_type=to_meth.getParameterTypes();
									boolean args_bitmap=false;
									for(int jj=0;jj<args_type.size();jj++){
										if((args_type.get(jj).toString().contains(args_check))||(args_type.get(jj).toString().contains(args_check1))||(args_type.get(jj).toString().contains(args_check2))){
											System.out.println("to_meth:   "+to_meth.getSignature());
											System.out.println("Args is bitmap:   "+args_type.get(jj).toString());
											args_bitmap=true;
										}
									}
									if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))&&args_bitmap){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_ArrayList(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(temp){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//�洢�����Ϣ
											result.add(to_meth.getSignature());
											result_set.add(result);
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							break;
						}
						
					}			
			}
		}
		

		

		
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ��ҵ�һ��decode�����й�����frequency����
	 * 
	 * start: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> frequency_backward_path_analysis_ArrayList(Set<Thread_info> thread_data_set,SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		System.out.println("The size of thread_data_set:  "+thread_data_set.size());
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�������getview��onDraw
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("frequency: Find the end method");
					figer = false;//���ټ�������
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		
		System.out.println("Start to perform backward_path_analysis for frequency method:    "+start.toString());	
		if(figer){
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						SootMethod child_doInbackground=null;
						SootMethod child_execute = null;
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						//ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
						ArrayList<String> result = new ArrayList<String>();//�洢��·����������
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeInto_temp.hasNext()) {//���ڵ����
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();
								edge_count++;
								//System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// ���from
									System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									//if(isApplication||isThread){
									if(isApplication){
										//System.out.println("isApplication||isThread is true");
									if((!(store_once.contains(to_meth)))&&(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										System.out.println("backward_path_analysis:    "+to_meth.getName());
										boolean temp=find_target_ArrayList_frequency(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(temp){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//�洢�����Ϣ
											result.add(to_meth.getSignature());
											result_set.add(result);
											System.out.println("result of frequency:  "+result.toString());
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											//leaf_figer=true;
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������   backward_path_analysis:    doInBackground   run
											//�����жϷ�������������̣߳���next_methodת�Ƶ��̵߳ĵ��÷����У�������ǣ����������
											if((to_meth.getName().equals("doInBackground"))||(to_meth.getName().equals("run"))){
												
												System.out.println("");
												for(Thread_info thread_info: thread_data_set){
													SootMethod sootmethod=thread_info.getSootmethod();
													String stmt=thread_info.getStmt();
													String classname=to_meth.getDeclaringClass().getName();
													//System.out.println("stme:  "+stmt);
													//System.out.println("to_meth.getSignature():  "+to_meth.getSignature());
													//System.out.println("to_meth.getDeclaringClass().getName():  "+classname);
													if(stmt.contains(classname)){
														System.out.println("Find the correspond of execute and doinbackground!");
														next_method=sootmethod;
														result.add(to_meth.getSignature());
														result.add(sootmethod.getSignature());
														store_once.add(sootmethod);
														
														child_doInbackground=to_meth;
														child_execute=sootmethod;
														break;
														
													}
												}
												
												break;
												
											}else{
												next_method=to_meth;
												result.add(to_meth.getSignature());
												break;
											}
												
										}									
										
									}
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������,����ڵ��ʾexecute���ڵķ���
									if(next_method.equals(child_execute)){
										store_leaf.add(child_doInbackground);//ͬʱdoInbackground���ڵķ������뼯��
										System.out.println("store_leaf.add:    "+child_doInbackground.getSignature());
									}
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							System.out.println("end_figer is true");
							break;
						}
						
					}	
		}
		

		

		System.out.println("The result of result_set:   "+result_set.toString());
		return result_set;
	}
	
	/**
	 * ���������Ƚϴ��ҵ�һ��decode�����й�����on��thread����
	 * 
	 * start: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static Result_Pattern2_back thread_backward_path_analysis_ArrayList_Set(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set1 = new ArrayList<ArrayList<String>>();//thread�洢��·����������
		ArrayList<ArrayList<String>> result_set2 = new ArrayList<ArrayList<String>>();//on�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//thread�洢��·����������
		ArrayList<String> result2 = new ArrayList<String>();//on�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
			for (String str: end) {
				if ((start.getName().equals(str))) {
					System.out.println("Find the end method");
					figer = false;//���ټ�������
					result1.add("Decode is call by thread:");
					result1.add(start.getSignature());
					result_set1.add(result1);
				}
			}
			if ((start.getName().startsWith("on"))||(start.getName().equals("getView"))) {
				result2.add(start.getSignature());
				result_set2.add(result2);
			}
		
		System.out.println("Start to perform backward_path_analysis for UI method:    "+start.toString());	
		if(figer){
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						ArrayList<String> result11 = new ArrayList<String>();//�洢��·����������
						ArrayList<String> result22 = new ArrayList<String>();//�洢��·����������
						result11.add("In call sequence:    ");
						result11.add(start.getSignature());
						result22.add("In call sequence:    ");
						result22.add(start.getSignature());
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							
							while (edgeInto_temp.hasNext()) {//���ڵ����
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// edgesOutOf
								edge_count++;
								//System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// ���from
									//System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									//System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									
									if((isApplication)&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										boolean find_figer=false;
										boolean find_on=false;
										boolean find_run=find_target_ArrayList1(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(find_run){
											result11.add(to_meth.getSignature());
											result_set1.add(result11);
										}
										
										if((to_meth.getName().startsWith("on"))||(to_meth.getName().equals("getView"))){
											find_on=true;
											result22.add(to_meth.getSignature());
											result_set2.add(result22);
										}
										if(find_on||find_run){
											find_figer=true;
										}
										
										if(find_figer){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
											next_method=to_meth;
											result11.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									result22.add(next_method.getSignature());
									result_set2.add(result22);
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							break;
						}
						
					}	
		}
		
		return new Result_Pattern2_back(result_set2,result_set1,result_set2.size(),result_set1.size());
	}
	
	/**
	 * ���������Ƚϴ��ҵ�һ��decode�����й�����frequency����
	 * 
	 * start: ·��������㷽������
	 * end:   ·�������յ㷽�����Ƽ���
	 * callGraph: ����ķ�������ͼ
	 * **/
	public static ArrayList<ArrayList<String>> thread_backward_path_analysis_ArrayList(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// �洢�Ѿ�����ȷ������Ҷ�ڵ�
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//�洢��·����������
		ArrayList<String> result1 = new ArrayList<String>();//�洢��·����������
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//����leaf�ڵ㼯�ϵ�������  1���ҵ�end��2��ָ��Ľڵ㶼�洢��leaf�����У�3�����into����ΪNull
		SootMethod next_method;
		// �жϵ�ǰstart�����Ƿ����Ҫ���ķ����б�
			for (String str: end) {
				if ((start.getName().equals(str))) {
					System.out.println("Find the end method");
					figer = false;//���ټ�������
					result1.add("Decode is call by thread:");
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		
		System.out.println("Start to perform backward_path_analysis for frequency method:    "+start.toString());	
		if(figer){
					while(true){
						end_figer=true;
						next_method=start;//�ع����
						//��ʼ������
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// �洢һ����ȱ����漰�Ľڵ㣬��Ҫ�˱���ѭ��
						ArrayList<String> result = new ArrayList<String>();//�洢��·����������
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//�����������next_methodΪnull  (1) �������ڵ���ǳ����е��ࣻ(2)������Ϊ���ķ���
							store_once.add(next_method);// ��ʾ�÷����Ѿ�����������
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // �����������next_method�����ķ���
							
							//��Ϣ���
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeInto_temp.hasNext()) {//���ڵ����
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//��ԭ��ʼֵ
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// ���from
									System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									
									if((isApplication)&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// ��ʾ�÷����Ѿ�����������
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_ArrayList1(to_meth,end);//��to_meth�Ƿ�Ϊλ��target��������
										if(temp){//���λ��target����
											next_method=null;//�ٴδ�start������ʼ����
											//�洢�����Ϣ
											result.add(to_meth.getSignature());
											result_set.add(result);
											//��ʾ���Ҷ�ڵ��Ѿ�����
											store_leaf.add(to_meth);
											break;
										}else{//���û���ҵ�target,��Ӹõ㿪ʼ�����������
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()��������
								//���edgeOut�ļ���Ҫô�Ǳ��أ�Ҫô�Ѿ�������,�����Ѿ�������Ҷ�ڵ㼯��
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//����Ҷ�ڵ�Ҳ��ʾ�ýڵ������ϣ�����Ҫ��������
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//�ڴ���׼����start��ʼ֮ǰ�����жϴ�start��ʼ�ĵ�һ������Ľڵ��Ƿ�ȫ�����ǣ����ȫ�����ǣ�������������
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // �����������next_method�����ķ���
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// ���from
								//�ж�����������ڵ����Ƿ�Ϊ�������漰�ķ���
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//��start�����������ӽڵ㶼����������startָ��Ľڵ㶼�Ѿ�������
							break;
						}
						
					}	
		}
		

		

		
		return result_set;
	}
	
	public static boolean find_target_ArrayList(SootMethod to_meth, ArrayList<String> end){
		// �ж��Ƿ���end����
		boolean find=false;
		for (int j = 0; j < end.size(); j++) {
			if ((to_meth.getSignature().contains(end.get(j)))) {
				System.out.println("Find the end method");
				find=true;				
			}
		}
		return find;
	}
	
	public static boolean find_target_ArrayList1(SootMethod to_meth, ArrayList<String> end){
		// �ж��Ƿ���end����
		boolean find=false;
		for (int j = 0; j < end.size(); j++) {
			if ((to_meth.getName().equals(end.get(j)))) {
				System.out.println("Find the end method");
				find=true;				
			}
		}
		return find;
	}
	
	public static boolean find_target_ArrayList_frequency(SootMethod to_meth, ArrayList<String> end){
		// �ж��Ƿ���end����
		boolean find=false;
		for (int j = 0; j < end.size(); j++) {
			if ((to_meth.getSignature().contains(end.get(j)))) {
				System.out.println("Find the end method");
				find=true;				
			}
		}
		return find;
	}
	
	public static boolean find_target_Set(SootMethod to_meth, Set<String> end){
		// �ж��Ƿ���end����
		boolean find=false;
		for (String str: end) {
			if ((to_meth.getSignature().contains(str))) {
				System.out.println("Find the end method");
				find=true;				
			}
		}
		return find;
	}

	/**
	 * �Ƿ������compress�������õķ������ڵ�����ϵ
	 * **/
	public static ArrayList<String> traverse_diskCache_analysis(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// �洢�Ѿ��������ķ���
		store.add(start);

		ArrayList<String> result = new ArrayList<String>();
		boolean figer = false;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ

		// �жϵ�ǰstart�����Ƿ�ʹ���compress�������ڵķ���
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method
					.get(i)))) {
				System.out
						.println("The decode method related with compress:   "
								+ compress_method.get(i));// ��֧��������
				figer = true;
				result.add(compress_method.get(i));
			}
		}

		stack.add(start);// ��ʼ��ջ
		while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
			int size = stack.size();
			System.out.println("pattern3 the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); // //get()�Ǵ�0 ��ʼ��
															// ����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
			store.add(sootmethod);
			System.out.println("get the method from stack:  "
					+ sootmethod.getName());

			stack.remove(size - 1);
			System.out.println("After Remove, the size of stack:  "
					+ stack.size());

			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);

			if (edgeInto != null) {
				// ��������
				while (edgeInto.hasNext()) {

					Edge to = edgeInto.next();
					SootMethod from = (SootMethod) to.getSrc();// ���from
					System.out.println("Invoke statement:   "
							+ sootmethod.getName().toString() + "            "
							+ from.getSignature());

					// �ж��Ƿ���frequent method����
					for (int i = 0; i < compress_method.size(); i++) {
						String method_name = compress_method.get(i);
						if (from.getSignature().toString()
								.contains(method_name)) {
							System.out
									.println("Pattern3 Disk_cache_analysis:  "
											+ from.getSignature());// ��֧��������
							result.add(from.getSignature());
							figer = true;
							figer_temp = false;

						}
					}
					if (figer_temp) {
						if (!(store.contains(from))) {// �ų�ѭ������--��û�б�Ҫ���п���
							stack.add(from);// ��ӵ�ջ��
							System.out.println("Add to stack");
						} else {
							break;
						}

					}
				}
			}
			if (!figer_temp) {
				break;
			}
		}

		return result;
	}

	/**
	 * ����ʵ�ֵ��Ǻ������������Ŀǰ�ķ�����Ϊ���
	 * �������ԣ�
	 *        call_sequences���,�������㣺edgeInto��Ϊ��-----------------�����
	 * 		  call_sequencesɾ����1�����û���µ�node��ӵ�stack�У��Ѿ��������ҵ���Ŀ��compress�������3��---------------�����
	 * 
	 * ��ʵ��Ҫ��stack��store�Ĺ�ϵ��
	 *        stack�洢��1��from�����еĽڵ㲻������store�У��Ҳ�����compress����stack�б�������-----------�����
	 *        
	 *        stack�����ɾ���������ѭ��������ѭ��
	 *        stackɾ�����������1��������Ϊ�գ����ڵ㶼��������;2����������compress����������ͷ�ˣ�3�������Լ������Ѿ���������,������store��
	 *                        4)������������ݶ���stack���Ѿ����ڣ�������ѭ�����򽫸ýڵ�ɾ������Ȼһֱѭ��
	 *                        5)----------------------�����
	 *                        
	 *        store�洢�Ѿ���������ȫ�Ľڵ㣬���������1��from����Ϊ�գ�2��from�ļ��϶���store�д��ڣ�3����������Ŀ��compress����������ͷ��
	 *                                        4) �Ѿ���stack��ȡ����-------�����
	 *        
	 *        
	 *        �Ƿ�������һ���ս᣺��������UI�̵߳ĺ�����ֹͣ
	 *        
	 * **/
	public static ArrayList<String> traverse_diskCache_analysis1(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {

		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		ArrayList<String> store = new ArrayList<String>();// �洢�Ѿ��������ķ���
		ArrayList<String> call_sequences = new ArrayList<String>();// �洢��⵽�ĵ�������
		ArrayList<String> result = new ArrayList<String>();// ���յļ����������⵽��compress_method���Ӽ�
		boolean figer = true;
		

		// �жϵ�ǰstart�����Ƿ�ʹ���compress�������ڵķ�����
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method.get(i)))) {
				System.out.println("The decode method related with compress:   "+ compress_method.get(i));// ��֧��������
				figer = false;// �����ǰ�������Ѿ�����compress���Ͳ��ؼ������з�����
				result.add(start.getSignature());
			}
		}

		stack.add(start);// ��ʼ���
		if (figer) {//start����������compress
			while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
				boolean stack_remove_1=false;
				boolean stack_remove_2=false;
				boolean stack_remove_3=false;
				boolean stack_remove_4=true;
				int size = stack.size();
				SootMethod sootmethod = stack.get(size - 1); //get()�Ǵ�0 ��ʼ��,����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
				System.out.println("The size of store:   " + store.size());
				System.out.println("get the method from stack:  "+ sootmethod.getSignature());
				System.out.println("After Remove, the size of stack:  " + size);
				
				//ȡ��һöstack����
				if (store.contains(sootmethod.getSignature())) {//���stackȡ���ķ����Ѿ���������
					stack_remove_3=true;//stackɾ��������
				}else{
					store.add(sootmethod.getSignature());
				
					Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);�������

					if (edgeInto != null) {
						if(!(call_sequences.contains(sootmethod.getSignature()))){
							call_sequences.add(sootmethod.getSignature());
					     }
						ArrayList<SootMethod> detected_compress = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
						// ��������
						boolean figer_store=true;
						boolean figer_call_seq_remove=true;
						while (edgeInto.hasNext()) {
							Edge edge = edgeInto.next();
							SootMethod from = (SootMethod) edge.getSrc();// ���from
							
							if((from.getName()).startsWith("on")){
								//�������on��ͷ�ģ�������������
								System.out.println("Encounter on start menthod");
							}else{
							if(store.contains(from.getSignature())){
								
							}else{
								figer_store=false;
							     System.out.println("traverse_diskCache_analysis1 Invoke statement:   "+ sootmethod.getSignature()+ "            "+ from.getSignature());
							     
							     
							// �ж��Ƿ���frequent method����
							for (int i = 0; i < compress_method.size(); i++) {
								String method_name = compress_method.get(i);
								if (from.getSignature().toString().contains(method_name)) {//���from����compress
									if (detected_compress.contains(from)) {
										System.out.println("the compress method have been invoked by current method");
									} 
									System.out.println("The decode method related with diskcache:  "+ from.getSignature());// ��֧��������
									System.out.println("call_sequences:    "+ call_sequences.size()+ "----------->  "+ call_sequences.toString());
									result.add(from.getSignature());
									detected_compress.add(from);
									stack_remove_2=true;//ɾ��������
									store.add(sootmethod.getSignature());// �洢�Ѿ������ķ���
								}else{
									figer_call_seq_remove=false;
									if(!(stack.contains(from))){
										stack_remove_4=false;
										stack.add(from);
									}else{
										//û�в���
									}
									
								}
							}
							}
						}//��������on��ͷ��UI����

						}
						if(figer_call_seq_remove){
							int size_seq=call_sequences.size();
							call_sequences.remove(size_seq-1);
							System.out.println("The size of call_sequence before remove current node:  "+ size_seq);
						}
						if(figer_store){
							store.add(sootmethod.getSignature());// �洢�Ѿ������ķ���							
						}
					} else {// if (edgeInto!= null) ���Ϊ�գ���ɾ���õ�
						System.out.println("The from set is null");
						stack_remove_1=true;//ɾ��������
						store.add(sootmethod.getSignature());// �洢�Ѿ������ķ���
					}
					//��һ����ӵ�store�е����
					
				}// if(!(store.contains(sootmethod.getSignature())))
				if(stack_remove_1||stack_remove_2||stack_remove_3||stack_remove_4){
					stack.remove(size-1);//ɾ��������
				}
			}// while (!(stack.isEmpty()))
		}
		System.out.println("result_disk.size():" + result.size());

		return result;

	}

	/**
	 * ������ĺ����ĸ���---����ʵ�ֵ��Ǻ������������Ŀǰ�ķ�����Ϊ���
	 * **/
	public static ArrayList<String> Copy_traverse_diskCache_analysis1(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {

		ArrayList<String> store = new ArrayList<String>();// �洢�Ѿ��������ķ���
		ArrayList<String> call_sequences = new ArrayList<String>();// �洢��⵽�ĵ�������

		ArrayList<String> result = new ArrayList<String>();// ���յļ����������⵽��compress_method���Ӽ�
		boolean figer = true;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ

		// �жϵ�ǰstart�����Ƿ�ʹ���compress�������ڵķ���
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method
					.get(i)))) {
				System.out
						.println("The decode method related with compress:   "
								+ compress_method.get(i));// ��֧��������
				figer = false;// �����ǰ�������Ѿ�����compress���Ͳ��ؼ������з�����
				result.add(start.getSignature());
			}
		}

		stack.add(start);// �洢������Ҫ���з����ķ���

		if (figer) {
			while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
				int size = stack.size();
				System.out.println("pattern3 the size of stack:  " + size);
				SootMethod sootmethod = stack.get(size - 1); // //get()�Ǵ�0 ��ʼ��
																// ����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��

				System.out.println("The size of store:   " + store.size());
				System.out.println("get the method from stack:  "
						+ sootmethod.getSignature());

				String remove = stack.get(size - 1).getSignature();
				stack.remove(size - 1);
				System.out.println("Remove:  " + remove);
				System.out.println("After Remove, the size of stack:  "
						+ stack.size());

				/* �ؼ���䣡�����������ظ��жϡ�����һ���жϣ��洢��stack�еķ����Ƿ��Ѿ�������� */
				if (!(store.contains(sootmethod.getSignature()))) {
					/* ������� */boolean remove_call_sequences_figer = true;
					store.add(sootmethod.getSignature());// �洢�Ѿ������ķ���
					call_sequences.add(sootmethod.getSignature());// �洢��������

					Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);
																				// //�������

					if (edgeInto != null) {

						// ��������
						while (edgeInto.hasNext()) {
							/* ������� */figer_temp = true;
							Edge edge = edgeInto.next();
							SootMethod from = (SootMethod) edge.getSrc();// ���from
							System.out
									.println("traverse_diskCache_analysis1 Invoke statement:   "
											+ sootmethod.getSignature()
											+ "            "
											+ from.getSignature());

							// �ж��Ƿ���frequent method����
							for (int i = 0; i < compress_method.size(); i++) {
								String method_name = compress_method.get(i);
								if (from.getSignature().toString()
										.contains(method_name)) {
									System.out
											.println("The decode method related with diskcache:  "
													+ from.getSignature());// ��֧��������
									/* ������� */
									System.out.println("call_sequences:    "
											+ call_sequences.size()
											+ "----------->  "
											+ call_sequences.toString());
									result.add(from.getSignature());
									figer_temp = false;// ����·����������
								}
							}
							if (figer_temp) {
								if (store.contains(from.getSignature())) {// �ų�ѭ������
									// System.out.println("store.contains:   "+from.getSignature());
								} else {
									// if(!(store.contains(from.getSignature()))){//�ų�ѭ������
									stack.add(from);// ��ӵ�ջ��
									System.out.println("Add to stack");
									remove_call_sequences_figer = false;
								}
							}
						}
					}
					/* ������� */
					if (remove_call_sequences_figer) {
						int call_size = call_sequences.size();
						System.out.println("The size of call_sequence:  "
								+ call_size);
						call_sequences.remove(call_size - 1);// ɾ������һ��ֵ
					}

				}
			}
		}
		System.out.println("result_disk.size():" + result.size());

		return result;

	}

	// /����
	public static void traverse_test(SootMethod start, CallGraph callGraph) {

		Iterator<Edge> edgeInto = callGraph.edgesOutOf(start); // ���õ�ǰ�����ļ���
		Iterator<Edge> edgeInto_temp = callGraph.edgesOutOf(start); // ���õ�ǰ�����ļ���
		
//		Iterator<Edge> edgeInto = callGraph.edgesInto(start); // ���õ�ǰ�����ļ���
//		Iterator<Edge> edgeInto_temp = callGraph.edgesInto(start); // ���õ�ǰ�����ļ���
		
		SootMethod second = null;
		System.out.println("Start test analyze:  "+start.getSignature());
		
		if (edgeInto_temp != null) {
			// ��������
			while (edgeInto_temp.hasNext()) {

				Edge to = edgeInto_temp.next();
				SootMethod from_temp = (SootMethod) to.getTgt();// ���from
				//SootMethod from_temp = (SootMethod) to.getSrc();// ���from

				System.out.println("First Invoke statement:   "+ start.getSignature() + "            "+ from_temp.getSignature());
			}
		}

		if (edgeInto != null) {
			// ��������
			while (edgeInto.hasNext()) {

				Edge to = edgeInto.next();
				SootMethod from = (SootMethod) to.getTgt();// ���from
				//SootMethod from = (SootMethod) to.getSrc();// ���from

				System.out.println("First Invoke statement:   "+ start.getSignature() + "            "+ from.getSignature());

				// if((from.getSignature()).equals("<java.lang.Thread: void run()>")){
				if ((from.getSignature()) != null) {
					second = from;
					System.out.println("Start analyze:  "+second.getSignature());
					// �ڶ���
					Iterator<Edge> edgeInto2 = callGraph.edgesOutOf(second); // ���õ�ǰ�����ļ���
					//Iterator<Edge> edgeInto2 = callGraph.edgesInto(second); // ���õ�ǰ�����ļ���
					if (edgeInto2 != null) {
						// ��������
						while (edgeInto2.hasNext()) {
							Edge to_2 = edgeInto2.next();
							SootMethod from_2 = (SootMethod) to_2.getTgt();// ���from
							//SootMethod from_2 = (SootMethod) to_2.getSrc();// ���from
							
							System.out.println("Second  Invoke statement:   "+ second.getSignature() + "            "+ from_2.getSignature());
						}
					}
				}
			}
		}

	}
	
	/**
	 * ������ʵ���µķ���������ͨ����������ռ�����ֵΪbitmap�ķ������ϣ��������Щ�������Ͻ��з��������Լ������СUI�����ȵȵ���Դ����
	 * �洢���ࣺ1�����е�·����2�����е�Ҷ����������������������
	 * **/
	public static ArrayList<SootMethod> traverse_bitmap_return_analysis(SootMethod start, CallGraph callGraph) {
		System.out.println("Start traverse_bitmap_return_analysis");
		ArrayList<SootMethod> result=new ArrayList<SootMethod>();
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		ArrayList<SootMethod> store = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		result.add(start);//�洢����decode�ķ���
		
		//�жϵ�ǰstart�����ķ���ֵ
		System.out.println("Get return Type:    "+start.getReturnType().toString());
		String return_type=start.getReturnType().toString();
		//android.graphics.drawable.Drawable
		if((return_type.equals("android.graphics.Bitmap"))||(return_type.equals("android.graphics.drawable.BitmapDrawable"))||(return_type.equals("android.graphics.drawable.Drawable"))){
			stack.add(start);
		}
		
		while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
			int size = stack.size();
			SootMethod sootmethod = stack.get(size - 1); // //get()�Ǵ�0 ��ʼ��,����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
			stack.remove(size - 1);
			store.add(sootmethod);
			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //�������
			if (edgeInto!= null) {
				while (edgeInto.hasNext()) {
					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();//���from
					System.out.println("traverse_bitmap_return_analysis Invoke statement:   "+ sootmethod.getSignature()+ "            "+ from.getSignature());
					if(from.getDeclaringClass().isApplicationClass()){
						result.add(from);
					}
					
					String return_type_temp=from.getReturnType().toString();
					if((return_type_temp.equals("android.graphics.Bitmap"))||(return_type_temp.equals("android.graphics.drawable.BitmapDrawable"))||(return_type_temp.equals("android.graphics.drawable.Drawable"))){
						if(!(store.contains(from))){
							stack.add(from);
						}
					}		
				}
			}
		}
		System.out.println("bitmap_return_method.size():"+result.size()+"      "+result.toString());
		System.out.println("Start class name:"+start.getSignature());
		return result;
	}
	
	public static Pattern34_result getResult_Pattern_34(ArrayList<String> compress_method,ArrayList<String> put_method,Set<String> recycle_method,Set<String> inbitmap_method,ArrayList<SootMethod> result, CallGraph callGraph){
		
		//��ʼreachable����pattern3��pattern4
		System.out.println("Start getResult_Pattern_34---------------------");
		ArrayList<String> pattern34_0=new ArrayList<String>();
		ArrayList<String> pattern34_1=new ArrayList<String>();
		ArrayList<String> pattern34_2=new ArrayList<String>();
		ArrayList<String> pattern34_3=new ArrayList<String>();
		ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) result);
		reachableMethods.update();
		QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//��ÿɴ�ķ�������
		int count_reachable=0;
		while (queueReader.hasNext()) {
			count_reachable++;
			SootMethod qReader=(SootMethod) queueReader.next();
			String method_name=qReader.getSignature();
			//System.out.println("queueReader.next():    "+method_name);      //seadroid������249������
					
			//DC��ʼ����ƥ��
			for (String str : compress_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with DC:  "+ str);// ��֧��������
				   pattern34_0.add(qReader.getSignature());
				}
			}
			
			//MC��ʼ����ƥ��
			for (String str : put_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with MC:  "+ str);// ��֧��������
				   pattern34_1.add(qReader.getSignature());
				}
			}
			
			//recycle��ʼ����ƥ��
			for (String str : recycle_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with recycle:  "+ str);// ��֧��������
				   pattern34_2.add(qReader.getSignature());
				}
			}
			
			//inbitmap��ʼ����ƥ��
			for (String str : inbitmap_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with inbitmap:  "+ str);// ��֧��������
				   pattern34_3.add(qReader.getSignature());
				}
			}
		}
		System.out.println("count_reachable:   "+count_reachable);
		Pattern34_result pattern34_result=new Pattern34_result(pattern34_0,pattern34_1,pattern34_2,pattern34_3);
		return pattern34_result;
	}
	
public static boolean Frequency_result(SootMethod start,String target, CallGraph callGraph){
		boolean result=false;
		//��ʼreachable����pattern3��pattern4
		System.out.println("Start Frequency_result-----------------");
		ArrayList<SootMethod> start_set=new ArrayList<SootMethod>();
		start_set.add(start);
		
		ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) start_set);
		reachableMethods.update();
		QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//��ÿɴ�ķ�������
		int count_reachable=0;
		while (queueReader.hasNext()) {
			count_reachable++;
			SootMethod qReader=(SootMethod) queueReader.next();
			String method_name=qReader.getSignature();
			//System.out.println("queueReader.next():    "+method_name);
					
			//DC��ʼ����ƥ��
			if(method_name.contains(target)){
				result=true;
			}
		}
		return result;
	}

public static boolean Frequency_result_set(SootMethod start,ArrayList<SootMethod> decode_method, CallGraph callGraph){
	boolean result=false;
	//��ʼreachable����pattern3��pattern4
	System.out.println("Start Frequency_result_set analysis-----------------");
	ArrayList<SootMethod> start_set=new ArrayList<SootMethod>();
	start_set.add(start);
	
	ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) start_set);
	reachableMethods.update();
	QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//��ÿɴ�ķ�������
	int count_reachable=0;
	while (queueReader.hasNext()) {
		count_reachable++;
		SootMethod qReader=(SootMethod) queueReader.next();
		String method_name=qReader.getSignature();
		System.out.println("queueReader.next():    "+method_name);
				
		//DC��ʼ����ƥ��
		for(int i=0;i<decode_method.size();i++){
			String target=decode_method.get(i).getSignature();
			if(method_name.contains(target)){
				Analysis_Bitmap.decode_frequency.get(i).add(start.getSignature());
			}
		}
		
	}
	return result;
}
	
	/**
	 * ����ʵ�ֵ��Ǻ������������Ŀǰ�ķ�����Ϊ���
	 * **/
	public static ArrayList<String> traverse_pattern4_analysis(
			Set<String> compress_method, SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// �洢�Ѿ��������ķ���
		store.add(start);

		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ

		// �жϵ�ǰstart�����Ƿ�ʹ���compress�������ڵķ���
		for (String str : compress_method) {
			if ((start.getSignature().toString().contains(str))) {
				System.out
						.println("The decode method related with recycle0:   "
								+ str);// ��֧��������
				figer = false;
				result.add(start.getSignature());
				return result;// �������
			}
		}

		stack.add(start);// ��ʼ��ջ
		if (figer) {
			while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
				int size = stack.size();
				System.out.println("pattern3 the size of stack:  " + size);
				SootMethod sootmethod = stack.get(size - 1); // //get()�Ǵ�0 ��ʼ��
																// ����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
				store.add(sootmethod);
				System.out.println("get the method from stack:  "
						+ sootmethod.getName());

				stack.remove(size - 1);
				System.out.println("After Remove, the size of stack:  "
						+ stack.size());

				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);
																			// //�������

				if (edgeInto != null) {
					// ��������
					while (edgeInto.hasNext()) {

						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// ���from
						System.out
								.println("traverse_recycle_analysis Invoke statement:   "
										+ sootmethod.getName().toString()
										+ "            " + from.getSignature());

						// �ж��Ƿ���frequent method����
						for (String str : compress_method) {
							String method_name = str;
							if (from.getSignature().toString()
									.contains(method_name)) {
								System.out
										.println("The decode method related with recycle1:  "
												+ from.getSignature());// ��֧��������
								result.add(from.getSignature());
								figer_temp = false;
								return result;

							}
						}
						if (figer_temp) {
							if (!(store.contains(from))) {// �ų�ѭ������--��û�б�Ҫ���п���
								stack.add(from);// ��ӵ�ջ��
								System.out.println("Add to stack");
							}
						}
					}
				}
			}
		}
		System.out.println("result_disk.size():" + result.size());
		return result;
	}

}
