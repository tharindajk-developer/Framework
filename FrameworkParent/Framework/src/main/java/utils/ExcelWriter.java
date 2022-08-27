package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by Devdun.k
 */

public class ExcelWriter {

    public static void excelLog(String description, String message, String sheetName, int rowNum,String filePath)throws IOException {
        String fileExtensionName = filePath.substring(filePath.indexOf("."));
        String dest = System.getProperty("user.dir") + filePath;
        String excelData[][] = new String[1][2];
        excelData[0][0] = description;
        excelData[0][1] = message;
        try {
            if(fileExtensionName.equals(".xlsx")) {
                FileInputStream file = new FileInputStream(dest);
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheet(sheetName);
                Cell cell = null;
                //Retrieve the row and check for null
                XSSFRow sheetrow = sheet.getRow(rowNum - 1);
                if (sheetrow == null) {
                    sheetrow = sheet.createRow(rowNum);
                }
                for (int cellNum = 0; cellNum < 2; cellNum++) {
                    cell = sheetrow.createCell(cellNum);
                    cell.setCellValue(excelData[0][cellNum]);
                }
                file.close();
                FileOutputStream outFile = new FileOutputStream(new File(dest));
                workbook.write(outFile);
                outFile.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
