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
	//�洢�߳���Ϣ--������Ҫ����Э�������޸�
		Set<String> thread_class_name_set=new HashSet<String>();
		Set<Thread_data> thread_data_set=new HashSet<Thread_data>();
		
		boolean pattern3_frequency_method=false;   //�Ƿ����frequency����
		
		int frequent_count=0;   //ͳ��frequency��������
		int count_compress=0;   //ͳ��compress������
		ArrayList<String> compress_method=new ArrayList<String>();
		boolean pattern4_result=false;
		ArrayList<String> pattern4_set=new ArrayList<String>();  //�洢���а���pattern4�����ķ���
		int[] pattern4_count={0,0};  //�ֱ�ͳ��recycle��inBitmap������
		int[] freq_count={0,0}; //�ֱ�ͳ��getview ��ondraw������
		int[] freq_decode={0,0}; //�ֱ�ͳ��decode��getview ��ondraw��ص�����
		
		int getCallGraph=1;

}
