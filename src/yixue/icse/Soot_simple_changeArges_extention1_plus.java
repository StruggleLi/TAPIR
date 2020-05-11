package yixue.icse;

import soot.*;
import soot.jimple.*;
import soot.options.Options;

import java.util.*;

//这个代码版本是基础成功执行的版本，后续会在这个版本的基础上进行增加操作。如果删除调用的函数，都可以无损失插桩的
//time:2019/7/23

//time:2020/4/2  用于进行性能评估实验
//time:2020/4/5  插入时间戳，但是无法获得实际decoding的时间，怎么办呢？
//time:2020/4/6  查看具体的时间输出是什么样的，然后找一个修复版本的apk，进行时间上的对比分析。
//time:2020/4/8  输出纳秒的时间，验证插桩获得的时间是否正确


public class Soot_simple_changeArges_extention1_plus {
    public static int timestampCounter=0;
    public static int tmpCount = 0;

    public static String androidJar="/Users/wenjieli/My-floder/Java-code/IID-extension/PALOMA-Analysis/lib/";
    public static String APK="/Users/wenjieli/My-floder/APITrace/TestApk/wpandroid-7.7.apk";//DSub.4.5.3.apk";//wpandroid-7.1.apk";//wpandroid-12.1.1.apk";//wpandroid-12.1.1.apk";//wpandroid-9.8.apk";//onebusaway.apk";//wpandroid-12.1.1.apk";//wpandroid-7.1.apk";//androFotoFinder.apk";//wpandroid-7.1.apk";//Application-release.apk";


    public static void main(String[] args) {
        System.out.println("Start to analyze the apk: "+APK);
        G.reset();

        Options.v().set_allow_phantom_refs(true);//设置允许伪类（Phantom class），指的是soot为那些在其classpath找不到的类建立的模型
        Options.v().set_prepend_classpath(true);//prepend the VM's classpath to Soot's own classpath
        Options.v().set_output_format(Options.output_format_dex);//设置soot的输出格式
        Options.v().set_android_jars(androidJar);//设置android jar包路径
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(APK));
        Options.v().set_android_api_version(21);//add
        Options.v().set_process_multiple_dex(true);//add

        //others
        //让soot加载java.io.PrintStream和java.lang.System这两个类，根据插桩的目的（插入System.out.println("HELLO")），
//        Options.v().set_include_all(true);
//        Options.v().set_validate(true);
//        Options.v().set_verbose(true);
//        Options.v().set_whole_program(true);

        //the following three options are very important, which are used to load local defined function calls
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Options.v().set_soot_classpath(System.getProperty("java.class.path"));
        //Options.v().set_soot_classpath(androidJar);
        Options.v().set_prepend_classpath(true);

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

        //the end of others

        Scene.v().loadNecessaryClasses();
        System.out.println("loadNecessaryClasses!");

        // resolve the PrintStream and System soot-classes
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

        System.out.println("Finished Scene!");

