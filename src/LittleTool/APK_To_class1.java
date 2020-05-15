/**
 * 
 */
package LittleTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Dell
 * ���ܣ���1������ת����ִ���ļ���ִ������д��.bat�ļ��С�
 *     ��2������һ���������ݣ���̬������ʱ����
 *
 */
public class APK_To_class1 {
	public static ArrayList<String> filename= new ArrayList<String>();
	public static ArrayList<String> jarname= new ArrayList<String>();
	public static ArrayList<String> foldername=new ArrayList<String>();
//	public static ArrayList<String> trans_str= new ArrayList<String>();
	
	public static void main(String[] args) throws IOException{
		int figer1=0;
		int figer2=0;
		int figer3=1;
		
		if(figer1==1){
		//��һ�����������.apk�ļ����ļ��У���õĸ����ļ���·��������filename����
		//����������.apk������д�뵽�ļ��У����ڼ�¼�����
		//����path�ļ����а�����.apk�ļ�ȫ����Ϊ.zip�ļ�
		//������apk2jarִ������������D:\\transf\\tran.bat�ļ���
		String path ="C:\\Users\\Dell\\Downloads\\APK"; // .apk�ļ�·��
//		String from=".apk";
//		String to=".zip";
//	    apktozip(path, from, to);
	        		
		getFileName(path);

		String filenameSet="";
		for(int j=0;j<filename.size();j++){
			filenameSet=filenameSet+filename.get(j)+"\r\n";
		}
		writeToFile(filenameSet,"D:\\apkname111.txt");
 
		String path1="D:\\tran111.bat";
		getStrFordex2Jar(path1,path);
		}

		
		
		
		if(figer2==1){
		//���ת�����Jar�ļ����б�����ѹ�ļ�
		getJarName();   //�����ɵ�jar�ļ�ת�Ƶ�����·����D:\\transf\\Jar������ļ����б�
		unpressJar();   //�����ϲ���õ��ļ����б���ѹ�ļ�����������ͬ�ļ��С�File jarFile = new File("D:\\transf\\Jar\\"+jarname.get(i));
		System.out.println("The end of unpressJar");
		}
		
		
		
		
		//��Ҫ����pathF��getStr_Jar�б���·���е�str1��
		if(figer3==1){
		String pathF="D:\\transf\\Jar";
        getfoldername(pathF);//��ý�ѹ����ļ����б�
		
		//���Jar�ļ����ͻ�þ�̬�������������run.bat
		getStr_Jar(pathF);//���ݻ�õĽ�ѹ�ļ������б�����soot��������,����3���ļ���Str[].txt��run.bat��filename.txt;
		System.out.println("The End!");
		}
		
		
		
	}
	
	public static void getfoldername(String path){
	String temp="";//�洢����Ҫ���о�̬�������ļ����б�
	//�����ЧĿ¼���ų�Android��׼��Ŀ¼
	File file = new File(path);
    File[] files = file.listFiles();//��õ�һ��Ŀ¼
    for(File fileIn : files){
    	if(fileIn.isDirectory()){
    		foldername.add(fileIn.getName());
    		System.out.println(fileIn.getName());	
    	}
    }
    System.out.println("foldername Number:   "+foldername.size());
	}
	/**
	 * ����apk2jarִ������
	 * @param path
	 * @throws IOException
	 */
	public static void getStrFordex2Jar(String path1,String path) throws IOException{
		String str="";//д��.bat�ļ�������
		for(int i=0;i<filename.size();i++){
			//���ڰ���.bat����䣬��ǰ����Ҫ����call����
			str=str+"call d2j-dex2jar.bat  "+path+"\\"+filename.get(i)+"\r\n";
		}
		writeToFile(str,path1);
	}
	
