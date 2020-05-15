package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class FileModify{

    /**
     * ��ȡ�ļ�ָ���С�
     * java����TXT�ļ������ж���д���޸Ĳ���
     * site: http://blog.csdn.net/ssyan/article/details/6534108
     * @throws IOException
     */
    public static void start(int lineNumber,String import_line,String replace_API_string,String file_path) throws IOException {


        String path1="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder1\\ImageWorker.java";
        String path2="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder2\\ImageWorker.java";
        String path3="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder3\\ImageWorker.java";
//        createFile(path2);

        String searchText="decodeResource";
    	String replaceText="decodeFile";
    	String line="add line in the line";
    	boolean import_figer=false;

    	int addLineLineNumber=119;

//        String fileStr=readAndModifyFile(path1,searchText,replaceText,lineNumber);
//        modifyFile(path1,lineNumber);
//        writeTxtFile(fileStr,path2);

        copyFile(path1,path3);

        //��ָ����ǰ�������
        String read=readAndAddLine(path1,line,addLineLineNumber,import_figer);
        writeTxtFile(read,path3);

//        ReadWriteFile.replaceTxtByStr("ken", "zhang");
    }

    /**
     * ��ָ����ǰ������䣬���߲���import���
     * **/
    public static String readAndAddLine(String path1,String line,int lineNumber,boolean import_figer){
    	String readStr ="";

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

                	if(lineCount==lineNumber&&(!import_figer)){
                     System.out.println(lineTXT.toString().trim());
                     temp=line+"\r\n"+temp;
                	}

                	if(import_figer&&(temp.startsWith("import"))){
                		temp=line+"\r\n"+temp;
                	}
                	readStr = readStr + temp+"\r\n";   //���������ļ�����
                     lineCount++;
                }
                read.close();
            }else{
                System.out.println("�Ҳ���ָ�����ļ���");
            }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݲ�������");
            e.printStackTrace();
        }
        return readStr;


    }



    public static void copyFile(String filepath1,String filepath3) throws IOException{
    	createFile(filepath3);
    	String read=readFile(filepath1);
    	writeTxtFile(read,filepath3);
    }

    /**
     * ��ȡ���޸��ļ���ָ���е�����
     * (1) insert import** ; (2) modify decode** function name.
     */
        public static String readAndModifyFile(String path,String searchText,String replaceText,
        		int lineNumber,String import_line,String paras) {

        	String readStr ="";
        	boolean import_figer=false;

            try {
                String encoding = "GBK"; // �ַ�����(�ɽ�������������� )
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    InputStreamReader read = new InputStreamReader(
                            new FileInputStream(file), encoding);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTXT = null;
                    int lineCount=1;
                    while ((lineTXT = bufferedReader.readLine()) != null) {
                    	String temp=lineTXT;

                    	//insert import** line
                    	if((!import_figer)&&temp.startsWith("import")){
                    		temp=import_line+"\r\n"+temp;
                    		import_figer=true;
                    	}

                    	//modify function name and add paras
                    	if(lineCount==lineNumber){
                         System.out.println(lineTXT.toString().trim());
                         temp=modyfyStmtAndAddPara(searchText,replaceText,lineTXT,paras);
                    	}
                    	readStr = readStr + temp+"\r\n";   //���������ļ�����
                         lineCount++;
                    }
                    read.close();
                }else{
                    System.out.println("�Ҳ���ָ�����ļ���");
                }
            } catch (Exception e) {
                System.out.println("��ȡ�ļ����ݲ�������");
                e.printStackTrace();
            }
            return readStr;
        }

        public static String readFile(String path) {

        	String readStr ="";

            try {
                String encoding = "GBK"; // �ַ�����(�ɽ�������������� )
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    InputStreamReader read = new InputStreamReader(
                            new FileInputStream(file), encoding);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTXT = null;
                    while ((lineTXT = bufferedReader.readLine()) != null) {
                    	String temp=lineTXT;
                    	readStr = readStr + temp+"\r\n";   //���������ļ�����
                    }
                    read.close();
                }else{
                    System.out.println("�Ҳ���ָ�����ļ���");
                }
            } catch (Exception e) {
                System.out.println("��ȡ�ļ����ݲ�������");
                e.printStackTrace();
            }
            return readStr;
        }


        /**
         * ���ļ��е�ĳһ�н����޸�
         * site:  http://www.jb51.net/article/47365.htm
         * ���ܣ���ĳһ�н����޸Ĳ���Ӳ���
         *
         * */
        public static String modyfyStmtAndAddPara(String searchText, String replaceText,String lineTXT,String paras){
        	System.out.println(lineTXT);
        	String lineTXT_change=lineTXT.replaceAll(searchText, replaceText);

        	System.out.println(lineTXT_change);
        	System.out.println("Function Name has been replaced!");

        	//begin change the para
        	String result=lineTXT_change;
        	String str1=lineTXT_change.substring((lineTXT_change.indexOf("(")+1),lineTXT_change.indexOf(")"));
        	String str2=str1+paras;
        	result=result.replaceAll(str1,str2);
        	return result;

        }

        /**
         * ���ļ��е�ĳһ�н����޸�
         * site:  http://www.jb51.net/article/47365.htm
         * ���ܣ���ĳһ�н����޸�
         *
         * */
        public static String modyfyStmt(String searchText, String replaceText,String lineTXT){
        	System.out.println(lineTXT);
        	String lineTXT_change=lineTXT.replaceAll(searchText, replaceText);

        	System.out.println(lineTXT_change);
        	System.out.println("Function Name has been replaced!");


        	return lineTXT_change;

        }


            /** *//**
             * �����ļ� .
             *
             */
            public static void createFile(String path) throws IOException{
            	File filename = new File(path);
                if (!filename.exists()) {
                    filename.createNewFile();
                    System.err.println(filename + "�Ѵ����� ");
                }
            }

            /** *//**
             * д�ļ� .
             *
             */
            public static void writeTxtFile(String str,String filename) throws IOException{

                //�ȶ�ȡԭ���ļ����ݣ�Ȼ�����д�����
            	FileOutputStream fos=new FileOutputStream(filename);
            	fos.write(str.toString().getBytes());
            	fos.close();

            }

            /** *//**
             * ���ļ���ָ�����ݵĵ�һ���滻Ϊ�������� .
             *
             * @param oldStr
             *            ��������
             * @param replaceStr
             *            �滻����
             */
            public void replaceTxtByStr(String oldStr,String replaceStr,String path) {
                String temp = "";
                try {
                    File file = new File(path);
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuffer buf = new StringBuffer();

                    // �������ǰ�������
                    for (int j = 1; (temp = br.readLine()) != null
                            && !temp.equals(oldStr); j++) {
                        buf = buf.append(temp);
                        buf = buf.append(System.getProperty("line.separator"));
                    }

                    // �����ݲ���
                    buf = buf.append(replaceStr);

                    // ������к��������
                    while ((temp = br.readLine()) != null) {
                        buf = buf.append(System.getProperty("line.separator"));
                        buf = buf.append(temp);
                    }

                    br.close();
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(fos);
                    pw.write(buf.toString().toCharArray());
                    pw.flush();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

}
