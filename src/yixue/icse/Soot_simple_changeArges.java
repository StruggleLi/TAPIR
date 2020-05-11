package yixue.icse;

        import java.util.Collections;
        import java.util.Iterator;
        import java.util.Map;

        import soot.Body;
        import soot.BodyTransformer;
        import soot.Local;
        import soot.PackManager;
        import soot.PatchingChain;
        import soot.RefType;
        import soot.Scene;
        import soot.SootClass;
        import soot.SootMethod;
        import soot.Transform;
        import soot.Unit;
        import soot.jimple.AbstractStmtSwitch;
        import soot.jimple.InvokeExpr;
        import soot.jimple.InvokeStmt;
        import soot.jimple.Jimple;
        import soot.jimple.StringConstant;
        import soot.options.Options;

//这个代码版本是基础成功执行的版本，后续会在这个版本的基础上进行增加操作。
public class Soot_simple_changeArges {

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
        //Options.v().set_keep_line_number(false);//add
        //Options.v().set_keep_offset(false);//add
        //Options.v().set_whole_program(true);//add
        //Options.v().set_verbose(false);//add
        //Options.v().set_validate(true);//add
        //Options.v().set_include_all(true);//add

        //Options.v().set_no_bodies_for_excluded(true);//add   表示不加载未被包含的类



        Scene.v().loadNecessaryClasses();
        System.out.println("loadNecessaryClasses");

        // resolve the PrintStream and System soot-classes
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);

        System.out.println("Finished Scene!");

        //样例程序中的是myInstrumenter，替换为internalTransform
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {
            @Override
            protected void internalTransform(final Body b, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                //System.out.println("Call internalTransform!");
                final PatchingChain<Unit> units = b.getUnits();

                //important to use snapshotIterator here
                for(Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
                    final Unit u = iter.next();
                    u.apply(new AbstractStmtSwitch() {

                        public void caseInvokeStmt(InvokeStmt stmt) {
                            InvokeExpr invokeExpr = stmt.getInvokeExpr();
                            if(invokeExpr.getMethod().getName().equals("onDraw")) {
                                System.out.println("Here is an onDraw");

                                Local tmpRef = addTmpRef(b);
                                Local tmpString = addTmpString(b);

                                // insert "tmpRef = java.lang.System.out;"
                                units.insertBefore(Jimple.v().newAssignStmt(
                                        tmpRef, Jimple.v().newStaticFieldRef(
                                                Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), u);

                                // insert "tmpLong = 'HELLO';"
                                units.insertBefore(Jimple.v().newAssignStmt(tmpString,
                                        StringConstant.v("HELLO")), u);

                                // insert "tmpRef.println(tmpString);"
                                SootMethod toCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(java.lang.String)");
                                units.insertBefore(Jimple.v().newInvokeStmt(
                                        Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpString)), u);

                                //check that we did not mess up the Jimple
                                b.validate();
                            }
                        }

                    });
                }
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
}
