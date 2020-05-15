/**
 *
 */
package Pattern3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import BaseData.Insert;
import BaseData.McallGraph;
import Pattern1.Analysis_Bitmap;
import Pattern2.Cheak_UI;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import Pattern1.Analysis_Bitmap;

/**
 * @author liwenjie
 *
 *         实现：检测是否真正的cache的核心依然是在进行decode之前，是否有判断是否缓存了数据？
 *
 *         MemoryCache的基本实现思路：
 *         定义LruCache,实现get,put,当要加载图片到ImageView时，首先看能不能get到，
 *         如果不能，在AsyncTask中decode图片 显示到ImageView中，并put到LruCache中。
 *
 *         好像都可以使用调用关系联系起来吧？Reachable()方法-->那么就得先检查出new的Lrucache了。
 *
 *         DiskCache的基本实现思路： 在AsyncTask中进行，其他的步骤一致。
 *
 *         还需要思考的问题：如何检测是否对所有的decode**都进行了缓存？----》逆向分析吗？即分析decode是否被与cache相关的方法
 *         调用。按照道理来说，decode应该和cache相关的所有方法存在方法调用关联关系。---可以直接利用pattern2中的代码
 *
 *
 *         注意：diskCache并非Android自带的，而是自己书写的。
 *
 */
public class Check_Cache {
	
	public static String[] check3_MemoryCache1 = {
		"android.support.v4.util.LruCache: java.lang.Object get(java.lang.Object)",
		"android.support.v4.util.LruCache: java.lang.Object put(java.lang.Object,java.lang.Object)",
		"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: boolean put(java.lang.String,android.graphics.Bitmap)"
		};
	//"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: "
	
