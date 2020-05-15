/**
 * 
 */
package LittleTool;

import java.io.File;

public class Delete_folder {
    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
    	//删除子文件夹中的文件夹
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
    	if (dir.isDirectory()) {
    		System.out.println("Exist Android dir");
    	}
        return dir.delete();
    }
    /**
     *测试
     */
    public static int delete_android_folder(String path) {

        String newDir2 = path;
        boolean success = deleteDir(new File(newDir2));
        if (success) {
            System.out.println("Successfully deleted populated directory: " + newDir2);
            return 1;
        } else {
            System.out.println("Failed to delete populated directory: " + newDir2);
            return 0;
        }     
    }
}