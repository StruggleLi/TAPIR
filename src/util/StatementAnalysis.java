/**
 *
 */
package util;

import java.util.ArrayList;

/**
 * @author liwenjie
 * ���ܣ����������һ����䣬�ܻ�ø�����Ҳ��������
 * ˼·��Ŀǰ��֪soot�ܹ���õģ����Ի��һ�����Ĳ������͡�һ�������޸ĵ�Ŀ����䶼�漰�������û����ǲ����޸ģ�
 *
 * ��ô����Ѿ�����˲��������ͣ�����֪����������������֪�����������������������дʷ�������Ҫ�򵥵Ķ��˰ɣ�
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
	 * ���һ��������������漰�Ĳ����б�
	 * **/
	public static ArrayList<String> getArgs(String str){
		ArrayList<String> Args=new ArrayList<String>();

		//���()�е����ַ���
		String str1=str.substring((str.indexOf("(")+1),str.indexOf(")"));

		System.out.println(str1);

		//��ôӵ�һ���ַ�����һ����,��֮����ַ���������һ��ʵ��
		String args1=str1.substring(0,str1.indexOf(","));
		Args.add(args1);
		System.out.println(args1);

		//��ôӵ�һ����,���ַ���ʼ֮����ַ���������ȥ��һ��ʵ�κ���ַ���
		String str2=str1.substring(str1.indexOf(",")+1);
		//��õڶ���ʵ��
		String args2=str2.substring(0,str2.indexOf(","));
		Args.add(args2);
		System.out.println(args2);

		//��õ�����ʵ��
		String args3=str2.substring(str1.indexOf(",")+1);
		Args.add(args3);

		System.out.println(args3);
		return Args;
	}

	/**
	 * ���һ�������������"="֮ǰ���ַ���
	 * **/
	public static String getLeft(String str){
		String result="";
		String str1=str.substring(0,str.indexOf("=")+1);

		System.out.println(str1);
		return result;
	}


	/**
	 * ���һ���������������õķ�����
	 *
	 * **/
	public static String getInvokeMethodName(String str){
		String result="";
		String str1=str.substring(str.indexOf("=")+1,str.indexOf("("));

		System.out.println(str1);
		return result;
	}
}






























