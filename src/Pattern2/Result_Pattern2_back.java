/**
 * 
 */
package Pattern2;

import java.util.ArrayList;

/**
 * @author Dell
 *
 */
public class Result_Pattern2_back {
	//Pattern2���ͳ��Ԫ��
		public ArrayList<ArrayList<String>> UI_method=new ArrayList<ArrayList<String>>();  //�洢ÿ��decode��ص�UI����
		public ArrayList<ArrayList<String>> child_method=new ArrayList<ArrayList<String>>();  //�洢ÿ��decode��ص�child����

		int UI_count;
		int Child_count;
		/**
		 * @param uI_method
		 * @param child_method
		 * @param uI_count
		 * @param child_count
		 */
		public Result_Pattern2_back(ArrayList<ArrayList<String>> uI_method,
				ArrayList<ArrayList<String>> child_method, int uI_count,
				int child_count) {
			super();
			UI_method = uI_method;
			this.child_method = child_method;
			UI_count = uI_count;
			Child_count = child_count;
		}
		/**
		 * @return the uI_method
		 */
		public ArrayList<ArrayList<String>> getUI_method() {
			return UI_method;
		}
		/**
		 * @param uI_method the uI_method to set
		 */
		public void setUI_method(ArrayList<ArrayList<String>> uI_method) {
			UI_method = uI_method;
		}
		/**
		 * @return the child_method
		 */
		public ArrayList<ArrayList<String>> getChild_method() {
			return child_method;
		}
		/**
		 * @param child_method the child_method to set
		 */
		public void setChild_method(ArrayList<ArrayList<String>> child_method) {
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
