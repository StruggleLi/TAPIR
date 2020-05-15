package Pattern3;

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
import Pattern2.Cheak_UI;
import Pattern2.Thread_info;
import Pattern3.Check_Cache;
import Pattern4.Check4;
import Perf.Do_Write;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.FieldRef;
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
import soot.toolkits.graph.pdg.HashMutablePDG;
import soot.toolkits.graph.pdg.PDGNode;
import soot.util.queue.QueueReader;
import util.Check_DiskCache_decode;
import util.Check_Frequently_opra;
import util.ThreadAnalysis;
import util.Thread_data;
import Pattern1.Analysis_Bitmap;
import Pattern1.Data1;

/**
 * @author liwenjie 功能： 获得所有的基本诊断信息。
 *
 */
public class TraverseTheApp {

	public static ArrayList<String> Traverse(Data1 data,Iterator<SootClass> scIterator3,Iterator<SootClass> scIterator4)throws Exception {
		ArrayList<String> pattern3_memorycache_method = new ArrayList<String>();//size()为0
		boolean[] figer_3_MC = { false, false };
		ArrayList<String> find_thread_class=Find_thread_class(scIterator4);//获得程序中的所有线程类
		ArrayList<String> find_thread_class1=new ArrayList<String>();
		System.out.println("The size of thread_class:  "+find_thread_class.size());
		String new_thread="new "; //  用于识别新建的线程类"$r3 = new ThreadTest;"
		for(int i=0;i<find_thread_class.size();i++){
			String temp=find_thread_class.get(i);
			String temp1=new_thread+temp;
			find_thread_class1.add(temp1);
		}

		//Analysis_Bitmap.thread_class_name_set=find_thread_class;
		
//add---Slow API for image showing
		//需要注意的，Android上的意思是这些方法在UI线程调用会导致什么问题，如果对这些方法的调用处于子线程可能就不属于问题了！
		String[] slow_API={
				"android.widget.ImageView: void setImageIcon",
				"android.widget.ImageView: void setImageResource(int)",
				"android.widget.ImageView: void setImageURI"};
		
		String[] displaying_API={"setBackground","drawBitmap","setimageBitmap"};


		String pattern3_scope = "";

		// 对类进行遍历
		while (scIterator3.hasNext()) {
			SootClass currentClass = scIterator3.next();
			Boolean bool = false;

			// 判断当前类为程序中的类且非接口类
			if ((currentClass.isApplicationClass())
					&& (!(currentClass.isInterface()))) {
				bool = true;
			}
			if (bool) {
				System.out.println("Start to analysis class:  "+currentClass.getName());
				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);// 这是对当前的类进行什么呢？
				List<SootMethod> smList = localSootClass.getMethods();// 获取某个类的方法列表，是按照程序实际方法的先后来进行的

				// 遍历当前类中的方法
				for (int i = 0; i < smList.size(); i++) {

					SootMethod sootmethod = smList.get(i);
					
					if (sootmethod.isConcrete()) {

						System.out.println("Start to analysis Method:  "+sootmethod.getName());
						
						// pattern3: 检测是否包含frequency方法--通过分析方法名
						boolean check_frequently = Check_Frequently_opra.find_frequen(sootmethod);
						if (check_frequently) {
							Analysis_Bitmap.pattern3_frequency_method = true;// 标记
							Analysis_Bitmap.frequent_count++;// 计数
							System.out.println("check_frequently:  "+ sootmethod.getSignature());
							Analysis_Bitmap.freq_method.add(sootmethod);
						}

						try {
							Body body = sootmethod.retrieveActiveBody();
							Body body4 = sootmethod.retrieveActiveBody();
							Body body5 = sootmethod.retrieveActiveBody();
							{
								//System.out.println("The body of the Method:  "+body5.toString());

// util.Thread---获得调用子线程的位置
								ArrayList<Thread_info> Thread_info_set= ThreadAnalysis.AnalysisThreadInvoke(body5,find_thread_class1);
								if (Thread_info_set.size()>0) {
									for(int jj=0;jj<Thread_info_set.size();jj++){
										Analysis_Bitmap.thread_data_set.add(Thread_info_set.get(jj));
									}
								}

								// pattern4---------------------------------------------------
								{
									// 进行Pattern4的分析,pattern4中只要包含一类就算是进行优化了，recycle()或者inBitmap
									boolean figer_41 = false;
									figer_41 = Check4.analysis_cache(body4,
											sootmethod);
									if (figer_41) {
										Analysis_Bitmap.pattern4_result = true;
									}
								}

		// 收集pattern3: MemoryCache 涉及的put,get的方法集合
								boolean[] figer_3_temp = { false, false };								
								figer_3_temp = Check_Cache.analysis_cache(body,sootmethod);

								if (figer_3_temp[1]) {// 这里只分析put方法，因为其与decode直接联系，而put则不是。
									pattern3_scope = sootmethod.getSignature();// 获得pattern3中put方法所在的方法名
									pattern3_memorycache_method.add(pattern3_scope);
									System.out.println("Pattern3 Memeory cache put():  "+ pattern3_scope);
									figer_3_MC[1] = true;
								}
								if (figer_3_temp[0]) {
									figer_3_MC[0] = true;
								}

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
										
//add--slow_API
										for(int s=0;s<slow_API.length;s++){
											if(slow_API[s].equals(sm.getName())){
												System.out.println("Slow_API:   "+sm.getSignature());
												System.out.println("Location:  "+sootmethod.getSignature());
												Analysis_Bitmap.slow_API.add(sootmethod.getSignature());
												Analysis_Bitmap.contain_inefAPI_count++;
											}
											
										}
//displaying API										
										for(int s=0;s<displaying_API.length;s++){
											if(displaying_API[s].equals(sm.getName())){
												System.out.println("Displaying_API:   "+sm.getSignature());
												System.out.println("Location:  "+sootmethod.getSignature());
												Analysis_Bitmap.displaying_API.add(sootmethod.getSignature());
											}
											
										}

										
										
										{
											boolean check_compress = Check_DiskCache_decode.DiskCache_compress(sm);
											if (check_compress) {
												System.out.println("The position of compress:    "+ sootmethod.getSignature());
												Analysis_Bitmap.count_compress++;
												Analysis_Bitmap.compress_method.add(sootmethod.getSignature());
											}
										}
										//Find the places where invoke setImageBitmap() method
										{
											boolean display=Bitmap_display.findSetImageBitmap(sm);
											if(display){
												Analysis_Bitmap.setImageBitmap_set.add(sootmethod.getSignature());
												System.out.println("setImageBitmap_set:   "+sootmethod.getSignature());
											}
										}
										//Find the places where invoke decode* method
										{
											// 匹配decode方法
											String[] bitmap_method = data.Bitmap_method;
											for (int k = 0; k < bitmap_method.length; k++) {
												if ((u.toString()).contains(bitmap_method[k])) {
													if(!(Analysis_Bitmap.decode_method.contains(sootmethod))){
														Analysis_Bitmap.decode_method.add(sootmethod);
														System.out.println("The place of decode:   "+sootmethod.getSignature());
													}
																								
												}
											}
										}

									} catch (Exception localException2) {
									}
								}// 逐条语句遍历结束

							}

						} catch (Exception localException2) {
						}

					} else {
						System.out.println("The method is not Concrete");
					}
				}// 方法分析结束
			}
		}
		// 整个程序分析结束

		// 这里是遍历完成之后，判断是否包含pattern3模式--即如果
		if (figer_3_MC[0] && figer_3_MC[1]) {
			System.out.println("Both contain MC_put and MC_get");
		}
		return pattern3_memorycache_method;
	}
	
	
	public static ArrayList<String> Find_thread_class(Iterator<SootClass> scIterator3)throws Exception {
		ArrayList<String> thread_class_name_set=new ArrayList<String>();
		// 对类进行遍历
		while (scIterator3.hasNext()) {
			SootClass currentClass = scIterator3.next();
			Boolean bool = false;

			// 判断当前类为程序中的类且非接口类
			if ((currentClass.isApplicationClass())&& (!(currentClass.isInterface()))) {
				bool = true;
			}
			if (bool) {
				// 修复用-util.Thread
				// 获得线程所在的类-线程的实现类
				String Thread_class_name = ThreadAnalysis.threadSuperAnalysis(currentClass);
				if (!(Thread_class_name.contains("null"))) {
					thread_class_name_set.add(Thread_class_name);
				}// 方法分析结束
			}
		}
		// 整个程序分析结束

		return thread_class_name_set;
	}

	

	// 获得语句的行号
	public int getLinenumber(Unit u) {
		List<Tag> tagList = u.getTags();// 获取uint对象的标签列表，unit即为每一行语句
		int lineNumber = 0;
		for (int n = 0; n < tagList.size(); n++) {
			Tag t = tagList.get(n);
			if (t instanceof LineNumberTag) {// 找到LineNumberTag
				lineNumber = ((LineNumberTag) t).getLineNumber();
			}
		}
		return lineNumber;

	}

}
