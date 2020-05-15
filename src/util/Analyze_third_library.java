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
 * 分析哪些路径的类来自于第三方库类
 */
public class Analyze_third_library {
	public static void main(String[] args){
		String path="C:\\Users\\Dell\\Desktop\\source code analysis\\AntennaPod-develop\\AntennaPod-develop\\build.gradle";
		ArrayList<String> re=new ArrayList<String>();
		re=readAndAddLine(path);
		System.out.println(re);
		
	}
	/**
     * 获得第三方库的特征
     * **/
    public static ArrayList<String> readAndAddLine(String path1){
    	ArrayList<String> third_lib =new ArrayList<String>();

        try {
            String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
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
                System.out.println("找不到指定的文件！");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容操作出错");
            e.printStackTrace();
        }
        return third_lib;


    }

}
