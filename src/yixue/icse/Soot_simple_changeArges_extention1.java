package yixue.icse;

import soot.*;
import soot.jimple.*;
import soot.options.Options;

import java.util.*;

//这个代码版本是基础成功执行的版本，后续会在这个版本的基础上进行增加操作。如果删除调用的函数，都可以无损失插桩的
public class Soot_simple_changeArges_extention1 {


    //命令行：-android-jars path/to/android-platforms -process-dir your.apk
    //-android-jars H:\PALOMA-Analysis\PALOMA-Analysis\lib\ -process-dir H:\TestApk\androFotoFinder.apk
    //public static String androidJar="H:\\Stoat-master\\Stoat\\libs\\android-platforms\\";
    //public static String APK="H:\\TestApk\\onebusaway.apk";//nori.apk";//wpandroid-7.1.apk";//wpandroid-12.1.1.apk";//androFotoFinder.apk";//Application-release.apk";

    public static String androidJar="/Users/wenjieli/My-floder/Java-code/IID-extension/PALOMA-Analysis/lib/";
    public static String APK="/Users/wenjieli/My-floder/APITrace/TestApk/wpandroid-12.1.1.apk";//wpandroid-12.1.1.apk";//wpandroid-9.8.apk";//onebusaway.apk";//wpandroid-12.1.1.apk";//wpandroid-7.1.apk";//androFotoFinder.apk";//wpandroid-7.1.apk";//Application-release.apk";


