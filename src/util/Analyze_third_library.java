/**
 * 
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Dell
 * ������Щ·�����������ڵ���������
 */
public class Analyze_third_library {
	public static void main(String[] args){
		String path="C:\\Users\\Dell\\Desktop\\source code analysis\\AntennaPod-develop\\AntennaPod-develop\\build.gradle";
		ArrayList<String> re=new ArrayList<String>();
		re=readAndAddLine(path);
		System.out.println(re);
		
	}
	/**
     * ��õ������������
     * **/
    public static ArrayList<String> readAndAddLine(String path1){
    	ArrayList<String> third_lib =new ArrayList<String>();

        try {
            String encoding = "GBK"; // �ַ�����(�ɽ�������������� )
            File file = new File(path1);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT = null;
                int lineCount=1;
                while ((lineTXT = bufferedReader.readLine()) != null) {
                	String temp=lineTXT;

                	if(temp.contains("classpath \'")){
                		temp=temp.substring((temp.indexOf("\'")+1),temp.indexOf(":"));
                		third_lib.add(temp);
                	}
                	if(temp.contains("classpath \"")){
                		temp=temp.substring((temp.indexOf("\"")+1),temp.indexOf(":"));
                		third_lib.add(temp);
                	}
                	
                }
                read.close();
            }else{
                System.out.println("�Ҳ���ָ�����ļ���");
            }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݲ�������");
            e.printStackTrace();
        }
        return third_lib;


    }

}
