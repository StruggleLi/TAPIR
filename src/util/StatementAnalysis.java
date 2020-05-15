/**
 *
 */
package util;

import java.util.ArrayList;

/**
 * @author liwenjie
 * 功能：对于输入的一条语句，能获得该语句右侧的特征：
 * 思路：目前已知soot能够获得的：可以获得一条语句的参数类型。一般我们修改的目标语句都涉及方法调用或者是参数修改，
 *
 * 那么如果已经获得了参数的类型，就能知道包含几个参数。知道包含几个参数，对语句进行词法分析就要简单的多了吧？
 *
 */
public class StatementAnalysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str="mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId, options);";
		ArrayList<String> Args=new ArrayList<String>();
		Args=getArgs(str);
		String left=getLeft(str);
	}

	/**
	 * 获得一个方法调用语句涉及的参数列表
	 * **/
	public static ArrayList<String> getArgs(String str){
		ArrayList<String> Args=new ArrayList<String>();

		//获得()中的子字符串
		String str1=str.substring((str.indexOf("(")+1),str.indexOf(")"));

		System.out.println(str1);

		//获得从第一个字符到第一个“,”之间的字符串，即第一个实参
		String args1=str1.substring(0,str1.indexOf(","));
		Args.add(args1);
		System.out.println(args1);

		//获得从第一个“,”字符开始之后的字符串，即除去第一个实参后的字符串
		String str2=str1.substring(str1.indexOf(",")+1);
		//获得第二个实参
		String args2=str2.substring(0,str2.indexOf(","));
		Args.add(args2);
		System.out.println(args2);

		//获得第三个实参
		String args3=str2.substring(str1.indexOf(",")+1);
		Args.add(args3);

		System.out.println(args3);
		return Args;
	}

	/**
	 * 获得一个方法调用语句"="之前的字符串
	 * **/
	public static String getLeft(String str){
		String result="";
		String str1=str.substring(0,str.indexOf("=")+1);

		System.out.println(str1);
		return result;
	}


	/**
	 * 获得一个方法调用语句调用的方法名
	 *
	 * **/
	public static String getInvokeMethodName(String str){
		String result="";
		String str1=str.substring(str.indexOf("=")+1,str.indexOf("("));

		System.out.println(str1);
		return result;
	}
}






























