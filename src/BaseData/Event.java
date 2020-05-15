/**
 * 
 */
package BaseData;

/**
 * @author liwenjie
 * 存储各类语句操作，例如对象创建、方法调用等
 */
public class Event {
	public String name;
	public String lineNumber;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	

}
