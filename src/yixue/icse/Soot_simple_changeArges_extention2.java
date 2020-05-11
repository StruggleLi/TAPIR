package yixue.icse;

import soot.*;
import soot.jimple.*;
import soot.options.Options;

import java.util.*;

//这个代码版本是基础成功执行的版本，后续会在这个版本的基础上进行增加操作。如果删除调用的函数，都可以无损失插桩的
public class Soot_simple_changeArges_extention2 {

    //命令行：-android-jars path/to/android-platforms -process-dir your.apk
    //-android-jars H:\PALOMA-Analysis\PALOMA-Analysis\lib\ -process-dir H:\TestApk\androFotoFinder.apk
    //public static String androidJar="H:\\Stoat-master\\Stoat\\libs\\android-platforms\\";
    //public static String APK="H:\\TestApk\\onebusaway.apk";//nori.apk";//wpandroid-7.1.apk";//wpandroid-12.1.1.apk";//androFotoFinder.apk";//Application-release.apk";

    public static String androidJar="/Users/wenjieli/My-floder/Java-code/IID-extension/PALOMA-Analysis/lib/";
    public static String APK="/Users/wenjieli/My-floder/APITrace/TestApk/wpandroid-12.1.1.apk";//wpandroid-9.8.apk";//onebusaway.apk";//wpandroid-12.1.1.apk";//wpandroid-7.1.apk";//androFotoFinder.apk";//wpandroid-7.1.apk";//Application-release.apk";


    public static void main(String[] args) {
        System.out.println("Start to analyze the apk: "+APK);
//        Options.v().set_android_jars(androidJar);
//        Options.v().set_process_dir(Collections.singletonList(APK));
//
//        //prefer Android APK files// -src-prec apk
//        Options.v().set_src_prec(Options.src_prec_apk);
//
//        //output as APK, too//-f J
//        Options.v().set_output_format(Options.output_format_dex);
//        Options.v().set_android_api_version(23);//add
//        Options.v().set_process_multiple_dex(true);//add
        Options.v().set_allow_phantom_refs(true);//设置允许伪类（Phantom class），指的是soot为那些在其classpath找不到的类建立的模型
        Options.v().set_prepend_classpath(true);//prepend the VM's classpath to Soot's own classpath
        Options.v().set_output_format(Options.output_format_dex);//设置soot的输出格式
        Options.v().set_android_jars(androidJar);//设置android jar包路径
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(APK));
        Options.v().set_android_api_version(21);//add
        Options.v().set_process_multiple_dex(true);//add

        //add additional arges
//        Options.v().set_keep_line_number(false);//add
//        Options.v().set_keep_offset(false);//add
//        Options.v().set_whole_program(true);//add
//        Options.v().set_verbose(false);//add
//        Options.v().set_validate(true);//add----这个参数似乎是需要的，用来
//        Options.v().set_include_all(true);//add
//
//        Options.v().set_no_bodies_for_excluded(true);//add   表示不加载未被包含的类



        Scene.v().loadNecessaryClasses();
        System.out.println("loadNecessaryClasses");

        // resolve the PrintStream and System soot-classes
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

        System.out.println("Finished Scene!");

        //样例程序中的是myInstrumenter，替换为internalTransform
        PackManager.v().getPack("jtp").add(new Transform("jtp.internalTransform", new BodyTransformer() {
            @Override
            protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                imageDecoding(b);
                //InsertDecodingLog2(b);//----比imageDecoding()的实现弱，因为部分执行没有通过。

            }


        }));
        //下面是增加的
        System.out.println("PackManager.runPacks()");
        PackManager.v().runPacks();
        PackManager.v().writeOutput();

        //soot.Main.main(args);//输出提示性语句
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

    /****Function****/
    public static void InsertDecodingLog2(Body body){
        String tag="InsertDecodingLog:";
        System.out.println("The className is:   "+body.getMethod().getDeclaringClass());   //android.support.v4.app.FragmentTransitionCompat21$2
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {
            Stmt stmt = (Stmt) unitsIterator.next();
            if (stmt.containsInvokeExpr()) {
                String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (checkDecodingAPI(stmt,body)) {//confirm the position
                    System.out.println("Begin to instrument InsertDecodingLog:   " + callAPI);
                    List<Unit> toastUnits = makeToast(body,tag,"Decoding:"+body.getMethod().getSignature()+callAPI );//具体信息标签在makeToast中设置
                    body.getUnits().insertAfter(toastUnits, stmt);
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }

    /*
    这里是维护一个API list，然后对每个符合的API list，现在先进行call的查找操作吧，后面再进行进一步的精细化
     */
    public static boolean checkDecodingAPI(Stmt stmt,Body b){
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
//		"getDrawable(",
//		"getDrawableForDensity("
        };
        if (stmt.containsInvokeExpr()) {
            //String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
            String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
            //String callAPI = stmt.getInvokeExpr().getMethod().getName();
            for(int i=0;i<Bitmap_method.length;i++){
                if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                    System.out.println("checkDecodingAPI:"+decodingAPI);
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

    private static List<Unit> makeToast(Body body,String tag,String toast){
        List<Unit> unitsList=new ArrayList<Unit>();
        SootClass logClass=Scene.v().getSootClass("android.util.Log");//获取android.util.Log类
        SootMethod sootMethod=logClass.getMethod("int i(java.lang.String,java.lang.String)");
        StaticInvokeExpr staticInvokeExpr=Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(),StringConstant.v(tag),StringConstant.v(toast));
        InvokeStmt invokeStmt=Jimple.v().newInvokeStmt(staticInvokeExpr);
        unitsList.add(invokeStmt);
        return unitsList;
    }

    public static boolean checkDecodingAPI(String methodName){
        boolean checkResult=false;
        String[] Bitmap_method={
                "decodeFile",
                "decodeByteArray",
                //"decodeResource",
                "decodeStream",
                "decodeFileDescriptor",
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
//		"getDrawable(",
//		"getDrawableForDensity("
        };
        //String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
        //String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
        //String callAPI = stmt.getInvokeExpr().getMethod().getName();
        for(int i=0;i<Bitmap_method.length;i++){
            if(methodName.equals(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                System.out.println("checkDecodingAPI:"+methodName);
                checkResult=true;
                break;
            }
        }
        return checkResult;
    }

    public static void imageDecoding(Body b){
        final PatchingChain<Unit> units = b.getUnits();
        //important to use snapshotIterator here
        for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
            final Unit u = iter.next();
            u.apply(new AbstractStmtSwitch() {

                public void caseInvokeStmt(InvokeStmt stmt) {
                    if (stmt.containsInvokeExpr()) {//如果是一条调用语句
                        String methodName = stmt.getInvokeExpr().getMethod().getName();//获取这个方法的名称
                        if (checkDecodingAPI(methodName)) {
                            Local tmpRef = addTmpRef(b);
                            Local tmpString = addTmpString(b);

                            // insert "tmpRef = java.lang.System.out;"
                            units.insertBefore(Jimple.v().newAssignStmt(
                                    tmpRef, Jimple.v().newStaticFieldRef(
                                            Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), u);

                            // insert "tmpLong = 'HELLO';"
                            units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                                    StringConstant.v("imageDecoding:")), u);//+invokeExpr.getMethod().getName()
                            System.out.println("image decoding insertion: " + b.getMethod().getSignature() + "---" + methodName);

                            // insert "tmpRef.println(tmpString);"
                            SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                            units.insertBefore(Jimple.v().newInvokeStmt(
                                    Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), u);

                            //check that we did not mess up the Jimple
                            b.validate();
                        }
                    }
                }
            });
        }
    }
}