    public static void main(String[] args) {
        System.out.println("Start to analyze the apk: "+APK);
        soot.G.reset();
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
//          Options.v().set_keep_offset(true);//add
//        Options.v().set_whole_program(true);//add
          //Options.v().set_verbose(true);//add
          //Options.v().set_validate(true);//add----这个参数似乎是需要的，用来
//        Options.v().set_include_all(true);//add
//
//        Options.v().set_no_bodies_for_excluded(true);//add   表示不加载未被包含的类



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
                //imageDecoding(b);
//                InsertDecodingLog2(b);
                //InsertDecodingLog_callback(b);
                InsertCallBackCallLog1(b);//----这个插桩执行通过了，估计真的是插桩代码写的有bug
                //InsertDecodingLog2(b);//----比imageDecoding()的实现弱，因为更多的部分执行没有通过。
                //InsertCallBackCallLog1(b);
                //insertSetContentView(b);
                //InsertAPICallLogForAll(b);//bu work

               //System.out.println("222222222");

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
        //System.out.println("The className is:   "+body.getMethod().getDeclaringClass());   //android.support.v4.app.FragmentTransitionCompat21$2
        Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
        while(unitsIterator.hasNext()) {
            Unit u=unitsIterator.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                //String callAPI = stmt.getInvokeExpr().getMethod().getName();
                if (checkDecodingAPI(stmt)) {//confirm the position
                    System.out.println("Begin to instrument InsertDecodingLog:   " + stmt.toString());
                    //List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
                    //body.getUnits().insertAfter(toastUnits, stmt);
                    //System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }
    public static void InsertCallBackCallLog3(Body body){
        String tag="InsertCallBackCallLog";
        SootMethod sootMethod=body.getMethod();
        if(checkCallbackMethod(sootMethod)){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
            //这里的操作是遍历方法体中的语句，然后在方法体中进行语句插入
            while(unitsIterator.hasNext()) {
                Stmt stmt = (Stmt) unitsIterator.next();//这条语句就是on方法本身
                if (stmt.containsInvokeExpr()) {
                    System.out.println("Begin to instrument InsertCallBackCallLog:   " + body.getMethod().getSignature());
//                    System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
//                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
//                    body.getUnits().insertAfter(toastUnits, stmt);
                    break;
                }
            }
        }
    }

    public static void InsertCallBackCallLog1(Body body){
        System.out.println("12");
        String tag="ImageDecoding";//
        SootMethod sootMethod=body.getMethod();//
        if(checkCallbackMethod4(body, sootMethod)){//
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();//这条语句就是on方法本身//
                if (stmt.containsInvokeExpr()) {//
                    //System.out.println("Begin to instrument ImageDecoding:   " + body.getMethod().getSignature());
                    //System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
//                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
//                    body.getUnits().insertAfter(toastUnits, stmt);
                    String[] Bitmap_method={//
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                    String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();//
                    for(int i=0;i<Bitmap_method.length;i++){
                        if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                            System.out.println("checkDecodingAPI:"+decodingAPI);
                            List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature()+decodingAPI);//具体信息标签在makeToast中设置
                            body.getUnits().insertAfter(toastUnits, stmt);
                            //break;------not work
                        }

                    }
                    //break;----not work
                }
            }
        }
    }

    //判断是否是以On开头
    public static boolean checkCallbackMethod4(Body body, SootMethod sootMethod){
        String[] Bitmap_method={//
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
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
    //判断是否是以On开头
    public static boolean checkCallbackMethod3(SootMethod sootMethod){
        String[] Bitmap_method={//
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
        };

        boolean checkResult=true;

        System.out.println("checkCallbackMethod:"+sootMethod.getName());
//        if (sootMethod.getName().startsWith("on")) {
//            checkResult=true;
//        }
        return true;
    }

    public static void InsertDecodingLog_callback(Body body){
        String tag="InsertDecodingLog:";//
        SootMethod sootMethod=body.getMethod();//
        if(checkCallbackMethod3(sootMethod)){//
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();//
            //这里的操作是遍历方法体中的语句，然后在方法体中进行语句插入
            while(unitsIterator.hasNext()) {//
                Stmt stmt = (Stmt) unitsIterator.next();//这条语句就是on方法本身//

                if (stmt.containsInvokeExpr()) {//
                    System.out.println("Begin to instrument InsertCallBackCallLog:   " + body.getMethod().getSignature());
//                    System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
//                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
//                    body.getUnits().insertAfter(toastUnits, stmt);
 //                   break;

                    boolean checkResult=false;
                    String[] Bitmap_method={
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                            "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
                    };
                        //String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
                        String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
                        //String callAPI = stmt.getInvokeExpr().getMethod().getName();
                        for(int i=0;i<Bitmap_method.length;i++){
                            if(decodingAPI.contains(Bitmap_method[i])){//&&b.getMethod().getSignature().contains("wordpress")
                                System.out.println("checkDecodingAPI:"+decodingAPI);
                                checkResult=true;
                            }
                        }
                    if(checkResult){
                        System.out.println("Begin to instrument InsertCallBackCallLog:   " + body.getMethod().getSignature());

                    }
                }
            }
        }
    }

    public static boolean checkDecodingAPI2(Stmt stmt){
        boolean checkResult=false;
        String[] Bitmap_method={
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
                "android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
//    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResourceStream",
                "android.graphics.BitmapRegionDecoder: decodeRegion(",
//    	"android.graphics.Bitmap: android.graphics.Bitmap createBitmap",
//    	"android.graphics.Bitmap: android.graphics.Bitmap createScaledBitmap",
//    	"setImageIcon",
//		"setImageResource(",
                "android.widget.ImageView: setImageURI"
//		"setImageViewIcon(",
             //   "setImageViewUri(",
//		"BitmapDrawable(",
//		"newInstance(",
             //   "createFromPath(",
//		"createFromResourceStream(",
             //   "createFromStream(",
//		"getDrawable(",
//		"getDrawableForDensity("
        };
        //if (stmt.containsInvokeExpr()) {
            String declaringClass = stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();
            String decodingAPI=stmt.getInvokeExpr().getMethod().getSignature();
            //String callAPI = stmt.getInvokeExpr().getMethod().getName();
            for(int i=0;i<Bitmap_method.length;i++){
                if(decodingAPI.contains(Bitmap_method[i])){
                    System.out.println("checkDecodingAPI:"+declaringClass+"---"+decodingAPI);
                    checkResult=true;
                    break;
                }
            }
        //}
        return checkResult;
    }

    /*
    这里是维护一个API list，然后对每个符合的API list，现在先进行call的查找操作吧，后面再进行进一步的精细化
     */
    public static boolean checkDecodingAPI(Stmt stmt){
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


    public static void InsertCallBackCallLog(Body body){
        String tag="InsertCallBackCallLog";
        SootMethod sootMethod=body.getMethod();
        if(checkCallbackMethod(sootMethod)){
            Iterator<Unit> unitsIterator=body.getUnits().snapshotIterator();
            //这里的操作是遍历方法体中的语句，然后在方法体中进行语句插入
            while(unitsIterator.hasNext()) {
                Stmt stmt = (Stmt) unitsIterator.next();//这条语句就是on方法本身
                if (stmt.containsInvokeExpr()) {
                    System.out.println("Begin to instrument InsertCallBackCallLog:   " + body.getMethod().getSignature());
//                    System.out.println("The stmt:   "+stmt.getInvokeExpr().getMethod().getSignature());
//                    List<Unit> toastUnits = makeToast(body,tag,body.getMethod().getSignature());//具体信息标签在makeToast中设置
//                    body.getUnits().insertAfter(toastUnits, stmt);
                    break;
                }
            }
        }
    }



    //判断是否是以On开头
    public static boolean checkCallbackMethod(SootMethod sootMethod){
        boolean checkResult=false;
        System.out.println("checkCallbackMethod:"+sootMethod.getName());
        if (sootMethod.getName().startsWith("on")) {
            checkResult=true;
        }
        return checkResult;
    }



    public static void insertSetContentView(Body arg0){
        Iterator<Unit> unitsIterator=arg0.getUnits().snapshotIterator();//获取Body里所有的units,一个Body对应Java里一个方法的方法体，Unit代表里面的语句
        while(unitsIterator.hasNext()){
            Stmt stmt=(Stmt)unitsIterator.next();//将Unit强制转换为Stmt,Stmt为Jimple里每条语句的表示
            if(stmt.containsInvokeExpr()){//如果是一条调用语句
                String declaringClass=stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();//获取这个方法所属的类
                String methodName=stmt.getInvokeExpr().getMethod().getName();//获取这个方法的名称
                if(declaringClass.equals("android.app.Activity")&&methodName.equals("setContentView")){
                    List<Unit> toastUnits=makeToast(arg0, "toast infor!");
                    arg0.getUnits().insertAfter(toastUnits, stmt);//在这条语句之后插入Toast消息
                    break;
                }
            }
        }

    }

    private static List<Unit> makeToast(Body body,String toast){
        List<Unit> unitsList=new ArrayList<Unit>();
        //插入语句Log.i("test",toast);
        SootClass logClass=Scene.v().getSootClass("android.util.Log");//获取android.util.Log类
        SootMethod sootMethod=logClass.getMethod("int i(java.lang.String,java.lang.String)");
        StaticInvokeExpr staticInvokeExpr=Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(),StringConstant.v("test"),StringConstant.v(toast));
        InvokeStmt invokeStmt=Jimple.v().newInvokeStmt(staticInvokeExpr);
        unitsList.add(invokeStmt);
        return unitsList;
    }

    public static void InsertAPICallLogForAll(Body body){
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

    public static void func(String str){
        String[] Decoding_method={
                "<org.wordpress.android.util.ImageUtils: boolean resizeImageAndWriteToStream(",
                "<org.wordpress.android.util.ImageUtils: android.graphics.Bitmap getWPImageSpanThumbnailFromFilePath(",
                "<org.wordpress.android.util.ImageUtils: int[] getImageSize",
                "<org.wordpress.android.util.ImageUtils: android.graphics.Bitmap downloadBitmap(",
                "<org.wordpress.android.util.HtmlToSpannedConverter: void startImg",
                "<zendesk.support.request.CellAttachmentLoadingUtil$ImageSizingLogic$ReadFromBitmap: zendesk.support.request.CellAttachmentLoadingUtil$ImageSizingLogic$ImageDimensions loadImageDimensions(",
                "<org.wordpress.android.util.MediaUtils: java.lang.String getMimeTypeOfInputStream",
                "<zendesk.belvedere.BitmapUtils: android.util.Pair getImageDimensions(",
                "<com.airbnb.lottie.ImageAssetBitmapManager: android.graphics.Bitmap bitmapForId("
        };
    }
}