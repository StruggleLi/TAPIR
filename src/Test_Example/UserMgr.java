package Test_Example;
public class UserMgr {
	public static void main(String[] args){
	  int a=1;
	  int b=2;
	  int c=a+b;
	  int[] aa={1,2,3,4,5};
	  a=aa[2];   //·ÃÎÊÊý×é
	  a=aa[1]+aa[2];
	  System.out.println(a);
	  String str="admin";
	  UserMgr um=new UserMgr();
	  a=um.getRole(str);
	  System.out.println("hello");
	  System.out.println("hello");
	 /* UserTwo ut=new UserTwo();
	  System.out.println(ut.name);
	  str=ut.name;
	  a=um.getRole(str);
	  a=ut.comput();*/
	}

    public int getRole(String username) {
        if (username.equals("admin")) {
            return 1;
        }

        if (username.equals("system")) {
            return 2;
        }

        return -1;
    }


}