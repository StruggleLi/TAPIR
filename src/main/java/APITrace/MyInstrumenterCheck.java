package main.java.APITrace;
//这里将是我的最干净的代码实现区域
//https://blog.csdn.net/zlp1992/article/details/42463463
//1)在进行参数替换之前，需要保证这些参数在被处理之前是否存在一些操作（或者在处理之前需要一些预先的处理判断），只能对一些变量进行重新赋值了
//2）要加入赋值操作，但是经过查找发现，不能得到变量名啊

//TIME
//2019-4-25
//2019-6-21
//2019/6-24

//TODO
/*

 */
//每修改一次代码，要记住重新编译所有修改过的class+rebuild项目


import java.io.PrintWriter;
import java.util.*;


import soot.*;
import soot.jimple.*;
import soot.options.Options;
import soot.Local;


public class MyInstrumenterCheck {
    public static String androidJar="/Users/wenjieli/My-floder/Java-code/IID-extension/PALOMA-Analysis/lib/";
    //OK的APK：nori.apk|fr.neamar.kiss_146.apk（已经尝试）|androFotoFinder.apk|passandroid_349.apk(已经尝试)|mpdroid_58.apk
    //被混淆的APK：nori.apk
    //wpandroid-11.8.apk和onebusaway.apk, org.xbmc.kore_25.apk, de.danoeh.antennapod_1070195.apk, com.newsblur_159.apk在runPacks出现空指针异常。
    public static String APK="/Users/wenjieli/My-floder/APITrace/TestApk/wpandroid-7.1.apk";//androFotoFinder.apk";//wpandroid-7.1.apk";//Application-release.apk";
    //public static String appFolder="H:\\PALOMA-Analysis\\PALOMA-Analysis";

    private static PrintWriter timeStampPrintWriter = null;
    private static PrintWriter urlPrintWriter = null;

