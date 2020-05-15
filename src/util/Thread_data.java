/**
 *
 */
package util;

import java.util.ArrayList;

/**
 * @author liwenjie
 *
 */
public class Thread_data {
	public String className;
	public String methodName;
	public int lineNumber[];
	public String invoke;
	ArrayList<String> thread_method_set;
	/**
	 * @param className
	 * @param methodName
	 * @param lineNumber
	 */
	public Thread_data(String className, String methodName, int[] lineNumber, String invoke,ArrayList<String> thread_method_set) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.lineNumber = lineNumber;
		this.invoke=invoke;
		this.thread_method_set=thread_method_set;
	}
	
	
	/**
	 * @return the thread_method_set
	 */
	public ArrayList<String> getThread_method_set() {
		return thread_method_set;
	}


	/**
	 * @param thread_method_set the thread_method_set to set
	 */
	public void setThread_method_set(ArrayList<String> thread_method_set) {
		this.thread_method_set = thread_method_set;
	}


	/**
	 * @return the invoke
	 */
	public String getInvoke() {
		return invoke;
	}


	/**
	 * @param invoke the invoke to set
	 */
	public void setInvoke(String invoke) {
		this.invoke = invoke;
	}


	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the lineNumber
	 */
	public int[] getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int[] lineNumber) {
		this.lineNumber = lineNumber;
	}



}
