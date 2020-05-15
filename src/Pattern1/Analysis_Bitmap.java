/**
 *
 */
package Pattern1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import BaseData.B;
import BaseData.Insert;
import BaseData.McallGraph;
import BaseData.Pattern3_problem_decode;
import BaseData.VH_data;
import Pattern2.Cheak_UI;
import Pattern2.Result_Pattern2;
import Pattern2.Result_Pattern2_back;
import Pattern2.Thread_info;
import Pattern3.Check_Cache;
import Pattern3.Data_flow_analysis;
import Pattern3.Pattern34_result;
import Pattern3.Scene_get;
import Pattern3.TraverseTheApp;
import Pattern4.Check4;
import Perf.Do_Write;
import soot.Body;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
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
import util.ThreadAnalysis;
import util.Thread_data;
import util.Check_DiskCache_decode;
import util.Check_Frequently_opra;

/**
 * @author liwenjie 
 * Time: 11-27
 * �Ľ�pattern3��4�ķ���������ʹ��ReachableMethod�����ǲ��÷���������
 * 
 */
public class Analysis_Bitmap {

	Set<String> appclass = new HashSet<String>();// �Է���������м���
	Set<Insert> insert_store_Problem = new HashSet<Insert>();// �洢���������insert����
	Set<Insert> insert_store_Right = new HashSet<Insert>();// �洢���������insert����
	Set<Insert> insert_store_Maybe = new HashSet<Insert>();// �洢���������insert����
	
	Set<Insert> insert_result_Problem = new HashSet<Insert>();// �洢���������insert����
	Set<Insert> insert_result_Maybe = new HashSet<Insert>();// �洢���������insert����
	Set<Insert> insert_result_Right = new HashSet<Insert>();// �洢���������insert����
	

	Set<Pattern3_problem_decode> pattern3_problem = new HashSet<Pattern3_problem_decode>();// �洢���������insert����
	Set<Pattern3_problem_decode> pattern3_right = new HashSet<Pattern3_problem_decode>();


	ArrayList<String> patter3_method=new ArrayList<String>();
	
	boolean pattern3_figer_MC=false;//��app�Ƿ�ʵ����pattern3ֻ��MC
	
	public static boolean pattern3_frequency_method=false;   //�Ƿ����frequency����
	
	public static int frequent_count=0;   //ͳ��frequency��������
	public static int count_compress=0;   //ͳ��compress������
	
	public static boolean pattern4_result=false;
	public static Set<String> pattern4_set_recycle=new HashSet<String>();  //�洢���а���pattern4�����ķ���
	public static Set<String> pattern4_set_inbitmap=new HashSet<String>();  //�洢���а���pattern4�����ķ���
	
	//Pattern3
	public static ArrayList<String> pattern3_MC_get=new ArrayList<String>();  
	public static Set<String> pattern3_MC_put=new HashSet<String>(); 
	public static ArrayList<String> compress_method=new ArrayList<String>();//DC
	
	//addition
	public static ArrayList<String> slow_API=new ArrayList<String>();
	public static ArrayList<String> displaying_API=new ArrayList<String>();
	
		
	public static int[] pattern4_count={0,0};  //�ֱ�ͳ��recycle��inBitmap������
	public static int[] freq_count={0,0}; //�ֱ�ͳ��getview ��ondraw������
	public static int[] freq_decode={0,0}; //�ֱ�ͳ��decode��getview ��ondraw��ص�����
	public static ArrayList<SootMethod> freq_method=new ArrayList<SootMethod>(); //�洢���е�Frequency����
	
	public static ArrayList<SootMethod> decode_method=new ArrayList<SootMethod>(); //�洢���еİ���decode�ķ���
	public static ArrayList<ArrayList<String>> decode_frequency=new ArrayList<ArrayList<String>>(); //�洢����decode��Ӧ��frequency����
	
	public static int getCallGraph=1;
	
	//�洢�߳���Ϣ--������Ҫ����Э�������޸�
	//public static		Set<String> thread_class_name_set=new HashSet<String>();
	public static		Set<Thread_info> thread_data_set=new HashSet<Thread_info>();
	
	//�洢setImageBitmap()���ڵ�λ��
	public static Set<String> setImageBitmap_set=new HashSet<String>();
	
	//ȫ��ͳ��
	int UI_count=0;
	int unresize_count=0;

	int Child_count=0;	
	int pattern3_disk_count=0;
	int pattern3_disk_count_wrong=0;
	int pattern3_MC_count=0;
	int pattern3_MC_count_wrong=0;
	int pattern3_frequency_count=0;
	int decode_count_app=0;
	
