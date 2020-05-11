package main.java.yixue.icse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


//import android.util.Log;
//import org.json.JSONException;
//import org.json.JSONObject;

//add
import java.util.Random;


/**
 * stub methods for instrumentation, then Xposed will rewrite the actual method
 * 意思是下面的这些方法在后面会被进一步修改
 * 
 * @author felicitia
 *
 */
public class Proxy {

	/**
	 * nodes: the node ids that should be checked, 
	 * and prefetch if every value is known
	 * @param nodes
	 */

	//define a global variable count
	private static int count=0;

	public static void triggerPrefetch(String body, String sig, String nodes, int Prefetch_Method){
		System.out.println("new prefetch :)");
	}
	
	public static InputStream getInputStream(URLConnection urlconn) throws IOException {
		System.out.println("new getInputStream :)");
		return null;
	}
	
	public static int getResponseCode(HttpURLConnection urlConn) throws IOException{
		System.out.println("new getResponseCode :)");
		return -1;
	}

	public static Object getContent(URLConnection urlconn) throws IOException {
		System.out.println("new getContent :)");
		return null;
	}
	
	public static InputStream openStream(URL url) throws IOException{
		System.out.println("new openStream :)");
		return null;
	}
	
//	public static String getURLfromJson(JSONObject obj){
//		if(obj.has("dataURL")){
//			try {
//				return obj.getString("dataURL");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
	
//	public static String getURLfromJsonParent(JSONObject obj){
//		try {
//			String itemID = obj.getString("loadScreenWithItemId");
//			String url = "https://www.buzztouch.com/applications110313/JA5479375BD29396CCCA51C9D/documents/customHTML_"+itemID+".html";
//			return url;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

	public static String returnBigURL(String url){
		if(url.contains("http://www.hablema.com/greyhatmoders/appl/wallpaper/love/thumb/")){
			return url.replace("thumb/", "");
		}
		return null;
	}
	
	public static void sendDef(String body, String sig, String value, String nodeId, int index,
			String pkgName) {
		System.out.println("Proxy: sendDef :)");
		System.out.println("args = "+ value+"\t"+nodeId+"\t"+index+"\t"+pkgName);
	}

	public static String getResult() {//public static String getResult(String urlStr) {
		System.out.println("Proxy: getResult :)");
		return null;
	}

	public static void printTimeStamp(String sig){
		Long time = System.currentTimeMillis(); 
		System.out.println("Yixue: timestamp = "+time.toString());
	}
	
	public static void printTimeDiff(String body, String sig, long timeDiff){
		System.out.println("printTimeDiff:"+timeDiff);
		//Log.i("printTimeDiff","time: "+timeDiff);
	}
	public static void printMemoryUsage(String body, String sig, long memoryUsage){
		System.out.println("printMemoryUsage:"+memoryUsage);
		//Log.i("printMemoryUsage","memory: "+memoryUsage);
	}
	public static void printOutUrl(String body, String sig, String url){
		System.out.println(body+"###"+sig+"###"+url);
	}
	public static void printUrl(String body, String sig, String value){
		System.out.println(body+"###"+sig+"###"+value);
	}

	public static long getTimeStamp(){
		long time=System.currentTimeMillis();
		long natime=System.nanoTime();
		System.out.println("getTimeStamp:"+String.valueOf(time)+"nanoTime: "+String.valueOf(natime));
		//Log.i("getTimeStamp",String.valueOf(time));
		return time;
	}

	public static String getUrlStr(){
		String url="abcd";
		String[] url_set={
				"/storage/emulated/0/DCIM/images/100-133.jpg",
				"/storage/emulated/0/DCIM/images/200-267.jpg",
				"/storage/emulated/0/DCIM/images/300-401.jpg",
				"/storage/emulated/0/DCIM/images/400-534.jpg",
				"/storage/emulated/0/DCIM/images/500-668.jpg",
				"/storage/emulated/0/DCIM/images/600-802.jpg",
				"/storage/emulated/0/DCIM/images/700-935.jpg",
				"/storage/emulated/0/DCIM/images/800-1069.jpg",
				"/storage/emulated/0/DCIM/images/900-1203.jpg",
				"/storage/emulated/0/DCIM/images/1000-1336.jpg",
				"/storage/emulated/0/DCIM/images/1100-1470.jpg",
				"/storage/emulated/0/DCIM/images/1200-1604.jpg",
				"/storage/emulated/0/DCIM/images/1300-1737.jpg",
				"/storage/emulated/0/DCIM/images/1400-1871.jpg",
				"/storage/emulated/0/DCIM/images/1500-2005.jpg",
				"/storage/emulated/0/DCIM/images/1600-2138.jpg",
				"/storage/emulated/0/DCIM/images/1700-2272.jpg",
				"/storage/emulated/0/DCIM/images/1800-2406.jpg",
				"/storage/emulated/0/DCIM/images/1900-2540.jpg",
				"/storage/emulated/0/DCIM/images/2000-2673.jpg",
				"/storage/emulated/0/DCIM/images/2100-2807.jpg",
				"/storage/emulated/0/DCIM/images/2200-2941.jpg",
				"/storage/emulated/0/DCIM/images/2300-3074.jpg",
				"/storage/emulated/0/DCIM/images/2400-3208.jpg",
				"/storage/emulated/0/DCIM/images/2500-3342.jpg",
				"/storage/emulated/0/DCIM/images/2600-3475.jpg",
				"/storage/emulated/0/DCIM/images/2700-3609.jpg",
				"/storage/emulated/0/DCIM/images/2800-3743.jpg",
				"/storage/emulated/0/DCIM/images/2900-3876.jpg",
				"/storage/emulated/0/DCIM/images/3000-4010.jpg",
				"/storage/emulated/0/DCIM/images/3100-4144.jpg",
				"/storage/emulated/0/DCIM/images/3200-4277.jpg",
				"/storage/emulated/0/DCIM/images/3300-4411.jpg",
				"/storage/emulated/0/DCIM/images/3400-4545.jpg",
				"/storage/emulated/0/DCIM/images/3500-4679.jpg",
				"/storage/emulated/0/DCIM/images/3600-4812.jpg",
				"/storage/emulated/0/DCIM/images/3700-4946.jpg",
				"/storage/emulated/0/DCIM/images/3800-5080.jpg",
				"/storage/emulated/0/DCIM/images/3900-5213.jpg",
				"/storage/emulated/0/DCIM/images/4000-5347.jpg",
				"/storage/emulated/0/DCIM/images/4100-5481.jpg",
				"/storage/emulated/0/DCIM/images/4200-5614.jpg",
		};

		/*
		Random rand = new Random();
		for(int i=0; i<10; i++) {
			url=url_set[rand.nextInt(50) + 1];
			System.out.println(url);
		}
		*/
		url=url_set[count];
		count++;
		if(count==url_set.length){
			count=0;
		}
		System.out.println("imagePath:"+url);
		//Log.i("imagePath",url);
		return url;
	}

	public static long getMemoryUsage(){
		return (Runtime.getRuntime().totalMemory());
	}

	public static void printResponseCode(String body, String sig, int responseCode){
		System.out.println(body+"###"+sig+"###"+responseCode);
	}
}
