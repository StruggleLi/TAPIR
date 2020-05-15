/**
 *
 */
package repair;

import java.io.IOException;

import util.FileModify;
import util.FileOperation;

import util.JarFileMove;

/**
 * @author liwenjie
 *
 */
public class Repair_Third_Lib {
	public static void main(String[] args) throws IOException{
		int pattern_number=1;
		repair_1(pattern_number);
	}

	public static void repair_1(int pattern_number) throws IOException{
		int lineNumber=201;  //the line to be repair
		String path_Jar="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\API";  //the path of optimization API
		String import_line="import android.displayingbitmaps.API.ImageResizer;";  //the statment to be added to the .java file
		String searchText="BitmapFactory.decodeFileDescriptor";   //the str to be modified
		String replace_API_string="ImageResizer.decodeSampledBitmapFromDescriptor";  // the string to be modify
		String to_path="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\libs";   // the location of the API file to be added
		String temp_file_path="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\folder3\\temp.java";  // the location of the temp file of .java file
		String path_TBM_file="C:\\Users\\liwenjie\\Desktop\\Android-Bitmap-Analysis\\Project_repair\\DisplayingBitmaps-gai-problem\\src\\com\\example\\android\\displayingbitmaps\\util\\ImageResizer.java";   // the .java file to be modified

		String reqWidth="reqWidth";
		String reqHeight="reqHeight";
		String args_TBM=","+reqWidth+","+reqHeight;

		//(1) move API file to the specify location
		JarFileMove.moveJarFile(path_Jar,to_path);

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
				}
	}


}
