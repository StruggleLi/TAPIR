/**
 *
 */
package Pattern4;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author liwenjie
 * ���ܣ���Android�е�project.properties�ļ����з�����ʶ�����Ӧ��API�汾��
 *
 */
public class Api_banben {
	public static void main(String[] args) throws IOException {
	    Properties props = new Properties();
	    String propsPath = "D:\\Workplace-All\\ICSE2014\\omnidroid\\project.properties";
	    try (InputStream in = Files.newInputStream(Paths.get(propsPath))) {
	      props.load(in);
	      String target=props.getProperty("target");//ȷ����target��
	      int tar_API=getApiNumber(target);
	      System.out.println(tar_API);
	    }
	  }

	/*
	 * ����ַ����а�����Android�汾��
	 * */
	private static int getApiNumber(String target){
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(target);
		String tar_str=m.replaceAll("").trim();
		int tar_int=Integer.parseInt(tar_str);
		return tar_int;

	}
	
	
	public static void getApkInformation(){
//		String filePath = "/sdcard/feijiedemo.apk";  
//	    PackageManager packageManager = getPackageManager();  
//	    PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);  
//	    Log.d("name", packageInfo.packageName);  
//	    Log.d("uid", packageInfo.sharedUserId);  
//	    Log.d("vname", packageInfo.versionName);  
//	    Log.d("code", packageInfo.versionCode+"");  
	}


}
