/**
 *
 */
package Perf;

import java.util.ArrayList;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import soot.SootMethod;
import util.Thread_data;
import BaseData.Insert;
import BaseData.McallGraph;
import BaseData.Pattern3_problem_decode;
import BaseData.VH_data;
import Pattern2.Result_Pattern2;
import Pattern2.Result_Pattern2_back;
import Pattern2.Thread_info;

/**
 * @author liwenjie
 *
 */
public class Do_Write {

	public void wirteToPattern1(Set<Insert> insert_store,String file_name) throws Exception{
			String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
	        System.out.println("Data Writing To :"+fileName);
	        //建立XML文件对象
	        Xml_writer writer = new Xml_writer();
	        Document doc;
			doc = writer.get_doc();
			Element root=writer.begin(doc, "Pattern1");//根元素名
            for(Insert mca: insert_store){
	        	String class_name=mca.getClass_name();
	        	String method_name=mca.getMethod_name();
	        	String decode_method=mca.getDecode_method();
	        	String statement=mca.getStatement();
	        	String count_decode=String.valueOf(mca.getCount1());
	        	String count_inJustDecodeBounds=String.valueOf(mca.getCount2());
	        	boolean figer_Options=mca.isBool1();
	        	boolean figer_inSampleSize=mca.isBool2();
	        	
	        	String figer_Options1="";
	        	String figer_inSampleSize1="";
	        	String lineNumber=String.valueOf(mca.getLineNumber());
	        	
	        	//为了避免 java.lang.NullPointerException
	        	String pattern3_frequen="null";
	        	if((mca.getPattern3_frequen())!=null){
	        	       pattern3_frequen=mca.getPattern3_frequen().toString();
	        	}
	        	
	        	Result_Pattern2 result_pattern2=mca.getResult_pattern2();
	        	
	        	String UI_count=String.valueOf(result_pattern2.getUI_count());
	        	String Child_count=String.valueOf(result_pattern2.getChild_count());
	        	
	        	Result_Pattern2_back result_Pattern2_back=mca.getResult_Pattern2_back();

	        	String UI="";
	        	String Child="";
	        	UI=result_Pattern2_back.getUI_count()+":"+result_Pattern2_back.getUI_method().toString();
	        	Child=result_Pattern2_back.getChild_count()+":"+result_Pattern2_back.getChild_method().toString();
	        	
	        	
	        	
	        	//为了避免 java.lang.NullPointerException
	        	
	        	String pattern3_memory_result_string=mca.getPattern3_MC();
	        	
	        	
	        	//为了避免 java.lang.NullPointerException
	        	//ArrayList<String> pattern3_disk_result=;
	        	String pattern3_disk_result_String=mca.getPattern3_disk_result();
	        	
	        	String pattern3_frequen_result_back=mca.getPath_backward_frequency().toString();
	        	
	        	

	        	if(figer_Options){
	        		figer_Options1="true";

	        	}else{
	        		figer_Options1="false";
	        	}
	        	if(figer_inSampleSize){
	        		figer_inSampleSize1="true";

	        	}else{
	        		figer_inSampleSize1="false";
	        	}

                Element book = writer.field(doc,root, "Position", ""); //下一层,在字符串中不能包含空格，空字符
	        	writer.field(doc,book, "Class_name",class_name);
	        	writer.field(doc,book, "Method_name",method_name);
	        	writer.field(doc,book, "Decode_name",decode_method);
	        	writer.field(doc,book, "Statement",statement);
	        	writer.field(doc,book, "count_decode",count_decode);
	        	writer.field(doc,book, "count_inJustDecodeBounds",count_inJustDecodeBounds);
	        	writer.field(doc,book, "figer_Options1",figer_Options1);
	        	writer.field(doc,book, "figer_inSampleSize1",figer_inSampleSize1);
	        	writer.field(doc,book, "Patthen1_LineNumber",lineNumber);
	        	
	        	
	        	//writer.field(doc,book, "Patthen2_UI_method",result_pattern2.getUI_method().toString());
	        	//writer.field(doc,book, "Patthen2_Child_method",result_pattern2.getChild_method().toString());
	        	writer.field(doc,book, "Patthen2_UI_count",UI_count);
	        	writer.field(doc,book, "Patthen2_Child_count",Child_count);
	        	writer.field(doc,book, "Patthen2_UI_back",UI);
	        	writer.field(doc,book, "Patthen2_Child_back",Child);
	        	writer.field(doc,book, "pattern3_MC_decode",pattern3_memory_result_string);
	        	writer.field(doc,book, "pattern3_Disk_decode",pattern3_disk_result_String);
	        	//writer.field(doc,book, "Patthen3_frequen_related_method",pattern3_frequen);
	        	writer.field(doc,book, "pattern3_frequen_result_back",pattern3_frequen_result_back);
	        	
	        	writer.field(doc,book, "pattern4_recycle",mca.getPattern4_recycle().toString());
	        	writer.field(doc,book, "pattern4_inbitmap",mca.getPattern4_inbitmap().toString());
	        	
	        	writer.field(doc,book, "Image_drawable",String.valueOf(mca.getImage_drawable()));
	        	writer.field(doc,book, "Image_without_return",String.valueOf(mca.getImage_without_return()));
	        	


	        }
	        writer.toSave(doc, fileName);//将信息写入文件
	}
	
	
	public void wirteToPattern4_cycle(Set<String> insert_store,String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Pattern4");//根元素名
        for(String mca: insert_store){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "Cycle", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        	
        }
        writer.toSave(doc, fileName);//将信息写入文件
}
	