	int pattern4_recycle_wrong=0;
	int pattern4_inbitmap_wrong=0;

	
	int contain_decode_method_count=0;   //ͳ�ư���decode�ķ�������
	public static int contain_inefAPI_count=0;
	
	
	
	public static boolean[] figer_3_MC={false,false};
	//ArrayList<String> pattern3_memorycache_method;//���ڴ洢pattern3��put�������ڵķ���
	
	

	void StartAnalysis(Iterator<SootClass> scIterator1,Iterator<SootClass> scIterator2, Iterator<SootClass> scIterator3, Iterator<SootClass> scIterator4,Data1 data,String file_name,int file_number)
			throws Exception {
				
		// ��������
		String[] bitmap_class = data.Bitmap_class;
		String[] bitmap_method = data.Bitmap_method;
		String[] bitmap_Options = data.Bitmap_Options;
		
		int frequent_problem_count=0;
		int frequent_right_count=0;
		int frequent_maybe_count=0;

		int count_method = 0;
		int count_UI=0;
		int count_UI_fre=0;
		int count_child=0;


//Pattern2--��õ���ͼ��point-to analysis����
		Scene_get scene_get;
		scene_get=Cheak_UI.getCallGraph(scIterator2);
		CallGraph callGraph=scene_get.getCallGraph();
		CallGraph callGraph1=scene_get.getCallGraph();
		PointsToAnalysis pointsTo=scene_get.getPointToAnalysis();
		
		
		
//���decode��frequen�����Ƿ�����ϵ������һ����reachable����---------------�������û����С����ʱ�俪����
		if(false)
		{
			if(freq_method.size()>0){//�����а���getview����onDraw
				for(SootMethod freq_meth: freq_method){
					Check_Frequently_opra.Frequency_result_set(freq_meth,decode_method,callGraph);
				}
			}
			
		}
		
				
//Pattern3 ���pattern3��memory cache��put()���ڵķ�������---------��������е�ͳ��Ԫ������
		{
		patter3_method=TraverseTheApp.Traverse(data,scIterator3,scIterator4);
		if(patter3_method!=null){
			System.out.println("The size of put() Set:   "+patter3_method.size());
			pattern3_figer_MC=true;
		}
		}
		System.out.println("The traverse of APP is over!");


		int pattern1_lineNumber=0;

		// ������б���
		while(scIterator1.hasNext()) {

			SootClass currentClass = scIterator1.next(); 
			Boolean bool = false;

			// �жϵ�ǰ��Ϊ�����е����ҷǽӿ���
			if ((currentClass.isApplicationClass())
					&& (!(currentClass.isInterface()))) {
				bool = true;
			}
			if (bool) {
				String strClassName = currentClass.getName();

				appclass.add(strClassName);
				System.out.println("Start to analysis class��     "	+ strClassName);

				SootClass localSootClass = Scene.v().forceResolve(currentClass.getName(), 3);// ���ǶԵ�ǰ�������ʲô�أ�
				List<SootMethod> smList = localSootClass.getMethods();// ��ȡĳ����ķ����б��ǰ��ճ���ʵ�ʷ������Ⱥ������е�

				count_method = count_method + smList.size();

				// ������ǰ���еķ���
				for (int i = 0; i < smList.size(); i++) {

					String method_temp="";
					String statement="";
					SootMethod sootmethod = smList.get(i);
					System.out.println("Start to analysis method: "	+ sootmethod.getSignature()+"    "+sootmethod.getName());
//����
					//���Ա���
//					if((sootmethod.getName()).equals("executeOnExecutor")){  //forward analysis  ƥ���ַ����������á�==��
//					if((sootmethod.getName()).equals("foo_thread")){  //backward analysis   ƥ���ַ����������á�==��
//						System.out.println("Start traverse");
//					Check_Frequently_opra.traverse_test(sootmethod,callGraph);
//					}
//					
//					if((sootmethod.getName()).equals("foo")){  //backward analysis   ƥ���ַ����������á�==��
//						System.out.println("Start traverse");
//					Check_Frequently_opra.traverse_test(sootmethod,callGraph);
//					}
					
					
					
					
					
					if (sootmethod.isConcrete()) {
						
//��������������						
//						if(sootmethod.getName().equals("paint")){
//							
//							System.out.println("test paint method");
//							Data_flow_analysis.data_flow_analysi_forward_invoke(sootmethod);	
//							Data_flow_analysis.data_flow_analysi_backward(sootmethod);
//							Data_flow_analysis.data_flow_analysi_forward_args(sootmethod);
//						}
		
						try{
						Body body = sootmethod.retrieveActiveBody();
//�������߳�--������еķ�������Ϣ
						if(strClassName.equals("Example")){
						System.out.println("Method name:  "+sootmethod.getName().toString());
						System.out.println("Method Body:  "+body.toString());
						}
						

						String str = "decode";
						String str1="createBitmap";
						
						if ((body.toString().contains(str))||(body.toString().contains(str1))) {
							//System.out.println("Body:   "+body.toString());
							pattern1_lineNumber=0;
							Result_Pattern2 result_pattern2 = null;
							Result_Pattern2_back result_Pattern2_back=null;

							int pattern3_frequency_count_figer=0;
							
							int image_drawable=0;
							int image_without_return=0;


							int count_decode = 0; // ͳ��decode**������
							int count_inJustDecodeBounds = 0;
							boolean figer[] = { false, false }; // �˶�new
																// BitmapFactory.Options()��inSampleSize
							ArrayList<String> pattern3_frequen_result = new ArrayList<String>();
							ArrayList<String> pattern3_frequen_result_back = new ArrayList<String>();
							ArrayList<String> pattern3_disk_result = new ArrayList<String>();
							String pattern3_disk_result_string="";
							String pattern3_memory_result_string="";
							ArrayList<String> pattern3_MC=new ArrayList<String>();
							
							ArrayList<String> pattern4_recycle=new ArrayList<String>();
							ArrayList<String> pattern4_inbitmap=new ArrayList<String>();

							Iterator<Unit> unitIterator = body.getUnits().iterator();

							// ���������е����
							while (unitIterator.hasNext()) {
								SootMethod sm = null;

								Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
								Stmt stmt = (Stmt) u;

								// ���������������
								try {
									InvokeExpr invokestmt = stmt.getInvokeExpr();
									sm = invokestmt.getMethod();// �����õķ���
									
									System.out.println(sootmethod.getSignature());

									// ƥ��new Options
									for (int j = 0; j < bitmap_class.length; j++) {

										if ((u.toString()).contains(bitmap_class[j])) {
											figer[0] = true;
											System.out.println("new Options:   "+ sm.toString());
										}
									}

									// ƥ��decode����
									for (int k = 0; k < bitmap_method.length; k++) {
										if ((u.toString()).contains(bitmap_method[k])) {
//��������������
											Data_flow_analysis.data_flow_analysi_forward_invoke(sootmethod);
											
											
											
//�ж�image��Դ��drawable
											if(2==k){//�����ǰ��decode�� "decodeResource"
												image_drawable=1;
											}
											
//���� PointsTo analysis						
//											{
//												List<Value> list=invokestmt.getArgs();
//												for(int i2=0;i2<list.size();i2++){
//													Value value=list.get(i2);
//													image_source(pointsTo,(Local) value);
//												}
//												
//											}
											
											
//������----������������drawable��ͼƬ�Ĵ�С
											pattern1_lineNumber=getLinenumber(u);
											decode_count_app++;
											
//Over--Pattern2--�Ƿ���UI�߳�----��Ȼ��Ҫ��������ж�����UI����on��ͷ�ķ�������onCreat������
											System.out.println("Start Cheak_UI.traversal_stack: "+sootmethod.getName());
											result_pattern2=Cheak_UI.traverse_UI_analysis(sootmethod,callGraph);
																						
//Pattern3��4 
											//Pattern34_result pattern34_result = null;
											{
												System.out.println("the number of compress_method:   "+compress_method.size());
												System.out.println("the number of patter3_method:    "+patter3_method.size());
												System.out.println("the number of pattern4_set_recycle:    "+pattern4_set_recycle.size());
												System.out.println("the number of pattern4_set_inbitmap:    "+pattern4_set_inbitmap.size());
												//��decode�����������÷���ֵΪbitmap�ķ���
												ArrayList<SootMethod> bitmap_return_result=Check_Frequently_opra.traverse_bitmap_return_analysis(sootmethod,callGraph);
												//pattern34_result=Check_Frequently_opra.getResult_Pattern_34(compress_method,patter3_method,pattern4_set_recycle,pattern4_set_inbitmap,bitmap_return_result,callGraph);
												//DC
												//pattern3_disk_result=pattern34_result.getPattern34_0();																										
												//pattern3_disk_result_string=pattern3_disk_result.toString();
												//MC
												//pattern3_MC=pattern34_result.getPattern34_1();
												//pattern3_memory_result_string=pattern3_MC.toString();
												
												//recycle
												//pattern4_recycle=pattern34_result.getPattern34_2();//Check_Frequently_opra.traverse_pattern4_analysis(pattern4_set_recycle,sootmethod,callGraph);
												//System.out.println("pattern4_recycle:   "+pattern4_recycle.size());
												//inbitmap									
												//pattern4_inbitmap=pattern34_result.getPattern34_3();//Check_Frequently_opra.traverse_pattern4_analysis(pattern4_set_inbitmap,sootmethod,callGraph);
												//System.out.println("pattern4_inbitmap:   "+pattern4_inbitmap.size());
												
//����·����������--������ں�����Ŀ�꺯��-----�߳�����������õ��Ľ���Ƿ���ȷ
//												String test_method="getErrorBitmap";
//												if(sootmethod.getName().equals(test_method)){
//													System.out.println("Start test111111:   "+sootmethod.getSignature());
//													ArrayList<ArrayList<String>> path=Check_Frequently_opra.forward_path_analysis_ArrayList(sootmethod, compress_method, callGraph1);
//													ArrayList<ArrayList<String>> path=Check_Frequently_opra.forward_path_analysis_Set(sootmethod, pattern4_set_recycle, callGraph1);
//													System.out.println("Start test111111  path  size:   "+path.size());
//													for(int ii=0;ii<path.size();ii++){
//														System.out.println("path_test:  "+path.get(ii).size()+"   "+path.get(ii).toString());
//													}
//												}
												
												
												{
													System.out.println("Start path_recycle:    "+sootmethod.getSignature());
													ArrayList<ArrayList<String>> path_recycle=Check_Frequently_opra.return_result_forward_path_analysis_Set(bitmap_return_result,pattern4_set_recycle,callGraph1);
													System.out.println("Finishde path_recycle:    "+sootmethod.getSignature());
													System.out.println("bitmap_return_result:  "+bitmap_return_result.size()+"   "+bitmap_return_result.toString());
													System.out.println("path_recycle:   "+path_recycle.size());
													System.out.println("path_recycle to String:   "+path_recycle.toString());
													pattern4_recycle.add(String.valueOf(path_recycle.size())+":");
													pattern4_recycle.add(path_recycle.toString());
													for(int ii=0;ii<path_recycle.size();ii++){
														System.out.println("path_test_recycle:  "+ii+"    "+path_recycle.get(ii).size()+"   "+path_recycle.get(ii).toString());
													}
													
												}
//MD�Ľ�����									
												{
													System.out.println("Start path_MD:    "+sootmethod.getSignature());
													ArrayList<ArrayList<String>> path_MD=Check_Frequently_opra.return_result_forward_path_analysis_ArrayList(bitmap_return_result,compress_method,callGraph1);
													pattern3_disk_result.add(String.valueOf(path_MD.size())+":");
													pattern3_disk_result.add(path_MD.toString());
													pattern3_disk_result_string=pattern3_disk_result.toString();
													for(int ii=0;ii<path_MD.size();ii++){
														System.out.println("path_test_MD:  "+ii+"    "+path_MD.get(ii).size()+"   "+path_MD.get(ii).toString());
													}
												}
												
//MC�Ľ�����									
												{
													System.out.println("Start path_MC:    "+sootmethod.getSignature());
													ArrayList<ArrayList<String>> path_MC=Check_Frequently_opra.return_result_forward_path_analysis_ArrayList(bitmap_return_result,pattern3_MC_get,callGraph1);//patter3_method
													pattern3_MC.add(String.valueOf(path_MC.size())+":");
													pattern3_MC.add(path_MC.toString());
													pattern3_memory_result_string=pattern3_MC.toString();
													for(int ii=0;ii<path_MC.size();ii++){
														System.out.println("path_test_MC:  "+ii+"    "+path_MC.get(ii).size()+"   "+path_MC.get(ii).toString());
													}
												}
												
//inbitmap�Ľ�����									
												{
													System.out.println("Start path_inbitmap:    "+sootmethod.getSignature());
													ArrayList<ArrayList<String>> path_inbitmap=Check_Frequently_opra.return_result_forward_path_analysis_Set_inbitmap(bitmap_return_result,pattern4_set_inbitmap,callGraph1);
													pattern4_inbitmap.add(String.valueOf(path_inbitmap.size())+":");
													pattern4_inbitmap.add(path_inbitmap.toString());
													for(int ii=0;ii<path_inbitmap.size();ii++){
														System.out.println("path_test_inbitmap:  "+ii+"    "+path_inbitmap.get(ii).size()+"   "+path_inbitmap.get(ii).toString());
													}
												}
											}

											
//���decode��frequen�����Ƿ�����ϵ��������ǰ��ķ���	
											//�޸ģ����Խ���reachable����
											if(false)
											{
												if(freq_method.size()>0){//�����а���getview����onDraw
													for(SootMethod freq_meth: freq_method){//��ÿ��freq�������б���
														boolean reach_decode=Check_Frequently_opra.Frequency_result(freq_meth,sootmethod.getSignature(),callGraph);
														if(reach_decode){
															pattern3_frequen_result.add(freq_meth.getSignature());
														}
													}
												}
												
											}
											//����ʹ��back����
											if(true){
											//if(pattern3_frequency_method){
												System.out.println("Start pattern3_frequency_method!");
												//�������ж��Ƿ��漰
												//pattern3_frequen_result=Check_Frequently_opra.traverse_frequent_analysis(sootmethod,callGraph);
												//�õ�����·��
												ArrayList<String> freq=new ArrayList<String>();
												freq.add("onDraw(android.graphics.Canvas)");
												freq.add("getView(int,android.view.View,android.view.ViewGroup)");
												System.out.println("The size of thread_data_set:  "+thread_data_set.size());
												ArrayList<ArrayList<String>> path_backward_frequency=Check_Frequently_opra.frequency_backward_path_analysis_ArrayList(thread_data_set,sootmethod,freq,callGraph1);
												
												pattern3_frequen_result_back.add(path_backward_frequency.size()+":");
												pattern3_frequen_result_back.add(path_backward_frequency.toString());
												if(pattern3_frequen_result_back!=null){
													pattern3_frequency_count_figer++;
													System.out.println("path_frequency:  "+path_backward_frequency.toString());
												}
											}
//Pattern2:���decode��UI,Child thread�Ƿ�����ϵ��������ǰ��ķ���												
											if(true){
												//if(pattern3_frequency_method){
													System.out.println("Start pattern2!");
													//�������ж��Ƿ��漰
													//pattern3_frequen_result=Check_Frequently_opra.traverse_frequent_analysis(sootmethod,callGraph);
													//�õ�����·��
													ArrayList<String> freq=new ArrayList<String>();
													freq.add("doInBackground");
													freq.add("run");
													result_Pattern2_back=Check_Frequently_opra.thread_backward_path_analysis_ArrayList_Set(sootmethod,freq,callGraph1);
													ArrayList<ArrayList<String>> Child=result_Pattern2_back.getChild_method();
													ArrayList<ArrayList<String>> UI=result_Pattern2_back.getUI_method();
													
													
												}

											method_temp=bitmap_method[k];
											count_decode++;
											statement=u.toString();
										}
									}

								} catch (Exception localException2) {
								}

								// �����ֶη������
								try {
									FieldRef fieldref = stmt.getFieldRef();// ����ֶη������
									SootField field = fieldref.getField();// ��ñ����ʵ��ֶΣ�
									System.out.println("Field access:    "+field.getSignature()+"------   "+field.getName());
									//Field access:    <android.graphics.BitmapFactory$Options: boolean inJustDecodeBounds>------   inJustDecodeBounds


									// ƥ���ֶ�
									if (field.getSignature().contains(
											bitmap_Options[0])) {
										count_inJustDecodeBounds++;

									}
									if (field.getSignature().contains(
											bitmap_Options[1])) {
										figer[1] = true;

									}

								} catch (Exception localException2) {

								}

							}// ���������е�������
							
							//�÷��������Ƿ����decode�����ĵ���
							if(count_decode >=1){
								contain_decode_method_count++;
							}
							
//�����ж�decode�������ͼƬ��С�������ʵ������--------��ͬʱ����options��inJustDecodeBounds���Ҳ�������������Ԫ��  
	                        if(
	                        		((count_inJustDecodeBounds >=1)&&(count_decode >= 1))&&figer[0]&&
									(!(figer[1]))
							  )
	                        	{
	                        	image_without_return=1;
	                        }
							
//pattern2
							if((image_drawable<1)&&(result_pattern2.UI_count>0)){
								if(image_without_return<1){//���decode��������ֵ
									count_UI++;
								}
								
							}
							
							if((result_pattern2.UI_count>0)&&(pattern3_frequency_count_figer>0)){
								count_UI_fre++;
							}
							
							
							if(result_pattern2.Child_count>0)
							{
								count_child++;
							}
							
//Pattern3-----ͳ�ư���frequency��decode�Ƿ���MC��DC����
							System.out.println("pattern3_disk_result:   "+pattern3_frequen_result.size());
						if(pattern3_frequen_result.size()>0){	
							pattern3_frequency_count++;
							
							if(image_without_return<1){//���decode��������ֵ
							if(pattern3_disk_result.size()>0){
								pattern3_disk_count++;
								System.out.println("pattern3_disk_result1:   "+pattern3_disk_result.toString());
							}else{
								pattern3_disk_count_wrong++;
								System.out.println("pattern3_disk_result0:   "+pattern3_disk_result.toString());
							}
							}
							
							if(image_without_return<1){//���decode��������ֵ
							if(pattern3_MC.size()>0){
								pattern3_MC_count++;
							}else{
								pattern3_MC_count_wrong++;
							}
							}
						}
						
						
//Pattern4---����漰ѭ������ȴ������recycle��inbitmap-------------�ܵ�ͳ�Ƹ���
                        if(pattern3_frequen_result.size()>0){
                        	if((pattern4_recycle.size())<1){
                        		if(image_without_return<1){//���decode��������ֵ
                        			pattern4_recycle_wrong++;
                        		}
                        		
                        	}
                        	
                        }
                        
                        if(pattern3_frequen_result.size()>0){
                        	if((pattern4_inbitmap.size())<1){
                        		if(image_without_return<1){//���decode��������ֵ
                        		    pattern4_inbitmap_wrong++;
                        		}
                        	}
                        	
                        }

						
							

							boolean figer1=false;
							boolean figer2=false;
							boolean figer3=false;
//Pattern1							
							if (count_inJustDecodeBounds == 2
									&& count_decode >= 2 && figer[0]
									&& figer[1])// �ж��Ƿ�ͬʱ������������,right
							{

								figer1=true;
								insert_store_Right.add(new Insert(currentClass
										.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
							}else{
								if ((count_inJustDecodeBounds >=1
										|| figer[0]|| figer[1])&&count_decode >= 1 )//��֤decode��������һ��������maybe
								{

									figer2=true;
									insert_store_Maybe.add(new Insert(currentClass
											.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
								}else{
									if ((!(count_inJustDecodeBounds >=1
											|| figer[0]
											|| figer[1]))&&count_decode >= 1)// ��֤decode���Ҳ������κ�һ������
									{
										figer3=true;
										insert_store_Problem.add(new Insert(currentClass
												.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
										if(image_drawable<1){//�������Ĳ���drawable�е�image
											unresize_count++;
										}
										        
									}
									
								}
								
							}
							
							
							//�����figer�����pattern2���Եġ������decode��frequen�ķ���Ҳ��Ҫ���е���
							if (figer3&&
									(pattern3_frequen_result.size()!=0))// �ж��Ƿ�ͬʱ������������
							{
								System.out.println("pattern3_frequen_result.toString():   "+pattern3_frequen_result.toString());
								frequent_problem_count++;
							}
							
							if (figer1&&(pattern3_frequen_result.size()!=0))// �ж��Ƿ�ͬʱ������������
							{
								frequent_right_count++;
							}
							
							if (figer2&&(pattern3_frequen_result.size()!=0))// �ж��Ƿ�ͬʱ������������
							{
								frequent_maybe_count++;
							}
							
							//��������ͳ�ƣ�ȷ��������Ϳ��ɵ����⣬ͬ��������漰Ƶ��ִ�е�decode
							System.out.println("find:"+figer2+" "+pattern3_frequen_result.size()+" "+pattern3_MC.size()+" "+pattern3_disk_result.size());
							if((pattern3_frequen_result.size()>0)&&figer2&&((pattern3_MC.size()<=0)||
									(pattern3_disk_result.size()<=0))){
								insert_result_Maybe.add(new Insert(currentClass
										.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
								        
							}
							//��֤fre������£�resize; child; MC; DCֻҪ��һ�������Ͼ����������
                            if((pattern3_frequen_result.size()>0)&&(
                            		(figer3)||
                            		(pattern3_MC.size()<=0)||
                            		(pattern3_disk_result.size()<=0))
									){
                            	insert_result_Problem.add(new Insert(currentClass
										.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
								        
							}
                            
                            //ͬʱ����resize; child; fre; MC; DC; 
                            System.out.println("pattern3_disk_result.size():"+pattern3_disk_result.size());
                            if((pattern3_frequen_result.size()>0)&&(!figer3)&&((pattern3_MC.size()>0)&&
									(pattern3_disk_result.size()>0))){
                            	System.out.println("Figer!");
                            	insert_result_Right.add(new Insert(currentClass
										.getName(), sootmethod.getSignature(),method_temp,statement,count_decode,count_inJustDecodeBounds,figer[0],figer[1],pattern1_lineNumber,pattern3_frequen_result,result_pattern2,result_Pattern2_back,pattern3_memory_result_string,pattern3_disk_result_string,pattern4_recycle,pattern4_inbitmap,image_drawable,image_without_return,pattern3_frequen_result_back));
								        
							}
                            
                            
                            
							

						}//�Ե�ǰ��һ��body�������������������������
						}catch (Exception localException2) {
						}

					} else {
						System.out.println("The method is not Concrete");
					}

				}//������������
			}
		}
		System.out.println("The number of method in this application:  "+ count_method);
		System.out.println("The number of decode invoke in this application:  "+ decode_count_app);
		System.out.println("The number of Bitmap Problems method in this application:  "+ insert_store_Problem.size());
		System.out.println("The number of Bitmap Right method in this application:  "+ insert_store_Right.size());
		
		System.out.println("The number of Bitmap Maybe in this application:  "+ insert_store_Maybe.size());
		System.out.println("The number of Method related with decode:  "+ contain_decode_method_count);
		


		System.out.println("The number of UI thread decode related method in this application:  "+ count_UI);
		System.out.println("The number of child thread decode related method in this application:  "+ count_child);
		System.out.println("The number of pattern3_disk_count:  "+ pattern3_disk_count);
		System.out.println("The number of pattern3_MC_count:  "+ pattern3_MC_count);
		System.out.println("The number of pattern3_frequency_count:  "+ pattern3_frequency_count);
		
		System.out.println("The number of SetImageBitmap:  "+ setImageBitmap_set.size());
		
		System.out.println("The number of Fre_recycle_wrong:  "+ pattern4_recycle_wrong);
		System.out.println("The number of Fre_inbitmap_wrong:  "+ pattern4_inbitmap_wrong);

		
		// ��������ѭ������

//Pattern3 and pattern4 result analysis
		int pattern3_MC=0;
		boolean pattern3_result=false;
		if(pattern3_figer_MC){
			pattern3_MC=1;
			pattern3_result=true;
		}
		
		int pattern4=0;
		if(pattern4_result){
			pattern4=1;
		}
				
		System.out.println("Pattern3 ����������    "+pattern3_MC);

		
		System.out.println("Pattern4 ����������    "+pattern4);



		System.out.println("�����а������������ϣ��������Ϊ��     " + appclass.size());
		Do_Write do_writer = new Do_Write();

		System.out.println("insert_store_Right:  "+insert_store_Right.size()+"  "+insert_store_Problem.size()+"  "+insert_store_Maybe.size());
		do_writer.wirteToPattern1(insert_store_Problem,file_name+"---Pattern1_Problem.xml");
		do_writer.wirteToPattern1(insert_store_Right,file_name+"---Pattern1_Right.xml");
		do_writer.wirteToPattern1(insert_store_Maybe,file_name+"---Pattern1_Maybe.xml");
		
//		do_writer.wirteToPattern1(insert_result_Maybe,file_name+"---result_Maybe.xml");
//		do_writer.wirteToPattern1(insert_result_Problem,file_name+"---result_Problem.xml");
//		do_writer.wirteToPattern1(insert_result_Right,file_name+"---result_Right.xml");
		
		//do_writer.wirteToPattern1Pattern2andPattern3and4(insert_store_Problem.size(),insert_store_Right.size(),pattern3_yes,pattern3_no,pattern3_problem,pattern3_right,count_UI,count_child,pattern3_result,pattern4_result,file_name+"---Pattern2andPattern3and4_Right.xml",pattern4_method);

		do_writer.wirteToThread(thread_data_set,file_name+"---Thread_Information.xml");
		do_writer.wirteToPattern4_cycle(pattern4_set_recycle,file_name+"---Pattern4_cycle.xml");//�洢pattern4��λ��
		do_writer.wirteToPattern4_inbitmap(pattern4_set_inbitmap,file_name+"---Pattern4_inbitmap.xml");//�洢pattern4��λ��
		
		do_writer.wirteToPattern3_MC(pattern3_MC_get,pattern3_MC_put,file_name+"---Pattern3_MC.xml");
		do_writer.wirteToPattern3_DC(compress_method,file_name+"---Pattern3_DC.xml");
		do_writer.wirteToPattern_API(slow_API,file_name+"---Pattern_slow_API.xml");
		
		System.out.println("The number of slow API :  "+contain_inefAPI_count);
		System.out.println("Current project is :  "+file_name);
//		String output=cache_method_name.size()+" "+file_number+" "+appclass.size()+" "+count_method+" "+insert_store_Right.size()+"/"+insert_store_Problem.size()+" "+count_child+"/"+count_UI+" "+pattern3_result+" "+pattern4_result+"\r\n";
								
			
		//                ���                                �������ͼ���          �������                                     ��������                       ondraw����                          getview����-6          ��ondraw��ص�decode    ��getview��ص�decode     pattern1��frequen֮����ȷ��                                                        ���ܵ�                                               �����                                 decodeͳ��                   
		String output=file_number+" "+getCallGraph+" "+appclass.size()+" "+count_method+" "+freq_count[0]+" "+freq_count[1]+" "+freq_decode[0]+" "+freq_decode[1]+" "+frequent_right_count+" "+frequent_maybe_count+" "+frequent_problem_count+" "+decode_count_app+" "+insert_store_Maybe.size()+" "+insert_store_Right.size()+" "+insert_store_Problem.size()+" "+count_child+" "+count_UI+" "+count_UI_fre+" "+pattern3_MC+" "+pattern3_MC_count+" "+pattern3_MC_count_wrong+" "+count_compress+" "+pattern3_disk_count+" "+pattern3_disk_count_wrong+" "+pattern4+" "+pattern4_count[0]+" "+pattern4_count[1]+" "+insert_result_Maybe.size()+" "+insert_result_Problem.size()+"\r\n";
		String output1=file_number+" "+"insert_store_Maybe.size():"+insert_store_Maybe.size()+" "+insert_store_Right.size()+" "+insert_store_Problem.size()+" "+"count_child:"+count_child+" "+"count_UI:"+count_UI+" "+"count_UI_fre:"+count_UI_fre+" "+"pattern3_MC:"+pattern3_MC+" "+"pattern3_MC_count:"+pattern3_MC_count+" "+"pattern3_MC_count_wrong:"+pattern3_MC_count_wrong+" "+"count_compress:"+count_compress+" "+"pattern3_disk_count:"+pattern3_disk_count+" "+"pattern3_disk_count_wrong:"+pattern3_disk_count_wrong+" "+pattern4+" "+pattern4_count[0]+" "+pattern4_count[1]+" "+insert_result_Maybe.size()+" "+insert_result_Problem.size()+"\r\n";
		String output11=file_name+" "+unresize_count+" "+count_UI+" "+pattern3_disk_count_wrong+" "+pattern3_MC_count_wrong+" "+pattern4_recycle_wrong+" "+pattern4_inbitmap_wrong+" "+insert_result_Maybe.size()+" "+insert_result_Problem.size()+"\r\n";
		
		//�����29��
		
		addToFile("D:\\transf\\result111.txt",output);//�������д������ļ�
		addToFile("D:\\transf\\result111-1.txt",output1);//�������д������ļ�
		addToFile("D:\\transf\\result111-1-1.txt",output11);//�������д������ļ�
		Main_Test.thread_stop_figer=true;
		
		//pattern3_MC_count_wrong:   û�б�MC��decode�ĸ�������������ǰ���decode�ķ���������
		
		if(0==getCallGraph){
			System.out.println("7����ͼ����ʧ��---0");
		}
		if(1==getCallGraph){
			System.out.println("7����ͼ�����ɹ�---1");
		}
		
	}
	
	

	public static void addToFile(String file,String str) throws IOException{
	FileOutputStream fos = new FileOutputStream (file,true ) ;     
	fos.write(str.getBytes()) ;
	fos.close ();   
	}
	
	

	// ��������к�
	public static int getLinenumber(Unit u) {
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
	
	
	//detecting the source type of image
	public static boolean image_source(PointsToAnalysis pointToAnalysis,Local sootfield){
		boolean result=false;
		System.out.println("enter image_source:   ");
		PointsToSet pointSet=pointToAnalysis.reachingObjects(sootfield);
		System.out.println("reachingObjects:   "+pointSet.isEmpty()+"         "+pointSet.possibleTypes().size()+"   "+pointSet.possibleTypes().toString());
		//�Լ��Ͻ��б���
		if(!(pointSet.isEmpty())){
			System.out.println("image_source1");
			System.out.println("possibleClassConstants():    "+pointSet.possibleClassConstants().size());
			Set<String> currentSet=pointSet.possibleStringConstants();//����Ҳ�����android studio������Ĵ�����Ч��Ŀǰ�ᱨ��
			//�ڸ�������Ļ���ַ�������֮�󣬾Ϳ��Խ��е�����
			
			for(String str: currentSet){
				System.out.println("pointSet:   "+str);
				//����
				//PointsToSet pointSet1=pointToAnalysis.reachingObjects(str);
			}
			
		}
		
		return result;		
	}

}
