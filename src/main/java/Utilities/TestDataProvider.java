package Utilities;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.HashMap;
import java.util.Map;

public class TestDataProvider {

    private Map<String, TestData> testDataMap = new HashMap<>();

    public void loadTestData(Sheet sheet) {
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            String usecase = ExcelReaderUtil.getCellValue(row.getCell(0));
            String payload = ExcelReaderUtil.getCellValue(row.getCell(1));
            String url = ExcelReaderUtil.getCellValue(row.getCell(2));

            TestData testData = new TestData(usecase, payload, url);
            testDataMap.put(usecase, testData);
        }
    }

    public TestData getTestData(String usecase) {
        return testDataMap.get(usecase);
    }
}
