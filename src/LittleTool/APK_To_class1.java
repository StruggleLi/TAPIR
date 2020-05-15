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
 * 功能：（1）生成转换的执行文件，执行命令写入.bat文件中。
 *     （2）生成一个数组内容，静态分析的时候用
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
		//第一步，遍历存放.apk文件的文件夹，获得的各个文件的路径保存在filename表中
		//并将分析的.apk的名字写入到文件中，便于记录到表格。
		//并将path文件夹中包含的.apk文件全部改为.zip文件
		//并生成apk2jar执行命令，命令保存在D:\\transf\\tran.bat文件中
		String path ="C:\\Users\\Dell\\Downloads\\APK"; // .apk文件路径
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
		//获得转换后的Jar文件名列表，并解压文件
		getJarName();   //将生成的jar文件转移到处理路径：D:\\transf\\Jar，获得文件名列表
		unpressJar();   //根据上步获得的文件名列表，解压文件，保存在相同文件夹。File jarFile = new File("D:\\transf\\Jar\\"+jarname.get(i));
		System.out.println("The end of unpressJar");
		}
		
		
		
		
		//需要重置pathF和getStr_Jar中保存路径中的str1和
		if(figer3==1){
		String pathF="D:\\transf\\Jar";
        getfoldername(pathF);//获得解压后的文件夹列表
		
		//获得Jar文件名和获得静态分析数组参数和run.bat
		getStr_Jar(pathF);//根据获得的解压文件夹名列表，生成soot分析命令,生成3个文件：Str[].txt；run.bat；filename.txt;
		System.out.println("The End!");
		}
		
		
		
	}
	
	public static void getfoldername(String path){
	String temp="";//存储最终要进行静态分析的文件名列表
	//获得有效目录，排除Android标准库目录
	File file = new File(path);
    File[] files = file.listFiles();//获得第一级目录
    for(File fileIn : files){
    	if(fileIn.isDirectory()){
    		foldername.add(fileIn.getName());
    		System.out.println(fileIn.getName());	
    	}
    }
    System.out.println("foldername Number:   "+foldername.size());
	}
	/**
	 * 生成apk2jar执行命令
	 * @param path
	 * @throws IOException
	 */
	public static void getStrFordex2Jar(String path1,String path) throws IOException{
		String str="";//写入.bat文件的内容
		for(int i=0;i<filename.size();i++){
			//对于包含.bat的语句，在前面需要加上call命令
			str=str+"call d2j-dex2jar.bat  "+path+"\\"+filename.get(i)+"\r\n";
		}
		writeToFile(str,path1);
	}
	
	public static void getStr_Jar(String path) throws IOException{
		String str1="";//用来静态分析的数组内容
		String str2="";//用来进行分析的执行命令
		String str3="";//生成真正静态分析的文件名列表
		
		for(int i=0;i<foldername.size();i++){

//			str=str+"call d2j-dex2jar.bat  C:\\Users\\Dell\\Downloads\\APK\\"+filename.get(i)+"\r\n";
			
			String fileN=foldername.get(i);
//			String fileN=getFileNameNoEx(temp);
			
			str1=str1+"\"D:/transf/Jar/"+fileN+"\",\""+fileN+"-dex2jar\","+"\r\n";
			
			str2=str2+"java -jar run.jar "+i+"   >"+i+"---"+fileN+".txt"+"\r\n";
			str3=str3+fileN+"\r\n";
			
			//调用命令行，并得到执行结果
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
		String str1="";//用来静态分析的数组内容
		String str2="";//用来进行分析的执行命令
		
		for(int i=0;i<filename.size();i++){

		
			String temp=filename.get(i);
			String fileN=getFileNameNoEx(temp);
			
			str1=str1+"\"D:/transf/Jar/"+fileN+"\",\""+fileN+"\","+"\r\n";
			
			str2=str2+"java -jar run.jar "+i+"   >"+i+"---"+fileN+".txt"+"\r\n";
			
			//调用命令行，并得到执行结果
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
		//解压文件到指定路径
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

        //先读取原有文件内容，然后进行写入操作
    	FileOutputStream fos=new FileOutputStream(filename);
    	fos.write(str.toString().getBytes());
    	fos.close();

    }
	
	/** 
     * 解压文件到当前目录 功能相当于右键 选择解压 
     * @param zipFile 
     * @param 
     * @author gabriel 
     */ 
    @SuppressWarnings("rawtypes") 
    public static void unZipFiles(File zipFile)throws IOException{ 
        //得到压缩文件所在目录 
        String path=zipFile.getAbsolutePath(); 
        path=path.substring(0,path.lastIndexOf("\\")); 
       // System.out.println("path "+path); 
        ZipFile zip = new ZipFile(zipFile); 
        for(Enumeration entries =zip.entries(); 
                entries.hasMoreElements();){ 
            ZipEntry entry = (ZipEntry)entries.nextElement(); 
            String zipEntryName = entry.getName(); 
            InputStream in = zip.getInputStream(entry); 
            //outPath输出目录 
            String outPath = (path+"\\"+zipEntryName).replaceAll("\\*", "/");; 
            //System.out.println("outPath "+outPath); 
            //判断路径是否存在,不存在则创建文件路径 
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/'))); 
            if(!file.exists()){ 
                file.mkdirs(); 
            } 
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压 
            if(new File(outPath).isDirectory()){ 
                continue; 
            } 
            //输出文件路径信息 
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
        System.out.println("******************解压完毕********************"); 
    }
	
	
	/**获得文件夹中的各个文件的名称**/
	public static void getFileName(String APK_path) {
        String path = APK_path; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                System.out.println(fs.getName());
                filename.add(fs.getName());
                
            }
        }
    }
	
	/**获得文件夹中的各个文件的名称**/
	public static void getJarName() {
        String path = "D:\\transf\\Jar"; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
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
	
	//执行命令行
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
     * 解压Jar文件 
     * @param jarFile 指定jar文件 
     * @param tarDir 指定解压文件夹 
     * @throws IOException 抛出异常 
     */  
    public static void uncompress(File jarFile, File tarDir) throws IOException {  
        JarFile jfInst = new JarFile(jarFile);  
        Enumeration<JarEntry> enumEntry = jfInst.entries();  
        while (enumEntry.hasMoreElements()) {  
            JarEntry jarEntry = enumEntry.nextElement();  
            //构造解压文件实体  
            File tarFile = new File(tarDir, jarEntry.getName());  
            //创建文件  
            makeFile(jarEntry, tarFile);  
            if (jarEntry.isDirectory()) {  
                continue;  
            }  
            //构造输出流  
            FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();  
            //取输入流  
            InputStream ins = jfInst.getInputStream(jarEntry);  
            transferStream(ins, fileChannel);  
        }  
    }  
    
    /** 
     * 创建文件 
     * @param jarEntry jar实体 
     * @param fileInst 文件实体 
     * @throws IOException 抛出异常 
     */  
    public static void makeFile(JarEntry jarEntry, File fileInst) {  
        if (!fileInst.exists()) {  
            if (jarEntry.isDirectory()) {  
                fileInst.mkdirs();  
            } else {  
                try {  
                    fileInst.createNewFile();  
                } catch (IOException e) {  
                    System.out.println("创建文件失败>>".concat(fileInst.getPath()));  
                }  
            }  
        }  
    }  
    
    /** 
     * 流交换 
     * @param ins 输入流 
     * @param targetChannel 输出流 
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
     * 修改文件的后缀名
     * path:文件路径
     * from:  原始的后缀名
     * to: 修改后的后缀名
     */
    public static void apktozip(String path, String from, String to) {
        File f = new File(path);
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; ++i) {
          File f2 = fs[i];
          if (f2.isDirectory()) {//如果是文件夹，则递归继续分析里面的文件
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
