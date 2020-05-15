/**
 * 
 */
package Pattern3;

import soot.SootMethod;

/**
 * @author Wenjie Li
 *
 */
public class Bitmap_display {
	public static String setImageBitmap="<android.widget.ImageView: void setImageBitmap(android.graphics.Bitmap)>";
	
	public static boolean findSetImageBitmap(SootMethod invoked_sm){
		boolean result=false;
		String methodname=invoked_sm.getSignature();
		if(methodname.equals(setImageBitmap)){
			System.out.println("setImageBitmap:    "+invoked_sm.getSignature());
			result=true;
		}
		
		return result;
		
	}

}
