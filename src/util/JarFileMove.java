package util;
/*ASM1-jar,
 * ����ѭ������ķ�������jar���е�class�ļ����з�����
 *
 * ����������ʱ��ļ�أ���������
 * �ڲ�׮��main�������õĵط�����һ��ʱ����������Ϊ��ʼʱ��/����׮��Ϣ�����д��ʱ��Ϊ��ֹʱ��
 *
 *ִ��Dacapo:   java -jar dacapo-9.12-bach.jar avrora
 *              java -jar dacapo-9.12-bach.jar avrora -s small
 *
 *������ζ�jar�ļ���ѹ��Ľ���ѹ����
 *           �������ˣ�¥�������װjar��ѹ����ļ����ļ���ѹ����.˵��������,������һ������123.jar���ļ���
 *           ѹ,123.jar��ѹ����ļ��������һ����123���ļ�����,��¥���������123���ļ���ѹ����,�Ӷ�����������
 *           ��������.��ȷ�������ǽ�123�ļ�������ļ����ļ��ж�ѡ��,ѹ��Ϊzip��ʽ��ok��,֮���zip��Ϊjar��
 *           ���ˣ�
 *
 * data:2015-1-2
 * */
import java.io.*;
import java.util.jar.*;
import java.util.*;

//������ʽʹ�ã��ַ���ƥ����
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

    	//�г�Ŀ¼�µ�����.jar�ļ�,����Щjar�ļ����б�������
    			File f= new File("D:\\jar\\");    //����׮�ļ�����·��
    	        File[] f1=f.listFiles();
    	        for(int i=0;i<f1.length;i++)
    	        {
    	           System.out.println("file is  "+f1[i].getName());
    	           String from_path="D:\\jar\\"+f1[i].getName();
    	           String to_path="D:\\jar-to\\"+f1[i].getName(); //��׮����ļ�����·��

    	        		moveJarFile(from_path,to_path);       //���ļ����в�׮
    	        }

    }

    public static void moveJarFile(String from_path,String to_path)
    {
    	try {
			// ��ȡJar�ļ�
			JarFile jarFile = new JarFile(from_path);
			Enumeration class_files = jarFile.entries();//���� �ļ���Ŀ��ö��
			List<JarEntry> lists=new LinkedList<JarEntry>();

			// ����jar���е��ļ�
			int count = 0;
			while (class_files.hasMoreElements()) {
				count++;
				JarEntry entry = (JarEntry) class_files.nextElement();
				lists.add(entry);//����ÿ���ļ������
			}

			//����һ��jaroutputstream��
			File handled=new File(to_path);
			JarOutputStream jos=null;
			FileOutputStream fos=new FileOutputStream(handled);
			jos=new JarOutputStream(fos);

			/*
			 * ��Դ�ļ��е����ݸ��ƹ�����
			 * ����ѭ����һ���ļ��е��ļ���д��jar���С�
			 * */
			for(JarEntry je:lists)
			{
				//jar�е�ÿһ���ļ��У�ÿһ���ļ�������һ��jarEntry
				JarEntry newEntry=new JarEntry(je.getName());
				//����entryд��jar�ļ��У����������ļ��к��ļ�
				jos.putNextEntry(newEntry);

				InputStream is =jarFile.getInputStream(je);
				byte[] bytes=new byte[is.available()];

				is.read(bytes);    //��ȡ����
				is.close();
				//������д��jar�е��ļ�
				jos.write(bytes);
			}
			jos.close();
			fos.close();

			//ɾ��ԭʼ�ļ����������ɵ��ļ�������Ϊԭʼ�ļ�����

    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

}
