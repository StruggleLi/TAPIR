/**
 *
 */
package util;

import java.util.ArrayList;

/**
 * @author liwenjie
 * 存储方法调用语句的参数信息，包括参数的数量和类型
 *
 */
public class data_invoke_parameters {
	int args_number;
	ArrayList<String> args=new ArrayList<String>();



	/**
	 * @param args_number
	 * @param args
	 */
	public data_invoke_parameters(int args_number, ArrayList<String> args) {
		super();
		this.args_number = args_number;
		this.args = args;
	}
	/**
	 * @return the args_number
	 */
	public int getArgs_number() {
		return args_number;
	}
	/**
	 * @param args_number the args_number to set
	 */
	public void setArgs_number(int args_number) {
		this.args_number = args_number;
	}
	/**
	 * @return the args
	 */
	public ArrayList<String> getArgs() {
		return args;
	}
	/**
	 * @param args the args to set
	 */
	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}



}
