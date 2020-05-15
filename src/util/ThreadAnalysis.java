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
 * ˼·������������--����1�����ļ����̳й�ϵ����2��new���--��Ҫ���������ֽ������õ�����ı�����ʽ
 *                       ��䣺new LoadImageTask().execute(uriString, picture);
 *
 *       ��ⱨ��--�������ַ���к�
 *
 */
public class ThreadAnalysis {

	/*
	 * �������࣬��λ���߳�ʵ�ֵĴ���λ��
	 * */
	public static String threadSuperAnalysis(SootClass currentSootClass){
		boolean bool=false;
	    SootClass superClass;
	    SootClass interfaceClass;

	    //�ж��Ƿ����������м̳У��Ҽ̳еĳ���Ϊ�޶�������---���������һ�£�������õ�ʲô���
	    if(currentSootClass.hasSuperclass()){
	    superClass=currentSootClass.getSuperclass();
	   	//�ж��Ƿ�Ϊ���߳�
	    bool=ifThread(superClass.getName());
	    }
	    
	    int interfaceCount=currentSootClass.getInterfaceCount();
	    if(interfaceCount>0){
	    	Chain<SootClass> interface_class=currentSootClass.getInterfaces();
	    	String interface_class_all=interface_class.toString();
	    	
		   	//�ж��Ƿ�Ϊ���߳�
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
	 * ����ÿ���������жϸ÷������Ƿ�������̵߳���,ͬʱҲ��Ҫ����һ����������ͬʱ����������̵߳ĵ��á�
	 * */
	public static ArrayList<Thread_info> AnalysisThreadInvoke(Body body5,ArrayList<String> find_thread_class1){
		ArrayList<Thread_info> Thread_info_set=new ArrayList<Thread_info>();
		Thread_info thread1=null;
		SootMethod sootmethod;
		String statement = "";
		//String[] thread_method={"executeOnExecutor","execute","start"};
		String[] thread_method={"executeOnExecutor","execute"};//���ڼ��doInbackground()
		// ����������
		try {
			String inbackground_name=body5.getMethod().getName();
			if(inbackground_name.equals("doInBackground")){
				System.out.println("doInBackground class name:  "+body5.getMethod().getDeclaringClass());
			}

			//���������Ƿ����new thread�ľ���
			{
				for(int ii=0;ii<find_thread_class1.size();ii++){
					if ((body5.toString()).contains(find_thread_class1.get(ii))) {
						sootmethod=body5.getMethod();//������ڵķ���
						thread1=new Thread_info(sootmethod,find_thread_class1.get(ii));
						System.out.println("There is thread1:   "+find_thread_class1.get(ii));
						Thread_info_set.add(thread1);
					}
				}
			}
			//ƥ����õķ���
			for (int k = 0; k < thread_method.length; k++) {
				if ((body5.toString()).contains(thread_method[k])) {
				//��body�е������б���
						{
							Iterator<Unit> unitIterator = body5.getUnits().iterator();
							while (unitIterator.hasNext()) {
								SootMethod sm = null;
								Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
								Stmt stmt = (Stmt) u;
								
								// ���������������
								try {
									InvokeExpr invokestmt = stmt.getInvokeExpr();
									sm = invokestmt.getMethod();// �����õķ���
									String sm_sampleName=sm.getName();
									for(int kk=0;kk<thread_method.length;kk++){
										if(sm_sampleName.equals(thread_method[kk])){
											System.out.println("Thread getDeclaringClass():��"+sm.getDeclaringClass().getName());
											System.out.println("Thread class:��"+sm.getSignature());
											System.out.println("Thread class1:��"+sm.getClass().getName());
											System.out.println("Thread class3:��"+sm.getDeclaration());
											System.out.println("Thread class4:��"+sm.getSubSignature());
											System.out.println("Thread class5:��"+stmt.toString());

											System.out.println("The class be called1:��"+body5.getMethod().getSignature());
											System.out.println("The body5:");
											System.out.println(body5.toString());
											sootmethod=body5.getMethod();//������ڵķ���
											statement=stmt.toString();//���̵߳��õ����
											thread1=new Thread_info(sootmethod,statement);
											Thread_info_set.add(thread1);
											
											//System.out.println("The class be called2:��"+body5.getMethod());
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
	 * ��λ����������
	 * */
	public static int[] locateLineNumber(Body body5){
				String[] thread_class={"android.os.AsyncTask"};
				String[] thread_method={"execute"};

				int lineNumber[]={0,0};

				Iterator<Unit> unitIterator = body5.getUnits().iterator();

				// ���������е����
				while (unitIterator.hasNext()) {
					// ������������ʶ�𣺣�1���������ã���2���ֶη���
					SootMethod sm = null;
					Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
					Stmt stmt = (Stmt) u;

				// ���������������
				try {
					InvokeExpr invokestmt = stmt.getInvokeExpr();

					// ƥ��new��䣬������������ڵ����Ƿ�̳���AsyncTask
					for (int j = 0; j < thread_class.length; j++) {

						if ((u.toString()).contains(thread_class[j])) {
							System.out.println("sm.toString():   "+ sm.toString());
							lineNumber[1] = Analysis_Bitmap.getLinenumber(u);//����к�
						}
					}

					//ƥ����õķ���
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

	//ƥ���Ƿ���Ҫ�������߳���
    public static boolean ifThread(String paramString)
    {
      String[] classSet={"android.os.AsyncTask","java.lang.Thread","java.lang.Runnable"};
      for (int i = 0; i < classSet.length; i++) {
        if (classSet[i].equals(paramString)) {//����ڷ�Χ֮��
        	System.out.println(classSet[i]);
          return true;
        }
      }
      return false;
    }
    
  //ƥ���Ƿ���Ҫ�������߳���
    public static boolean ifThread_implement(String paramString)
    {
      String[] classSet={"android.os.AsyncTask","java.lang.Thread","java.lang.Runnable"};
      for (int i = 0; i < classSet.length; i++) {
        if (paramString.contains(classSet[i])) {//����ڷ�Χ֮��
        	System.out.println("implement:   "+classSet[i]);
          return true;
        }
      }
      return false;
    }

}




