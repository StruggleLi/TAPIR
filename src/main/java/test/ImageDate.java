package main.java.test;

import java.io.*;

public class ImageDate {
    public static void main(String[] args) {

        File file = new File("/Users/wenjieli/My-floder/images/1.jpg");
        File file1 = new File("/Users/wenjieli/My-floder/images/logcat.txt");
        String site="/Users/wenjieli/My-floder/images/1.jpg";
        readFile(file1,site);

        //System.out.println(readFile(file,site));

    }

    public static String readFile(File file,String site) {


        try{
            FileInputStream fis = new FileInputStream(file);//读取文件为输入流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] b=new byte[1024];
            int i=0;
            while((i=fis.read(b))!=-1){
                baos.write(b,0,b.length);
            }

            String requestString = new String(baos.toByteArray());//这里转化为字符串
            System.out.println("requestString:----"+requestString);

            String[] headers = requestString.split("\n");
            for (int j = 0; j < headers.length; j++) {
                if (headers[j].startsWith("Host")) {
                    String[] hostHeader = headers[j].split(":");
                    if (hostHeader.length > 1) {
                        System.out.println("hostHeader:"+hostHeader[1]);
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }





//-----------
        StringBuilder result = new StringBuilder();

        try {

            BufferedReader br = new BufferedReader(new FileReader(file));

            String s = null;

            while((s = br.readLine()) != null) { //一次读一行内容

                result.append(System.lineSeparator() + s);

            }

            br.close();

        } catch (FileNotFoundException e) {

// TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

// TODO Auto-generated catch block

            e.printStackTrace();

        }


        return result.toString();

    }
}