	public static void getStr_Jar(String path) throws IOException{
		String str1="";//������̬��������������
		String str2="";//�������з�����ִ������
		String str3="";//����������̬�������ļ����б�
		
		for(int i=0;i<foldername.size();i++){

//			str=str+"call d2j-dex2jar.bat  C:\\Users\\Dell\\Downloads\\APK\\"+filename.get(i)+"\r\n";
			
			String fileN=foldername.get(i);
//			String fileN=getFileNameNoEx(temp);
			
			str1=str1+"\"D:/transf/Jar/"+fileN+"\",\""+fileN+"-dex2jar\","+"\r\n";
			
			str2=str2+"java -jar run.jar "+i+"   >"+i+"---"+fileN+".txt"+"\r\n";
			str3=str3+fileN+"\r\n";
			
			//���������У����õ�ִ�н��
//			String commandStr = "d2j-dex2jar.bat  C:\\Users\\Dell\\Downloads\\APK\\"+filename.get(i);  
//			System.out.println(commandStr);
////			
//			Runtime.getRuntime().exec(commandStr,null,new File("D:\\TransF\\dex2jar\\"));
			
			//exeCmd(commandStr); 
			
		}
		
		writeToFile(str1,"D:\\TransF\\Str111[].txt");
		writeToFile(str2,"D:\\TransF\\run111.bat");
		writeToFile(str2,"D:\\TransF\\filename111.txt");
	}
	
	public static void getStr1() throws IOException{
		String str1="";//������̬��������������
		String str2="";//�������з�����ִ������
		
		for(int i=0;i<filename.size();i++){

		
			String temp=filename.get(i);
			String fileN=getFileNameNoEx(temp);
			
			str1=str1+"\"D:/transf/Jar/"+fileN+"\",\""+fileN+"\","+"\r\n";
			
			str2=str2+"java -jar run.jar "+i+"   >"+i+"---"+fileN+".txt"+"\r\n";
			
			//���������У����õ�ִ�н��
//			String commandStr = "d2j-dex2jar.bat  C:\\Users\\Dell\\Downloads\\APK\\"+filename.get(i);  
//			System.out.println(commandStr);
////			
//			Runtime.getRuntime().exec(commandStr,null,new File("D:\\TransF\\dex2jar\\"));
			
			//exeCmd(commandStr); 
			
		}
		writeToFile(str1,"D:\\TransF\\Str[].txt");
		writeToFile(str2,"D:\\TransF\\run.bat");
	}
	
