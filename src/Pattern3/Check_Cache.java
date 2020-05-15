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
 *         ʵ�֣�����Ƿ�������cache�ĺ�����Ȼ���ڽ���decode֮ǰ���Ƿ����ж��Ƿ񻺴������ݣ�
 *
 *         MemoryCache�Ļ���ʵ��˼·��
 *         ����LruCache,ʵ��get,put,��Ҫ����ͼƬ��ImageViewʱ�����ȿ��ܲ���get����
 *         ������ܣ���AsyncTask��decodeͼƬ ��ʾ��ImageView�У���put��LruCache�С�
 *
 *         ���񶼿���ʹ�õ��ù�ϵ��ϵ�����ɣ�Reachable()����-->��ô�͵��ȼ���new��Lrucache�ˡ�
 *
 *         DiskCache�Ļ���ʵ��˼·�� ��AsyncTask�н��У������Ĳ���һ�¡�
 *
 *         ����Ҫ˼�������⣺��μ���Ƿ�����е�decode**�������˻��棿----����������𣿼�����decode�Ƿ���cache��صķ���
 *         ���á����յ�����˵��decodeӦ�ú�cache��ص����з������ڷ������ù�����ϵ��---����ֱ������pattern2�еĴ���
 *
 *
 *         ע�⣺diskCache����Android�Դ��ģ������Լ���д�ġ�
 *
 */
public class Check_Cache {
	
	public static String[] check3_MemoryCache1 = {
		"android.support.v4.util.LruCache: java.lang.Object get(java.lang.Object)",
		"android.support.v4.util.LruCache: java.lang.Object put(java.lang.Object,java.lang.Object)",
		"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: boolean put(java.lang.String,android.graphics.Bitmap)"
		};
	//"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: "
	
	//Ϊ�λ�Ϊ�����cache���ϣ��ͳ���������ѭ���������
	public static String[] check3_MemoryCache = {
		"android.util.LruCache: java.lang.Object get(java.lang.Object)",
		"android.util.LruCache: java.lang.Object put(java.lang.Object,java.lang.Object)",
		"com.nostra13.universalimageloader.cache.memory.BaseMemoryCache: android.graphics.Bitmap get(java.lang.String)",
		};
	

//��ÿһ����������з������ж��Ƿ������Cache����Ҫ������Ԫ�أ���Ҫ��¼λ�á�
//���ܽ�����Ҫ����decode**��put��get���ڵ��ù�ϵ�Ϳ�����
	/**
	 * ���MemoryCache
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
		
		//��body�е����е��÷������з��������������Ƿ����put��add���������Ҳ�������bitmap
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
//				String sm_name=sm.getName();
//				List args_type=sm.getParameterTypes();

				//check memory cache-----------put(String id, Bitmap bitmap)
				int para_number=sm.getParameterCount();
				if(2==para_number){
					String para1=sm.getParameterType(0).toString();
					String para2=sm.getParameterType(1).toString();
					
					//���Ǵ洢Bitmap������Drawable
					if(((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.Bitmap")))||((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.drawable.Drawable")))||((para1.equals("java.lang.String"))&&(para2.equals("android.graphics.drawable.BitmapDrawable")))){
						System.out.println("Memeory Cache:  "+sm.getSignature());
						System.out.println("Location:  "+sootmethod.getSignature());
						Analysis_Bitmap.pattern3_MC_get.add(sootmethod.getSignature());
					}
				}
				
				//�ǳ��Ĳ���ȷ
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



	/**���������Ƿ�decode**�ܵ�cache�Ĺ�Ͻ---���Ƿ���cache��ص�put��get������ϵ
	 * ��������decode**�Ƿ��漰Ƶ��ִ�еĺ������ã�getview  onDraw   ���ǣ���Ļ�����Щ����ϵ��
	 * @param pattern3_method
	 * @param start
	 * @param callGraph
	 * @return
	 */
	
	/**
	 * ����ʵ�ֵ��Ǻ������������Ŀǰ�ķ�����Ϊ���
	 * **/
	public static ArrayList<String> traverse_memorycache_analysis1(ArrayList<String> put_method,SootMethod start, CallGraph callGraph) {
		ArrayList<SootMethod> store= new ArrayList<SootMethod>();//�洢�Ѿ��������ķ���
		store.add(start);
		
		ArrayList<String> result=new ArrayList<String>();
		boolean figer=true;
		boolean figer_temp=true;
		ArrayList<SootMethod> stack = new ArrayList<SootMethod>(); // �洢����ջ�ṹ
		
		//�жϵ�ǰstart�����Ƿ�ʹ���compress�������ڵķ���
		for(int i=0;i<put_method.size();i++){
				if((start.getSignature().toString().contains(put_method.get(i)))){
					System.out.println("The decode method related with put():   "+put_method.get(i));//��֧��������
					figer=false;
					result.add(start.getSignature());
					System.out.println("result.add(start.getSignature()):"+result.size());
				}
		}
				

		stack.add(start);// ��ʼ��ջ
		if(figer){
		while (!(stack.isEmpty())) { // �ж�ջ�����ݷǿգ�ArrayList��������ķ�ʽʵ�ֵ�
			int size = stack.size();
			System.out.println("pattern3 the size of stack:  " + size);
			SootMethod sootmethod = stack.get(size - 1); // //get()�Ǵ�0 ��ʼ��
															// ����Ҫ��ȥ1�����ArrayList�е�����һ��Ԫ��
			store.add(sootmethod);
			System.out.println("get the method from stack:  "+ sootmethod.getName());

			stack.remove(size - 1);
			System.out.println("After Remove, the size of stack:  "	+ stack.size());

			Iterator<Edge> edgeInto = callGraph.edgesInto(sootmethod);  //�������

			if (edgeInto!= null) {
				// ��������
				while (edgeInto.hasNext()) {

					Edge to = edgeInto.next();
					SootMethod from=(SootMethod) to.getSrc();//���from
					System.out.println("Invoke statement:   "+sootmethod.getName().toString()+"            "+from.getSignature());

					//�ж��Ƿ���put()����
					for(int i=0;i<put_method.size();i++)
					{
						String method_name=put_method.get(i);
						if(from.getSignature().toString().contains(method_name)){
							System.out.println("The decode method related with put():  "+from.getSignature());//��֧��������
							result.add(from.getSignature());
							System.out.println("result.add(start.getSignature()):"+result.size());
							figer_temp=false;

						}
					}
					if(figer_temp){
							if(!(store.contains(from))){//�ų�ѭ������--��û�б�Ҫ���п���
							stack.add(from);// ��ӵ�ջ��
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
	 * ͨ������ͼ�Ŀɴ��Է����Ƿ����ʹ�ù��������ķ���
	 */
	public boolean check_(CallGraph callGraph, SootMethod sootmethod) {
		boolean[] figer = { false, false };

		ArrayList<SootMethod> collectionsootMethod = new ArrayList<SootMethod>();
		collectionsootMethod.add(sootmethod);
		ReachableMethods reachableMethods = new ReachableMethods(callGraph,
				(Iterator<MethodOrMethodContext>) collectionsootMethod);
		reachableMethods.update();

		QueueReader<MethodOrMethodContext> queueReader = reachableMethods
				.listener();// ��ÿɴ�ķ�������

		while (queueReader.hasNext()) {// �Կɴ�ķ������б���
			SootMethod qReader = (SootMethod) queueReader.next();
			String str = qReader.getSignature();// �ɴ�ķ�����

			if (!(str.equals("")))// �����Ϊ��
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
