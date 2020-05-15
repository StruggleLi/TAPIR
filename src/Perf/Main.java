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

//---���
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
import soot.toolkits.graph.UnitGraph;//----------------------���õ�unitGraph
import soot.toolkits.graph.pdg.HashMutablePDG;
import soot.toolkits.graph.pdg.PDGNode;           //����һ��ʲô������
import soot.toolkits.graph.pdg.PDGNode.Type;
import soot.toolkits.graph.pdg.ProgramDependenceGraph;

/**
 * Begin Time:2015-11-14
 * End Time:2015
 * �������ʦ���ĵ����漰������---�������ǽ��з������ڲ������з���
 * @author Li
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

		//���Ҫ����Ŀ¼�е�Java�ļ�����Ӵ���Ŀ¼
		//String strClassPath ="D:\\jar1\\cycle";
		//String strClassPath ="D:\\LiWenjie\\Android\\app-debug-dex2jar";  //���������ɵ�.class�ļ�·��
		//String strClassPath ="D:\\LiWenjie\\Android\\OpenGPSTracker_1.3.5-dex2jar";  //���������ɵ�.class�ļ�·��

		//String strClassPath ="D:\\LiWenjie\\Android\\net.osmand-1.0.0-dex2jar";
		//String strClassPath ="D:\\LiWenjie\\Android\\imsdroid-2.548.870-dex2jar";
		String strClassPath="D:\\Workplace-All\\ICSE2014\\omnidroid\\bin\\classes";
		//String strClassPath="D:\\Workplace-All\\ICSE2014\\FBReaderJ\\bin\\classes";
		//  String strClassPath="D:\\Workplace-All\\ICSE2014\\AmbilWarna\\bin\\classes";
		//String strClassPath="D:\\Workplace-All\\ICSE2014\\FBReaderJ\\bin\\classes";
		/*
		 * ·��һ��Ҫ��λ����������һ�㣬��������ʼ����eclipse��д�ģ���һ����bin�¾Ϳ�����
		 * ������������Ant����ģ�����classes����*/

		//String strClassPath="D:\\java-new-workspace\\ASM_4.1_final_04_just_for_test\\bin";

		ArrayList<String> processDir = new ArrayList<String>();
		processDir.add(strClassPath); //�洢����ļ�Ŀ¼
		Options.v().set_process_dir(processDir);

		System.out.println(Scene.v().getSootClassPath());
		Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ";" + strClassPath);
		Scene.v().loadNecessaryClasses();//���ر�Ҫ����

		//��ӻ������ݣ�������Ҫ�����������--------�����漰��Ҫ���ض�������������鿴�����������ʽ��Ȼ�����
		//����ʹ��������ʽ������ʶ��
		Data data=new Data();
		data.addData();

		//Problems����
		Iterator<SootClass> scIterator1 = Scene.v().getApplicationClasses().iterator();//���Ӧ�ó����е���

		Analysis_Insert insert_analysis=new Analysis_Insert();
		//insert_analysis.StartAnalysis(scIterator1,data);//��ʼ����

		//ֻ�Ǳ�д��������û�в��ԣ���Ϊ���ڵĳ����в�����ͼƬ����
		Analysis_Bitmap bitmap_analysis=new Analysis_Bitmap();
		//bitmap_analysis.StartAnalysis(scIterator1,data);//��ʼ����

		//ֻ�Ǳ�д��������û�в��ԣ���Ϊ���ڵĳ����в�����Set����
		Analysis_Loop loop_analysis=new Analysis_Loop();
		//loop_analysis.StartAnalysis(scIterator1,data);//��ʼ����

		//ֻ�Ǳ�д��������û�в��ԣ���Ϊ���ڵĳ����в�����Set����
		Analysis_OnDraw onDraw_analysis=new Analysis_OnDraw();
		onDraw_analysis.StartAnalysis(scIterator1,data);//��ʼ����

		System.out.println("The Four Analysis is Finished!!!!");



		System.out.println("The Recent Analysis is Finished!!!!");

	}
}

















