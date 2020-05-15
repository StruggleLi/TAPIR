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
 * @author Dell GridView的存在表示需要使用到大量的图片
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
	 * 返回该decode方法关联的frequent 方法的集合
	 * 关键是注意涉及子线程时如何进行分析。
	 * **/
	public static ArrayList<String> traverse_frequent_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// 存储已经分析过的方法
		store.add(start);
		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean add_figer = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		// 判断当前start方法是否就处于frequency方法所在的方法
		for (int i = 0; i < feature.length; i++) {
			if ((start.getSignature().contains(feature[i]))) {
				System.out.println("The decode method related with frequency:"+ i + "----------" + start.getSignature() + "   "	+ "   " + feature[i]);// 分支遍历结束
				figer = false;
				result.add(start.getSignature());
				Analysis_Bitmap.freq_decode[i]++;
			}
		}

		if (figer) {
			stack.add(start);// 初始入栈
			while (!(stack.isEmpty())) {
				int size = stack.size();
				System.out.println("traverse_frequent_analysis the size of stack:  "+ size);
				SootMethod sootmethod = stack.get(size - 1); //get()是从0 开始的,所以要减去1，获得ArrayList中的最后的一个元素
				store.add(sootmethod);// 表示该方法已经遍历分析过
				System.out.println("get the method from stack:  "+ sootmethod.getName());
				stack.remove(size - 1);
				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod); // 调用当前方法的集合
				if (edgeInto != null) {
					// 遍历集合
					while (edgeInto.hasNext()) {
						add_figer=true;
						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// 获得from
						System.out.println("Invoke statement:   "+ sootmethod.getSignature() + "            "+ from.getSignature());
						// 判断是否与frequent method关联
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
							if ((!(store.contains(from)))&&(!(stack.contains(from)))) {// 排除循环调用--》没有必要进行考虑
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
	 * 返回该decode方法关联的frequent 方法的集合
	 * **/
	public static ArrayList<String> Forward_traverse_analysis(SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// 存储已经分析过的方法
		store.add(start);
		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean add_figer = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		// 判断当前start方法是否就处于frequency方法所在的方法
		for (int i = 0; i < feature.length; i++) {
			if ((start.getSignature().contains(feature[i]))) {
				System.out.println("The decode method related with frequency:"+ i + "----------" + start.getSignature() + "   "	+ "   " + feature[i]);// 分支遍历结束
				figer = false;
				result.add(start.getSignature());
				Analysis_Bitmap.freq_decode[i]++;
			}
		}

		if (figer) {
			stack.add(start);// 初始入栈
			while (!(stack.isEmpty())) {
				int size = stack.size();
				System.out.println("traverse_frequent_analysis the size of stack:  "+ size);
				SootMethod sootmethod = stack.get(size - 1); //get()是从0 开始的,所以要减去1，获得ArrayList中的最后的一个元素
				store.add(sootmethod);// 表示该方法已经遍历分析过
				System.out.println("get the method from stack:  "+ sootmethod.getName());
				stack.remove(size - 1);
				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod); // 调用当前方法的集合
				if (edgeInto != null) {
					// 遍历集合
					while (edgeInto.hasNext()) {
						add_figer=true;
						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// 获得from
						System.out.println("Invoke statement:   "+ sootmethod.getSignature() + "            "+ from.getSignature());
						// 判断是否与frequent method关联
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
							if ((!(store.contains(from)))&&(!(stack.contains(from)))) {// 排除循环调用--》没有必要进行考虑
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
	 * 遍历开销比较大，仅仅适合对特定检测结果进行验证分析时使用
	 * 
	 * start: 路径分析起点方法
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> forward_path_analysis_ArrayList(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
		for (int i = 0; i < end.size(); i++) {
			if ((start.getSignature().contains(end.get(i)))) {
				System.out.println("Find the end method");
				figer = false;//不再继续分析
				result1.add(start.getSignature());
				result_set.add(result1);
			}
		}
		System.out.println("Start to perform forward_path_analysis(start):    "+start.getSignature());

		if (figer) {
			while(true){
				end_figer=true;
				next_method=start;//回归起点
				//初始化数据
				ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
				ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
				System.out.println("Enter while-1");
				while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
					System.out.println("Enter while-2");
					store_once.add(next_method);// 表示该方法已经遍历分析过
					Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
					Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
					
					//信息输出
					int edge_count=0;
					System.out.println("next_method:   "+next_method.getSignature());
					while (edgeOut_temp.hasNext()) {//单节点遍历
						Edge to = edgeOut_temp.next();
						SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
						edge_count++;
						System.out.println("edge:    "+to_meth.getSignature());
					}
					System.out.println("The count of edge:   "+edge_count);
					
					if (edgeOut != null) {
						leaf_figer=true;//还原初始值
						while (edgeOut.hasNext()) {
							Edge to = edgeOut.next();
							SootMethod to_meth = (SootMethod) to.getTgt();// 获得from
							System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
							System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
							if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
									(!(store_leaf.contains(to_meth)))){
								store_once.add(to_meth);// 表示该方法已经遍历分析过
								leaf_figer=false;
								System.out.println("forward_path_analysis:    "+to_meth.getSignature());
								boolean temp=find_target_ArrayList(to_meth,end);//该to_meth是否为位于target方法集合
								if(temp){//如果位于target集合
									next_method=null;//再次从start方法开始遍历
									//存储检测信息
									result.add(to_meth.getSignature());
									result_set.add(result);
									//表示这个叶节点已经检测过
									store_leaf.add(to_meth);
									break;
								}else{//如果没有找到target,则从该点开始继续深入分析
									next_method=to_meth;
									break;
								}									
								
							}
						}//edgeOut.hasNext()遍历结束
						//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
						System.out.println("leaf_figer:  "+leaf_figer);
						if(leaf_figer){
							System.out.println("store_leaf.add:    "+next_method.getSignature());
							store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
							next_method=null;
						}
					}else{
						System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
						store_leaf.add(next_method);
					}

				}
				//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
				Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
				if (edgeOut_start != null) {
					while (edgeOut_start.hasNext()) {
						Edge to = edgeOut_start.next();
						SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
						//判断这个方法所在的类是否为程序中涉及的方法
						if(to_meth.getDeclaringClass().isApplicationClass()){
							if(!(store_leaf.contains(to_meth))){
								end_figer=false;
							}
						}
					}
				}
				
				if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
					break;
				}
				
			}
			
		}

		
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，仅仅适合对特定检测结果进行验证分析时使用
	 * 
	 * start: 路径分析起点方法
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> forward_path_analysis_Set(SootMethod start, Set<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
		for (String str: end) {
			if ((start.getSignature().contains(str))) {
				System.out.println("Find the end method");
				figer = false;//不再继续分析
				result1.add(start.getSignature());
				result_set.add(result1);
			}
		}
		System.out.println("Start to perform forward_path_analysis(start):    "+start.getSignature());

		if (figer) {
			while(true){
				end_figer=true;
				next_method=start;//回归起点
				//初始化数据
				ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
				ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
				System.out.println("Enter while-1");
				while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
					System.out.println("Enter while-2");
					store_once.add(next_method);// 表示该方法已经遍历分析过
					Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
					Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
					
					//信息输出
					int edge_count=0;
					System.out.println("next_method:   "+next_method.getSignature());
					while (edgeOut_temp.hasNext()) {//单节点遍历
						Edge to = edgeOut_temp.next();
						SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
						edge_count++;
						System.out.println("edge:    "+to_meth.getSignature());
					}
					System.out.println("The count of edge:   "+edge_count);
					
					if (edgeOut != null) {
						leaf_figer=true;//还原初始值
						while (edgeOut.hasNext()) {
							Edge to = edgeOut.next();
							SootMethod to_meth = (SootMethod) to.getTgt();// 获得from
							System.out.println("Enter edgeOut-element to_meth:   "+to_meth.getSignature());
							System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
							if((to_meth.getDeclaringClass().isApplicationClass())&&(!(store_once.contains(to_meth)))&&
									(!(store_leaf.contains(to_meth)))){
								store_once.add(to_meth);// 表示该方法已经遍历分析过
								leaf_figer=false;
								System.out.println("forward_path_analysis:    "+to_meth.getSignature());
								boolean temp=find_target_Set(to_meth,end);//该to_meth是否为位于target方法集合
								if(temp){//如果位于target集合
									next_method=null;//再次从start方法开始遍历
									//存储检测信息
									result.add(to_meth.getSignature());
									result_set.add(result);
									//表示这个叶节点已经检测过
									store_leaf.add(to_meth);
									break;
								}else{//如果没有找到target,则从该点开始继续深入分析
									next_method=to_meth;
									break;
								}									
								
							}
						}//edgeOut.hasNext()遍历结束
						//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
						System.out.println("leaf_figer:  "+leaf_figer);
						if(leaf_figer){
							System.out.println("store_leaf.add:    "+next_method.getSignature());
							store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
							next_method=null;
						}
					}else{
						System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
						store_leaf.add(next_method);
					}

				}
				//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
				Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
				if (edgeOut_start != null) {
					while (edgeOut_start.hasNext()) {
						Edge to = edgeOut_start.next();
						SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
						//判断这个方法所在的类是否为程序中涉及的方法
						if(to_meth.getDeclaringClass().isApplicationClass()){
							if(!(store_leaf.contains(to_meth))){
								end_figer=false;
							}
						}
					}
				}
				
				if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
					break;
				}
				
			}
			
		}

		
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，对一个decode的所有关联方法进行分析
	 * 
	 * start_set: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_Set(ArrayList<SootMethod> start_set, Set<String> end, CallGraph callGraph) {
		String args_check="android.graphics.Bitmap";
		String args_check1="android.graphics.drawable.BitmapDrawable";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//不再继续分析
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
						next_method=start;//回归起点
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//单节点遍历
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//还原初始值
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// 获得from
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
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_Set(to_meth,end);//该to_meth是否为位于target方法集合
										if(temp){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//存储检测信息
											result.add(to_meth.getSignature());
											result_set.add(result);
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							break;
						}
						
					}			
			}
		}
		

		

		
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，对一个decode的所有关联方法进行分析
	 * 
	 * start_set: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_Set_inbitmap(ArrayList<SootMethod> start_set, Set<String> end, CallGraph callGraph) {
		String args_check="android.graphics.BitmapFactory$Options";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//不再继续分析
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
						next_method=start;//回归起点
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						System.out.println();
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//单节点遍历
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//还原初始值
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// 获得from
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
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_Set(to_meth,end);//该to_meth是否为位于target方法集合
										if(temp){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//存储检测信息
											result.add(to_meth.getSignature());
											result_set.add(result);
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							break;
						}
						
					}			
			}
		}
		
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，对一个decode的所有关联方法进行分析
	 * 
	 * start_set: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> return_result_forward_path_analysis_ArrayList(ArrayList<SootMethod> start_set, ArrayList<String> end, CallGraph callGraph) {
		String args_check="android.graphics.Bitmap";
		String args_check1="android.graphics.drawable.BitmapDrawable";
		String args_check2="android.graphics.drawable.Drawable";
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
		for(int i=0;i<start_set.size();i++){
			SootMethod start=start_set.get(i);
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("Find the end method");
					figer = false;//不再继续分析
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
						next_method=start;//回归起点
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeOut = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeOut_temp = callGraph.edgesOutOf(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeOut_temp.hasNext()) {//单节点遍历
								Edge to = edgeOut_temp.next();
								SootMethod to_meth = (SootMethod) to.getTgt();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeOut != null) {
								leaf_figer=true;//还原初始值
								while (edgeOut.hasNext()) {
									Edge to = edgeOut.next();
									SootMethod to_meth = (SootMethod) to.getTgt();// 获得from
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
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("forward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_ArrayList(to_meth,end);//该to_meth是否为位于target方法集合
										if(temp){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//存储检测信息
											result.add(to_meth.getSignature());
											result_set.add(result);
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							break;
						}
						
					}			
			}
		}
		

		

		
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，找到一个decode的所有关联的frequency方法
	 * 
	 * start: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> frequency_backward_path_analysis_ArrayList(Set<Thread_info> thread_data_set,SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		System.out.println("The size of thread_data_set:  "+thread_data_set.size());
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表，即包含getview或onDraw
			for (String str: end) {
				if ((start.getSignature().contains(str))) {
					System.out.println("frequency: Find the end method");
					figer = false;//不再继续分析
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		
		System.out.println("Start to perform backward_path_analysis for frequency method:    "+start.toString());	
		if(figer){
					while(true){
						end_figer=true;
						next_method=start;//回归起点
						SootMethod child_doInbackground=null;
						SootMethod child_execute = null;
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						//ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
						ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeInto_temp.hasNext()) {//单节点遍历
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();
								edge_count++;
								//System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//还原初始值
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
									System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									//if(isApplication||isThread){
									if(isApplication){
										//System.out.println("isApplication||isThread is true");
									if((!(store_once.contains(to_meth)))&&(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										System.out.println("backward_path_analysis:    "+to_meth.getName());
										boolean temp=find_target_ArrayList_frequency(to_meth,end);//该to_meth是否为位于target方法集合
										if(temp){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//存储检测信息
											result.add(to_meth.getSignature());
											result_set.add(result);
											System.out.println("result of frequency:  "+result.toString());
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											//leaf_figer=true;
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析   backward_path_analysis:    doInBackground   run
											//增加判断分析，如果是子线程，则将next_method转移到线程的调用方法中，如果不是，则继续分析
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
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析,这个节点表示execute所在的方法
									if(next_method.equals(child_execute)){
										store_leaf.add(child_doInbackground);//同时doInbackground所在的方法加入集合
										System.out.println("store_leaf.add:    "+child_doInbackground.getSignature());
									}
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							System.out.println("end_figer is true");
							break;
						}
						
					}	
		}
		

		

		System.out.println("The result of result_set:   "+result_set.toString());
		return result_set;
	}
	
	/**
	 * 遍历开销比较大，找到一个decode的所有关联的on和thread方法
	 * 
	 * start: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static Result_Pattern2_back thread_backward_path_analysis_ArrayList_Set(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set1 = new ArrayList<ArrayList<String>>();//thread存储的路径调用序列
		ArrayList<ArrayList<String>> result_set2 = new ArrayList<ArrayList<String>>();//on存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//thread存储的路径调用序列
		ArrayList<String> result2 = new ArrayList<String>();//on存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
			for (String str: end) {
				if ((start.getName().equals(str))) {
					System.out.println("Find the end method");
					figer = false;//不再继续分析
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
						next_method=start;//回归起点
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						ArrayList<String> result11 = new ArrayList<String>();//存储的路径调用序列
						ArrayList<String> result22 = new ArrayList<String>();//存储的路径调用序列
						result11.add("In call sequence:    ");
						result11.add(start.getSignature());
						result22.add("In call sequence:    ");
						result22.add(start.getSignature());
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							
							while (edgeInto_temp.hasNext()) {//单节点遍历
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// edgesOutOf
								edge_count++;
								//System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//还原初始值
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
									//System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									//System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									
									if((isApplication)&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										boolean find_figer=false;
										boolean find_on=false;
										boolean find_run=find_target_ArrayList1(to_meth,end);//该to_meth是否为位于target方法集合
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
										
										if(find_figer){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析
											next_method=to_meth;
											result11.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									result22.add(next_method.getSignature());
									result_set2.add(result22);
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							break;
						}
						
					}	
		}
		
		return new Result_Pattern2_back(result_set2,result_set1,result_set2.size(),result_set1.size());
	}
	
	/**
	 * 遍历开销比较大，找到一个decode的所有关联的frequency方法
	 * 
	 * start: 路径分析起点方法集合
	 * end:   路径分析终点方法名称集合
	 * callGraph: 程序的方法调用图
	 * **/
	public static ArrayList<ArrayList<String>> thread_backward_path_analysis_ArrayList(SootMethod start, ArrayList<String> end, CallGraph callGraph) {
		
		ArrayList<SootMethod> store_leaf = new ArrayList<SootMethod>();// 存储已经分析确定过的叶节点
		ArrayList<ArrayList<String>> result_set = new ArrayList<ArrayList<String>>();//存储的路径调用序列
		ArrayList<String> result1 = new ArrayList<String>();//存储的路径调用序列
		boolean figer = true;
		boolean end_figer=false;
		boolean leaf_figer=true;//加入leaf节点集合的条件：  1）找到end；2）指向的节点都存储在leaf集合中；3）如果into集合为Null
		SootMethod next_method;
		// 判断当前start方法是否包含要检测的方法列表
			for (String str: end) {
				if ((start.getName().equals(str))) {
					System.out.println("Find the end method");
					figer = false;//不再继续分析
					result1.add("Decode is call by thread:");
					result1.add(start.getSignature());
					result_set.add(result1);
				}
			}
		
		System.out.println("Start to perform backward_path_analysis for frequency method:    "+start.toString());	
		if(figer){
					while(true){
						end_figer=true;
						next_method=start;//回归起点
						//初始化数据
						ArrayList<SootMethod> store_once = new ArrayList<SootMethod>();// 存储一条深度遍历涉及的节点，主要了避免循环
						ArrayList<String> result = new ArrayList<String>();//存储的路径调用序列
						result.add("In call sequence:    ");
						result.add(start.getSignature());
						while (next_method!=null) {//两种情况设置next_method为null  (1) 方法所在的类非程序中的类；(2)方法即为检测的方法
							store_once.add(next_method);// 表示该方法已经遍历分析过
							Iterator<Edge> edgeInto = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							Iterator<Edge> edgeInto_temp = callGraph.edgesInto(next_method); // 正向分析，从next_method出来的方法
							
							//信息输出
							int edge_count=0;
							System.out.println("next_method:   "+next_method.getSignature());
							while (edgeInto_temp.hasNext()) {//单节点遍历
								Edge to = edgeInto_temp.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// edgesOutOf
								edge_count++;
								System.out.println("edge:    "+to_meth.getSignature());
							}
							System.out.println("The count of edge:   "+edge_count);
							
							if (edgeInto != null) {
								leaf_figer=true;//还原初始值
								while (edgeInto.hasNext()) {
									Edge to = edgeInto.next();
									SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
									System.out.println("Enter edgeInto-element to_meth:   "+to_meth.getSignature());
									System.out.println("isApplicationClass():   "+to_meth.getDeclaringClass().isApplicationClass());
									//System.out.println("is-java.lang.Thread: void run()"+to_meth.getSignature().contains("java.lang.Thread: void run()"));
									boolean isApplication=to_meth.getDeclaringClass().isApplicationClass();
									//boolean isThread=to_meth.getSignature().contains("java.lang.Thread: void run()");
									
									if((isApplication)&&(!(store_once.contains(to_meth)))&&
											(!(store_leaf.contains(to_meth)))){
										store_once.add(to_meth);// 表示该方法已经遍历分析过
										leaf_figer=false;
										System.out.println("backward_path_analysis:    "+to_meth.getSignature());
										boolean temp=find_target_ArrayList1(to_meth,end);//该to_meth是否为位于target方法集合
										if(temp){//如果位于target集合
											next_method=null;//再次从start方法开始遍历
											//存储检测信息
											result.add(to_meth.getSignature());
											result_set.add(result);
											//表示这个叶节点已经检测过
											store_leaf.add(to_meth);
											break;
										}else{//如果没有找到target,则从该点开始继续深入分析
											next_method=to_meth;
											result.add(to_meth.getSignature());
											break;
										}									
										
									}
								}//edgeOut.hasNext()遍历结束
								//如果edgeOut的集合要么非本地，要么已经分析过,加入已经分析的叶节点集合
								System.out.println("leaf_figer:  "+leaf_figer);
								if(leaf_figer){
									System.out.println("store_leaf.add:    "+next_method.getSignature());
									store_leaf.add(next_method);//加入叶节点也表示该节点分析完毕，不需要继续分析
									next_method=null;
								}
							}else{
								System.out.println("store_leaf.add--edgeOut != null:    "+next_method.getSignature());
								store_leaf.add(next_method);
							}

						}
						//在从新准备从start开始之前，先判断从start开始的第一层遍历的节点是否全部覆盖，如果全部覆盖，结束整个遍历
						Iterator<Edge> edgeOut_start = callGraph.edgesOutOf(start); // 正向分析，从next_method出来的方法
						if (edgeOut_start != null) {
							while (edgeOut_start.hasNext()) {
								Edge to = edgeOut_start.next();
								SootMethod to_meth = (SootMethod) to.getSrc();// 获得from
								//判断这个方法所在的类是否为程序中涉及的方法
								if(to_meth.getDeclaringClass().isApplicationClass()){
									if(!(store_leaf.contains(to_meth))){
										end_figer=false;
									}
								}
							}
						}
						
						if(end_figer){//当start方法的所有子节点都分析结束，start指向的节点都已经被遍历
							break;
						}
						
					}	
		}
		

		

		
		return result_set;
	}
	
	public static boolean find_target_ArrayList(SootMethod to_meth, ArrayList<String> end){
		// 判断是否与end关联
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
		// 判断是否与end关联
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
		// 判断是否与end关联
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
		// 判断是否与end关联
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
	 * 是否与包含compress方法调用的方法存在调用联系
	 * **/
	public static ArrayList<String> traverse_diskCache_analysis(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// 存储已经分析过的方法
		store.add(start);

		ArrayList<String> result = new ArrayList<String>();
		boolean figer = false;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构

		// 判断当前start方法是否就处于compress方法所在的方法
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method
					.get(i)))) {
				System.out
						.println("The decode method related with compress:   "
								+ compress_method.get(i));// 分支遍历结束
				figer = true;
				result.add(compress_method.get(i));
			}
		}

		stack.add(start);// 初始入栈
		while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
			int size = stack.size();
			System.out.println("pattern3 the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); // //get()是从0 开始的
															// 所以要减去1，获得ArrayList中的最后的一个元素
			store.add(sootmethod);
			System.out.println("get the method from stack:  "
					+ sootmethod.getName());

			stack.remove(size - 1);
			System.out.println("After Remove, the size of stack:  "
					+ stack.size());

			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);

			if (edgeInto != null) {
				// 遍历集合
				while (edgeInto.hasNext()) {

					Edge to = edgeInto.next();
					SootMethod from = (SootMethod) to.getSrc();// 获得from
					System.out.println("Invoke statement:   "
							+ sootmethod.getName().toString() + "            "
							+ from.getSignature());

					// 判断是否与frequent method关联
					for (int i = 0; i < compress_method.size(); i++) {
						String method_name = compress_method.get(i);
						if (from.getSignature().toString()
								.contains(method_name)) {
							System.out
									.println("Pattern3 Disk_cache_analysis:  "
											+ from.getSignature());// 分支遍历结束
							result.add(from.getSignature());
							figer = true;
							figer_temp = false;

						}
					}
					if (figer_temp) {
						if (!(store.contains(from))) {// 排除循环调用--》没有必要进行考虑
							stack.add(from);// 添加到栈中
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
	 * 这里实现的是后向分析，即以目前的方法作为起点
	 * 分析策略：
	 *        call_sequences添加,必须满足：edgeInto不为空-----------------》完成
	 * 		  call_sequences删除：1）如果没有新的node添加到stack中，已经涵盖了找到了目标compress的情况；3）---------------》完成
	 * 
	 * 其实主要是stack和store的关系：
	 *        stack存储：1）from集合中的节点不包含在store中，且不包含compress；且stack中本身不包含-----------》完成
	 *        
	 *        stack下面的删除如果出现循环，会死循环
	 *        stack删除三种情况：1）遍历后为空，即节点都被分析过;2）遍历遇到compress，分析到尽头了；3）发现自己本身已经被分析过,存在于store中
	 *                        4)如果遍历的内容都在stack中已经存在，即存在循环，则将该节点删除，不然一直循环
	 *                        5)----------------------》完成
	 *                        
	 *        store存储已经遍历过完全的节点，情况包括：1）from集合为空；2）from的集合都在store中存在；3）遍历遇到目标compress，分析到尽头了
	 *                                        4) 已经从stack中取出过-------》完成
	 *        
	 *        
	 *        是否可以添加一类终结：遍历遇到UI线程的函数，停止
	 *        
	 * **/
	public static ArrayList<String> traverse_diskCache_analysis1(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {

		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		ArrayList<String> store = new ArrayList<String>();// 存储已经分析过的方法
		ArrayList<String> call_sequences = new ArrayList<String>();// 存储检测到的调用序列
		ArrayList<String> result = new ArrayList<String>();// 最终的检测结果，即检测到的compress_method的子集
		boolean figer = true;
		

		// 判断当前start方法是否就处于compress方法所在的方法的
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method.get(i)))) {
				System.out.println("The decode method related with compress:   "+ compress_method.get(i));// 分支遍历结束
				figer = false;// 如果当前方法中已经包含compress，就不必继续进行分析了
				result.add(start.getSignature());
			}
		}

		stack.add(start);// 初始添加
		if (figer) {//start方法不包含compress
			while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
				boolean stack_remove_1=false;
				boolean stack_remove_2=false;
				boolean stack_remove_3=false;
				boolean stack_remove_4=true;
				int size = stack.size();
				SootMethod sootmethod = stack.get(size - 1); //get()是从0 开始的,所以要减去1，获得ArrayList中的最后的一个元素
				System.out.println("The size of store:   " + store.size());
				System.out.println("get the method from stack:  "+ sootmethod.getSignature());
				System.out.println("After Remove, the size of stack:  " + size);
				
				//取出一枚stack数据
				if (store.contains(sootmethod.getSignature())) {//如果stack取出的方法已经被分析过
					stack_remove_3=true;//stack删除该数据
				}else{
					store.add(sootmethod.getSignature());
				
					Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);后向分析

					if (edgeInto != null) {
						if(!(call_sequences.contains(sootmethod.getSignature()))){
							call_sequences.add(sootmethod.getSignature());
					     }
						ArrayList<SootMethod> detected_compress = new ArrayList<SootMethod>(); // 存储方法栈结构
						// 遍历集合
						boolean figer_store=true;
						boolean figer_call_seq_remove=true;
						while (edgeInto.hasNext()) {
							Edge edge = edgeInto.next();
							SootMethod from = (SootMethod) edge.getSrc();// 获得from
							
							if((from.getName()).startsWith("on")){
								//如果遇到on开头的，不做分析处理
								System.out.println("Encounter on start menthod");
							}else{
							if(store.contains(from.getSignature())){
								
							}else{
								figer_store=false;
							     System.out.println("traverse_diskCache_analysis1 Invoke statement:   "+ sootmethod.getSignature()+ "            "+ from.getSignature());
							     
							     
							// 判断是否与frequent method关联
							for (int i = 0; i < compress_method.size(); i++) {
								String method_name = compress_method.get(i);
								if (from.getSignature().toString().contains(method_name)) {//如果from包含compress
									if (detected_compress.contains(from)) {
										System.out.println("the compress method have been invoked by current method");
									} 
									System.out.println("The decode method related with diskcache:  "+ from.getSignature());// 分支遍历结束
									System.out.println("call_sequences:    "+ call_sequences.size()+ "----------->  "+ call_sequences.toString());
									result.add(from.getSignature());
									detected_compress.add(from);
									stack_remove_2=true;//删除该数据
									store.add(sootmethod.getSignature());// 存储已经检测过的方法
								}else{
									figer_call_seq_remove=false;
									if(!(stack.contains(from))){
										stack_remove_4=false;
										stack.add(from);
									}else{
										//没有操作
									}
									
								}
							}
							}
						}//遇到的是on开头的UI函数

						}
						if(figer_call_seq_remove){
							int size_seq=call_sequences.size();
							call_sequences.remove(size_seq-1);
							System.out.println("The size of call_sequence before remove current node:  "+ size_seq);
						}
						if(figer_store){
							store.add(sootmethod.getSignature());// 存储已经检测过的方法							
						}
					} else {// if (edgeInto!= null) 如果为空，则删除该点
						System.out.println("The from set is null");
						stack_remove_1=true;//删除该数据
						store.add(sootmethod.getSignature());// 存储已经检测过的方法
					}
					//另一种添加到store中的情况
					
				}// if(!(store.contains(sootmethod.getSignature())))
				if(stack_remove_1||stack_remove_2||stack_remove_3||stack_remove_4){
					stack.remove(size-1);//删除该数据
				}
			}// while (!(stack.isEmpty()))
		}
		System.out.println("result_disk.size():" + result.size());

		return result;

	}

	/**
	 * 是上面的函数的复制---这里实现的是后向分析，即以目前的方法作为起点
	 * **/
	public static ArrayList<String> Copy_traverse_diskCache_analysis1(
			ArrayList<String> compress_method, SootMethod start,
			CallGraph callGraph) {

		ArrayList<String> store = new ArrayList<String>();// 存储已经分析过的方法
		ArrayList<String> call_sequences = new ArrayList<String>();// 存储检测到的调用序列

		ArrayList<String> result = new ArrayList<String>();// 最终的检测结果，即检测到的compress_method的子集
		boolean figer = true;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构

		// 判断当前start方法是否就处于compress方法所在的方法
		for (int i = 0; i < compress_method.size(); i++) {
			if ((start.getSignature().toString().contains(compress_method
					.get(i)))) {
				System.out
						.println("The decode method related with compress:   "
								+ compress_method.get(i));// 分支遍历结束
				figer = false;// 如果当前方法中已经包含compress，就不必继续进行分析了
				result.add(start.getSignature());
			}
		}

		stack.add(start);// 存储后续需要进行分析的方法

		if (figer) {
			while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
				int size = stack.size();
				System.out.println("pattern3 the size of stack:  " + size);
				SootMethod sootmethod = stack.get(size - 1); // //get()是从0 开始的
																// 所以要减去1，获得ArrayList中的最后的一个元素

				System.out.println("The size of store:   " + store.size());
				System.out.println("get the method from stack:  "
						+ sootmethod.getSignature());

				String remove = stack.get(size - 1).getSignature();
				stack.remove(size - 1);
				System.out.println("Remove:  " + remove);
				System.out.println("After Remove, the size of stack:  "
						+ stack.size());

				/* 关键语句！！！避免了重复判断。增加一个判断，存储在stack中的方法是否已经被检测了 */
				if (!(store.contains(sootmethod.getSignature()))) {
					/* 新增语句 */boolean remove_call_sequences_figer = true;
					store.add(sootmethod.getSignature());// 存储已经检测过的方法
					call_sequences.add(sootmethod.getSignature());// 存储调用序列

					Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);
																				// //后向分析

					if (edgeInto != null) {

						// 遍历集合
						while (edgeInto.hasNext()) {
							/* 新增语句 */figer_temp = true;
							Edge edge = edgeInto.next();
							SootMethod from = (SootMethod) edge.getSrc();// 获得from
							System.out
									.println("traverse_diskCache_analysis1 Invoke statement:   "
											+ sootmethod.getSignature()
											+ "            "
											+ from.getSignature());

							// 判断是否与frequent method关联
							for (int i = 0; i < compress_method.size(); i++) {
								String method_name = compress_method.get(i);
								if (from.getSignature().toString()
										.contains(method_name)) {
									System.out
											.println("The decode method related with diskcache:  "
													+ from.getSignature());// 分支遍历结束
									/* 新增语句 */
									System.out.println("call_sequences:    "
											+ call_sequences.size()
											+ "----------->  "
											+ call_sequences.toString());
									result.add(from.getSignature());
									figer_temp = false;// 该条路径遍历结束
								}
							}
							if (figer_temp) {
								if (store.contains(from.getSignature())) {// 排除循环调用
									// System.out.println("store.contains:   "+from.getSignature());
								} else {
									// if(!(store.contains(from.getSignature()))){//排除循环调用
									stack.add(from);// 添加到栈中
									System.out.println("Add to stack");
									remove_call_sequences_figer = false;
								}
							}
						}
					}
					/* 新增语句 */
					if (remove_call_sequences_figer) {
						int call_size = call_sequences.size();
						System.out.println("The size of call_sequence:  "
								+ call_size);
						call_sequences.remove(call_size - 1);// 删除最后的一个值
					}

				}
			}
		}
		System.out.println("result_disk.size():" + result.size());

		return result;

	}

	// /测试
	public static void traverse_test(SootMethod start, CallGraph callGraph) {

		Iterator<Edge> edgeInto = callGraph.edgesOutOf(start); // 调用当前方法的集合
		Iterator<Edge> edgeInto_temp = callGraph.edgesOutOf(start); // 调用当前方法的集合
		
//		Iterator<Edge> edgeInto = callGraph.edgesInto(start); // 调用当前方法的集合
//		Iterator<Edge> edgeInto_temp = callGraph.edgesInto(start); // 调用当前方法的集合
		
		SootMethod second = null;
		System.out.println("Start test analyze:  "+start.getSignature());
		
		if (edgeInto_temp != null) {
			// 遍历集合
			while (edgeInto_temp.hasNext()) {

				Edge to = edgeInto_temp.next();
				SootMethod from_temp = (SootMethod) to.getTgt();// 获得from
				//SootMethod from_temp = (SootMethod) to.getSrc();// 获得from

				System.out.println("First Invoke statement:   "+ start.getSignature() + "            "+ from_temp.getSignature());
			}
		}

		if (edgeInto != null) {
			// 遍历集合
			while (edgeInto.hasNext()) {

				Edge to = edgeInto.next();
				SootMethod from = (SootMethod) to.getTgt();// 获得from
				//SootMethod from = (SootMethod) to.getSrc();// 获得from

				System.out.println("First Invoke statement:   "+ start.getSignature() + "            "+ from.getSignature());

				// if((from.getSignature()).equals("<java.lang.Thread: void run()>")){
				if ((from.getSignature()) != null) {
					second = from;
					System.out.println("Start analyze:  "+second.getSignature());
					// 第二层
					Iterator<Edge> edgeInto2 = callGraph.edgesOutOf(second); // 调用当前方法的集合
					//Iterator<Edge> edgeInto2 = callGraph.edgesInto(second); // 调用当前方法的集合
					if (edgeInto2 != null) {
						// 遍历集合
						while (edgeInto2.hasNext()) {
							Edge to_2 = edgeInto2.next();
							SootMethod from_2 = (SootMethod) to_2.getTgt();// 获得from
							//SootMethod from_2 = (SootMethod) to_2.getSrc();// 获得from
							
							System.out.println("Second  Invoke statement:   "+ second.getSignature() + "            "+ from_2.getSignature());
						}
					}
				}
			}
		}

	}
	
	/**
	 * 这里是实现新的分析，首先通过逆向分析收集返回值为bitmap的方法集合，而后对这些方法集合进行分析，可以极大的缩小UI分析等等的资源开销
	 * 存储两类：1）所有的路径；2）所有的叶方法，代表继续分析的起点
	 * **/
	public static ArrayList<SootMethod> traverse_bitmap_return_analysis(SootMethod start, CallGraph callGraph) {
		System.out.println("Start traverse_bitmap_return_analysis");
		ArrayList<SootMethod> result=new ArrayList<SootMethod>();
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		ArrayList<SootMethod> store = new ArrayList<SootMethod>(); // 存储方法栈结构
		result.add(start);//存储包含decode的方法
		
		//判断当前start方法的返回值
		System.out.println("Get return Type:    "+start.getReturnType().toString());
		String return_type=start.getReturnType().toString();
		//android.graphics.drawable.Drawable
		if((return_type.equals("android.graphics.Bitmap"))||(return_type.equals("android.graphics.drawable.BitmapDrawable"))||(return_type.equals("android.graphics.drawable.Drawable"))){
			stack.add(start);
		}
		
		while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
			int size = stack.size();
			SootMethod sootmethod = stack.get(size - 1); // //get()是从0 开始的,所以要减去1，获得ArrayList中的最后的一个元素
			stack.remove(size - 1);
			store.add(sootmethod);
			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //后向分析
			if (edgeInto!= null) {
				while (edgeInto.hasNext()) {
					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();//获得from
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
		
		//开始reachable分析pattern3和pattern4
		System.out.println("Start getResult_Pattern_34---------------------");
		ArrayList<String> pattern34_0=new ArrayList<String>();
		ArrayList<String> pattern34_1=new ArrayList<String>();
		ArrayList<String> pattern34_2=new ArrayList<String>();
		ArrayList<String> pattern34_3=new ArrayList<String>();
		ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) result);
		reachableMethods.update();
		QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//获得可达的方法集合
		int count_reachable=0;
		while (queueReader.hasNext()) {
			count_reachable++;
			SootMethod qReader=(SootMethod) queueReader.next();
			String method_name=qReader.getSignature();
			//System.out.println("queueReader.next():    "+method_name);      //seadroid共产生249万次输出
					
			//DC开始进行匹配
			for (String str : compress_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with DC:  "+ str);// 分支遍历结束
				   pattern34_0.add(qReader.getSignature());
				}
			}
			
			//MC开始进行匹配
			for (String str : put_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with MC:  "+ str);// 分支遍历结束
				   pattern34_1.add(qReader.getSignature());
				}
			}
			
			//recycle开始进行匹配
			for (String str : recycle_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with recycle:  "+ str);// 分支遍历结束
				   pattern34_2.add(qReader.getSignature());
				}
			}
			
			//inbitmap开始进行匹配
			for (String str : inbitmap_method) {
				if (method_name.contains(str)) {
				   System.out.println("The decode method related with inbitmap:  "+ str);// 分支遍历结束
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
		//开始reachable分析pattern3和pattern4
		System.out.println("Start Frequency_result-----------------");
		ArrayList<SootMethod> start_set=new ArrayList<SootMethod>();
		start_set.add(start);
		
		ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) start_set);
		reachableMethods.update();
		QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//获得可达的方法集合
		int count_reachable=0;
		while (queueReader.hasNext()) {
			count_reachable++;
			SootMethod qReader=(SootMethod) queueReader.next();
			String method_name=qReader.getSignature();
			//System.out.println("queueReader.next():    "+method_name);
					
			//DC开始进行匹配
			if(method_name.contains(target)){
				result=true;
			}
		}
		return result;
	}

