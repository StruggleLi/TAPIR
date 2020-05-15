package util;
/*ASM1-jar,
 * 采用循环处理的方法，对jar包中的class文件进行分析。
 *
 * 《《《《对时间的监控：》》》》
 * 在插桩的main函数调用的地方插入一条时间输出语句作为开始时间/将插桩信息的最后写入时间为截止时间
 *
 *执行Dacapo:   java -jar dacapo-9.12-bach.jar avrora
 *              java -jar dacapo-9.12-bach.jar avrora -s small
 *
 *关于如何对jar文件解压后的进行压缩：
 *           不是损坏了！楼主是你把装jar解压后的文件的文件夹压缩了.说具体点就是,如果你把一个叫做123.jar的文件解
 *           压,123.jar解压后的文件都存放在一个叫123的文件夹里,而楼主把这个叫123的文件夹压缩了,从而导致了帖子
 *           标题的情况.正确的做法是将123文件夹里的文件和文件夹都选中,压缩为zip格式就ok了,之后把zip改为jar就
 *           完了！
 *
 * data:2015-1-2
 * */
import java.io.*;
import java.util.jar.*;
import java.util.*;

//正则表达式使用，字符串匹配类
import java.util.regex.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class JarFileMove {

    public static void main(String[] args) throws IOException {

    	//列出目录下的所有.jar文件,对这些jar文件进行遍历操作
    			File f= new File("D:\\jar\\");    //被插桩文件保存路径
    	        File[] f1=f.listFiles();
    	        for(int i=0;i<f1.length;i++)
    	        {
    	           System.out.println("file is  "+f1[i].getName());
    	           String from_path="D:\\jar\\"+f1[i].getName();
    	           String to_path="D:\\jar-to\\"+f1[i].getName(); //插桩后的文件保存路径

    	        		moveJarFile(from_path,to_path);       //对文件进行插桩
    	        }

    }

    public static void moveJarFile(String from_path,String to_path)
    {
    	try {
			// 读取Jar文件
			JarFile jarFile = new JarFile(from_path);
			Enumeration class_files = jarFile.entries();//返回 文件条目的枚举
			List<JarEntry> lists=new LinkedList<JarEntry>();

			// 遍历jar包中的文件
			int count = 0;
			while (class_files.hasMoreElements()) {
				count++;
				JarEntry entry = (JarEntry) class_files.nextElement();
				lists.add(entry);//保存每个文件的入口
			}

			//定义一个jaroutputstream流
			File handled=new File(to_path);
			JarOutputStream jos=null;
			FileOutputStream fos=new FileOutputStream(handled);
			jos=new JarOutputStream(fos);

			/*
			 * 将源文件中的内容复制过来，
			 * 利用循环将一个文件中的文件都写入jar包中。
			 * */
			for(JarEntry je:lists)
			{
				//jar中的每一个文件夹，每一个文件，都是一个jarEntry
				JarEntry newEntry=new JarEntry(je.getName());
				//将该entry写入jar文件中，即创建该文件夹和文件
				jos.putNextEntry(newEntry);

				InputStream is =jarFile.getInputStream(je);
				byte[] bytes=new byte[is.available()];

				is.read(bytes);    //读取操作
				is.close();
				//将内容写入jar中的文件
				jos.write(bytes);
			}
			jos.close();
			fos.close();

			//删除原始文件，将新生成的文件重命名为原始文件名称

    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

}
