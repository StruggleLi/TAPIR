/**
 * 
 */
package Pattern3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

/**
 * @author Dell
 * Data: 2016/12/27
 *
 */
public class Data_flow_analysis {
	/*
	 * ����һ������
	 * ����������������е�����䣬ʶ�𷵻�ֵ
	 * 1�������������ֵ����Щ���÷�������---������MC,DC,recycle,inbitmap
	 *    �Է������е�������˳����������б����ò��ú�����ʹ��
	 *    
	 * 2�����������ʱ�������ݷ���ֵ�Ƿ�Ϊbitmap������ж��Ƿ�����decode���ɵ�bitmap����ֱ����ء�
	 *    �����������ж�return��bitmap�����Ƿ���decode�Ķ�����ͬ���ж��Ƿ�Ϊֱ����ء� 
	 *
	 * */
	
	//bitmap���������ڷ������еķ������õļ�����
	public static ArrayList<SootMethod> data_flow_analysi_forward_invoke(SootMethod sootmethod){
		ArrayList<SootMethod> result=new ArrayList<SootMethod>();
		System.out.println("Start to data_flow analysis, method name:  "+sootmethod.getSignature());
		//��÷����Ĳ���
		Body body = sootmethod.retrieveActiveBody();
		Iterator<Unit> unitIterator = body.getUnits().iterator();
		Value value_from = null;//��¼Ҫ��������bitmap�������ֵ
		String from_method_name="<org.ebookdroid.common.bitmaps.BitmapManager: android.graphics.Bitmap getResource(int)>";
		// ���������е����
		while (unitIterator.hasNext()) {
			SootMethod sm = null;
			Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
			Stmt stmt = (Stmt) u;

			try {
				InvokeExpr invokestmt = stmt.getInvokeExpr();
				sm = invokestmt.getMethod();// �����õķ���
				System.out.println("Soot method--:  "+sm.getSignature());
				if(sm.getSignature().contains(from_method_name)){
					//����и�ֵ������=��   c=a+b;
					//�����-��getDefBoxes      [LinkedVariableBox(i2)]
					List<ValueBox> def=stmt.getDefBoxes();//��ñ��ʽ��������  
					System.out.println(def.toString());
					for(int j=0;j<def.size();j++){
						ValueBox valuebox=def.get(j);
						value_from=valuebox.getValue();
					}
				}

				//����Ҳ������
				List<Value> LV1=invokestmt.getArgs();
				for(int i=0;i<LV1.size();i++){
					Value va=LV1.get(i);
					if(value_from.equals(va)){
						System.out.println("Yes, data_flow_analysi_forward_invoke");
						result.add(sm);
					}
					Type type1=va.getType();
					System.out.println("Value:  "+va.toString()+"-----------"+"type:  "+type1.toString());
				}
				
			}catch (Exception localException2) {
			}
		
		}
		System.out.println("The detail of Body:     "+body.toString());
		return result;
	}
	
	
	//bitmap���������ڷ������ʵ��
		public static ArrayList<SootMethod> data_flow_analysi_forward_args(SootMethod sootmethod){
			ArrayList<SootMethod> result=new ArrayList<SootMethod>();
			System.out.println("Start to data_flow_args analysis, method name:  "+sootmethod.getSignature());
			
			Body body = sootmethod.retrieveActiveBody();
			Iterator<Unit> unitIterator = body.getUnits().iterator();
			Value value_from = null;//��¼Ҫ��������bitmap�������ֵ
			
			// ���������е����
			while (unitIterator.hasNext()) {
				SootMethod sm = null;
				Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
				Stmt stmt = (Stmt) u;
				
				//��÷����Ĳ���---����body��ǰ�漸���ǶԷ������а����ĸ��ֱ����ļ��ж��壬�������Ƿ�����ʵ�Σ���ֱ��ȡ��һ���Ϳ����ˡ�
				if(stmt.toString().contains("@parameter")){
					if(stmt.toString().contains("android.graphics.Bitmap")){
						//����и�ֵ������=��   c=a+b;
						//�����-��getDefBoxes      [LinkedVariableBox(i2)]
						List<ValueBox> def=stmt.getDefBoxes();//��ñ��ʽ��������  
						System.out.println("@parameter");
						for(int j=0;j<def.size();j++){
							ValueBox valuebox=def.get(j);
							value_from=valuebox.getValue();
						}
					}
				}

				try {
					InvokeExpr invokestmt = stmt.getInvokeExpr();
					sm = invokestmt.getMethod();// �����õķ���
					System.out.println("Soot method--:  "+sm.getSignature());

					
					//����Ҳ������
					List<Value> LV1=invokestmt.getArgs();
					for(int i=0;i<LV1.size();i++){
						Value va=LV1.get(i);
						if(value_from.equals(va)){
							System.out.println("Yes, data_flow_analysi_forward_args");
							result.add(sm);
						}
						Type type1=va.getType();
						System.out.println("Value:  "+va.toString()+"-----------"+"type:  "+type1.toString());
					}
					
				}catch (Exception localException2) {
				}
			
			}
			System.out.println("The detail of Body:     "+body.toString());
			return result;
		}
	
		
	public static boolean data_flow_analysi_backward(SootMethod sootmethod){
		boolean result=false;
		System.out.println("Start to data_flow analysis, method name:  "+sootmethod.getSignature());
		//��÷����Ĳ���
		Body body = sootmethod.retrieveActiveBody();
		Iterator<Unit> unitIterator = body.getUnits().iterator();
		Value value_from = null;//��¼Ҫ��������bitmap�������ֵ
		String from_method_name="<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(int,int,android.graphics.Bitmap$Config)>";
		// ���������е����
		while (unitIterator.hasNext()) {
			SootMethod sm = null;
			Unit u = unitIterator.next(); // soot���ýӿ�Unit��ʾstatement
			Stmt stmt = (Stmt) u;

			try {
				InvokeExpr invokestmt = stmt.getInvokeExpr();
				sm = invokestmt.getMethod();// �����õķ���
				System.out.println("Soot method--:  "+sm.getSignature());

				
				//����и�ֵ������=��   c=a+b;
				//�����-��getDefBoxes      [LinkedVariableBox(i2)]
				if(sm.getSignature().equals(from_method_name)){
					List<ValueBox> def=stmt.getDefBoxes();//��ñ��ʽ��������  
					System.out.println(def.toString());
					for(int j=0;j<def.size();j++){
						ValueBox valuebox=def.get(j);
						value_from=valuebox.getValue();
					}
				}
			}catch (Exception localException2) {
			}
			
			//���return��ֵ���ص����ĸ�bitmap����
			//return�����ֽ��������ʽ��    "return r2;"
			sootmethod.getReturnType();
			if(stmt.toString().contains("return")){
				if(stmt.toString().contains(value_from.toString())){
					System.out.println("Yes, data_flow_analysi_backward");
					result=true;
				}
			}
		}
		return result;
	}

}
