/**
 *
 */
package BaseData;

/**
 * @author liwenjie
 *
 */
public class McallGraph {
	String from;
	String to;
	String lineNumber;

	/**
	 * @param from
	 * @param to
		 */
	public McallGraph(String from, String to,String lineNumber) {
		super();
		this.from = from;
		this.to = to;
		this.lineNumber=lineNumber;


	}
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
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
