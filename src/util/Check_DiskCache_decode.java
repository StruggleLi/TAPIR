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
 * 排除一种情况：从DiskCache中获取图片流，从这里获得的图片流就没有必要进行大小设置了。
 * ：
 * 1）分析是否使用了DiskCache
 * 2）分析分析decode的来源于DiskCache有关。--------按照word中的分析，完全可以通过刘博士的depend那样的方法来分析两行语句之间的依赖关系
 * 
 * 3）尝试分析decode与if判断语句分离的情况。分析调用decode所在方法所在的语句中是否存在。
 * 
 * 先检测通过，然后再分析复杂的情况。
 * 
 * 
 * 测试程序：owncloud
 *
 */
public class Check_DiskCache_decode {
	static Set<VH_data> vh_data=new HashSet<VH_data>();    //存储分析结果
	
	/**
	 * Try to find the frequency oprated methods
	 */
	public static void check_DiskCache(SootClass sootclass,SootMethod sootMethod,String decode){
		
		            Scene.v().loadClassAndSupport(sootclass.getName());

                    Exception localObject1=null;
                    UnitGraph graph = null;
                    HashMutablePDG mutablePDG=null;
                    PDGNode pdgNode=null;              //PDG-》program dependent graph

                    //三个集合，节点存储
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
                       {  mutablePDG = new HashMutablePDG(graph);//获得getView()方法中的全部PDGNode，即程序依赖图
                       }
                       catch (Exception localException1)
                       {  System.err.println("[error] " + localException1.getMessage());
                       }

                	   if (mutablePDG == null)//如果PDG没有成功获得，进行信息输出
                       {
                          System.err.println("[error] program dependency graph construction failure");//程序依赖图构建失败
                       }

	               Iterator<Object> localObject3 = mutablePDG.getNodes().iterator();//得到PDGNode节点集合

                	  //对程序依赖图中的节点进行遍历
                       while ((localObject3).hasNext()) {
                    	   pdgNode=(PDGNode) localObject3.next();
                    	   
                    	   if (pdgNode instanceof PDGNode){
                    		   System.out.println("PDGNode:    "+pdgNode.toString());
                    		   
                    		   //这里对应判断检测DiskCache中是否包含Bitmap
                    	        if ((pdgNode.getType() == PDGNode.Type.CFGNODE)
                    	             &&((pdgNode.toString().contains("== null"))||pdgNode.toString().contains("!= null"))){
                    	   	             localArrayList.add(pdgNode);//如果是，添加到集合----获得所有的CFG节点？
                    	                       			   //测试----------------》对节点信息进行输出
                    	                       			   //System.out.println("FindRecycledTtemCheckingNodes:       "+pdgNode.toString());
                    	                       		   }

                    	        if (pdgNode.getType() == PDGNode.Type.REGION){//获得类型，REGION代表的普通语句，非判断
                    	             String pdgNodeString=pdgNode.toString();//获得节点信息

                    	             String[] b2 = {"android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)"};//存储函数名称
                    	             String[] arrayOfStringb2 = b2;//复制数组b内容(成功)
                    	             String str;
                    	  	         for (int j = 0; j <= 0; j++)
                    	                 {
                    	                     str = arrayOfStringb2[j];//取出数组中的内容--这里是decode方法

                    	                     if (pdgNodeString.contains(str)) {//看看是否是数组中保存的类
                    	                       	localArrayList1.add(pdgNode);//如果是，将PDG节点添加到集合1
                    	                       	//System.out.println("FindDecodeNodes :       "+pdgNodeString);
                    	                     }
                    	                 }

/**待填写**/                    	                 String[] c2 = { "android.view.View findViewById(int)" };//存储函数名
												String[] c22={"java.io.InputStream"};


                    	             String[] arrayOfStringc2 = c2;//复制数组c中的内容    String[] c = { "android.view.View findViewById(int)" };//存储函数名
                    	         	 for (int j = 0; j <= 0; j++)
                                     {
                    	                 str = arrayOfStringc2[j];
                    	                 if (pdgNodeString.contains(str)) {
                    	                     localArrayList2.add(pdgNode);//如果是，将PDG节点添加到集合2
                    	                     //System.out.println("BufferedInputStream :       "+pdgNode.toString());
                    	                     }
                    	             }
                    	                       		  }
                    	                       	  }
                    	   //System.out.println("the size of localArrayList1,2,3:   "+localArrayList.size()+"   "+localArrayList1.size()+"   "+localArrayList2.size());

                    	   //如果没有if等判断语句-直接就认为该decode没有进行cache。
                           if(localArrayList1.size()== 0){
                         	  vh_data.add(new VH_data(sootMethod.getSignature()));
                           }
                            if (localArrayList1.size()!= 0){//看看PDG节点列表为空
                         	  //System.out.println("Start 集合1");
                         	  /****对集合1***/
                         	  Iterator<PDGNode> iter1=localArrayList1.iterator();
                         	  int i;
                         	  PDGNode temp;
                         	  while (iter1.hasNext())//对PDG节点进行遍历
                         	  {
                         		  temp = iter1.next();
                         		  i=0;
                         		  Iterator<PDGNode> iter=localArrayList.iterator();
                         		  while (iter.hasNext())
                         		  {
                         			  PDGNode tempPDG=iter.next();
                         			  if (mutablePDG.dependentOn(temp,tempPDG))
                         				  //分析是否二者存在“控制依赖”关系------》即语句之间的执行关系依赖
                         	            {//dependentOn-->determine if node1 is control-dependent on node2 in this PDG
                         	              i = 1;
                         	              break;
                         	            }
                         		  }
                                   
                         		  if (i == 0)//这是在调用函数吗？进行条件识别？-------------是在识别什么呢？如果i=0，且没有被保存在warning report中。
                                   {
                         			  //下面是去除重复。
                                     boolean figer=true;

     	                            if(figer){
     	                                vh_data.add(new VH_data(sootMethod.getSignature()));//其中就包含了类名和方法名
     	                                //System.out.println("Find the part2!");
     	                            }
                                   }
                         	  }
                         	  /****对集合2***/
                         	  //System.out.println("Start 集合2");
                         	//第二个集合进行遍历
                         	  Iterator<PDGNode> iter2 = localArrayList2.iterator();
                               while (iter2.hasNext())
                               {
                                 temp = iter2.next();
                                 i = 0;
                                 Iterator<PDGNode> iter=localArrayList.iterator();
                                 while (iter.hasNext())
                                 {
                                 	PDGNode tempPDG =iter.next();
                                   if (mutablePDG.dependentOn((PDGNode)temp,tempPDG))//判定二者是否存在依赖关系//mutablePDG//((ProgramDependenceGraph)sootclass)
                                   {
                                     i = 1;
                                     break;
                                   }
                                 }

                                 if (i == 0)//如果被标记为0----对信息进行存储
                                 {
                                     boolean figer=true;
                                     for(VH_data part:vh_data){
                                     	if((part.getMethod_name()).equals(sootMethod.getSignature())){
                                     		figer=false;
                                     		break;
                                     	}
                                     }
                                     if(figer){
                                     vh_data.add(new VH_data(sootMethod.getSignature()));//其中就包含了类名和方法名
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
