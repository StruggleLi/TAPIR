/**
 * 
 */
package Pattern2;

import java.util.ArrayList;

/**
 * @author Dell
 *
 */
public class Result_Pattern2 {
	//Pattern2检测统计元素
		public ArrayList<String> UI_method=new ArrayList<String>();  //存储每个decode相关的UI方法
		public ArrayList<String> child_method=new ArrayList<String>();  //存储每个decode相关的child方法
		public int UI_count=0;   //每一个decode与几个on开头的方法有关系
		public int Child_count=0;
		/**
		 * @param uI_method
		 * @param child_method
		 * @param return_String
		 * @param uI_count
		 * @param child_count
		 */
		public Result_Pattern2(ArrayList<String> uI_method,
				ArrayList<String> child_method, 
				int uI_count, int child_count) {
			super();
			this.UI_method = uI_method;
			this.child_method = child_method;
			this.UI_count = uI_count;
			this.Child_count = child_count;
		}
		/**
		 * @return the uI_method
		 */
		public ArrayList<String> getUI_method() {
			return UI_method;
		}
		/**
		 * @param uI_method the uI_method to set
		 */
		public void setUI_method(ArrayList<String> uI_method) {
			UI_method = uI_method;
		}
		/**
		 * @return the child_method
		 */
		public ArrayList<String> getChild_method() {
			return child_method;
		}
		/**
		 * @param child_method the child_method to set
		 */
		public void setChild_method(ArrayList<String> child_method) {
			this.child_method = child_method;
		}
		
		/**
		 * @return the uI_count
		 */
		public int getUI_count() {
			return UI_count;
		}
		/**
		 * @param uI_count the uI_count to set
		 */
		public void setUI_count(int uI_count) {
			UI_count = uI_count;
		}
		/**
		 * @return the child_count
		 */
		public int getChild_count() {
			return Child_count;
		}
		/**
		 * @param child_count the child_count to set
		 */
		public void setChild_count(int child_count) {
			Child_count = child_count;
		}
		
		

}
