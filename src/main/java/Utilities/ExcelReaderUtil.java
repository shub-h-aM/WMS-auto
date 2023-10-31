package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReaderUtil {

    public static Sheet getSheet(String filePath, String sheetName) throws IOException {
        FileInputStream excelFile = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(excelFile);
        return workbook.getSheet(sheetName);
    }

    public static String getCellValue(Cell cell) {
        cell.setCellType(CellType.STRING); // To handle both numeric and string cell types
        return cell.getStringCellValue();
    }
}
