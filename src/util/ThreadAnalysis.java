/**
 *
 */
package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import BaseData.VH_data;
import BaseData.f;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;


import soot.util.Chain;
import Pattern1.Analysis_Bitmap;
import Pattern2.Thread_info;

/**
 * @author liwenjie
 * 思路：检测两类语句--》（1）类文件，继承关系；（2）new语句--需要分析具体字节码来得到具体的表现形式
 *                       语句：new LoadImageTask().execute(uriString, picture);
 *
 *       检测报告--》具体地址，行号
 *
 */
public class ThreadAnalysis {

	/*
	 * 分析超类，定位子线程实现的代码位置
	 * */
	public static String threadSuperAnalysis(SootClass currentSootClass){
		boolean bool=false;
	    SootClass superClass;
	    SootClass interfaceClass;

	    //判断是否对其他类进行继承，且继承的超类为限定的类型---》可以输出一下，看看会得到什么结果
	    if(currentSootClass.hasSuperclass()){
	    superClass=currentSootClass.getSuperclass();
	   	//判断是否为子线程
	    bool=ifThread(superClass.getName());
	    }
	    
	    int interfaceCount=currentSootClass.getInterfaceCount();
	    if(interfaceCount>0){
	    	Chain<SootClass> interface_class=currentSootClass.getInterfaces();
	    	String interface_class_all=interface_class.toString();
	    	
		   	//判断是否为子线程
		    bool=ifThread_implement(interface_class_all);
		    }	    
	    
	    if(bool){
        	System.out.println("Find the thread class:   "+currentSootClass.getName());
	    	return currentSootClass.getName();
	    	
	    }else{
	    	return "null";
	    }
	}


	/*
	 * 分析每个方法，判断该方法中是否存在子线程调用,同时也需要考虑一个方法体中同时包含多个子线程的调用。
	 * */
	public static ArrayList<Thread_info> AnalysisThreadInvoke(Body body5,ArrayList<String> find_thread_class1){
		ArrayList<Thread_info> Thread_info_set=new ArrayList<Thread_info>();
		Thread_info thread1=null;
		SootMethod sootmethod;
		String statement = "";
		//String[] thread_method={"executeOnExecutor","execute","start"};
		String[] thread_method={"executeOnExecutor","execute"};//用于检测doInbackground()
		// 分析方法体
		try {
			String inbackground_name=body5.getMethod().getName();
			if(inbackground_name.equals("doInBackground")){
				System.out.println("doInBackground class name:  "+body5.getMethod().getDeclaringClass());
			}

			//方法体中是否包含new thread的句子
			{
				for(int ii=0;ii<find_thread_class1.size();ii++){
					if ((body5.toString()).contains(find_thread_class1.get(ii))) {
						sootmethod=body5.getMethod();//语句所在的方法
						thread1=new Thread_info(sootmethod,find_thread_class1.get(ii));
						System.out.println("There is thread1:   "+find_thread_class1.get(ii));
						Thread_info_set.add(thread1);
					}
				}
			}
			//匹配调用的方法
			for (int k = 0; k < thread_method.length; k++) {
				if ((body5.toString()).contains(thread_method[k])) {
				//对body中的语句进行遍历
						{
							Iterator<Unit> unitIterator = body5.getUnits().iterator();
							while (unitIterator.hasNext()) {
								SootMethod sm = null;
								Unit u = unitIterator.next(); // soot中用接口Unit表示statement
								Stmt stmt = (Stmt) u;
								
								// 分析方法调用语句
								try {
									InvokeExpr invokestmt = stmt.getInvokeExpr();
									sm = invokestmt.getMethod();// 被调用的方法
									String sm_sampleName=sm.getName();
									for(int kk=0;kk<thread_method.length;kk++){
										if(sm_sampleName.equals(thread_method[kk])){
											System.out.println("Thread getDeclaringClass():　"+sm.getDeclaringClass().getName());
											System.out.println("Thread class:　"+sm.getSignature());
											System.out.println("Thread class1:　"+sm.getClass().getName());
											System.out.println("Thread class3:　"+sm.getDeclaration());
											System.out.println("Thread class4:　"+sm.getSubSignature());
											System.out.println("Thread class5:　"+stmt.toString());

											System.out.println("The class be called1:　"+body5.getMethod().getSignature());
											System.out.println("The body5:");
											System.out.println(body5.toString());
											sootmethod=body5.getMethod();//语句所在的方法
											statement=stmt.toString();//子线程调用的语句
											thread1=new Thread_info(sootmethod,statement);
											Thread_info_set.add(thread1);
											
											//System.out.println("The class be called2:　"+body5.getMethod());
										}
									}
									
									
								}catch (Exception localException2) {
								}
								
							}
						}
					}
				}
				
				
	
		}catch (Exception localException2) {
		}
		return Thread_info_set;

			
	}

	/*
	 * 定位到具体的语句
	 * */
	public static int[] locateLineNumber(Body body5){
				String[] thread_class={"android.os.AsyncTask"};
				String[] thread_method={"execute"};

				int lineNumber[]={0,0};

				Iterator<Unit> unitIterator = body5.getUnits().iterator();

				// 遍历方法中的语句
				while (unitIterator.hasNext()) {
					// 进行两类语句的识别：（1）方法调用；（2）字段访问
					SootMethod sm = null;
					Unit u = unitIterator.next(); // soot中用接口Unit表示statement
					Stmt stmt = (Stmt) u;

				// 分析方法调用语句
				try {
					InvokeExpr invokestmt = stmt.getInvokeExpr();

					// 匹配new语句，分析该语句所在的类是否继承了AsyncTask
					for (int j = 0; j < thread_class.length; j++) {

						if ((u.toString()).contains(thread_class[j])) {
							System.out.println("sm.toString():   "+ sm.toString());
							lineNumber[1] = Analysis_Bitmap.getLinenumber(u);//获得行号
						}
					}

					//匹配调用的方法
					for (int k = 0; k < thread_method.length; k++) {

						if ((u.toString()).contains(thread_method[k])) {
							lineNumber[2]=Analysis_Bitmap.getLinenumber(u);
						}
					}

				}catch (Exception localException2) {
				}
				}

					return lineNumber;

	}



//-------------------------------------------------

	//匹配是否是要分析的线程类
    public static boolean ifThread(String paramString)
    {
      String[] classSet={"android.os.AsyncTask","java.lang.Thread","java.lang.Runnable"};
      for (int i = 0; i < classSet.length; i++) {
        if (classSet[i].equals(paramString)) {//如果在范围之内
        	System.out.println(classSet[i]);
          return true;
        }
      }
      return false;
    }
    
  //匹配是否是要分析的线程类
    public static boolean ifThread_implement(String paramString)
    {
      String[] classSet={"android.os.AsyncTask","java.lang.Thread","java.lang.Runnable"};
      for (int i = 0; i < classSet.length; i++) {
        if (paramString.contains(classSet[i])) {//如果在范围之内
        	System.out.println("implement:   "+classSet[i]);
          return true;
        }
      }
      return false;
    }

}




