/**
 *
 */
package Pattern4;

import Pattern1.Analysis_Bitmap;
import soot.Body;
import soot.SootField;
import soot.SootMethod;

/**
 * @author liwenjie
 *
 *
 */
public class Check4 {

	public static String[] Pattern4_str={
		"<android.graphics.Bitmap: void recycle()>",
		"<android.graphics.BitmapFactory$Options: android.graphics.Bitmap inBitmap>"
	};

	public static boolean analysis_cache(Body body,SootMethod sootmethod) {
		boolean figer_4=false;
		for(int i=0;i<Pattern4_str.length;i++){
		if (body.toString().contains(Pattern4_str[i])) {
            //System.out.println("Pattern4 Body:"+i+"      "+body.toString());
			System.out.println("Pattern4 Body:"+i+"      "+Pattern4_str[i]+"-------------"+body.getMethod().getSignature().toString());
			Analysis_Bitmap.pattern4_count[i]++;
            figer_4=true;
            
            if(0==i){
            	Analysis_Bitmap.pattern4_set_recycle.add(sootmethod.getSignature());
            	//Analysis_Bitmap.pattern4_set_recycle_out.add(sootmethod.getClass().getName()+"+"+sootmethod.getName());
            }else{
            	Analysis_Bitmap.pattern4_set_inbitmap.add(sootmethod.getSignature());
            	//Analysis_Bitmap.pattern4_set_inbitmap_out.add(sootmethod.getClass().getName()+"+"+sootmethod.getName());
            }
		}
		}
		return figer_4;
	}


}