	public void wirteToPattern4_inbitmap(Set<String> insert_store,String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Pattern4");//根元素名
        for(String mca: insert_store){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "Inbitmap", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        	
        }
        writer.toSave(doc, fileName);//将信息写入文件
}
	
	public void wirteToPattern3_MC(ArrayList<String> pattern3_MC_get,Set<String> pattern3_MC_put,String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Pattern3_MC");//根元素名
        for(String mca: pattern3_MC_get){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "get", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        	
        }
        
        for(String mca: pattern3_MC_put){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "put", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        	
        }
        writer.toSave(doc, fileName);//将信息写入文件
}
	
	public void wirteToPattern3_DC(ArrayList<String> pattern3_DC,String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Pattern3_DC");//根元素名
        for(String mca: pattern3_DC){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "compress", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        	
        }
        
        
        writer.toSave(doc, fileName);//将信息写入文件
}
	
	public void wirteToPattern_API(ArrayList<String> pattern3_DC,String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Pattern_API");//根元素名
        for(String mca: pattern3_DC){
        	String class_name=mca;
        	
            Element book = writer.field(doc,root, "API", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book, "Position",class_name);
        }
        
        writer.toSave(doc, fileName);//将信息写入文件
}
	
	

	public void wirteToPattern1Pattern2andPattern3and4(int problem,int right, int pattern3_yes,int pattern3_no,Set<Pattern3_problem_decode> pattern3_problem,Set<Pattern3_problem_decode> pattern3_right,int count_UI,int count_child,boolean pattern3_result,boolean pattern4_result,String file_name,String pattern4_method) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
          //建立XML文件对象
          Xml_writer writer3 = new Xml_writer();
          Document doc3 = writer3.get_doc();
          Element root3=writer3.begin(doc3, "Pattern3and4");//根元素名
          	String pattern3="false";
            String pattern4="false";

          if(pattern3_result){
        	  pattern3="true";
          }
          if(pattern4_result){
        	  pattern4="true";
          }
          Element book3 = writer3.field(doc3,root3, "Result", ""); //下一层,在字符串中不能包含空格，空字符
          writer3.field(doc3,book3, "Pattern1_Problem",String.valueOf(problem));
          writer3.field(doc3,book3, "Pattern1_Right",String.valueOf(right));

          writer3.field(doc3,book3, "Pattern2_UI_Thread",String.valueOf(count_UI));
          writer3.field(doc3,book3, "Pattern2_Child_Thread",String.valueOf(count_child));
          writer3.field(doc3,book3, "Pattern3",pattern3);
          writer3.field(doc3,book3, "Pattern3_yes",String.valueOf(pattern3_yes));
          writer3.field(doc3,book3, "Pattern3_no",String.valueOf(pattern3_no));
          writer3.field(doc3,book3, "Pattern4",pattern4);
          writer3.field(doc3,book3, "Pattern4_method",pattern4_method);

