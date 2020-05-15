/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import BaseData.VH_data;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.HashMutablePDG;
import soot.toolkits.graph.pdg.PDGNode;

/**
 * @author Dell
 * �ų�һ���������DiskCache�л�ȡͼƬ�����������õ�ͼƬ����û�б�Ҫ���д�С�����ˡ�
 * ��
 * 1�������Ƿ�ʹ����DiskCache
 * 2����������decode����Դ��DiskCache�йء�--------����word�еķ�������ȫ����ͨ������ʿ��depend�����ķ����������������֮���������ϵ
 * 
 * 3�����Է���decode��if�ж���������������������decode���ڷ������ڵ�������Ƿ���ڡ�
 * 
 * �ȼ��ͨ����Ȼ���ٷ������ӵ������
 * 
 * 
 * ���Գ���owncloud
 *
 */
public class Check_DiskCache_decode {
	static Set<VH_data> vh_data=new HashSet<VH_data>();    //�洢�������
	
	/**
	 * Try to find the frequency oprated methods
	 */
	public static void check_DiskCache(SootClass sootclass,SootMethod sootMethod,String decode){
		
		            Scene.v().loadClassAndSupport(sootclass.getName());

                    Exception localObject1=null;
                    UnitGraph graph = null;
                    HashMutablePDG mutablePDG=null;
                    PDGNode pdgNode=null;              //PDG-��program dependent graph

                    //�������ϣ��ڵ�洢
                    ArrayList<PDGNode> localArrayList = new ArrayList<PDGNode>();
                    ArrayList<PDGNode> localArrayList1 = new ArrayList<PDGNode>();
                    ArrayList<PDGNode> localArrayList2 = new ArrayList<PDGNode>();

                	   try
                       {  graph = new ExceptionalUnitGraph(sootMethod.retrieveActiveBody());
                       }
                       catch (IllegalStateException paramSootClass)
                       {  System.err.println("[error] " + paramSootClass.getMessage());
                       }
                	   try
                       {  mutablePDG = new HashMutablePDG(graph);//���getView()�����е�ȫ��PDGNode������������ͼ
                       }
                       catch (Exception localException1)
                       {  System.err.println("[error] " + localException1.getMessage());
                       }

                	   if (mutablePDG == null)//���PDGû�гɹ���ã�������Ϣ���
                       {
                          System.err.println("[error] program dependency graph construction failure");//��������ͼ����ʧ��
                       }

	               Iterator<Object> localObject3 = mutablePDG.getNodes().iterator();//�õ�PDGNode�ڵ㼯��

                	  //�Գ�������ͼ�еĽڵ���б���
                       while ((localObject3).hasNext()) {
                    	   pdgNode=(PDGNode) localObject3.next();
                    	   
                    	   if (pdgNode instanceof PDGNode){
                    		   System.out.println("PDGNode:    "+pdgNode.toString());
                    		   
                    		   //�����Ӧ�жϼ��DiskCache���Ƿ����Bitmap
                    	        if ((pdgNode.getType() == PDGNode.Type.CFGNODE)
                    	             &&((pdgNode.toString().contains("== null"))||pdgNode.toString().contains("!= null"))){
                    	   	             localArrayList.add(pdgNode);//����ǣ���ӵ�����----������е�CFG�ڵ㣿
                    	                       			   //����----------------���Խڵ���Ϣ�������
                    	                       			   //System.out.println("FindRecycledTtemCheckingNodes:       "+pdgNode.toString());
                    	                       		   }

                    	        if (pdgNode.getType() == PDGNode.Type.REGION){//������ͣ�REGION�������ͨ��䣬���ж�
                    	             String pdgNodeString=pdgNode.toString();//��ýڵ���Ϣ

                    	             String[] b2 = {"android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)"};//�洢��������
                    	             String[] arrayOfStringb2 = b2;//��������b����(�ɹ�)
                    	             String str;
                    	  	         for (int j = 0; j <= 0; j++)
                    	                 {
                    	                     str = arrayOfStringb2[j];//ȡ�������е�����--������decode����

                    	                     if (pdgNodeString.contains(str)) {//�����Ƿ��������б������
                    	                       	localArrayList1.add(pdgNode);//����ǣ���PDG�ڵ���ӵ�����1
                    	                       	//System.out.println("FindDecodeNodes :       "+pdgNodeString);
                    	                     }
                    	                 }

/**����д**/                    	                 String[] c2 = { "android.view.View findViewById(int)" };//�洢������
												String[] c22={"java.io.InputStream"};


                    	             String[] arrayOfStringc2 = c2;//��������c�е�����    String[] c = { "android.view.View findViewById(int)" };//�洢������
                    	         	 for (int j = 0; j <= 0; j++)
                                     {
                    	                 str = arrayOfStringc2[j];
                    	                 if (pdgNodeString.contains(str)) {
                    	                     localArrayList2.add(pdgNode);//����ǣ���PDG�ڵ���ӵ�����2
                    	                     //System.out.println("BufferedInputStream :       "+pdgNode.toString());
                    	                     }
                    	             }
                    	                       		  }
                    	                       	  }
                    	   //System.out.println("the size of localArrayList1,2,3:   "+localArrayList.size()+"   "+localArrayList1.size()+"   "+localArrayList2.size());

                    	   //���û��if���ж����-ֱ�Ӿ���Ϊ��decodeû�н���cache��
                           if(localArrayList1.size()== 0){
                         	  vh_data.add(new VH_data(sootMethod.getSignature()));
                           }
                            if (localArrayList1.size()!= 0){//����PDG�ڵ��б�Ϊ��
                         	  //System.out.println("Start ����1");
                         	  /****�Լ���1***/
                         	  Iterator<PDGNode> iter1=localArrayList1.iterator();
                         	  int i;
                         	  PDGNode temp;
                         	  while (iter1.hasNext())//��PDG�ڵ���б���
                         	  {
                         		  temp = iter1.next();
                         		  i=0;
                         		  Iterator<PDGNode> iter=localArrayList.iterator();
                         		  while (iter.hasNext())
                         		  {
                         			  PDGNode tempPDG=iter.next();
                         			  if (mutablePDG.dependentOn(temp,tempPDG))
                         				  //�����Ƿ���ߴ��ڡ�������������ϵ------�������֮���ִ�й�ϵ����
                         	            {//dependentOn-->determine if node1 is control-dependent on node2 in this PDG
                         	              i = 1;
                         	              break;
                         	            }
                         		  }
                                   
                         		  if (i == 0)//�����ڵ��ú����𣿽�������ʶ��-------------����ʶ��ʲô�أ����i=0����û�б�������warning report�С�
                                   {
                         			  //������ȥ���ظ���
                                     boolean figer=true;

     	                            if(figer){
     	                                vh_data.add(new VH_data(sootMethod.getSignature()));//���оͰ����������ͷ�����
     	                                //System.out.println("Find the part2!");
     	                            }
                                   }
                         	  }
                         	  /****�Լ���2***/
                         	  //System.out.println("Start ����2");
                         	//�ڶ������Ͻ��б���
                         	  Iterator<PDGNode> iter2 = localArrayList2.iterator();
                               while (iter2.hasNext())
                               {
                                 temp = iter2.next();
                                 i = 0;
                                 Iterator<PDGNode> iter=localArrayList.iterator();
                                 while (iter.hasNext())
                                 {
                                 	PDGNode tempPDG =iter.next();
                                   if (mutablePDG.dependentOn((PDGNode)temp,tempPDG))//�ж������Ƿ����������ϵ//mutablePDG//((ProgramDependenceGraph)sootclass)
                                   {
                                     i = 1;
                                     break;
                                   }
                                 }

                                 if (i == 0)//��������Ϊ0----����Ϣ���д洢
                                 {
                                     boolean figer=true;
                                     for(VH_data part:vh_data){
                                     	if((part.getMethod_name()).equals(sootMethod.getSignature())){
                                     		figer=false;
                                     		break;
                                     	}
                                     }
                                     if(figer){
                                     vh_data.add(new VH_data(sootMethod.getSignature()));//���оͰ����������ͷ�����
                                     //System.out.println("Find the part2!");
                                     }
                                   }
                               }
                           }         
                       }
                    }
	
	public static boolean DiskCache_compress(SootMethod sootmethod){
		//if(sootmethod.getName().contains("compress")){
		String compress="<android.graphics.Bitmap: boolean compress(android.graphics.Bitmap$CompressFormat,int,java.io.OutputStream)>";
		if(sootmethod.getSignature().contains(compress)){		
			return true;
		}
		return false;
		
	}


}
