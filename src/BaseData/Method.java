/**
 *
 */
package BaseData;

import java.util.ArrayList;

/**
 * @author liwenjie
 *
 */
public class Method {
	public String invoke_from;
	public ArrayList<Event> invoke_to=new ArrayList<Event>();
	public ArrayList<Event> creation_operation=new ArrayList<Event>();
	public ArrayList<Event> heavy_invoke=new ArrayList<Event>();
	/**
	 * @return the invoke_from
	 */
	public String getInvoke_from() {
		return invoke_from;
	}
	/**
	 * @param invoke_from the invoke_from to set
	 */
	public void setInvoke_from(String invoke_from) {
		this.invoke_from = invoke_from;
	}
	/**
	 * @return the invoke_to
	 */
	public ArrayList<Event> getInvoke_to() {
		return invoke_to;
	}
	/**
	 * @param invoke_to the invoke_to to set
	 */
	public void setInvoke_to(ArrayList<Event> invoke_to) {
		this.invoke_to = invoke_to;
	}
	/**
	 * @return the creation_operation
	 */
	public ArrayList<Event> getCreation_operation() {
		return creation_operation;
	}
	/**
	 * @param creation_operation the creation_operation to set
	 */
	public void setCreation_operation(ArrayList<Event> creation_operation) {
		this.creation_operation = creation_operation;
	}
	/**
	 * @return the heavy_invoke
	 */
	public ArrayList<Event> getHeavy_invoke() {
		return heavy_invoke;
	}
	/**
	 * @param heavy_invoke the heavy_invoke to set
	 */
	public void setHeavy_invoke(ArrayList<Event> heavy_invoke) {
		this.heavy_invoke = heavy_invoke;
	}




}
