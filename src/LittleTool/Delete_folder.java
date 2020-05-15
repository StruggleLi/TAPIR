/**
 * 
 */
package LittleTool;

import java.io.File;

public class Delete_folder {
    /**
     * ɾ����Ŀ¼
     * @param dir ��Ҫɾ����Ŀ¼·��
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
     * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
     * @param dir ��Ҫɾ�����ļ�Ŀ¼
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
    	//ɾ�����ļ����е��ļ���
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Ŀ¼��ʱΪ�գ�����ɾ��
    	if (dir.isDirectory()) {
    		System.out.println("Exist Android dir");
    	}
        return dir.delete();
    }
    /**
     *����
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