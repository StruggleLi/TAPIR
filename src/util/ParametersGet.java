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
 * ���ܣ�������緽��������䴫�ݵĲ�������ֵ�����ı������ȣ�����Ӧ������伶��
 *
 * һ������޷������������ͨ��ֵ�͵���
 *
 * ���������߷ֱ�Ϊ��    ����и�ֵ������=��   c=a+b;
 *                                            �����-��getDefBoxes      [LinkedVariableBox(i2)]
 *                                            �ұ�-��getUseBoxes       [ImmediateBox(b0), ImmediateBox(b1), LinkedRValueBox(b0 + b1)]                ���Ұ���������
 *
 *                    ���û�и�ֵ����        add(a,b);
 *                                         �����-��getDefBoxes      []
 *                                            �ұ�-��getUseBoxes       [ImmediateBox(b0), ImmediateBox(b1), InvokeExprBox(staticinvoke <main.Main: int add(int,int)>(b0, b1))]                ���Ұ���������
 *
 */
public class ParametersGet {

	//���һ�������������Ĳ��������Ͳ�������
	public static data_invoke_parameters getInvokePara(Stmt stmt){
		ArrayList<String> para=new ArrayList<String>();

        //����������䣬��ø��еĵ�����Ϣ
		int number=0;
		try{
		InvokeExpr IE=stmt.getInvokeExpr();
		System.out.println("getInvokeExpr:     "+IE.toString());
		List<Value> LV1=IE.getArgs();
		number=LV1.size();//����������
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