	public static void unpressJar(){
	
		System.out.println("the size of jarname is "+jarname.size());
	for(int i=0;i<jarname.size();i++){
		//��ѹ�ļ���ָ��·��
		File jarFile = new File("D:\\transf\\Jar\\"+jarname.get(i));  
		System.out.println("D:\\transf\\Jar\\"+jarname.get(i));
		
		String fileN=getFileNameNoEx(jarname.get(i));
		
        //JarFileUtil.printJarEntry();  
        File targetDir = new File("D:\\transf\\Jar\\"+fileN);  
        System.out.println("D:\\transf\\Jar\\"+fileN);
        try {  
            uncompress(jarFile, targetDir);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		}
	}
	
	public static void writeToFile(String str,String filename) throws IOException{

        //�ȶ�ȡԭ���ļ����ݣ�Ȼ�����д�����
    	FileOutputStream fos=new FileOutputStream(filename);
    	fos.write(str.toString().getBytes());
    	fos.close();

    }
	
	/** 
     * ��ѹ�ļ�����ǰĿ¼ �����൱���Ҽ� ѡ���ѹ 
     * @param zipFile 
     * @param 
     * @author gabriel 
     */ 
    @SuppressWarnings("rawtypes") 
    public static void unZipFiles(File zipFile)throws IOException{ 
        //�õ�ѹ���ļ�����Ŀ¼ 
        String path=zipFile.getAbsolutePath(); 
        path=path.substring(0,path.lastIndexOf("\\")); 
       // System.out.println("path "+path); 
        ZipFile zip = new ZipFile(zipFile); 
        for(Enumeration entries =zip.entries(); 
                entries.hasMoreElements();){ 
            ZipEntry entry = (ZipEntry)entries.nextElement(); 
            String zipEntryName = entry.getName(); 
            InputStream in = zip.getInputStream(entry); 
            //outPath���Ŀ¼ 
            String outPath = (path+"\\"+zipEntryName).replaceAll("\\*", "/");; 
            //System.out.println("outPath "+outPath); 
            //�ж�·���Ƿ����,�������򴴽��ļ�·�� 
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/'))); 
            if(!file.exists()){ 
                file.mkdirs(); 
            } 
            //�ж��ļ�ȫ·���Ƿ�Ϊ�ļ���,����������Ѿ��ϴ�,����Ҫ��ѹ 
            if(new File(outPath).isDirectory()){ 
                continue; 
            } 
            //����ļ�·����Ϣ 
            System.out.println(outPath); 

            FileOutputStream out = new FileOutputStream(outPath); 
            byte[] buf1 = new byte[1024]; 
            int len; 
            while((len=in.read(buf1))>0){ 
                out.write(buf1,0,len); 
            } 
            in.close(); 
            out.close(); 
            } 
        System.out.println("******************��ѹ���********************"); 
    }
	
	
	/**����ļ����еĸ����ļ�������**/
	public static void getFileName(String APK_path) {
        String path = APK_path; // ·��
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [Ŀ¼]");
            } else {
                System.out.println(fs.getName());
                filename.add(fs.getName());
                
            }
        }
    }
	
	/**����ļ����еĸ����ļ�������**/
	public static void getJarName() {
        String path = "D:\\transf\\Jar"; // ·��
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [Ŀ¼]");
            } else {
                System.out.println(fs.getName());
                jarname.add(fs.getName());
                
            }
        }
    }
	
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
	
	//ִ��������
	public static void exeCmd(String commandStr) {  
        BufferedReader br = null;  
        try {  
            Process p = Runtime.getRuntime().exec(commandStr);  
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            StringBuilder sb = new StringBuilder();  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
            System.out.println(sb.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        finally  
        {  
            if (br != null)  
            {  
                try {  
                    br.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
	
	/** 
     * ��ѹJar�ļ� 
     * @param jarFile ָ��jar�ļ� 
     * @param tarDir ָ����ѹ�ļ��� 
     * @throws IOException �׳��쳣 
     */  
    public static void uncompress(File jarFile, File tarDir) throws IOException {  
        JarFile jfInst = new JarFile(jarFile);  
        Enumeration<JarEntry> enumEntry = jfInst.entries();  
        while (enumEntry.hasMoreElements()) {  
            JarEntry jarEntry = enumEntry.nextElement();  
            //�����ѹ�ļ�ʵ��  
            File tarFile = new File(tarDir, jarEntry.getName());  
            //�����ļ�  
            makeFile(jarEntry, tarFile);  
            if (jarEntry.isDirectory()) {  
                continue;  
            }  
            //���������  
            FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();  
            //ȡ������  
            InputStream ins = jfInst.getInputStream(jarEntry);  
            transferStream(ins, fileChannel);  
        }  
    }  
    
    /** 
     * �����ļ� 
     * @param jarEntry jarʵ�� 
     * @param fileInst �ļ�ʵ�� 
     * @throws IOException �׳��쳣 
     */  
    public static void makeFile(JarEntry jarEntry, File fileInst) {  
        if (!fileInst.exists()) {  
            if (jarEntry.isDirectory()) {  
                fileInst.mkdirs();  
            } else {  
                try {  
                    fileInst.createNewFile();  
                } catch (IOException e) {  
                    System.out.println("�����ļ�ʧ��>>".concat(fileInst.getPath()));  
                }  
            }  
        }  
    }  
    
    /** 
     * ������ 
     * @param ins ������ 
     * @param targetChannel ����� 
     */  
    private static void transferStream(InputStream ins, FileChannel targetChannel) {  
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);  
        ReadableByteChannel rbcInst = Channels.newChannel(ins);  
        try {  
            while (-1 != (rbcInst.read(byteBuffer))) {  
                byteBuffer.flip();  
                targetChannel.write(byteBuffer);  
                byteBuffer.clear();  
            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            if (null != rbcInst) {  
                try {  
                    rbcInst.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (null != targetChannel) {  
                try {  
                    targetChannel.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
    
    
    /**
     * �޸��ļ��ĺ�׺��
     * path:�ļ�·��
     * from:  ԭʼ�ĺ�׺��
     * to: �޸ĺ�ĺ�׺��
     */
    public static void apktozip(String path, String from, String to) {
        File f = new File(path);
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; ++i) {
          File f2 = fs[i];
          if (f2.isDirectory()) {//������ļ��У���ݹ��������������ļ�
            apktozip(f2.getPath(), from, to);
          } else {
            String name = f2.getName();
            if (name.endsWith(from)) {
              f2.renameTo(new File(f2.getParent() + "/" + name.substring(0, name.indexOf(from)) + to));
            }
          }
        }
      }
	
	
	

}
