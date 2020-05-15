package Perf;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



import polyglot.ast.Stmt;

//---添加
//import soot.SystemContext;







import soot.Body;
import soot.Context;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.CombinedAnalysis;
import soot.toolkits.scalar.CombinedDUAnalysis;
import soot.util.Chain;
import soot.util.queue.QueueReader;
//import toolkits.hierarchy.ClassHierarchyGraph;
//import toolkits.hierarchy.ClassNode;

import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;//----------------------》得到unitGraph
import soot.toolkits.graph.pdg.HashMutablePDG;
import soot.toolkits.graph.pdg.PDGNode;           //这是一种什么东东？
import soot.toolkits.graph.pdg.PDGNode.Type;
import soot.toolkits.graph.pdg.ProgramDependenceGraph;

/**
 * Begin Time:2015-11-14
 * End Time:2015
 * 完成许老师的文档中涉及的问题---》方法是进行方法体内部的逐行分析
 * @author Li
 *
 */
public class Main {
	public static void main(String [] args) throws Exception{
		//环境设置
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_app(true);
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_output_format(Options.output_format_jimp);//对中间码进行设定为jimp

		//如果要处理目录中的Java文件，添加处理目录
		//String strClassPath ="D:\\jar1\\cycle";
		//String strClassPath ="D:\\LiWenjie\\Android\\app-debug-dex2jar";  //反编译生成的.class文件路径
		//String strClassPath ="D:\\LiWenjie\\Android\\OpenGPSTracker_1.3.5-dex2jar";  //反编译生成的.class文件路径

		//String strClassPath ="D:\\LiWenjie\\Android\\net.osmand-1.0.0-dex2jar";
		//String strClassPath ="D:\\LiWenjie\\Android\\imsdroid-2.548.870-dex2jar";
		String strClassPath="D:\\Workplace-All\\ICSE2014\\omnidroid\\bin\\classes";
		//String strClassPath="D:\\Workplace-All\\ICSE2014\\FBReaderJ\\bin\\classes";
		//  String strClassPath="D:\\Workplace-All\\ICSE2014\\AmbilWarna\\bin\\classes";
		//String strClassPath="D:\\Workplace-All\\ICSE2014\\FBReaderJ\\bin\\classes";
		/*
		 * 路径一定要定位到包名的上一层，如果程序初始是在eclipse中写的，则一般是bin下就可以了
		 * 如果例如好像在Ant编译的，则在classes下面*/

		//String strClassPath="D:\\java-new-workspace\\ASM_4.1_final_04_just_for_test\\bin";

		ArrayList<String> processDir = new ArrayList<String>();
		processDir.add(strClassPath); //存储多个文件目录
		Options.v().set_process_dir(processDir);

		System.out.println(Scene.v().getSootClassPath());
		Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ";" + strClassPath);
		Scene.v().loadNecessaryClasses();//加载必要的类

		//添加基本数据（可以是要检测的语句种类--------这里涉及到要对特定的语句进行输出查看具体的内容形式，然后根据
		//内容使用正则表达式来进行识别）
		Data data=new Data();
		data.addData();

		//Problems分析
		Iterator<SootClass> scIterator1 = Scene.v().getApplicationClasses().iterator();//获得应用程序中的类

		Analysis_Insert insert_analysis=new Analysis_Insert();
		//insert_analysis.StartAnalysis(scIterator1,data);//开始分析

		//只是编写结束，还没有测试，因为现在的程序中不包含图片处理
		Analysis_Bitmap bitmap_analysis=new Analysis_Bitmap();
		//bitmap_analysis.StartAnalysis(scIterator1,data);//开始分析

		//只是编写结束，还没有测试，因为现在的程序中不包含Set集合
		Analysis_Loop loop_analysis=new Analysis_Loop();
		//loop_analysis.StartAnalysis(scIterator1,data);//开始分析

		//只是编写结束，还没有测试，因为现在的程序中不包含Set集合
		Analysis_OnDraw onDraw_analysis=new Analysis_OnDraw();
		onDraw_analysis.StartAnalysis(scIterator1,data);//开始分析

		System.out.println("The Four Analysis is Finished!!!!");



		System.out.println("The Recent Analysis is Finished!!!!");

	}
}

















