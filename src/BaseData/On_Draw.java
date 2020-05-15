/**
 *
 */
package BaseData;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liwenjie
 *
 */
public class On_Draw {
	String method;
	String new_class;
	int lineNumber;
	/**
	 * @param method
	 * @param new_class
	 * @param lineNumber
	 */
	public On_Draw(String method, String new_class, int lineNumber) {
		super();
		this.method = method;
		this.new_class = new_class;
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the new_class
	 */
	public String getNew_class() {
		return new_class;
	}
	/**
	 * @param new_class the new_class to set
	 */
	public void setNew_class(String new_class) {
		this.new_class = new_class;
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


}
