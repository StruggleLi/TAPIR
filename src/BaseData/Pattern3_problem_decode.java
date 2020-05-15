/**
 *
 */
package BaseData;

/**
 * @author liwenjie
 * 功能：用来存储问题位置
 *
 */
public class Pattern3_problem_decode {
	String class_name;
	String method_name;
	String decode_method;


	/**
	 * @param class_name
	 * @param method_name
	 */
	public Pattern3_problem_decode(String class_name, String method_name,String decode_method) {
		super();
		this.class_name = class_name;
		this.method_name = method_name;
		this.decode_method=decode_method;

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

}
