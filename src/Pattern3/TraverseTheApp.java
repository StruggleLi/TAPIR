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
 * @author liwenjie ���ܣ� ������еĻ��������Ϣ��
 *
 */
public class TraverseTheApp {

	public static ArrayList<String> Traverse(Data1 data,Iterator<SootClass> scIterator3,Iterator<SootClass> scIterator4)throws Exception {
		ArrayList<String> pattern3_memorycache_method = new ArrayList<String>();//size()Ϊ0
		boolean[] figer_3_MC = { false, false };
		ArrayList<String> find_thread_class=Find_thread_class(scIterator4);//��ó����е������߳���
		ArrayList<String> find_thread_class1=new ArrayList<String>();
		System.out.println("The size of thread_class:  "+find_thread_class.size());
		String new_thread="new "; //  ����ʶ���½����߳���"$r3 = new ThreadTest;"
		for(int i=0;i<find_thread_class.size();i++){
			String temp=find_thread_class.get(i);
			String temp1=new_thread+temp;
			find_thread_class1.add(temp1);
		}

		//Analysis_Bitmap.thread_class_name_set=find_thread_class;
		
//add---Slow API for image showing
		//��Ҫע��ģ�Android�ϵ���˼����Щ������UI�̵߳��ûᵼ��ʲô���⣬�������Щ�����ĵ��ô������߳̿��ܾͲ����������ˣ�
		String[] slow_API={
				"android.widget.ImageView: void setImageIcon",
				"android.widget.ImageView: void setImageResource(int)",
				"android.widget.ImageView: void setImageURI"};
		
		String[] displaying_API={"setBackground","drawBitmap","setimageBitmap"};


		String pattern3_scope = "";

		// ������б���
		while (scIterator3.hasNext()) {
			SootClass currentClass = scIterator3.next();
			Boolean bool = false;

			// �жϵ�ǰ��Ϊ�����е����ҷǽӿ���
			if ((currentClass.isApplicationClass())
					&& (!(currentClass.isInterface()))) {
				bool = true;
			}
			if (bool) {
				System.out.println("Start to analysis class:  "+currentClass.getName());
				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);// ���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();// ��ȡĳ����ķ����б��ǰ��ճ���ʵ�ʷ������Ⱥ������е�

				// ������ǰ���еķ���
				for (int i = 0; i < smList.size(); i++) {

					SootMethod sootmethod = smList.get(i);
					
					if (sootmethod.isConcrete()) {

						System.out.println("Start to analysis Method:  "+sootmethod.getName());
						
						// pattern3: ����Ƿ����frequency����--ͨ������������
						boolean check_frequently = Check_Frequently_opra.find_frequen(sootmethod);
						if (check_frequently) {
							Analysis_Bitmap.pattern3_frequency_method = true;// ���
							Analysis_Bitmap.frequent_count++;// ����
							System.out.println("check_frequently:  "+ sootmethod.getSignature());
							Analysis_Bitmap.freq_method.add(sootmethod);
						}

						try {
							Body body = sootmethod.retrieveActiveBody();
							Body body4 = sootmethod.retrieveActiveBody();
							Body body5 = sootmethod.retrieveActiveBody();
							{
								//System.out.println("The body of the Method:  "+body5.toString());

// util.Thread---��õ������̵߳�λ��
								ArrayList<Thread_info> Thread_info_set= ThreadAnalysis.AnalysisThreadInvoke(body5,find_thread_class1);
								if (Thread_info_set.size()>0) {
									for(int jj=0;jj<Thread_info_set.size();jj++){
										Analysis_Bitmap.thread_data_set.add(Thread_info_set.get(jj));
									}
								}

								// pattern4---------------------------------------------------
								{
									// ����Pattern4�ķ���,pattern4��ֻҪ����һ������ǽ����Ż��ˣ�recycle()����inBitmap
									boolean figer_41 = false;
									figer_41 = Check4.analysis_cache(body4,
											sootmethod);
									if (figer_41) {
										Analysis_Bitmap.pattern4_result = true;
									}
								}

		// �ռ�pattern3: MemoryCache �漰��put,get�ķ�������
								boolean[] figer_3_temp = { false, false };								
								figer_3_temp = Check_Cache.analysis_cache(body,sootmethod);

								if (figer_3_temp[1]) {// ����ֻ����put��������Ϊ����decodeֱ����ϵ����put���ǡ�
									pattern3_scope = sootmethod.getSignature();// ���pattern3��put�������ڵķ�����
									pattern3_memorycache_method.add(pattern3_scope);
									System.out.println("Pattern3 Memeory cache put():  "+ pattern3_scope);
									figer_3_MC[1] = true;
								}
								if (figer_3_temp[0]) {
									figer_3_MC[0] = true;
								}

								// ��ʼ���������з���
								Iterator<Unit> unitIterator = body.getUnits().iterator();

								while (unitIterator.hasNext()) {

									SootMethod sm = null;
									Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
									Stmt stmt = (Stmt) u;
									// ���������������
									try {
										InvokeExpr invokestmt = stmt.getInvokeExpr();
										sm = invokestmt.getMethod();// �����õķ���
										
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
											// ƥ��decode����
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
								}// ��������������

							}

						} catch (Exception localException2) {
						}

					} else {
						System.out.println("The method is not Concrete");
					}
				}// ������������
			}
		}
		// ���������������

		// �����Ǳ������֮���ж��Ƿ����pattern3ģʽ--�����
		if (figer_3_MC[0] && figer_3_MC[1]) {
			System.out.println("Both contain MC_put and MC_get");
		}
		return pattern3_memorycache_method;
	}
	
	
	public static ArrayList<String> Find_thread_class(Iterator<SootClass> scIterator3)throws Exception {
		ArrayList<String> thread_class_name_set=new ArrayList<String>();
		// ������б���
		while (scIterator3.hasNext()) {
			SootClass currentClass = scIterator3.next();
			Boolean bool = false;

			// �жϵ�ǰ��Ϊ�����е����ҷǽӿ���
			if ((currentClass.isApplicationClass())&& (!(currentClass.isInterface()))) {
				bool = true;
			}
			if (bool) {
				// �޸���-util.Thread
				// ����߳����ڵ���-�̵߳�ʵ����
				String Thread_class_name = ThreadAnalysis.threadSuperAnalysis(currentClass);
				if (!(Thread_class_name.contains("null"))) {
					thread_class_name_set.add(Thread_class_name);
				}// ������������
			}
		}
		// ���������������

		return thread_class_name_set;
	}

	

	// ��������к�
	public int getLinenumber(Unit u) {
		List<Tag> tagList = u.getTags();// ��ȡuint����ı�ǩ�б�unit��Ϊÿһ�����
		int lineNumber = 0;
		for (int n = 0; n < tagList.size(); n++) {
			Tag t = tagList.get(n);
			if (t instanceof LineNumberTag) {// �ҵ�LineNumberTag
				lineNumber = ((LineNumberTag) t).getLineNumber();
			}
		}
		return lineNumber;

	}

}