	//为何换为下面的cache集合，就出现类似死循环的情况？
	public static String[] check3_MemoryCache = {
		"android.util.LruCache: java.lang.Object get(java.lang.Object)",
		"android.util.LruCache: java.lang.Object put(java.lang.Object,java.lang.Object)",
		"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: android.graphics.Bitmap get(java.lang.String)",
		};
	

//对每一个方法体进行分析，判断是否包含了Cache所需要的所有元素，需要记录位置。
//可能仅仅需要分析decode**和put，get存在调用关系就可以了
	/**
	 * 检测MemoryCache
	 * @param body
	 * @param figer_3
	 * @return
	 */
	public static boolean[] analysis_cache(Body body,SootMethod sootmethod) {
		boolean[] figer_3_MC={false,false};
		
		for(int i=0;i<check3_MemoryCache.length;i++){
		if (body.toString().contains(check3_MemoryCache[i])) {
            System.out.println("Pattern3 Memory cache figer:"+i+"      "+body.toString());
            
            if(1==i){
            	Analysis_Bitmap.pattern3_MC_get.add(sootmethod.getSignature());
            }else{
            	Analysis_Bitmap.pattern3_MC_put.add(sootmethod.getSignature());
            }
            
            figer_3_MC[i]=true;
		}
		}
		int figer=1;
		if(1==figer){
		for(int i=0;i<check3_MemoryCache1.length;i++){
			if (body.toString().contains(check3_MemoryCache1[i])) {
	            System.out.println("Pattern3 Memory cache figer:"+i+"      "+body.toString());
	            
	            if(1==i){
	            	Analysis_Bitmap.pattern3_MC_get.add(sootmethod.getSignature());
	            }else{
	            	Analysis_Bitmap.pattern3_MC_put.add(sootmethod.getSignature());
	            }
	            
	            figer_3_MC[i]=true;
			}
			}
		}
		
		//对body中的所有调用方法进行分析，分析其中是否包含put或add方法，并且参数包含bitmap
		// 开始逐条语句进行分析
		Iterator<Unit> unitIterator = body.getUnits().iterator();
		while (unitIterator.hasNext()) {
			SootMethod sm = null;
			Unit u = unitIterator.next(); // soot中用接口Unit表示statement
			Stmt stmt = (Stmt) u;
			
			// 分析方法调用语句
			try {
				InvokeExpr invokestmt = stmt.getInvokeExpr();
				sm = invokestmt.getMethod();// 被调用的方法
//				String sm_name=sm.getName();
//				List args_type=sm.getParameterTypes();

				//check memory cache-----------put(String id, Bitmap bitmap)
				int para_number=sm.getParameterCount();
				if(2==para_number){
					String para1=sm.getParameterType(0).toString();
					String para2=sm.getParameterType(1).toString();
					
					//考虑存储Bitmap或者是Drawable
					if(((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.Bitmap")))||((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.drawable.Drawable")))||((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.drawable.BitmapDrawable")))){
						System.out.println("Memeory Cache:  "+sm.getSignature());
						System.out.println("Location:  "+sootmethod.getSignature());
						Analysis_Bitmap.pattern3_MC_get.add(sootmethod.getSignature());
					}
				}
				
				//非常的不精确
//				if((sm_name.equals("get"))){
//					for(int jj=0;jj<args_type.size();jj++){
//						if((args_type.get(jj).toString().contains("java.lang.String"))){	
//							System.out.println("analysis_cache:   "+args_type.get(jj).toString());
//							Analysis_Bitmap.pattern3_MC_get.add(sootmethod.getSignature());
//						}
//					}
//				}
				
				
			} catch (Exception localException2) {
			}
		}
		
		
		return figer_3_MC;
	}



	/**遍历分析是否decode**受到cache的管辖---即是否与cache相关的put，get产生联系
	 * 遍历分析decode**是否涉及频繁执行的函数调用：getview  onDraw   但是，真的会与这些有联系吗？
	 * @param pattern3_method
	 * @param start
	 * @param callGraph
	 * @return
	 */
	
	/**
	 * 这里实现的是后向分析，即以目前的方法作为起点
	 * **/
	public static ArrayList<String> traverse_memorycache_analysis1(ArrayList<String> put_method,SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store= new ArrayList<SootMethod>();//存储已经分析过的方法
		store.add(start);
		
		ArrayList<String> result=new ArrayList<String>();
		boolean figer=true;
		boolean figer_temp=true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // 存储方法栈结构
		
		//判断当前start方法是否就处于compress方法所在的方法
		for(int i=0;i<put_method.size();i++){
				if((start.getSignature().toString().contains(put_method.get(i)))){
					System.out.println("The decode method related with put():   "+put_method.get(i));//分支遍历结束
					figer=false;
					result.add(start.getSignature());
					System.out.println("result.add(start.getSignature()):"+result.size());
				}
		}
				

		stack.add(start);// 初始入栈
		if(figer){
		while (!(stack.isEmpty())) { // 判断栈中数据非空，ArrayList是以数组的方式实现的
			int size = stack.size();
			System.out.println("pattern3 the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); // //get()是从0 开始的
															// 所以要减去1，获得ArrayList中的最后的一个元素
			store.add(sootmethod);
			System.out.println("get the method from stack:  "+ sootmethod.getName());

			stack.remove(size - 1);
			System.out.println("After Remove, the size of stack:  "	+ stack.size());

			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //后向分析

			if (edgeInto!= null) {
				// 遍历集合
				while (edgeInto.hasNext()) {

					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();//获得from
					System.out.println("Invoke statement:   "+sootmethod.getName().toString()+"            "+from.getSignature());

					//判断是否与put()关联
					for(int i=0;i<put_method.size();i++)
					{
						String method_name=put_method.get(i);
						if(from.getSignature().toString().contains(method_name)){
							System.out.println("The decode method related with put():  "+from.getSignature());//分支遍历结束
							result.add(from.getSignature());
							System.out.println("result.add(start.getSignature()):"+result.size());
							figer_temp=false;

						}
					}
					if(figer_temp){
							if(!(store.contains(from))){//排除循环调用--》没有必要进行考虑
							stack.add(from);// 添加到栈中
							System.out.println("Add to stack");
							}
					}
				}
			}
			}
		}

		System.out.println("result.size():"+result.size());
		return result;
	}


	/*
	 * 通过调用图的可达性分析是否存在使得功能完整的方法
	 */
	public boolean check_(CallGraph callGraph, SootMethod sootmethod) {
		boolean[] figer = { false, false };

		ArrayList<SootMethod> collectionsootMethod = new ArrayList<SootMethod>();
		collectionsootMethod.add(sootmethod);
		ReachableMethods reachableMethods = new ReachableMethods(callGraph,
				(Iterator<MethodOrMethodContext>) collectionsootMethod);
		reachableMethods.update();

		QueueReader<MethodOrMethodContext> queueReader = reachableMethods
				.listener();// 获得可达的方法集合

		while (queueReader.hasNext()) {// 对可达的方法进行遍历
			SootMethod qReader = (SootMethod) queueReader.next();
			String str = qReader.getSignature();// 可达的方法名

			if (!(str.equals("")))// 如果不为空
			{
				for (int k = 0; k < check3_MemoryCache.length; k++) {

					if (str.equals(check3_MemoryCache[k])) {
						figer[k - 1] = true;
						System.out.println("We have find :  " + str);

					}
				}
			}
		}

		return figer[0] && figer[1];
	}

}
