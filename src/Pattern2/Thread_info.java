/**
 * 
 */
package Pattern2;

import soot.SootMethod;

/**
 * @author Dell
 *
 */
public class Thread_info {
	SootMethod sootmethod;//start��execute���ڵķ���
	String stmt;
	/**
	 * @param sootmethod
	 * @param stmt
	 */
	public Thread_info(SootMethod sootmethod, String stmt) {
		super();
		this.sootmethod = sootmethod;
		this.stmt = stmt;
	}
	/**
	 * @return the sootmethod
	 */
	public SootMethod getSootmethod() {
		return sootmethod;
	}
	/**
	 * @param sootmethod the sootmethod to set
	 */
	public void setSootmethod(SootMethod sootmethod) {
		this.sootmethod = sootmethod;
	}
	/**
	 * @return the stmt
	 */
	public String getStmt() {
		return stmt;
	}
	/**
	 * @param stmt the stmt to set
	 */
	public void setStmt(String stmt) {
		this.stmt = stmt;
	}
	
	
	

}
