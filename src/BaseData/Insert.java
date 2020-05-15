/**
 *
 */
package BaseData;

import java.util.ArrayList;

import Pattern2.Result_Pattern2;
import Pattern2.Result_Pattern2_back;

/**
 * @author liwenjie
 * 功能：用来存储问题位置
 *
 */
public class Insert {
	String class_name;
	String method_name;
	String decode_method;
	String statement;
	int count1;
	int count2;
	boolean bool1;
	boolean bool2;
	int lineNumber;
	ArrayList<String> pattern3_frequen;
	Result_Pattern2 result_pattern2;
	Result_Pattern2_back result_Pattern2_back;
	//ArrayList<String> pattern3_MC;
	String pattern3_memory_result_string;
	//ArrayList<String> pattern3_disk_result;
	String pattern3_disk_result_string;
	
	ArrayList<String> pattern4_recycle;
	ArrayList<String> pattern4_inbitmap;
	int image_drawable;
	int image_without_return;
	
	ArrayList<String> path_backward_frequency;
	
	

	
	/**
	 * @return the result_Pattern2_back
	 */
	public Result_Pattern2_back getResult_Pattern2_back() {
		return result_Pattern2_back;
	}
	/**
	 * @param result_Pattern2_back the result_Pattern2_back to set
	 */
	public void setResult_Pattern2_back(Result_Pattern2_back result_Pattern2_back) {
		this.result_Pattern2_back = result_Pattern2_back;
	}
	/**
	 * @return the image_without_return
	 */
	public int getImage_without_return() {
		return image_without_return;
	}
	/**
	 * @param image_without_return the image_without_return to set
	 */
	public void setImage_without_return(int image_without_return) {
		this.image_without_return = image_without_return;
	}
	/**
	 * @return the image_drawable
	 */
	public int getImage_drawable() {
		return image_drawable;
	}
	/**
	 * @param image_drawable the image_drawable to set
	 */
	public void setImage_drawable(int image_drawable) {
		this.image_drawable = image_drawable;
	}
	/**
	 * @return the pattern3_memory_result_string
	 */
	public String getPattern3_memory_result_string() {
		return pattern3_memory_result_string;
	}
	/**
	 * @param pattern3_memory_result_string the pattern3_memory_result_string to set
	 */
	public void setPattern3_memory_result_string(
			String pattern3_memory_result_string) {
		this.pattern3_memory_result_string = pattern3_memory_result_string;
	}
	/**
	 * @return the pattern3_disk_result_string
	 */
	public String getPattern3_disk_result_string() {
		return pattern3_disk_result_string;
	}
	/**
	 * @param pattern3_disk_result_string the pattern3_disk_result_string to set
	 */
	public void setPattern3_disk_result_string(String pattern3_disk_result_string) {
		this.pattern3_disk_result_string = pattern3_disk_result_string;
	}
	/**
	 * @return the pattern4_recycle
	 */
	public ArrayList<String> getPattern4_recycle() {
		return pattern4_recycle;
	}
	/**
	 * @param pattern4_recycle the pattern4_recycle to set
	 */
	public void setPattern4_recycle(ArrayList<String> pattern4_recycle) {
		this.pattern4_recycle = pattern4_recycle;
	}
	/**
	 * @return the pattern4_inbitmap
	 */
	public ArrayList<String> getPattern4_inbitmap() {
		return pattern4_inbitmap;
	}
	/**
	 * @param pattern4_inbitmap the pattern4_inbitmap to set
	 */
	public void setPattern4_inbitmap(ArrayList<String> pattern4_inbitmap) {
		this.pattern4_inbitmap = pattern4_inbitmap;
	}
	public Insert(String class_name, String method_name,String decode_method,String statement,
			int count1,int count2,boolean bool1,boolean bool2,int lineNumber,
			ArrayList<String> pattern3_frequen,Result_Pattern2 result_pattern2,Result_Pattern2_back result_Pattern2_back,String pattern3_memory_result_string,String pattern3_disk_result_string,
			ArrayList<String> pattern4_recycle,ArrayList<String> pattern4_inbitmap,int image_drawable,int image_without_return,ArrayList<String> path_backward_frequency) {
		super();
		this.class_name = class_name;
		this.method_name = method_name;
		this.decode_method=decode_method;
		this.statement=statement;
		this.count1=count1;
		this.count2=count2;
		this.bool1=bool1;
		this.bool2=bool2;
		
		this.lineNumber=lineNumber;
		this.pattern3_frequen=pattern3_frequen;
		this.result_pattern2=result_pattern2;
		this.result_Pattern2_back=result_Pattern2_back;
		this.pattern3_memory_result_string=pattern3_memory_result_string;
		this.pattern3_disk_result_string=pattern3_disk_result_string;
		
		this.pattern4_recycle=pattern4_recycle;
		this.pattern4_inbitmap=pattern4_inbitmap;
		this.image_drawable=image_drawable;
		this.image_without_return=image_without_return;
		this.path_backward_frequency=path_backward_frequency;
	}
	
	
	/**
	 * @return the path_backward_frequency
	 */
	public ArrayList<String> getPath_backward_frequency() {
		return path_backward_frequency;
	}
	/**
	 * @param path_backward_frequency the path_backward_frequency to set
	 */
	public void setPath_backward_frequency(ArrayList<String> path_backward_frequency) {
		this.path_backward_frequency = path_backward_frequency;
	}
	/**
	 * @return the class_name
	 */
	public String getClass_name() {
		return class_name;
	}
	/**
	 * @param class_name the class_name to set
	 */
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	/**
	 * @return the method_name
	 */
	public String getMethod_name() {
		return method_name;
	}
	/**
	 * @param method_name the method_name to set
	 */
	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}
	/**
	 * @return the decode_method
	 */
	public String getDecode_method() {
		return decode_method;
	}
	/**
	 * @param decode_method the decode_method to set
	 */
	public void setDecode_method(String decode_method) {
		this.decode_method = decode_method;
	}
	/**
	 * @return the statement
	 */
	public String getStatement() {
		return statement;
	}
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}
	/**
	 * @return the count1
	 */
	public int getCount1() {
		return count1;
	}
	/**
	 * @param count1 the count1 to set
	 */
	public void setCount1(int count1) {
		this.count1 = count1;
	}
	/**
	 * @return the count2
	 */
	public int getCount2() {
		return count2;
	}
	/**
	 * @param count2 the count2 to set
	 */
	public void setCount2(int count2) {
		this.count2 = count2;
	}
	/**
	 * @return the bool1
	 */
	public boolean isBool1() {
		return bool1;
	}
	/**
	 * @param bool1 the bool1 to set
	 */
	public void setBool1(boolean bool1) {
		this.bool1 = bool1;
	}
	/**
	 * @return the bool2
	 */
	public boolean isBool2() {
		return bool2;
	}
	/**
	 * @param bool2 the bool2 to set
	 */
	public void setBool2(boolean bool2) {
		this.bool2 = bool2;
	}
	
	/**
	 * @return the pattern3_MC
	 */
	public String getPattern3_MC() {
		return pattern3_memory_result_string;
	}
	/**
	 * @param pattern3_MC the pattern3_MC to set
	 */
	public void setPattern3_MC(String pattern3_memory_result_string) {
		this.pattern3_memory_result_string = pattern3_memory_result_string;
	}
	/**
	 * @return the pattern3_disk_result
	 */
	public String getPattern3_disk_result() {
		return pattern3_disk_result_string;
	}
	/**
	 * @param pattern3_disk_result the pattern3_disk_result to set
	 */
	public void setPattern3_disk_result(String pattern3_disk_result) {
		this.pattern3_disk_result_string = pattern3_disk_result_string;
	}
	/**
	 * @return the result_pattern2
	 */
	public Result_Pattern2 getResult_pattern2() {
		return result_pattern2;
	}
	/**
	 * @param result_pattern2 the result_pattern2 to set
	 */
	public void setResult_pattern2(Result_Pattern2 result_pattern2) {
		this.result_pattern2 = result_pattern2;
	}
	/**
	 * @return the pattern3_frequen
	 */
	public ArrayList<String> getPattern3_frequen() {
		return pattern3_frequen;
	}
	/**
	 * @param pattern3_frequen the pattern3_frequen to set
	 */
	public void setPattern3_frequen(ArrayList<String> pattern3_frequen) {
		this.pattern3_frequen = pattern3_frequen;
	}
	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @param class_name
	 * @param method_name
	 */

}
