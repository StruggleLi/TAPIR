/**
 *
 */
package Pattern1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import Pattern3.Check_Cache;
import LittleTool.Delete_folder;
import Test.Test;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;
import util.Thread1;


/**
 * @author liwenjie
 * ���ӹ��ܣ�
 * ��1��ʶ��getview, ondraw, gridview��ѭ���塣�������Ƿ���decode������ϵ������⵽decodeû�н���Pattern1��pattern3�Ż�
 * ��ʱ��Ҫ�����Ƿ�����Щ���ڹ����������档
 *
 */
public class Main {
	public static void main(String [] args) throws Exception{
		//��������
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_app(true);
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_output_format(Options.output_format_jimp);//���м�������趨Ϊjimp
		
		String strClassPath1 ="";
		String file_name1="";
		
		pathSet pathset=new pathSet();
		String[] Pathset1=pathset.pathSet1;
		String[] Pathset2=pathset.pathSet2;
		String[] Pathset3=pathset.pathSet3;
		String[] Pathset4=pathset.pathSet4;
		String[] Pathset5=pathset.pathSet5;
		String[] Pathset6=pathset.pathSet6;
		
		int i=Integer.valueOf(args[0]);
		//int i=7;
		int count=1;  //ÿ����Ҫ���ã����⣬����Ҫ�洢��������������result*.txt
		
		if(1==count){
			strClassPath1=Pathset1[i*2];
			file_name1=Pathset1[i*2+1];
			Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
		}
		
		if(2==count){
			strClassPath1=Pathset2[i*2];
			file_name1=Pathset2[i*2+1];
			Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
		}
		
		if(3==count){
			strClassPath1=Pathset3[i*2];
			file_name1=Pathset3[i*2+1];
			Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
		}
		
		if(4==count){
			strClassPath1=Pathset4[i*2];
			file_name1=Pathset4[i*2+1];
			Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
		}
		
		//��commercial������м��
		int figer_android_lib=0;
		if(5==count){
			strClassPath1=Pathset5[i*2];
			file_name1=Pathset5[i*2+1];
			figer_android_lib=Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
		}
		
		if(6==count){
			strClassPath1=Pathset6[i*2];
			file_name1=Pathset6[i*2+1];
			//figer_android_lib=Delete_folder.delete_android_folder(strClassPath1+"/android");   //ɾ���������ļ��е�Android�������android�ļ���
			Delete_folder.delete_android_folder(strClassPath1+"/android");
		}
		
		//int i=187;

			ArrayList<String> processDir = new ArrayList<String>();
			
//			String temp_path="";
//			//�����ЧĿ¼���ų�Android��׼��Ŀ¼
//			File file = new File(strClassPath1);
//	        File[] files = file.listFiles();//��õ�һ��Ŀ¼
//	        for(File fileIn : files){
//	        	if(fileIn.isDirectory()){
//	        		
//	        		//�жϸ��ļ���Ϊ��������
//	        		if(!(fileIn.getPath().endsWith("android"))){
//	        			processDir.add(fileIn.getPath()); //�洢����ļ�Ŀ¼
//	        			temp_path=temp_path+";"+fileIn.getPath();
//	        			System.out.println(fileIn.getPath());
//	        		}
//	        	}
//	        }
//	        System.out.println("temp_path:   "+temp_path);
			
		processDir.add(strClassPath1);
		Options.v().set_process_dir(processDir);
		System.out.println(strClassPath1);

		System.out.println("Scene.v().getSootClassPath():     "+Scene.v().getSootClassPath());
		
		Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ";" + strClassPath1);   //ԭ����

		Scene.v().loadNecessaryClasses();//���ر�Ҫ����

		Data1 data=new Data1();

		Iterator<SootClass> scIterator1 = Scene.v().getApplicationClasses().iterator();//���Ӧ�ó����е���
		Iterator<SootClass> scIterator2 = Scene.v().getApplicationClasses().iterator();//���Ӧ�ó����е���
		Iterator<SootClass> scIterator3 = Scene.v().getApplicationClasses().iterator();//���Ӧ�ó����е���
		Iterator<SootClass> scIterator4 = Scene.v().getApplicationClasses().iterator();//���Ӧ�ó����е���

		//�������߳̽����ļ���С���
		Thread1 fiel_monitor=new Thread1("C:\\Users\\Dell\\Desktop\\output.txt");
		
		fiel_monitor.start();
		//Pattern 1
		Analysis_Bitmap bitmap_analysis=new Analysis_Bitmap();
		bitmap_analysis.StartAnalysis(scIterator1,scIterator2,scIterator3,scIterator4,data,file_name1,i);//��ʼ����

		//Test
//		Iterator<SootClass> scIterator_Test = Scene.v().getApplicationClasses().iterator();
//		Test.TestAnalysis(scIterator_Test);

		System.out.println("The Bitmap Analysis is Finished!");
		
	}
	
}
