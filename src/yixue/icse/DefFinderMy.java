package yixue.icse;
/*
* function: the class is used to perform string analysis
* time: 2019/4/3
* auther: Wenjie Li
* 输入：一个decoding API和其中的image参数名
* 输出：找到与这个image参数相关的值，仅仅关注右值
* 方法：找到def-use对
* 困难的地方：如何准确分析到resource，例如url=getUrl(arg1,arg2);这个时候就需要对arg1和arg2同时进行分析判断到底哪个是真正的image resource。
*             这个时候从函数的return语句的值开始。然后一个方法体中可能会有多个return，
*             如果在一个method中分析，发现image resource的来源是方法的参数，这个时候就要判断这个参数的位置。
* 注意：这里要先构建callGraph，然后在得到方法body之后，需要得到control Graph，然后在control graph的基础上进行data分析。
*
* 现在要做的：build call graph，然后进行分析。
*
* */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.options.Options;

public class DefFinderMy {
	static List<DefSpot> defSpotList = null;   //包含了def字段的所有的信息
	static List<TargetField> targetFieldList = null;  //和上面的信息差不多
	static List<DefSpot> manualDefSpotList = null;    //人工定义的字段

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//这里的输入是一组APK
		getDefSpotListFromTargetField("/Users/felicitia/Documents/Research/Prefetch/ICSE2018/MBMRoot/MBMApp", "app-release.apk", "/Users/felicitia/Documents/Research/Prefetch/Develop/AndroidJar", "edu.usc.mbm");
	}

	public static List<DefSpot> createDefSpots(TargetField field){
		List<DefSpot> defSpotsOfField = new ArrayList<DefSpot>();
		SootClass sootClass = Scene.v().loadClassAndSupport(field.classBelonged);//这是什么操作？这是仅仅加载要分析的类吗？是的，并且不使用任何call relationship
		Scene.v().loadNecessaryClasses();
//		SootField  field = sootClass.getFieldByName("favCityId");
		for(SootMethod method: sootClass.getMethods()){//对要分析的类中的方法进行遍历操作
			try{
				Body body = method.retrieveActiveBody();
				final PatchingChain<Unit> units = body.getUnits();//获得一个方法中的所有语句？
				for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
					final Stmt stmt = (Stmt) iter.next();
//					System.out.println("stmt = "+stmt);
					for(ValueBox defBox: stmt.getDefBoxes()){//获得语句中的定义
//					System.out.println(defBox.getValue().getClass().getName());
						if(defBox.getValue().getClass().getName().contains("Field") ){//这是什么操作？
							// ">" is to make sure it's not the other fields that start with the same name
							// example of defBox.getValue().toString is:  $r0.<edu.usc.yixue.weatherapp.MainActivity: java.lang.String cityName>  //居然还能显示cityName的名字？
							if(defBox.getValue().toString().contains(field.fieldName+">")){//这是在匹配变量名？
								if(!stmt.toString().endsWith(" = null")){//这里为什么要是这样的设置？还是需要输出一些body来查看具体的情况
									DefSpot defSpot = new DefSpot();
									defSpot.jimple = stmt.toString();
									defSpot.body = method.getSignature();
									defSpot.nodeId = field.nodeId;
									defSpot.pkgName = field.pkgName;
									defSpot.subStrPos = field.substrPos;
									defSpotsOfField.add(defSpot);
								}
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		return defSpotsOfField;
	}
	
	// read from targetField.csv
	public static List<TargetField> getTargetFieldList(String appFolder, String pkgName){
		if(targetFieldList == null){
			targetFieldList = new ArrayList<TargetField>();
			String csvFilePath = appFolder+"/Input/targetField.csv";
			try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
				String sCurrentLine;
				br.readLine(); //skip the header
				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					String[] values = sCurrentLine.split(",");
					TargetField targetFiled = new TargetField();
					targetFiled.classBelonged = values[0];
					targetFiled.fieldName = values[1];
					targetFiled.nodeId = values[2];
					targetFiled.pkgName = pkgName;
					targetFiled.substrPos = Integer.parseInt(values[3]);
					targetFieldList.add(targetFiled);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return targetFieldList;
	}

	public static void sootSettingAndroid(String apkPath, String androidJar){
		// prefer Android APK files// -src-prec apk
				Options.v().set_src_prec(Options.src_prec_apk);
				// output as APK, too//-f J
				Options.v().set_android_jars(androidJar);
				Options.v().set_whole_program(true);
				Options.v().set_verbose(false);
				Options.v().set_allow_phantom_refs(true);

				// sootClassPath = Scene.v().getSootClassPath() + File.pathSeparator
				// + sootClassPath;
				// Scene.v().setSootClassPath(Scene.v().getSootClassPath());
				Options.v().set_keep_line_number(true);
				Options.v().setPhaseOption("jb", "use-original-names:true");
				// resolve the PrintStream and System soot-classes

				// System.out.println("------------------------java.class.path = "+System.getProperty("java.class.path"));
				Options.v().set_soot_classpath(System.getProperty("java.class.path"));
				Options.v().set_prepend_classpath(true);

				List<String> stringlist = new LinkedList<String>();
				stringlist.add(apkPath);
				Options.v().set_process_dir(stringlist);
	}
	
	public static List<DefSpot> getDefSpotListFromTargetField(String appFolder, String apkName, String androidJar, String pkgName){
		if(defSpotList == null){
			defSpotList = new ArrayList<DefSpot>();
			sootSettingAndroid(appFolder+"/"+apkName, androidJar);
			 
			for(TargetField field: getTargetFieldList(appFolder, pkgName)){//这里从targetField.csv 中读取事先准备好的target field，可以对应到我们的decodeFile里面的图片资源参数
				defSpotList.addAll(createDefSpots(field));//这里开始查找定义的地方，并都放入集合
			}
			for(DefSpot defSpot: defSpotList){
				System.out.println("body = " + defSpot.body);
				System.out.println("jimple = " + defSpot.jimple);
				System.out.println("node Id = " + defSpot.nodeId);
				System.out.println("pkg name = " + defSpot.pkgName);
				System.out.println("substr pos = " + defSpot.subStrPos);
				System.out.println();
			}
		}
		return defSpotList;
	}
	
	public static List<DefSpot> getDefSpotListFromDefSpots(String appFolder, String pkgName){
		if(manualDefSpotList == null){
			manualDefSpotList = new ArrayList<DefSpot>();
			String csvFilePath = appFolder+"/Input/DefSpots.csv";
			try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
				String sCurrentLine;
				br.readLine(); //skip the header
				while ((sCurrentLine = br.readLine()) != null) {
					String[] values = sCurrentLine.split(",");
					DefSpot defSpot = new DefSpot();
					defSpot.jimple = values[0];
					defSpot.body = values[1];
					defSpot.nodeId = values[2];
					defSpot.subStrPos = Integer.parseInt(values[3]);
					defSpot.pkgName = values[4];
					manualDefSpotList.add(defSpot);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return manualDefSpotList;
	}
}
