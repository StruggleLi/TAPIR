package APITrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;


public class Instrument1 {
    //public final static String androidJar="D:\\Eclipse\\adt-bundle-windows-x86-20130917\\sdk\\platforms\\";
    //public final static String APK="D:\\Eclipse\\SootTest\\Application-debug.apk";
    public static String androidJar="/Users/wenjieli/My-floder/Java-code/IID-extension/PALOMA-Analysis/lib/";
    public static String APK="/Users/wenjieli/My-floder/Java-code/IID-extension/TestApk/app-debug.apk";
    public static void initsoot(){
        //G.reset();
        Options.v().set_allow_phantom_refs(true);//设置允许伪类（Phantom class），指的是soot为那些在其classpath找不到的类建立的模型
        Options.v().set_prepend_classpath(true);//prepend the VM's classpath to Soot's own classpath
        Options.v().set_output_format(Options.output_format_dex);//设置soot的输出格式--dex是Android平台上(Dalvik虚拟机)的可执行文件, 相当于Windows平台中的exe文件, 每个Apk安装包中都有dex文件, 里面包含了该app的所有源码, 通过反编译工具可以获取到相应的java源码。
        Options.v().set_android_jars(androidJar);//设置android jar包路径
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(APK));
        Options.v().set_android_api_version(21);


        //Options.v().set_soot_classpath("");
        Scene.v().loadNecessaryClasses();
        //call soot.Main
        //soot.Main.main(args);
    }
    public static void main(String[] args) {
        initsoot();
        //这里主要是不知道Body是如何得到的？
        PackManager.v().getPack("jtp").add(new Transform("jtp.MyTransform", new MyTransform()));//添加自己的BodyTransformer
        PackManager.v().runPacks();
        PackManager.v().writeOutput();//写输出？

        System.out.println("hahaha");
    }

}

// custom bodyTransformer
class MyTransform extends BodyTransformer{
    @Override
    protected void internalTransform(Body arg0, String arg1,
                                     Map<String, String> arg2) {
        Iterator<Unit> unitsIterator=arg0.getUnits().snapshotIterator();//获取Body里所有的units,一个Body对应Java里一个方法的方法体，Unit代表里面的语句
        while(unitsIterator.hasNext()){

            Stmt stmt=(Stmt)unitsIterator.next();//将Unit强制转换为Stmt,Stmt为Jimple里每条语句的表示
            if(stmt.containsInvokeExpr()){//如果是一条调用语句
                String declaringClass=stmt.getInvokeExpr().getMethod().getDeclaringClass().getName();//获取这个方法所属的类
                String methodName=stmt.getInvokeExpr().getMethod().getName();//获取这个方法的名称
                if(declaringClass.equals("android.app.Activity")&&methodName.equals("setContentView")){//插桩点

                    System.out.println("Begin to instrument:   "+arg0.getMethod().getSignature());

                    List<Unit> toastUnits=makeToast(arg0, "toast infor!");
                    arg0.getUnits().insertAfter(toastUnits, stmt);//在这条语句之后插入Toast消息
                    System.out.println("Insert the statement successfully!");
                    break;
                }
            }
        }

    }
    private List<Unit> makeToast(Body body,String toast){
        List<Unit> unitsList=new ArrayList<Unit>();
        //插入语句Log.i("test",toast);
        SootClass logClass=Scene.v().getSootClass("android.util.Log");//获取android.util.Log类
        SootMethod sootMethod=logClass.getMethod("int i(java.lang.String,java.lang.String)");
        StaticInvokeExpr staticInvokeExpr=Jimple.v().newStaticInvokeExpr(sootMethod.makeRef(),StringConstant.v("test"),StringConstant.v(toast));
        InvokeStmt invokeStmt=Jimple.v().newInvokeStmt(staticInvokeExpr);
        unitsList.add(invokeStmt);
        return unitsList;
    }
}

