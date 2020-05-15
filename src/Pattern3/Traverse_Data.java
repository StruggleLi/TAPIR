/**
 * 
 */
package Pattern3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import util.Thread_data;

/**
 * @author Dell
 *
 */
public class Traverse_Data {
	//存储线程信息--这里主要用于协助问题修复
		Set<String> thread_class_name_set=new HashSet<String>();
		Set<Thread_data> thread_data_set=new HashSet<Thread_data>();
		
		boolean pattern3_frequency_method=false;   //是否包含frequency方法
		
		int frequent_count=0;   //统计frequency方法数量
		int count_compress=0;   //统计compress的数量
		ArrayList<String> compress_method=new ArrayList<String>();
		boolean pattern4_result=false;
		ArrayList<String> pattern4_set=new ArrayList<String>();  //存储所有包含pattern4特征的方法
		int[] pattern4_count={0,0};  //分别统计recycle和inBitmap的数量
		int[] freq_count={0,0}; //分别统计getview 和ondraw的数量
		int[] freq_decode={0,0}; //分别统计decode与getview 和ondraw相关的数量
		
		int getCallGraph=1;

}
