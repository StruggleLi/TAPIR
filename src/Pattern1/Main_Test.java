/**
 *对工具进行了重构---2016/11/21
 *修改了对于pattern3和4的分析策略
 *
 *逆向分析的一种替代方法：
 *关于Frequency:  遍历所有的getview和onDraw，然后以这两类方法为起点进行reachable，看看是否与目标decode存在联系。如果存在，则目标涉及循环。
 *   时间开销比较：  逆向分析：   5分钟
 *              reachable：  3分钟              可能需要先事先统计decode，集中处理才能更加的节约时间。decode有多少个，就能缩小多少倍
 *
 *关于UI：     能够很快遍历完，因为不会涉及子线程中包含的库方法。
 *
 *.dex文件解压之后的确可以得到res文件夹。
 *
 *对工具进行了重构---2017/01/16
 *增加对API的分析，
 *增加对checkpoint的分析（createBitmap）。
 *删除无用的XML报告信息
 *
 */
package Pattern1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import Pattern3.Check_Cache;
import LittleTool.Delete_folder;
import Test.Test;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;
import util.Thread1;


/**
 * @author liwenjie
 * 增加功能：
 * （1）识别getview, ondraw, gridview，循环体。分析其是否与decode存在联系。当检测到decode没有进行Pattern1或pattern3优化
 * 的时候，要分析是否与这些存在关联，并报告。
 *
 * 不全功能：
 * 1）pattern4只相关位置信息的输出；
 * 2）
 *
 *
 */
public class Main_Test {
	public static boolean thread_stop_figer=false;

