package yixue.icse;

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

public class Example {
    public final static String androidJar="H:\\PALOMA-Analysis\\PALOMA-Analysis\\lib\\";
    //对nori.apk,androFotoFinder.apk进行分析是成功的，但是对MUZEI.apk,Application-release.apk，wpandroid-11.5.2.apk,muzei_7.apk, wpandroid-11.8.apk分析失败
    public final static String APK="H:\\TestApk\\fr.neamar.kiss_146.apk";//Application-release.apk";
    public static void initsoot(String[] args){
        //G.reset();
        Options.v().set_allow_phantom_refs(true);//设置允许伪类（Phantom class），指的是soot为那些在其classpath找不到的类建立的模型
        Options.v().set_prepend_classpath(true);//prepend the VM's classpath to Soot's own classpath
        Options.v().set_output_format(Options.output_format_dex);//设置soot的输出格式
        Options.v().set_android_jars(androidJar);//设置android jar包路径
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(APK));

        System.out.println("It is an example!");
        //Options.v().set_soot_classpath("");
        Scene.v().loadNecessaryClasses();
        //call soot.Main
        //soot.Main.main(args);
    }
    public static void main(String[] args) {
        initsoot(args);
        PackManager.v().getPack("jtp").add(new Transform("jtp.ATransform", new ATransform()));//添加自己的BodyTransformer
        PackManager.v().runPacks();
        PackManager.v().writeOutput();

    }

}

// custom bodyTransformer
class ATransform extends BodyTransformer{
    @Override
    protected void internalTransform(Body arg0, String arg1,
                                     Map<String, String> arg2) {
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
