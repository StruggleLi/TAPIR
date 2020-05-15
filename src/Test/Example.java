package Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 简单的待分析类
 * @author wang
 *
 */
class Example2 {
	int a;
	int b;
	static int c;

	public Example2(int a, int b) {
		this.a = a;
		this.b = b;
		//Example2.numCount();
	}

	public int getSum() {
		return a + b;
	}

	public int getDifferenceAbs() {
		return Math.abs(a - b);
	}

	static void numCount() {
		++c;
		System.out.println(c);
	}
}

//主类
public class Example {
	int a;
	int b;
	static int c;

	public Example(int a, int b) {
		this.a = a;
		this.b = b;
		//Example.numCount();
	}

	public int getSum() {
		getDifferenceAbs();
		return (a + b)/2;
	}

	public int getDifferenceAbs() {
		return Math.abs(a - b);
	}

	static void numCount() {
		++c;
		System.out.println(c);
	}

	public static void main(String[] args) {
//		System.out.println("Example");
//		Example app = new Example(1, 2);
//		Example app2 = app;
//		Example app3 = new Example(2, 3);
//		app2 = app3;
//		app3 = app;
//
//		System.out.println("Example2");
		Example2 app11 = new Example2(1, 2);//会自动调用方法numCount()
		Example2 app12 = app11;
//		Example2 app13 = new Example2(2, 3);
//		app12 = app13;
//		app13 = app11;

		int a=2;
		int b=2;
		int c=0;
		int[] aa={1,2,3,4,5,6,7};
		Set<Integer> ab=new HashSet<Integer>();
		Map<Integer,Integer> ab2=new HashMap<Integer,Integer>();
		ab2.put(1,1);
		ab2.put(2,2);
		ab.add(1);
		ab.add(2);
		for(int i=0;i<ab.size() ;i++){
			a=b+c;
		}

		for(int i=0;i<ab2.size() ;i++){
			a=b+c;
		}

		for(int i=0;i<5;i++){
			a=b+c;

		}


		for(int i=0;i<aa.length ;i++){
			a=b+c;
		}
		

		for(int a1: aa){
			a=b+c;
		}
		

		int d=5;
		if(a>=2){
			c=a+b;
			c=app12.getSum();
			if(a>=3){
				c=a+b+d;

			}
		}
		else{
			c=a/b;
		}

		if(c==0){
			c=a*b;
			c=app12.getSum();
		}
		Example ex=new Example(1,2);
		ex.getSum();
		(new Thread(new MyRunnable())).start();    //测试Runnable多线程分析
		ThreadTest t = new ThreadTest();           //测试Thread多线程分析
	    t.start();

	}
}

class MyRunnable implements Runnable {

    public void run() {
        foo(); // let's say this is a heavy api
    }
    void foo(){
    	System.out.println("the child thread is run. foo");
    }

}



class ThreadTest extends Thread{
	  private int ticket = 100;
	  public void run(){
	    foo_thread();
	  }
	  public void foo_thread(){
		  System.out.println("the child thread is run. foo_thread");
	  }
	}
