	public static void main(String [] args) throws Exception{
		//环境设置
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_app(true);
		Options.v().set_whole_program(true);
		Options.v().set_keep_line_number(true);
		Options.v().set_output_format(Options.output_format_jimp);//对中间码进行设定为jimp

		/*
		 * 路径一定要定位到包名的上一层，如果程序初始是在eclipse中写的，则一般是bin下就可以了
		 * 如果例如好像在Ant编译的，则在classes下面*/

		int i=999;
		//测试third library
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\UIL";

		//Confirm analysis
		//String  strClassPath1="D:\\11-confirm analysis\\AntennaPod-0995-dex2jar";
		//String strClassPath1="D:\\11-confirm analysis\\APhotoManager-0.4.1.150911\\app\\build\\intermediates\\classes\\debug";
		//String strClassPath1="D:\\11-confirm analysis\\WordPress-vanilla-release-dex2jar";
		//String strClassPath1="D:\\11-confirm analysis\\NewPipe-0.6.1\\app\\build\\intermediates\\classes\\debug";
		//String strClassPath1="D:\\11-confirm analysis\\OpenNoteScanner-1.0.11-dex2jar";
		//String strClassPath1="D:\\11-confirm analysis\\com.seafile.seadroid2_63-dex2jar";
		String strClassPath1="D:\\11-confirm analysis\\vanilla-1.0.34\\build\\intermediates\\classes\\debug";


		//APK文件分析
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-08owncloud.android_20100100-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-06-13newpipe_18-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-08nori_8-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-06-25antennapod_1060102-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-13sufficientlysecure.viewer_2777-dex2jar";//document-viewer
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2015-12-21kore_13-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-09-13passandroid_323-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-08-03kiss_85-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-07-29comics_7-dex2jar";//   bubble
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-07-28satstat_3010-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-07-28androFotoFinder_19-dex2jar";



		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-07-07newsblur_129-dex2jar";
		//String  strClassPath1="D:\\11-confirm analysis\\com.newsblur_132-dex2jar";                 //update  1017-1-4
		//String  strClassPath1="D:\\updata-APK\\com.newsblur_132-2016-12-05-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\opennotescanner_27-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\cyclestreets_15-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2016-03-08episodes_12-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2014-10-08-opds_scanner_102001-dex2jar";//FBReaderJ
		//String  strClassPath1="D:\\transf\\Jar-9-16\\trikita.slide_4-dex2jar";//Slide
		//String  strClassPath1="D:\\transf\\Jar-9-16\\lightning_88-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-16\\redditslide_244-dex2jar";//Slide1
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-16easy_xkcd_134-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-02owncloudnewsreader_113-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-08-26aslfms_40-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-08-16vanilla_10430-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-19\\2016-09-09transistor_30-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2013-04-08squeezer_13-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2014-03-03muzei_1008-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2015-01-11sudoq_11-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2012-11-30-geopaparazzi_39-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2015-03-13aizoban_27-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\mapever_1-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-16\\MAL_14-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-16\\mpdroid_54-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-09-10mimanganu_54-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-18\\2016-08-16vanilla_10430-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\cameratimer_6-dex2jar";//-over
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2016-06-20-uhabits_22-dex2jar";//-over

		//11月1日
		//String  strClassPath1="D:\\transf\\Jar\\2016-10-29-twidere_213-dex2jar";//Twidere--无法构建调用图
		//String  strClassPath1="D:\\transf\\Jar\\2016-10-11-redreader_75-dex2jar";//redreader
		//String  strClassPath1="D:\\transf\\Jar\\2016-10-29-steamgifts_1004501-dex2jar";//SteamGifts
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-18andstatus.app_191-dex2jar";//andstatus---无法构建调用图
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-27-weather_31-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2015-10-30-chanapps.four.activity_90-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-08-goblim_4-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-05-17-kontalk_124-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2014-10-20-repay.android_25-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2014-04-12wordpress.android_103-dex2jar";//wordpress
		//String  strClassPath1="D:\\transf\\Jar\\2016-10-28-conversations_180-dex2jar";//conversations
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-08-createpdf_1-dex2jar";//
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-03seadroid2_60-dex2jar";//seadroid2
		//String  strClassPath1="D:\\transf\\Jar\\2016-10-06-chan_56-dex2jar";//clover
		//String  strClassPath1="D:\\transf\\Jar\\2016-07-29-nkanaev.comics_7-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-08-02messenger_8213-dex2jar";//循环没有执行完
		//String  strClassPath1="D:\\transf\\Jar\\2016-09-08QKSMS_135-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-05-23-transdroid.full_227-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-05-11-bienvenidoainternet.app_11-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar\\2016-05-05-forpdaplus_568-dex2jar";     //4pdaClient



		//String  strClassPath1="D:\\transf\\Jar\\2014-10-23-starslinger.demo_17301504-dex2jar";//不包含decode





		/*编译通过的APP*/
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\APhotoManager-FDroid\\debug";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\seadroid-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\NewPipe-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\nori-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\bubble";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\Clover";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\document-viewer-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\episodes-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\kiss";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\OpenNoteScanner-master";
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\passandroid";  //没有编译成功

		 //基础测试
		//String strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\displayingbitmaps";


		String file_name1="1111111111111-result-test";


		    //String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\Clover-dev";//conversations--delete Android
		    //String  strClassPath1="D:\\11-Paper-AppSourceSet\\Clover-dev1\\Clover-dev\\Clover\\app\\build\\intermediates\\classes\\normal\\debug";//conversations--complement


//----------------------feedback
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\Easy_xkcd"; //over
		//String  strClassPath1="D:\\11-Paper-AppSourceSet\\1Comple-result\\OpenNoteScanner-master"; //over


//测试子线程
		//String  strClassPath1="D:\\java-new-workspace\\ZZZsoot_Test1\\bin";


		    //String  strClassPath1="D:\\transf\\Jar\\2016-08-05audiobook_126-dex2jar";
		    //  String  strClassPath1="D:\\transf\\Jar-9-18\\2013-04-08squeezer_13-dex2jar";  //暂时死循环--包含MC


		     // String  strClassPath1="D:\\transf\\Jar-9-17\\2016-05-01runnerup_14000053-dex2jar";
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2016-01-23-mpp_6534-dex2jar";  //依旧死循环--包含MC
		//String  strClassPath1="D:\\transf\\Jar-9-17\\2015-05-12-QuickLyric_47-dex2jar";  //依然死循环--包含MC
		//String  strClassPath1="D:\\transf\\Jar-9-16\\sickstache_43-dex2jar";  //over



		//String strClassPath1="D:\\transf\\Jar-9-17\\2016-08-21-openflood_9-dex2jar";//无decode


			System.out.println(strClassPath1+"/android");

			Delete_folder.delete_android_folder(strClassPath1+"/android");   //删除待分析文件中的Android基础类库android文件夹


		ArrayList<String> processDir = new ArrayList<String>();

		processDir.add(strClassPath1);
		Options.v().set_process_dir(processDir);
		System.out.println(strClassPath1);

		System.out.println("Scene.v().getSootClassPath():     "+Scene.v().getSootClassPath());

		Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ";" + strClassPath1);   //原来的

		Scene.v().loadNecessaryClasses();//加载必要的类

		Data1 data=new Data1();

		Iterator<SootClass> scIterator1 = Scene.v().getApplicationClasses().iterator();//获得应用程序中的类
		Iterator<SootClass> scIterator2 = Scene.v().getApplicationClasses().iterator();//获得应用程序中的类
		Iterator<SootClass> scIterator3 = Scene.v().getApplicationClasses().iterator();//获得应用程序中的类
		Iterator<SootClass> scIterator4 = Scene.v().getApplicationClasses().iterator();//获得应用程序中的类


//调用子线程进行文件大小监控
		//Thread1 fiel_monitor=new Thread1("D:\\transf\\Jar\\"+i+"---"+"owncloud.android_20100100-dex2jar");
		Thread1 fiel_monitor=new Thread1("C:\\Users\\Dell\\Desktop\\output.txt");
		fiel_monitor.start();


		//Pattern 1
		Analysis_Bitmap bitmap_analysis=new Analysis_Bitmap();
		bitmap_analysis.StartAnalysis(scIterator1,scIterator2,scIterator3,scIterator4,data,file_name1,i);//开始分析

		//Test
//		Iterator<SootClass> scIterator_Test = Scene.v().getApplicationClasses().iterator();
//		Test.TestAnalysis(scIterator_Test);




		System.out.println("The Bitmap Analysis is Finished!");

	}

}