        //样例程序中的是myInstrumenter，替换为internalTransform
        PackManager.v().getPack("jtp").add(new Transform("jtp.internalTransform", new BodyTransformer() {
            @Override
            protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                //下面进行一些插桩操作
                //insertMemory(b);
                insertTime(b);
                //new:目前仅仅是覆盖我们关注的代码
                InsertImageDecodingLog(b);//----这个插桩通过，用来对image decoding api进行插桩

                //下面开始增加对图片资源对修改---我们不应该对图片资源有修改，因为我们不能保证可以修改的范围。


            }


        }));
        //下面是增加的
        System.out.println("PackManager.runPacks()");
        PackManager.v().runPacks();
        PackManager.v().writeOutput();

        //soot.Main.main(args);//输出提示性语句
    }

    public static void InsertTimeStamp2(Body body,Stmt stmt){
        final PatchingChain<Unit> units = body.getUnits();
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

    public static void InsertMemoryStamp4(Body body,Stmt stmt){
        //输出信息的tag:printMemoryUsage:
        final PatchingChain<Unit> units = body.getUnits();
                    System.out.println("Begin to Insert Memory Stamp： " + body.getMethod().getSignature());
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

    public static Local addTmpLong2Local(Body body) {
        Local tmpLong = Jimple.v().newLocal("tmpLong" + (tmpCount++),
                LongType.v());
        body.getLocals().add(tmpLong);
        return tmpLong;
    }

    public static void InsertImageDecodingLog(Body body){
        //这里做的是定点的插桩，所以，我们要对class，method进行过滤----这里有一个tag做标记，但是如果有输出，我们也是可以看到的，因为已经有标记了
        String tag="ImageDecoding----liwenjie";//
        String tag1="ImageDecoding----liwenjie1";//
        String tag2="ImageDecoding----liwenjie2";//
        SootMethod sootMethod=body.getMethod();

        //下面检查是否是我们要插桩的位置
        boolean ifCheck1=false;
        boolean ifCheck2=false;
        String cName1="AztecImageLoader";
        String cName2="ImageSettingsDialogFragment";
        String mName1="loadImage";
        String mName2="loadThumbnail";
        String dName1="decodeFile(";
        String dName2="setImageUrl(";

        String mN=sootMethod.getSignature();

        if(mN.contains(cName1)&&mN.contains(mName1)){
            System.out.println("Liwenjie---1----"+mN);
            ifCheck1=true;
        }
        if(mN.contains(cName2)&&mN.contains(mName2)){
            System.out.println("Liwenjie----2---"+mN);
            ifCheck2=true;
        }

        if(ifCheck1){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    System.out.println("ifCheck1:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                        if(decodingAPI.contains(dName1)){//&&b.getMethod().getSignature().contains("wordpress")
                            System.out.println("Location method name:"+sootMethod.getSignature());
                            System.out.println("checkDecodingAPI:"+decodingAPI);
                            List<Unit> toastUnits = makeToast(tag1,body.getMethod().getSignature()+decodingAPI);//第2，3个参数都是输出信息。具体信息标签在makeToast中设置
                            body.getUnits().insertAfter(toastUnits, stmt);
                        }
                }
            }
        }

        if(ifCheck2){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    System.out.println("ifCheck1::   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    if(decodingAPI.contains(dName2)){//&&b.getMethod().getSignature().contains("wordpress")
                        System.out.println("Location method name:"+sootMethod.getSignature());
                        System.out.println("checkDecodingAPI:"+decodingAPI);
                        List<Unit> toastUnits = makeToast(tag2,body.getMethod().getSignature()+decodingAPI);//第2，3个参数都是输出信息。具体信息标签在makeToast中设置
                        body.getUnits().insertAfter(toastUnits, stmt);
                    }
                }
            }
        }

        //这是原版的
        boolean flag=false;
        if(checkDecodingBody(body, sootMethod)&&flag){//检测body中是否存在decoding api
        //if((!(ifCheck1))&&(!(ifCheck2))){//检测body中是否存在decoding api
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    //System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String[] Bitmap_method={//
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            //"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    for(int i=0;i<Bitmap_method.length;i++){
                        if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                            System.out.println("Location method name:"+sootMethod.getSignature());
                            System.out.println("checkDecodingAPI:"+decodingAPI);
                            List<Unit> toastUnits = makeToast(tag,body.getMethod().getSignature()+decodingAPI);//第2，3个参数都是输出信息。具体信息标签在makeToast中设置
                            body.getUnits().insertAfter(toastUnits, stmt);
                        }
                    }
                }
            }
        }


    }

    public static void insertTime(Body body){
        SootMethod sootMethod=body.getMethod();


        //下面是现在进行修改的
        boolean ifCheck1=false;
        boolean ifCheck2=false;
        String cName1="AztecImageLoader";
        String cName2="ImageSettingsDialogFragment";
        String mName1="loadImage";
        String mName2="loadThumbnail";
        String dName1="decodeFile(";
        String dName2="setImageUrl(";

        String mN=sootMethod.getSignature();

        if(mN.contains(cName1)&&mN.contains(mName1)){
            System.out.println("Liwenjie---1----"+mN);
            ifCheck1=true;
        }
        if(mN.contains(cName2)&&mN.contains(mName2)){
            System.out.println("Liwenjie----2---"+mN);
            ifCheck2=true;
        }

        if(ifCheck1){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    System.out.println("ifCheck1:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    if(decodingAPI.contains(dName1)){//&&b.getMethod().getSignature().contains("wordpress")
                        System.out.println("Location method name:"+sootMethod.getSignature());
                        System.out.println("Insert time:"+decodingAPI);
                        //这里确定了包含decoding api的stmt
                        InsertTimeStamp2(body,stmt);
                    }
                }
            }
        }

        if(ifCheck2){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    System.out.println("ifCheck1::   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    if(decodingAPI.contains(dName2)){//&&b.getMethod().getSignature().contains("wordpress")
                        System.out.println("Location method name:"+sootMethod.getSignature());
                        System.out.println("Insert time:"+decodingAPI);
                        //这里确定了包含decoding api的stmt
                        InsertTimeStamp2(body,stmt);
                    }
                }
            }
        }



        //下面是原版本的
        /*
        if(checkDecodingBody(body, sootMethod)){//检测body中是否存在decoding api
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    //System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String[] Bitmap_method={//
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    for(int i=0;i<Bitmap_method.length;i++){
                        if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                            //这里确定了包含decoding api的stmt
                            InsertTimeStamp2(body,stmt);

                        }
                    }
                }
            }
        }
        */
    }

    public static void insertMemory(Body body){
        SootMethod sootMethod=body.getMethod();//
        if(checkDecodingBody(body, sootMethod)){//检测body中是否存在decoding api
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    //System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String[] Bitmap_method={//
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    for(int i=0;i<Bitmap_method.length;i++){
                        if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                            //这里确定了包含decoding api的stmt
                            InsertMemoryStamp4(body,stmt);

                        }
                    }
                }
            }
        }
    }

    public static void insertImage(Body body){
        SootMethod sootMethod=body.getMethod();//
        if(checkDecodingBody(body, sootMethod)){//检测body中是否存在decoding api
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();
                if (stmt.containsInvokeExpr()) {//
                    //System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
                    String[] Bitmap_method={//
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//这里获得相关的语句
                    for(int i=0;i<Bitmap_method.length;i++){
                        if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                            //这里确定了包含decoding api的stmt
                            InsertMemoryStamp4(body,stmt);

                        }
                    }
                }
            }
        }
    }


    public static boolean checkDecodingBody(Body body, SootMethod sootMethod){
        String[] Bitmap_method={//
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                //"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
        };
        boolean checkResult=false;
        for(int i=0;i<Bitmap_method.length;i++){
            if(body.toString().contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                //System.out.println();
                checkResult=true;
            }
        }
        return checkResult;
    }

    private static List<Unit> makeToast(String tag,String toast){
        List<Unit> unitsList=new ArrayList<Unit>();
        SootClass logClass=Scene.v().getSootClass("android.util.Log");//获取android.util.Log类
        SootMethod sootMethod=logClass.getMethod("int i(java.lang.String,java.lang.String)");
        StaticInvokeExpr staticInvokeExpr=Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(),StringConstant.v(tag),StringConstant.v(toast));
        InvokeStmt invokeStmt=Jimple.v().newInvokeStmt(staticInvokeExpr);
        unitsList.add(invokeStmt);
        return unitsList;
    }



}