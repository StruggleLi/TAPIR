/**
 *
 */
package Pattern1;


/**
 * @author liwenjie
 * 功能： 存放基本数据
 */
public class Data1 {

    static String[] Bitmap_class={
    	"android.graphics.BitmapFactory$Options: void <init>()"}; //  android.graphics.BitmapFactory.Options

    //5类decode*方法--》Creates Bitmap objects from various sources, including files, streams, and byte-arrays.
    public static String[] Bitmap_method={
    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile",
    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray",
    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource",
    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream",
    	"android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor",
    	"android.graphics.Bitmap: android.graphics.Bitmap createBitmap"

    };
    /*
     * "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile(java.lang.String,android.graphics.BitmapFactory$Options)>",
    	"<android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray(byte[],int,int)>",
    	"<android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource(android.content.res.Resources,int)>",
    	"<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>",
    	"<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor)>"
     * */


    //Options参数
    static String[] Bitmap_Options={
    	"android.graphics.BitmapFactory$Options: boolean inJustDecodeBounds",
    	"android.graphics.BitmapFactory$Options: int inSampleSize"
    };
    
    static String[] ineffAPI={
    	"setImageIcon",
    	"setImageResource",
    	"setImageURI"
    };



}
