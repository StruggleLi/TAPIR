/**
 *
 */
package Pattern2;

import java.util.ArrayList;

import BaseData.B;

/**
 * @author liwenjie
 * ���ܣ��洢����Bitmap��Heavy API
 */
public class Data2 {


    ArrayList<B> Bitmap_Heavy_API=new ArrayList<B>();


    String[] Thread_API={
    		"doInBackground",
    		"run()"
    		};


    void addData(){

    //part1��Ҫ������洢�Ŀ���������+����----����Ҫ��API
    //���磺     Method Name:  <android.support.v7.internal.widget.ActionBarOverlayLayout: void <clinit>()>

    	Bitmap_Heavy_API.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFile(java.lang.String,android.graphics.BitmapFactory$Options)>"));
    	Bitmap_Heavy_API.add(new B("android.graphics.Bitmap", "<android.graphics.Bitmap: boolean compress(android.graphics.Bitmap$CompressFormat,int,java.io.OutputStream)>"));
    	Bitmap_Heavy_API.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeByteArray(byte[],int,int)>"));
    	Bitmap_Heavy_API.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeResource(android.content.res.Resources,int)>"));
    	Bitmap_Heavy_API.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>"));
    	Bitmap_Heavy_API.add(new B("android.graphics.BitmapFactory", "<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor)>"));


    }

}
