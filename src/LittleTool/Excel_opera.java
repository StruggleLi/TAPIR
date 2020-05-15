package LittleTool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class Excel_opera {

    public static void main(String[] args) throws Exception {
       File file = new File("D:\\transf\\fdroid.xlsx");
       String[][] result = getData(file, 1);
       int rowLength = result.length;
       for(int i=0;i<rowLength;i++) {
           for(int j=0;j<result[i].length;j++) {
              System.out.print(result[i][j]+"\t\t");
           }
           System.out.println();
       }

    }
    /**
     * ��ȡExcel�����ݣ���һά����洢����һ���и��е�ֵ����ά����洢���Ƕ��ٸ���
     * @param file ��ȡ���ݵ�ԴExcel
     * @param ignoreRows ��ȡ���ݺ��Ե�������������ͷ����Ҫ���� ���Ե�����Ϊ1
     * @return ������Excel�����ݵ�����
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getData(File file, int ignoreRows)
           throws FileNotFoundException, IOException {
       List<String[]> result = new ArrayList<String[]>();
       int rowSize = 0;
       BufferedInputStream in = new BufferedInputStream(new FileInputStream(
              file));
       // ��HSSFWorkbook
       POIFSFileSystem fs = new POIFSFileSystem(in);
       HSSFWorkbook wb = new HSSFWorkbook(fs);
       HSSFCell cell = null;
       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
           HSSFSheet st = wb.getSheetAt(sheetIndex);
           // ��һ��Ϊ���⣬��ȡ
           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
              HSSFRow row = st.getRow(rowIndex);
              if (row == null) {
                  continue;
              }
              int tempRowSize = row.getLastCellNum() + 1;
              if (tempRowSize > rowSize) {
                  rowSize = tempRowSize;
              }
              String[] values = new String[rowSize];
              Arrays.fill(values, "");
              boolean hasValue = false;
              for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                  String value = "";
                  cell = row.getCell(columnIndex);
                  if (cell != null) {
                     // ע�⣺һ��Ҫ��������������ܻ��������
                     cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                     switch (cell.getCellType()) {
                     case HSSFCell.CELL_TYPE_STRING:
                         value = cell.getStringCellValue();
                         break;
                     case HSSFCell.CELL_TYPE_NUMERIC:
                         if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date != null) {
                                value = new SimpleDateFormat("yyyy-MM-dd")
                                       .format(date);
                            } else {
                                value = "";
                            }
                         } else {
                            value = new DecimalFormat("0").format(cell
                                   .getNumericCellValue());
                         }
                         break;
                     case HSSFCell.CELL_TYPE_FORMULA:
                         // ����ʱ���Ϊ��ʽ���ɵ���������ֵ
                         if (!cell.getStringCellValue().equals("")) {
                            value = cell.getStringCellValue();
                         } else {
                            value = cell.getNumericCellValue() + "";
                         }
                         break;
                     case HSSFCell.CELL_TYPE_BLANK:
                         break;
                     case HSSFCell.CELL_TYPE_ERROR:
                         value = "";
                         break;
                     case HSSFCell.CELL_TYPE_BOOLEAN:
                         value = (cell.getBooleanCellValue() == true ? "Y"
                                : "N");
                         break;
                     default:
                         value = "";
                     }
                  }
                  if (columnIndex == 0 && value.trim().equals("")) {
                     break;
                  }
                  values[columnIndex] = rightTrim(value);
                  hasValue = true;
              }

              if (hasValue) {
                  result.add(values);
              }
           }
       }
       in.close();
       String[][] returnArray = new String[result.size()][rowSize];
       for (int i = 0; i < returnArray.length; i++) {
           returnArray[i] = (String[]) result.get(i);
       }
       return returnArray;
    }

    /**
     * ȥ���ַ����ұߵĿո�
     * @param str Ҫ�������ַ���
     * @return ��������ַ���
     */
     public static String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }
}






































///**
// * 
// */
//package LittleTool;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
///**
// * @author Dell
// *
// */
//public class Excel_opera {
//
//	/**
//	 * @param args
//	 */
////	public static void main(String[] args) {
////		// TODO Auto-generated method stub
////		
////		
//		
//		
//		
//		
//		
//		
//		
////		File file = new File("c:\\a.xls");  
////		InputStream in = new FileInputStream(file);  
////		Workbook workbook = Workbook.getWorkbook(in);  
////		//��ȡ��һ��Sheet��  
////		Sheet sheet = workbook.getSheet(0);  
////		  
////		//���Ǽȿ���ͨ��Sheet����������������Ҳ����ͨ���±��������������ͨ���±������ʵĻ���Ҫע���һ�����±��0��ʼ����������һ����  
////		//��ȡ��һ�У���һ�е�ֵ   
////		Cell c00 = rs.getCell(0, 0);   
////		String strc00 = c00.getContents();   
////		//��ȡ��һ�У��ڶ��е�ֵ   
////		Cell c10 = rs.getCell(1, 0);   
////		String strc10 = c10.getContents();   
////		//���ǿ���ͨ��ָ���к��еõ�ָ���ĵ�Ԫ��Cell����  
////		  Cell cell = sheet.getCell(column, row);  
////		  //Ҳ���Եõ�ĳһ�л���ĳһ�е����е�Ԫ��Cell����  
////		  Cell[] cells = sheet.getColumn(column);  
////		  Cell[] cells2 = sheet.getRow(row);  
////		  //Ȼ����ȡÿһ��Cell�е�ֵ  
////		  String content = cell.getContents();  
////
//	}
//
//}