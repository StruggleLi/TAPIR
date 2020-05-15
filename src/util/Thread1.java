/**
 * 
 */
package util;

import java.io.File;

import Pattern1.Main_Test;

/**
 * @author Dell
 *
 */
public class Thread1 extends Thread {
	private String file_name;
	static int wait_count=0;
		
	public Thread1(String file_name){
		this.file_name=file_name;//向子线程中传入参数。
	}
	
	public void run() {
		while(true){
		try {
			long time=10000;
			wait_count++;
			Thread.sleep(time);//间隔10s时间检查一次
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getfileSize(file_name);
		}
	}
	
	/**
	 * 获得文件大小，如果超过范围，则结束程序运行。
	 */
	public static void getfileSize(String file_name) {  
//	    File f= new File(file_name);  
//	    if (f.exists() && f.isFile()){  
//	        System.out.println("The size of the file:  "+f.length()/1000000.0+"MB"); 
//	        double file_size=f.length()/1000000.0;
//	        if((file_size>100.0)||Main_Test.thread_stop_figer||wait_count>40){//程序运行超过400s
//	        	System.out.println("Stop the excution");
//	        	System.exit(0);//如果输出文件大小大于100MB，则结束程序运行
//	        }
//	    }else{  
//	    	System.out.println("file doesn't exist or is not a file"); 
//	       
//	    }  
		if(Main_Test.thread_stop_figer||wait_count>80){//程序运行超过400s
        	System.out.println("Stop the excution");
        	System.exit(0);//如果输出文件大小大于100MB，则结束程序运行
        }
	}  

}
