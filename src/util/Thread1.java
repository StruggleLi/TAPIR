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
		this.file_name=file_name;//�����߳��д��������
	}
	
	public void run() {
		while(true){
		try {
			long time=10000;
			wait_count++;
			Thread.sleep(time);//���10sʱ����һ��
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getfileSize(file_name);
		}
	}
	
	/**
	 * ����ļ���С�����������Χ��������������С�
	 */
	public static void getfileSize(String file_name) {  
//	    File f= new File(file_name);  
//	    if (f.exists() && f.isFile()){  
//	        System.out.println("The size of the file:  "+f.length()/1000000.0+"MB"); 
//	        double file_size=f.length()/1000000.0;
//	        if((file_size>100.0)||Main_Test.thread_stop_figer||wait_count>40){//�������г���400s
//	        	System.out.println("Stop the excution");
//	        	System.exit(0);//�������ļ���С����100MB���������������
//	        }
//	    }else{  
//	    	System.out.println("file doesn't exist or is not a file"); 
//	       
//	    }  
		if(Main_Test.thread_stop_figer||wait_count>80){//�������г���400s
        	System.out.println("Stop the excution");
        	System.exit(0);//�������ļ���С����100MB���������������
        }
	}  

}