          for(Pattern3_problem_decode mca: pattern3_problem){
        	  Element book = writer3.field(doc3,root3, "Pattern3_no", ""); //下一层,在字符串中不能包含空格，空字符
	        	String class_name=mca.getClass_name();
	        	String method_name=mca.getMethod_name();
	        	String decode_name=mca.getDecode_method();

	        	writer3.field(doc3,book, "Class_name",class_name);
	        	writer3.field(doc3,book, "Method_name",method_name);
	        	writer3.field(doc3,book, "Decode_name",decode_name);

          }
          for(Pattern3_problem_decode mca: pattern3_right){
        	  Element book = writer3.field(doc3,root3, "Pattern3_yes", ""); //下一层,在字符串中不能包含空格，空字符
	        	String class_name=mca.getClass_name();
	        	String method_name=mca.getMethod_name();
	        	String decode_name=mca.getDecode_method();

	        	writer3.field(doc3,book, "Class_name",class_name);
	        	writer3.field(doc3,book, "Method_name",method_name);
	        	writer3.field(doc3,book, "Decode_name",decode_name);

          }
          writer3.toSave(doc3, fileName);//将信息写入文件
}

	public void wirteToThread(Set<Thread_info> thread_data_set, String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc;
		doc = writer.get_doc();
		Element root=writer.begin(doc, "Thread_Information");//根元素名
        Element book = writer.field(doc,root, "Thread_class", ""); //下一层,在字符串中不能包含空格，空字符
        

        for(Thread_info mca: thread_data_set){
        	SootMethod sootmethod=mca.getSootmethod();
        	String stmt=mca.getStmt();
 
            Element book1 = writer.field(doc,root, "Thread_invoke", ""); //下一层,在字符串中不能包含空格，空字符
        	writer.field(doc,book1, "Thread_invoke_position",sootmethod.getSignature());
        	writer.field(doc,book1, "Thread_invoke_stmt",stmt);
        }

        writer.toSave(doc, fileName);//将信息写入文件
}
	
	
	
	public void wirteToPattern4(Set<String> pattern4_set_recycle,Set<String> pattern4_set_inbitmap, String file_name) throws Exception{
		String fileName = "D:\\LiWenjie\\Android\\"+file_name;   //XML文件保存路径--output
        System.out.println("Data Writing To :"+fileName);
        //建立XML文件对象
        Xml_writer writer = new Xml_writer();
        Document doc5;
		doc5 = writer.get_doc();
		Element root5=writer.begin(doc5, "Pattern4_Information");//根元素名
		Element book5 = writer.field(doc5,root5, "Pattern4_position", ""); //下一层,在字符串中不能包含空格，空字符
        System.out.println("The size of recycle and inbitmap:    "+pattern4_set_recycle.size()+"    "+pattern4_set_inbitmap.size());
        
        for(String mca: pattern4_set_recycle){
        	
        	String site=mca;

        	writer.field(doc5,book5, "Recycle()",site);
        }
        
       for(String mca: pattern4_set_inbitmap){
        	
        	String site=mca;

        	writer.field(doc5,book5, "inbitmap",site);
        }
       
        writer.toSave(doc5, fileName);//将信息写入文件
}




	void writerToOnmcallGraph(Set<McallGraph> onmcallGraph) throws Exception{
	      		String fileName1 = "D:\\LiWenjie\\Android\\"+"OnmcallGraph.xml";   //XML文件保存路径--output1
	              System.out.println("Data Writing To :"+fileName1);
	              //建立XML文件对象
	              Xml_writer writer1 = new Xml_writer();
	              Document doc1 = writer1.get_doc();
	              Element root1=writer1.begin(doc1, "One");//根元素名
	              for(McallGraph mca: onmcallGraph){
	              	String from=mca.getFrom();
	              	String to=mca.getTo();
	              	Element book1 = writer1.field(doc1,root1, "Edge", ""); //下一层,在字符串中不能包含空格，空字符
	              	writer1.field(doc1,book1, "From_method",from);
	              	writer1.field(doc1,book1, "To_method",to);
	              }
	              writer1.toSave(doc1, fileName1);//将信息写入文件
	}
	void writeToHeavyApi(Set<McallGraph> Heavy_Call) throws Exception{
	       		String fileName2 = "D:\\LiWenjie\\Android\\LM_HeavyApi.xml";   //XML文件保存路径
	              System.out.println("Data Writing To :"+fileName2);
	              //建立XML文件对象
	              Xml_writer writer2 = new Xml_writer();
	              Document doc2 = writer2.get_doc();
	              Element root2=writer2.begin(doc2, "HeavyApi");//根元素名
	              for(McallGraph p2: Heavy_Call){
	            	  String from=p2.getFrom();
		              	String to=p2.getTo();
		              	String lineNumber=p2.getLineNumber();
		              	Element book2 = writer2.field(doc2,root2, "Edge", ""); //下一层,在字符串中不能包含空格，空字符
		              	writer2.field(doc2,book2, "From_method",from);
		              	writer2.field(doc2,book2, "To_method",to);
		              	writer2.field(doc2,book2, "LineNumber",lineNumber);
	              }
	              writer2.toSave(doc2, fileName2);//将信息写入文件
	}

	void writerToOnHeavyApi(Set<VH_data> Heavy_API_class) throws Exception{
	      		String fileName3 = "D:\\LiWenjie\\Android\\Heavy_API_class.xml";   //XML文件保存路径
	            System.out.println("Data Writing To :"+fileName3);
	              //建立XML文件对象
	              Xml_writer writer3 = new Xml_writer();
	              Document doc3 = writer3.get_doc();
	              Element root3=writer3.begin(doc3, "Heavy_API_class");//根元素名
	              for(VH_data heavy_API_class: Heavy_API_class){

	                	String str=heavy_API_class.getMethod_name();
	                	Element book3 = writer3.field(doc3,root3, "Name", ""); //下一层,在字符串中不能包含空格，空字符
	                	writer3.field(doc3,book3, "Name",str);

	              }
	              writer3.toSave(doc3, fileName3);//将信息写入文件
	}
	void writerToVH(Set<VH_data> vh_data) throws Exception{
        //----将基本数据写入XML文件
		String fileName4 = "D:\\LiWenjie\\Android\\VH.xml";   //XML文件保存路径
        System.out.println("Data Writing To :"+fileName4);
        //建立XML文件对象
        Xml_writer writer4 = new Xml_writer();
        Document doc4 = writer4.get_doc();
        Element root4=writer4.begin(doc4, "VH");//根元素名
        for(VH_data data: vh_data){
        	String method_name=data.getMethod_name();
        	Element book4 = writer4.field(doc4,root4, "class_name_AND_method_name", ""); //下一层,在字符串中不能包含空格，空字符
        	writer4.field(doc4,book4, "detail",method_name);
        }
        writer4.toSave(doc4, fileName4);//将信息写入文件
}

}