    public static void main(String[] args) {
        System.out.println("Start!");
        //create files for output
/*
        try {
            File createFile = new File(appFolder + "/Output/timestamp.txt");
            createFile = new File(appFolder + "/Output/url.txt");
            timeStampPrintWriter = new PrintWriter(appFolder
                    + "/Output/timestamp.txt", "UTF-8");
            urlPrintWriter = new PrintWriter(appFolder + "/Output/url.txt",
                    "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        Options.v().set_allow_phantom_refs(true);//设置允许伪类（Phantom class），指的是soot为那些在其classpath找不到的类建立的模型
        Options.v().set_prepend_classpath(true);//prepend the VM's classpath to Soot's own classpath
        Options.v().set_output_format(Options.output_format_dex);
        Options.v().set_android_jars(androidJar);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(APK));
        Options.v().set_keep_line_number(true);
        Options.v().set_whole_program(true);
        Options.v().set_verbose(true);
        Options.v().set_validate(true);
        Options.v().set_android_api_version(23);//收到一个提示：Android API version '25' not available, using minApkVersion '16' instead
        Options.v().set_include_all(true);



        //the following three options are very important, which are used to load local defined function calls
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_soot_classpath(System.getProperty("java.class.path"));
        //Options.v().set_soot_classpath(androidJar);
        Options.v().set_prepend_classpath(true);

        //让soot加载java.io.PrintStream和java.lang.System这两个类，根据插桩的目的（插入System.out.println("HELLO")），
        Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.Object.Runtime",SootClass.SIGNATURES);//used to get totalMemory()
        Scene.v().addBasicClass("java.lang.Object",SootClass.SIGNATURES);
        //add
        Scene.v().addBasicClass("java.util.Random",SootClass.SIGNATURES);

        //加载我们自我实现的类到APK文件中去
        Scene.v().addBasicClass(ProxyHelper.ProxyClass);//String ProxyClass = "yixue.icse.Proxy";这里是加载了Proxy这个类吗？然后这个类中定义了很多方法
        SootClass scc = Scene.v().loadClassAndSupport(ProxyHelper.ProxyClass);
        scc.setApplicationClass();


        Scene.v().loadNecessaryClasses();
        //soot.Main.main(args);
        System.out.println("Have load the classes!");
        PackManager.v().getPack("jtp").add(new Transform("jtp.MyTransform1", new MyTransform1()));//添加自己的BodyTransformer
        PackManager.v().runPacks();//runPacks() methods triggers the execution of the packs and calls the overwritten internalTransform() method inside the MyBodyStranformer class derived from BodyTranformer
        PackManager.v().writeOutput();//create a .apk file

        System.out.println("The instrumentation is finished!!!");
    }

}

class MyTransform1 extends BodyTransformer{
    public static int timestampCounter=0;
    public static int tmpCount = 0;

    @Override
    protected void internalTransform(Body body, String arg1,
                                     Map<String, String> arg2) {//I don't know the arguments of arg1 and arg2
        InsertDecodingLog2(body);//success--改造完成
        InsertCallBackCallLog(body);
        InsertAPICallLogForAll(body);
        //InsertReadingLog2(body);
        InsertTimeStamp2(body);//success

    }

    public void InsertLog(Body body,String P_className,String P_methodName,String P_APICall,int P_lineNumber){
        String className=body.getMethod().getSignature();
        System.out.println("The className is:   "+body.getMethod().getDeclaringClass());
        System.out.println("The MethodName is:   "+body.getMethod().getSignature());
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (className.contains(P_className)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position
                    System.out.println("Begin to instrument InsertLog():   " + body.getMethod().getSignature());
                    //List<Unit> toastUnits = makeToast(body, "toast infor!");--------------正确的实现需要将这两行打开
                    //body.getUnits().insertAfter(toastUnits, stmt);
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }

    public void InsertDecodingLog2(Body body){
        String tag="InsertDecodingLog:";
        //System.out.println("The className is:   "+body.getMethod().getDeclaringClass());   //android.support.v4.app.FragmentTransitionCompat21$2
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (checkDecodingAPI(stmt)) {//confirm the position
                    System.out.println("Begin to instrument InsertDecodingLog:   " + callAPI);
                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature()+callAPI );//具体信息标签在makeToast中设置
                    body.getUnits().insertAfter(toastUnits, stmt);
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }

    public void InsertReadingLog2(Body body){
        String tag="InsertReadingLog:";
        //System.out.println("The className is:   "+body.getMethod().getDeclaringClass());   //android.support.v4.app.FragmentTransitionCompat21$2
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (checkReadingAPI(stmt)) {//confirm the position
                    System.out.println("Begin to instrument InsertReadingLog:   " + callAPI);
                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature()+callAPI );//具体信息标签在makeToast中设置
                    body.getUnits().insertAfter(toastUnits, stmt);
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }

    public void InsertCallBackCallLog(Body body){
        String tag="InsertCallBackCallLog";
        SootMethod sootMethod=body.getMethod();
        if(checkCallbackMethod(sootMethod)){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
            while(unitsIterator.hasNext()) {
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {
                    System.out.println("Begin to instrument InsertCallBackCallLog:   " + body.getMethod().getSignature());
                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
                    body.getUnits().insertAfter(toastUnits, stmt);
                    break;
                }
            }
        }
    }

    public void InsertAPICallLogForAll(Body body){
        String tag="API_Invocation:";
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                    System.out.println("Begin to instrument InsertAPICallLogForAll:   " + stmt.getInvokeExpr().getMethod().getName());
                    List<Unit> toastUnits = makeToast(body,tag,stmt.getInvokeExpr().getMethod().getSignature() );
                    body.getUnits().insertAfter(toastUnits, stmt);
                }
            }
    }

    public void InsertDecodingLog3(Body body,String P_className,String P_methodName,String P_APICall,int P_lineNumber){
        String tag="InsertDecodingLog3";
        String className=body.getMethod().getSignature();
        System.out.println("The className is:   "+body.getMethod().getDeclaringClass());
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()&&(stmt instanceof AssignStmt)) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (className.contains(P_className)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position
                    System.out.println("Begin to instrument InsertLog():   " + body.getMethod().getSignature());
                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature()+callAPI );//具体信息标签在makeToast中设置
                    body.getUnits().insertAfter(toastUnits, stmt);
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }
    }

    private List<Unit> makeToast(Body body,String tag,String toast){
        List<Unit> unitsList=new ArrayList<Unit>();
        SootClass logClass=Scene.v().getSootClass("android.util.Log");//获取android.util.Log类
        SootMethod sootMethod=logClass.getMethod("int i(java.lang.String,java.lang.String)");
        StaticInvokeExpr staticInvokeExpr=Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(),StringConstant.v(tag),StringConstant.v(toast));
        InvokeStmt invokeStmt=Jimple.v().newInvokeStmt(staticInvokeExpr);
        unitsList.add(invokeStmt);
        return unitsList;
    }


    public void InsertPrintln(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        final PatchingChain<Unit> units = body.getUnits();
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Unit unit=unitsIterator.next();
            Stmt stmt = (Stmt) unit;
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertPrintln() " + body.getMethod().getSignature());

                    Local tmpRef = addTmpRef(body);//这里使用到了BODY参数啊？
                    Local tmpString = addTmpString(body);

                    // insert "tmpRef = java.lang.System.out;"
                    units.insertBefore(Jimple.v().newAssignStmt(
                            tmpRef, Jimple.v().newStaticFieldRef(
                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), unit);

                    // insert "tmpLong = 'HELLO';"
                    String location="location";
                    units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                            StringConstant.v("HELLO"+location)), unit);

                    // insert "tmpRef.println(tmpString);"
                    SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                    units.insertBefore(Jimple.v().newInvokeStmt(
                            Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), unit);

                    //check that we did not mess up the Jimple
                    body.validate();
                }
            }
        }
    }

    public void InsertPrintln2(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        final PatchingChain<Unit> units = body.getUnits();
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Unit unit=unitsIterator.next();
            Stmt stmt = (Stmt) unit;
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertPrintln() " + body.getMethod().getSignature());

                    Local tmpRef = addTmpRef(body);//这里使用到了BODY参数啊？
                    Local tmpString = addTmpString(body);

                    // insert "tmpRef = java.lang.System.out;"
                    units.insertBefore(Jimple.v().newAssignStmt(
                            tmpRef, Jimple.v().newStaticFieldRef(
                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), unit);

                    // insert "tmpLong = 'HELLO';"
                    String location="location";
                    units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                            StringConstant.v("HELLO"+location)), unit);

                    // insert "tmpRef.println(tmpString);"
                    SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                    units.insertBefore(Jimple.v().newInvokeStmt(
                            Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), unit);

                    //check that we did not mess up the Jimple
                    body.validate();
                }
            }
        }

    }

    public void InsertPrintlnLoation(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        final PatchingChain<Unit> units = body.getUnits();
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Unit unit=unitsIterator.next();
            Stmt stmt = (Stmt) unit;
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertPrintln() " + body.getMethod().getSignature());

                    Local tmpRef = addTmpRef(body);//这里使用到了BODY参数啊？
                    Local tmpString = addTmpString(body);

                    // insert "tmpRef = java.lang.System.out;"
                    units.insertBefore(Jimple.v().newAssignStmt(
                            tmpRef, Jimple.v().newStaticFieldRef(
                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), unit);

                    // insert "tmpLong = 'HELLO';"
                    units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                            StringConstant.v("Location: method--"+body.getMethod().getSignature()+"---stmt--"+stmt.toString())), unit);//StringConstant.v(body.getMethod().getSignature())

                    // insert "tmpRef.println(tmpString);"
                    SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                    units.insertBefore(Jimple.v().newInvokeStmt(
                            Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), unit);

                    //check that we did not mess up the Jimple
                    body.validate();
                }
            }
        }

    }

    public void InsertPrintlnLoation2(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        final PatchingChain<Unit> units = body.getUnits();
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Unit unit=unitsIterator.next();
            Stmt stmt = (Stmt) unit;
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertPrintln() " + body.getMethod().getSignature());

                    Local tmpRef = addTmpRef(body);//这里使用到了BODY参数啊？
                    Local tmpString = addTmpString(body);

                    // insert "tmpRef = java.lang.System.out;"
                    units.insertBefore(Jimple.v().newAssignStmt(
                            tmpRef, Jimple.v().newStaticFieldRef(
                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), unit);

                    // insert "tmpLong = 'HELLO';"
                    units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                            StringConstant.v("Location: method--"+body.getMethod().getSignature()+"---stmt--"+stmt.toString())), unit);//StringConstant.v(body.getMethod().getSignature())

                    // insert "tmpRef.println(tmpString);"
                    SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                    units.insertBefore(Jimple.v().newInvokeStmt(
                            Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), unit);

                    //check that we did not mess up the Jimple
                    body.validate();
                }
            }
        }

    }

    private static Local addTmpRef(Body body)
    {
        Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        body.getLocals().add(tmpRef);
        return tmpRef;
    }

    private static Local addTmpString(Body body)
    {
        Local tmpString = Jimple.v().newLocal("tmpString", RefType.v("java.lang.String"));
        body.getLocals().add(tmpString);
        return tmpString;
    }

    public static Local addTmpString2Local(Body body) {
        Local tmpString = Jimple.v().newLocal("tmpString" + (tmpCount++),
                RefType.v("java.lang.String"));
        body.getLocals().add(tmpString);
        return tmpString;
    }

    public void InsertTimeStamp(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getTimeStamp);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printeTimeDiff);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newSubExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    public void InsertTimeStamp2(Body body){
        final PatchingChain<Unit> units = body.getUnits();
        if(checkCallbackMethod(body.getMethod())){
            //在第一行中插入时间戳
            Iterator<Unit> unitsIterator=units.snapshotIterator();
            while(unitsIterator.hasNext()) {
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {
                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());
                    timestampCounter++;
                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getTimeStamp);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printeTimeDiff);//String printeTimeDiff = "void printTimeDiff(java.lang.String,
                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newSubExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句
                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                    break;
                }
            }
        }


    }
    //if (stmt.containsInvokeExpr()&&(stmt instanceof AssignStmt)) {
    public void InsertTimeStamp3(Body body,String P_className,String P_methodName,String P_APICall,int P_lineNumber){
        String className=body.getMethod().getSignature();
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()&&(stmt instanceof AssignStmt)) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (className.contains(P_className)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getTimeStamp);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printeTimeDiff);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newSubExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }



    public static Local findLocal(Body body){
        String APIName="getFullFilePathfromArray";
        Local local=null;
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (callAPI.contains(APIName)) {
                    List<ValueBox> defBox=stmt.getDefBoxes();
                    //List<ValueBox> useBox=stmt.getUseBoxes();
                    System.out.println("The size of the defBox:   "+defBox.size());
                    System.out.println("The name of the def:    "+defBox.get(0).toString()+"----"+defBox.get(0).getValue().toString());
                    if(defBox.get(0).getValue() instanceof Local){
                        System.out.println("Yes, it is a Local");
                    }
                    local=(Local)defBox.get(0).getValue();

                    if(stmt instanceof AssignStmt){
                        System.out.println("The stmt is an AssignStmt");
                    }
                    if(stmt instanceof InvokeStmt){
                        System.out.println("The stmt is an InvokeStmt");
                    }
                }
            }
        }
        return local;
    }

    public static Local addTmpLong2Local(Body body) {
        Local tmpLong = Jimple.v().newLocal("tmpLong" + (tmpCount++),
                LongType.v());
        body.getLocals().add(tmpLong);
        return tmpLong;
    }




    public void InsertAssignment(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        Value argsUrl=null;
        Local localUrl=null;
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Local> localsIterator=body.getLocals().snapshotIterator();//Locals set
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                P_APICall="loadImage";
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertAssignment " + stmt.toString());
                    List<Value> args=stmt.getInvokeExpr().getArgs();
                    int argsNumber=stmt.getInvokeExpr().getArgCount();
                    //接下来感觉需要根据不同API ，根据参数的数量，来人工判定第几个参数是URL
                    if(argsNumber==3){
                        argsUrl=args.get(0);
                        if(argsUrl instanceof Local){
                            System.out.println("Yes, it is a Local value!");
                        }
                    }
                    String urlName=argsUrl.toString();
                    System.out.println("The name of url argument:  "+urlName);//print the str such as "$r0"
                    while(localsIterator.hasNext()){
                        localUrl=localsIterator.next();
                        String localStr=localUrl.getName();
                        System.out.println("The name of Locals:  "+localStr);
                        if(localStr.equals(urlName)){
                            System.out.println("Find the args in Locals!!");
                            break;
                        }

                    }
                    if(true){
                        System.out.println("localUrl:  "+localUrl.getName());

                        Local url = addTmpString2Local(body);
                        Local tmpString = addTmpString2Local(body);

                        //AssignStmt tmpUrl = Jimple.v().newAssignStmt(tmpString, StringConstant.v("HELLO"));//将值赋值给临时变量
                        Stmt assignTmp = Jimple.v().newAssignStmt(
                                tmpString,StringConstant.v("HELLO"));
                                //localUrl, tmpString);
                        Stmt assignUrl = Jimple.v().newAssignStmt(
                                url,tmpString);

                        units.insertBefore(assignTmp, stmt);
                        units.insertBefore(assignUrl, stmt);

                        //check that we did not mess up the Jimple
                        body.validate();
                        System.out.println("Finish instrument:  InsertAssignment() " + body.getMethod().getSignature());

                    }

                }
            }
        }

    }


    public void ReplaceParameter(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        Value argsUrl=null;
        Local localUrl=null;
        String methodName=body.getMethod().getName();
        if (methodName.equals(P_methodName)){

            final PatchingChain<Unit> units = body.getUnits();
            Iterator<Local> localsIterator=body.getLocals().snapshotIterator();//Locals set
            Iterator<Unit> unitsIterator=units.snapshotIterator();
            while(unitsIterator.hasNext()) {
                Stmt stmt = (Stmt) unitsIterator.next();
                String stmtStr=stmt.toString();
                System.out.println("Stmt:   "+stmtStr);
                if (stmt.containsInvokeExpr()) {
                    String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                    String callAPI = stmt.getInvokeExpr().getMethod().getName();
                    P_APICall="loadImage";
                    if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                        System.out.println("Begin to instrument:  InsertAssignment " + stmt.toString());
                        List<Value> args=stmt.getInvokeExpr().getArgs();
                        int argsNumber=stmt.getInvokeExpr().getArgCount();
                        //接下来感觉需要根据不同API ，根据参数的数量，来人工判定第几个参数是URL
                        if(argsNumber==3){
                            argsUrl=args.get(0);
                            if(argsUrl instanceof Local){
                                System.out.println("Yes, it is a Local value!");
                            }
                        }
                        String urlName=argsUrl.toString();
                        System.out.println("The name of url argument:  "+urlName);//print the str such as "$r0"
                        while(localsIterator.hasNext()){
                            localUrl=localsIterator.next();
                            String localStr=localUrl.getName();
                            System.out.println("The name of Locals:  "+localStr);
                            if(localStr.equals(urlName)){
                                System.out.println("Find the args in Locals!!");
                                break;
                            }

                        }
                        if(true){
                            System.out.println("localUrl:  "+localUrl.getName());

                            Local url = addTmpString2Local(body);
                            Local tmpString = addTmpString2Local(body);

                            //AssignStmt tmpUrl = Jimple.v().newAssignStmt(tmpString, StringConstant.v("HELLO"));//将值赋值给临时变量
                            Stmt assignTmp = Jimple.v().newAssignStmt(
                                    tmpString,StringConstant.v("HELLO"));
                            //localUrl, tmpString);
                            Stmt assignUrl = Jimple.v().newAssignStmt(
                                    url,tmpString);

                            units.insertBefore(assignTmp, stmt);
                            units.insertBefore(assignUrl, stmt);

                            //check that we did not mess up the Jimple
                            body.validate();
                            System.out.println("Finish instrument:  InsertAssignment() " + body.getMethod().getSignature());

                        }

                    }
                }
            }

        }

    }



    //插入语句：Proxy.getMemoryUsage();
    public void InsertMemoryUsage0(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertMemoryUsage() " + body.getMethod().getSignature());


                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    //SootMethod printTimeDiff = ProxyHelper
                    //        .findMethod(ProxyHelper.printeTimeDiff);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    //Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    //Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                    //        timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    //Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    //Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                    //        Jimple.v().newSubExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    //LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    //arglist.add(StringConstant.v(body.getMethod()
                    //        .getSignature()));
                    //arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    //arglist.add(timeDiff);
                    //Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                    //        Jimple.v().newStaticInvokeExpr(
                    //                printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertAfter(assignTimeStampBefore, stmt);//这里是插入一个方法调用加赋值语句
                    //units.insertAfter(printTimeDiffInvoke, stmt);
                    //units.insertAfter(assignTimeDiff, stmt);
                    //units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }



    //InsertMemoryUsage   这里获得的是内存消耗的两倍大小，换算成M似乎要 / (1024.0 * 1024)
    //在编码的过程中，要注意数据类型的一致，否则会反编译不通过
    public void InsertMemoryUsage(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertMemoryUsage() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getMemory = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printMemoryAdd = ProxyHelper
                            .findMethod(ProxyHelper.printMemoryUsage);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getMemoryInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getMemory.makeRef()));//新建方法调用语句？
                    Local MemoryStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignMemoryBefore = Jimple.v().newAssignStmt(
                            MemoryStamp1, getMemoryInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local MemoryStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignMemoryAfter = Jimple.v().newAssignStmt(
                            MemoryStamp2, getMemoryInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local MemoryAdd = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignMemoryAdd = Jimple.v().newAssignStmt(MemoryAdd,
                            Jimple.v().newAddExpr(MemoryStamp2, MemoryStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();
                    //LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v("MemoryUsage:   "+body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(MemoryAdd);
                    Stmt printMemoryAddInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printMemoryAdd.makeRef(), arglist));//新建一个信息输出语句

                    units.insertAfter(assignMemoryBefore, stmt);//这里是插入一个方法调用加赋值语句
                    units.insertAfter(printMemoryAddInvoke, stmt);
                    units.insertAfter(assignMemoryAdd, stmt);
                    units.insertAfter(assignMemoryAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertMemoryUsage() " + body.getMethod().getSignature());
                }
            }
        }

    }

    //插入语句：Proxy.getMemoryUsage();
    public void InsertMemoryUsage1(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertMemoryUsage() " + body.getMethod().getSignature());
                    timestampCounter++;


                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printMemoryUsage);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newSubExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertAfter(assignTimeStampBefore, stmt);//这里是插入一个方法调用加赋值语句
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    public void InsertPrintlnLoation1(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        final PatchingChain<Unit> units = body.getUnits();
        String methodName=body.getMethod().getName();
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {

            Unit unit=unitsIterator.next();
            Stmt stmt = (Stmt) unit;
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertPrintln() " + body.getMethod().getSignature());
                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    //SootMethod printTimeDiff = ProxyHelper
                    //        .findMethod(ProxyHelper.printeTimeDiff);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    //Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    //Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                    //        timeStamp1, getTimeStampInvoke.getInvokeExpr());




                    Local tmpRef = addTmpRef(body);//这里使用到了BODY参数啊？
                    Local tmpLong = addTmpLong2Local(body);

                    // insert "tmpRef = java.lang.System.out;"
                    units.insertBefore(Jimple.v().newAssignStmt(
                            tmpRef, Jimple.v().newStaticFieldRef(
                                    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), unit);

                    // insert "tmpLong = 'HELLO';"
                    units.insertBefore(Jimple.v().newAssignStmt(tmpLong,
                            getTimeStampInvoke.getInvokeExpr()), unit);//StringConstant.v(body.getMethod().getSignature())

                    // insert "tmpRef.println(tmpString);"
                    SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                    units.insertBefore(Jimple.v().newInvokeStmt(
                            Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpLong)), unit);

                    //check that we did not mess up the Jimple
                    body.validate();
                }
            }
        }

    }

    public void InsertTimeStamp1(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printMemoryUsage);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newAddExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    public void InsertMemoryStamp3(Body body,String P_className,String P_methodName,String P_APICall,int P_lineNumber){
        String className=body.getMethod().getSignature();
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (className.contains(P_className)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printMemoryUsage);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newAddExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    public void InsertMemoryStamp4(Body body,String P_className,String P_methodName,String P_APICall,int P_lineNumber){
        String className=body.getMethod().getSignature();
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()&&(stmt instanceof AssignStmt)) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (className.contains(P_className)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    System.out.println("Begin to instrument:  InsertTimeStamp() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getMemoryUsage);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printMemoryUsage);//String printeTimeDiff = "void printTimeDiff(java.lang.String,

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            timeStamp1, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpLong2Local(body);//这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpLong2Local(body);//这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            Jimple.v().newAddExpr(timeStamp2, timeStamp1));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

//实现了语句的插入：String str = Proxy.getUrlStr()；Proxy.getUrlStr();;和一个信息输出语句  本地的local并没有得到直接的赋值
    public void InsertAssignment2(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    Local local=findLocal(body);//找到要进行赋值操作的变量
                    System.out.println("Begin to instrument:  InsertAssignment2() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getUrlStr);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printOutUrl);

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句？
                    //Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            local, getTimeStampInvoke.getInvokeExpr());//timeStamp1-->local新建一个赋值语句，将时间赋值给本地变量1
                    Local timeStamp2 = addTmpString2Local(body);//addTmpLong2Local-->addTmpString2Local这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            timeStamp2, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    Local timeDiff = addTmpString2Local(body);//addTmpLong2Local-->addTmpString2Local这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(timeDiff,
                            local);
                            //Jimple.v().newAddExpr(timeStamp2, local));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    //失败的尝试
    public void InsertAssignment3(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {

            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    Local local=findLocal(body);//找到要进行赋值操作的变量
                    System.out.println("Begin to instrument:  InsertAssignment2() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getUrlStr);//String getTimeStamp = "long getTimeStamp()"
                    SootMethod printTimeDiff = ProxyHelper
                            .findMethod(ProxyHelper.printOutUrl);

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句，涉及getUrlStr()方法
                    //Local timeStamp1 = addTmpLong2Local(body);//这里新建一个本地变量1
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            local, getTimeStampInvoke.getInvokeExpr());//timeStamp1-->local新建一个赋值语句，将时间赋值给本地变量1
                    //Local timeStamp2 = addTmpString2Local(body);//addTmpLong2Local-->addTmpString2Local这里新建一个本地变量2
                    Stmt assignTimeStampAfter = Jimple.v().newAssignStmt(
                            local, getTimeStampInvoke.getInvokeExpr());//新建一个赋值语句，将时间赋值给本地变量2
                    //Local timeDiff = addTmpString2Local(body);//addTmpLong2Local-->addTmpString2Local这里新建一个本地变量diff
                    Stmt assignTimeDiff = Jimple.v().newAssignStmt(local,
                            local);
                    //Jimple.v().newAddExpr(timeStamp2, local));////新建一个运算操作语句，将时间赋值给本地变量diff

                    LinkedList<Value> arglist = new LinkedList<Value>();//新建一个列表用来存储新建的语句
                    arglist.add(StringConstant.v(body.getMethod()
                            .getSignature()));
                    arglist.add(StringConstant.v(stmt.toString()));//感受是在为下面的printTimeDiffInvoke做准备
                    //arglist.add(timeDiff);
                    Stmt printTimeDiffInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    printTimeDiff.makeRef(), arglist));//新建一个信息输出语句

                    units.insertBefore(assignTimeStampBefore, stmt);
                    units.insertAfter(printTimeDiffInvoke, stmt);
                    units.insertAfter(assignTimeDiff, stmt);
                    units.insertAfter(assignTimeStampAfter, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }



    //现在的思路是自定义写一个方法，这个方法返回一个UR，至于这个URL是怎么来的，就可以完全自己定义了，例如采用随机的方法。
    public void InsertAssignment2TEMP(Body body,String P_methodName,String P_APICall,int P_lineNumber){
        String methodName=body.getMethod().getName();
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position

                    Local local=findLocal(body);//找到要进行赋值操作的变量
                    System.out.println("Begin to instrument:  InsertAssignment2() " + body.getMethod().getSignature());

                    timestampCounter++;

                    SootMethod getTimeStamp = ProxyHelper
                            .findMethod(ProxyHelper.getUrlStr);//getUrlStr = "String getUrlStr()";

                    Stmt getTimeStampInvoke = Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                    getTimeStamp.makeRef()));//新建方法调用语句
                    //Local timeStamp1 = addTmpString2Local(body);//这里新建一个本地字符串变量
                    Stmt assignTimeStampBefore = Jimple.v().newAssignStmt(
                            local, getTimeStampInvoke.getInvokeExpr());//timeStamp1-->local新建一个赋值语句，将时间赋值给本地变量1
                    units.insertBefore(assignTimeStampBefore, stmt);

                    //check that we did not mess up the Jimple
                    body.validate();
                    System.out.println("Finish instrument:  InsertPrintln() " + body.getMethod().getSignature());
                }
            }
        }

    }

    public static void replaceRight(Body body,String P_classNmae,String P_methodName,String P_APICall){
        Local local=null;
        String methodName=body.getMethod().getName();
        String className=body.getMethod().getDeclaringClass().getName();
        System.out.println("SootMethod.getDeclaringClass().getName():    "+className+"-----------------getShortName:  "+body.getMethod().getDeclaringClass().getShortName());
        final PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> unitsIterator=units.snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String callAPI = stmt.getInvokeExpr().getMethod().getName();//这里有一个疑问，如果一条语句中涉及多个API的调用怎么办？
                if (className.contains(P_classNmae)&&methodName.equals(P_methodName) && callAPI.equals(P_APICall)) {//confirm the position
                    //begin to write
                    System.out.println("Begin to replace right value");
                    InvokeExpr invoke = stmt.getInvokeExpr();
                    List<Value> arglist = new LinkedList<Value>();
                    /*
                    for (ValueBox vb : invoke.getUseBoxes()) {
                        arglist.add(vb.getValue());
                    }*/
                    if (stmt instanceof AssignStmt) {
                        SootMethod getUrlStr = ProxyHelper
                                .findMethod(ProxyHelper.getUrlStr);//getUrlStr = "String getUrlStr()";

                        Value assivalue = ((AssignStmt) stmt).getLeftOp();
                        Stmt newassign = Jimple.v().newAssignStmt(
                                assivalue,
                                Jimple.v().newStaticInvokeExpr(
                                        getUrlStr.makeRef(), arglist));//---为什么这里依然使用这以前方法调用的参数呢？
                        units.insertBefore(newassign, stmt);
                        units.remove(stmt);

                    }



                    /*
                    List<ValueBox> defBox=stmt.getDefBoxes();
                    //List<ValueBox> useBox=stmt.getUseBoxes();
                    System.out.println("The size of the defBox:   "+defBox.size());
                    System.out.println("The name of the def:    "+defBox.get(0).toString()+"----"+defBox.get(0).getValue().toString());
                    if(defBox.get(0).getValue() instanceof Local){
                        System.out.println("Yes, it is a Local");
                    }
                    local=(Local)defBox.get(0).getValue();

                    if(stmt instanceof AssignStmt){
                        System.out.println("The stmt is an AssignStmt");
                    }
                    if(stmt instanceof InvokeStmt){
                        System.out.println("The stmt is an InvokeStmt");
                    }
                    */
                }
            }
        }
        //return local;
    }

    //一些可以借鉴的代码
    /*
    //print the response of the request
	private static void printResponse(Body body, Stmt stmt, InvokeExpr invoke, PatchingChain<Unit> units){
		if(Prefetch_Method == Prefetch_GETCONTENT){

		}else if(Prefetch_Method == Prefetch_GETINPUTSTREAM){

		}else if(Prefetch_Method == Prefetch_GETRESPONSECODE){
			System.out.println("stmt = "+stmt +"\n\n\n\n\n");
			if(invoke.getMethod().getSignature().equals(ProxyHelper.getResponseCodeOriginal)){
				System.out.println("sig = "+invoke.getMethod().getSignature());
				if (stmt instanceof AssignStmt) {
					SootMethod printResponseCodeMethod = ProxyHelper
							.findMethod(ProxyHelper.printResponseCode);
					Value value = ((AssignStmt) stmt).getLeftOp();-----------------这里直接获取左值
					LinkedList<Value> arglist = new LinkedList<Value>();
					arglist.add(StringConstant.v(body.getMethod()
							.getSignature()));
					arglist.add(StringConstant.v(stmt.toString()));
					arglist.add(value);---------------将左值放入LIST
					Stmt printResponseCodeInvk = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(
									printResponseCodeMethod.makeRef(), arglist));
					System.out.println(printResponseCodeInvk);
					// units.insertBefore(assignIndex, targetAssign);
					// units.insertBefore(assignNodeId, targetAssign);
					units.insertAfter(printResponseCodeInvk, stmt);

				} else {
					System.out.println("getresponse code is not an assignment statement!!!!!!!");

				}
			}
		}else if(Prefetch_Method == Prefetch_OPENSTREAM){

		}
	}






	private static Local addTmpString2Local(Body body) {
		Local tmpString = Jimple.v().newLocal("tmpString" + (tmpCount++),
				RefType.v("java.lang.String"));
		body.getLocals().add(tmpString);//这是添加变量到locals集合，后面会做units里面语句的参数
		return tmpString;
	}



//-----------------------这个是直接替换一个赋值语句的右值啊！！！！！并且是替换成一个API调用，正好符合我们的要求。
	public static void instrumentWithBody(Body body) {
		// find the body that needs to be instrumented (replace
		// methods)
		if (ProxyHelper.instrumentMap.containsKey(body.getMethod()
				.getSignature())) {
			final PatchingChain<Unit> units = body.getUnits();
			// important to use snapshotIterator here
			for (Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
				final Stmt stmt = (Stmt) iter.next();
				if (stmt.containsInvokeExpr()) {
					InvokeExpr invoke = stmt.getInvokeExpr();
					// instrumentTimestamp(invoke, stmt, units);
					SootMethod replaceMethod = ProxyHelper
							.queryReplaceMethodWithBody(invoke.getMethod()
									.getSignature());
					// System.out.println("replacement@@@@@@@@@@"+replaceMethod);
					if (replaceMethod != null) {
						// System.out.println("replacement ============ "+replaceMethod.getSignature());
						List<Value> arglist = new LinkedList<Value>();
						for (ValueBox vb : invoke.getUseBoxes()) {
							arglist.add(vb.getValue());
						}
						// Jimple.v().newStaticInvokeExpr(agent.makeRef())
						if (stmt instanceof AssignStmt) {
							Value assivalue = ((AssignStmt) stmt).getLeftOp();
							Stmt newassign = Jimple.v().newAssignStmt(
									assivalue,
									Jimple.v().newStaticInvokeExpr(
											replaceMethod.makeRef(), arglist));//---为什么这里依然使用这以前方法调用的参数呢？
							units.insertBefore(newassign, stmt);
							units.remove(stmt);

						} else if (stmt instanceof InvokeStmt) {
							Stmt newinvoke = Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(
											replaceMethod.makeRef(), arglist));
							units.insertBefore(newinvoke, stmt);
							units.remove(stmt);

						}
					}
				}
			}
		}
	}

     */

    /*
    这里是维护一个API list，然后对每个符合的API list，现在先进行call的查找操作吧，后面再进行进一步的精细化
     */
    public boolean checkDecodingAPI(Stmt stmt){
        boolean checkResult=false;
        String[] Bitmap_method={
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
             	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
//    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResourceStream",
                "decodeRegion(",
//    	"android.graphics.Bitmap: android.graphics.Bitmap createBitmap",
//    	"android.graphics.Bitmap: android.graphics.Bitmap createScaledBitmap",
//    	"setImageIcon",
//		"setImageResource(",
                "setImageURI(",
//		"setImageViewIcon(",
                "setImageViewUri(",
//		"BitmapDrawable(",
//		"newInstance(",
                "createFromPath(",
//		"createFromResourceStream(",
                "createFromStream(",
                "displayImage(",
//		"getDrawable(",
//		"getDrawableForDensity("
        };
        if (stmt.containsInvokeExpr()) {
            String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
            String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
            //String callAPI = stmt.getInvokeExpr().getMethod().getName();
            for(int i=0;i<Bitmap_method.length;i++){
                if(decodingAPI.contains(Bitmap_method[i])){
                    System.out.println("checkDecodingAPI:"+declaringClass);
                    checkResult=true;
                }
            }
        }
        return checkResult;
    }

    public boolean checkReadingAPI(Stmt stmt){
        boolean checkResult=false;
        String[] Reading_method={
                "toByteArray(",
                "openInputStream(",
                "getInputStream(",
                "getFileDescriptor("
        };
        if (stmt.containsInvokeExpr()) {
            String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
            String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
            //String callAPI = stmt.getInvokeExpr().getMethod().getName();
            for(int i=0;i<Reading_method.length;i++){
                if(decodingAPI.contains(Reading_method[i])){
                    System.out.println("checkDecodingAPI:"+declaringClass);
                    checkResult=true;
                }
            }
        }
        return checkResult;
    }

//判断是否是以On开头
    public boolean checkCallbackMethod(SootMethod sootMethod){
        boolean checkResult=false;
        System.out.println("checkCallbackMethod:"+sootMethod.getName());
        if (sootMethod.getName().startsWith("on")) {
            checkResult=true;
        }
        return checkResult;
    }



}

