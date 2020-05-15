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
	 * 给定一个方法
	 * 分析这个方法的所有调用语句，识别返回值
	 * 1）分析这个返回值被哪些调用方法调用---》分析MC,DC,recycle,inbitmap
	 *    对方法体中的语句进行顺序分析，先有被调用才用后续的使用
	 *    
	 * 2）在逆向分析时，会依据返回值是否为bitmap对象而判断是否是与decode生成的bitmap对象直接相关。
	 *    数据流分析判断return的bitmap对象是否与decode的对象相同来判断是否为直接相关。 
	 *
	 * */
	
	//bitmap对象来自于方法体中的方法调用的计算结果
	public static ArrayList<SootMethod> data_flow_analysi_forward_invoke(SootMethod sootmethod){
		ArrayList<SootMethod> result=new ArrayList<SootMethod>();
		System.out.println("Start to data_flow analysis, method name:  "+sootmethod.getSignature());
		//获得方法的参数
		Body body = sootmethod.retrieveActiveBody();
		Iterator<Unit> unitIterator = body.getUnits().iterator();
		Value value_from = null;//记录要检查的语句的bitmap对象变量值
		String from_method_name="<org.ebookdroid.common.bitmaps.BitmapManager: android.graphics.Bitmap getResource(int)>";
		// 遍历方法中的语句
		while (unitIterator.hasNext()) {
			SootMethod sm = null;
			Unit u = unitIterator.next(); // soot中用接口Unit表示statement
			Stmt stmt = (Stmt) u;

			try {
				InvokeExpr invokestmt = stmt.getInvokeExpr();
				sm = invokestmt.getMethod();// 被调用的方法
				System.out.println("Soot method--:  "+sm.getSignature());
				if(sm.getSignature().contains(from_method_name)){
					//如果有赋值操作“=”   c=a+b;
					//则：左边-》getDefBoxes      [LinkedVariableBox(i2)]
					List<ValueBox> def=stmt.getDefBoxes();//获得表达式左侧的内容  
					System.out.println(def.toString());
					for(int j=0;j<def.size();j++){
						ValueBox valuebox=def.get(j);
						value_from=valuebox.getValue();
					}
				}

				//获得右侧的内容
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
	
	
	//bitmap对象来自于方法体的实参
		public static ArrayList<SootMethod> data_flow_analysi_forward_args(SootMethod sootmethod){
			ArrayList<SootMethod> result=new ArrayList<SootMethod>();
			System.out.println("Start to data_flow_args analysis, method name:  "+sootmethod.getSignature());
			
			Body body = sootmethod.retrieveActiveBody();
			Iterator<Unit> unitIterator = body.getUnits().iterator();
			Value value_from = null;//记录要检查的语句的bitmap对象变量值
			
			// 遍历方法中的语句
			while (unitIterator.hasNext()) {
				SootMethod sm = null;
				Unit u = unitIterator.next(); // soot中用接口Unit表示statement
				Stmt stmt = (Stmt) u;
				
				//获得方法的参数---方法body的前面几行是对方法体中包含的各种变量的集中定义，因此如果是方法的实参，则直接取第一个就可以了。
				if(stmt.toString().contains("@parameter")){
					if(stmt.toString().contains("android.graphics.Bitmap")){
						//如果有赋值操作“=”   c=a+b;
						//则：左边-》getDefBoxes      [LinkedVariableBox(i2)]
						List<ValueBox> def=stmt.getDefBoxes();//获得表达式左侧的内容  
						System.out.println("@parameter");
						for(int j=0;j<def.size();j++){
							ValueBox valuebox=def.get(j);
							value_from=valuebox.getValue();
						}
					}
				}

				try {
					InvokeExpr invokestmt = stmt.getInvokeExpr();
					sm = invokestmt.getMethod();// 被调用的方法
					System.out.println("Soot method--:  "+sm.getSignature());

					
					//获得右侧的内容
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
		//获得方法的参数
		Body body = sootmethod.retrieveActiveBody();
		Iterator<Unit> unitIterator = body.getUnits().iterator();
		Value value_from = null;//记录要检查的语句的bitmap对象变量值
		String from_method_name="<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(int,int,android.graphics.Bitmap$Config)>";
		// 遍历方法中的语句
		while (unitIterator.hasNext()) {
			SootMethod sm = null;
			Unit u = unitIterator.next(); // soot中用接口Unit表示statement
			Stmt stmt = (Stmt) u;

			try {
				InvokeExpr invokestmt = stmt.getInvokeExpr();
				sm = invokestmt.getMethod();// 被调用的方法
				System.out.println("Soot method--:  "+sm.getSignature());

				
				//如果有赋值操作“=”   c=a+b;
				//则：左边-》getDefBoxes      [LinkedVariableBox(i2)]
				if(sm.getSignature().equals(from_method_name)){
					List<ValueBox> def=stmt.getDefBoxes();//获得表达式左侧的内容  
					System.out.println(def.toString());
					for(int j=0;j<def.size();j++){
						ValueBox valuebox=def.get(j);
						value_from=valuebox.getValue();
					}
				}
			}catch (Exception localException2) {
			}
			
			//获得return的值返回的是哪个bitmap对象
			//return语句的字节码表现形式：    "return r2;"
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
