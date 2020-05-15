/**
 *
 */
package util;

import java.util.ArrayList;
import java.util.List;

import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.tagkit.Tag;

/**
 * @author liwenjie
 * 功能：获得诸如方法调用语句传递的参数，赋值操作的变量名等，分析应该是语句级别。
 *
 * 一条语句无非两项操作：普通赋值和调用
 *
 * 操作符两边分别为：    如果有赋值操作“=”   c=a+b;
 *                                            则：左边-》getDefBoxes      [LinkedVariableBox(i2)]
 *                                            右边-》getUseBoxes       [ImmediateBox(b0), ImmediateBox(b1), LinkedRValueBox(b0 + b1)]                ，且包含操作符
 *
 *                    如果没有赋值操作        add(a,b);
 *                                         则：左边-》getDefBoxes      []
 *                                            右边-》getUseBoxes       [ImmediateBox(b0), ImmediateBox(b1), InvokeExprBox(staticinvoke <main.Main: int add(int,int)>(b0, b1))]                ，且包含操作符
 *
 */
public class ParametersGet {

	//获得一个方法调用语句的参数个数和参数类型
	public static data_invoke_parameters getInvokePara(Stmt stmt){
		ArrayList<String> para=new ArrayList<String>();

        //对于问题语句，获得该行的调用信息
		int number=0;
		try{
		InvokeExpr IE=stmt.getInvokeExpr();
		System.out.println("getInvokeExpr:     "+IE.toString());
		List<Value> LV1=IE.getArgs();
		number=LV1.size();//参数的数量
		for(int i=0;i<LV1.size();i++){
			Value va=LV1.get(i);
			Type type=va.getType();
			para.add(type.toString());
		}
		}catch (Exception localException2) {
		}


		return new data_invoke_parameters(number,para);
	}

}
