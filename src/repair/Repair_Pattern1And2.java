/**
 *
 */
package repair;

import java.io.IOException;

import util.FileOperation;
import util.FileModify;

/**
 * @author liwenjie
 *
 */
public class Repair_Pattern1And2 {
	public static void main(String[] args) throws IOException{
		int pattern_number=2;
		repair_1(pattern_number);
	}


	//如何将mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
	//改为：mLoadingBitmap = decodeSampledBitmapFromResource(mResources, resId,
    //reqWidth, reqHeight, cache)
	public static void repair_1(int pattern_number) throws IOException{
		//是否进行了API库类文件复制
		boolean figer_API=false;


//----------pattern1
		int lineNumber=201;  //the line to be repair
		String path_API="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\API";  //the path of optimization API
		String import_line="import android.displayingbitmaps.API.ImageResizer;";  //the statment to be added to the .java file
		String searchText="BitmapFactory.decodeFileDescriptor";   //the str to be modified
		String replace_API_string="ImageResizer.decodeSampledBitmapFromDescriptor";  // the string to be modify
		String path_src="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\src";   // the location of the API file to be added
		String temp_file_path="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder3\\temp.java";  // the location of the temp file of .java file
		String path_TBM_file="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\src\\com\\example\\android\\displayingbitmaps\\util\\ImageResizer.java";   // the .java file to be modified

		String reqWidth="reqWidth";
		String reqHeight="reqHeight";
		String args_TBM=","+reqWidth+","+reqHeight;

//----------pattern2
		int lineNumber_2=119;
		String import_line_2="import android.displayingbitmaps.API.Interface;";  //the statment to be added to the .java file
		String searchText_2="BitmapFactory.decodeResource";   //the str to be modified
		String replace_API_string_2="ImageResizer.decodeSampledBitmapFromResource";  // the string to be modify
		String path_src_2="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\src";   // the location of the API file to be added
		String temp_file_path_2="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder3\\temp.java";  // the location of the temp file of .java file
		String path_TBM_file_2="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\src\\com\\example\\android\\displayingbitmaps\\util\\ImageWorker.java";   // the .java file to be modified

		String reqWidth_2="reqWidth";
		String reqHeight_2="reqHeight";
		String args_TBM_2=","+reqWidth+","+reqHeight;

		//(1) move API file to the specify location
		if(figer_API){
		     FileOperation.copyFolder(path_API,path_src);
		}

		if(pattern_number==1){

		//(2) modify the problem .java file
		String temp_str="";
		temp_str=FileModify.readAndModifyFile(path_TBM_file,searchText,replace_API_string,lineNumber,import_line
				,args_TBM);

		//(3) write the modify file content to a temp file
		FileModify.createFile(temp_file_path);
		FileModify.writeTxtFile(temp_str,temp_file_path);

		//(4) copy the temp file content to the problem file
		FileModify.copyFile(temp_file_path,path_TBM_file);

		}else{
			//(2) modify the problem .java file
			String temp_str_2="";
			temp_str_2=FileModify.readAndModifyFile(path_TBM_file_2,searchText_2,replace_API_string_2,lineNumber_2,import_line_2
					,args_TBM_2);

			//(3) write the modify file content to a temp file
			FileModify.createFile(temp_file_path_2);
			FileModify.writeTxtFile(temp_str_2,temp_file_path_2);

			//(4) copy the temp file content to the problem file
			FileModify.copyFile(temp_file_path_2,path_TBM_file_2);

		}


	}

}