public static boolean Frequency_result_set(SootMethod start,ArrayList<SootMethod> decode_method, CallGraph callGraph){
	boolean result=false;
	//开始reachable分析pattern3和pattern4
	System.out.println("Start Frequency_result_set analysis-----------------");
	ArrayList<SootMethod> start_set=new ArrayList<SootMethod>();
	start_set.add(start);
	
	ReachableMethods reachableMethods= new ReachableMethods(callGraph,(Collection) start_set);
	reachableMethods.update();
	QueueReader<MethodOrMethodContext> queueReader=reachableMethods.listener();//获得可达的方法集合
	int count_reachable=0;
	while (queueReader.hasNext()) {
		count_reachable++;
		SootMethod qReader=(SootMethod) queueReader.next();
		String method_name=qReader.getSignature();
		System.out.println("queueReader.next():    "+method_name);
				
		//DC开始进行匹配
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
	 * 这里实现的是后向分析，即以目前的方法作为起点
	 * **/
	public static ArrayList<String> traverse_pattern4_analysis(
			Set<String> compress_method, SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store = new ArrayList<SootMethod>();// 存储已经分析过的方法
		store.add(start);

		ArrayList<String> result = new ArrayList<String>();
		boolean figer = true;
		boolean figer_temp = true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构

		// 判断当前start方法是否就处于compress方法所在的方法
		for (String str : compress_method) {
			if ((start.getSignature().toString().contains(str))) {
				System.out
						.println("The decode method related with recycle0:   "
								+ str);// 分支遍历结束
				figer = false;
				result.add(start.getSignature());
				return result;// 结束检查
			}
		}

		stack.add(start);// 初始入栈
		if (figer) {
			while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
				int size = stack.size();
				System.out.println("pattern3 the size of stack:  " + size);
				SootMethod sootmethod = stack.get(size - 1); // //get()是从0 开始的
																// 所以要减去1，获得ArrayList中的最后的一个元素
				store.add(sootmethod);
				System.out.println("get the method from stack:  "
						+ sootmethod.getName());

				stack.remove(size - 1);
				System.out.println("After Remove, the size of stack:  "
						+ stack.size());

				Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);// .edgesOutOf(sootmethod);
																			// //后向分析

				if (edgeInto != null) {
					// 遍历集合
					while (edgeInto.hasNext()) {

						Edge to = edgeInto.next();
						SootMethod from = (SootMethod) to.getSrc();// 获得from
						System.out
								.println("traverse_recycle_analysis Invoke statement:   "
										+ sootmethod.getName().toString()
										+ "            " + from.getSignature());

						// 判断是否与frequent method关联
						for (String str : compress_method) {
							String method_name = str;
							if (from.getSignature().toString()
									.contains(method_name)) {
								System.out
										.println("The decode method related with recycle1:  "
												+ from.getSignature());// 分支遍历结束
								result.add(from.getSignature());
								figer_temp = false;
								return result;

							}
						}
						if (figer_temp) {
							if (!(store.contains(from))) {// 排除循环调用--》没有必要进行考虑
								stack.add(from);// 添加到栈中
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
