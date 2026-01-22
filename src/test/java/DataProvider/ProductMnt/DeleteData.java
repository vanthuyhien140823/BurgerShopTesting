package DataProvider.ProductMnt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import Common.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DeleteData {

    @DataProvider(name = "deleteData")
    public Object[][] deleteData() {

        String excelFile = "src/test/resources/TestData/TestData_ModuleProduct.xlsx";
        List<Object[]> testData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("DeleteProduct");
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet 'DeleteProduct' trong file Excel!");
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int currentRowNum = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                currentRowNum = row.getRowNum() + 1; // Excel row index (1-based)

                // Bỏ header & row null
                if (currentRowNum == 1 || row == null || ExcelUtils.isRowEmpty(row)) {
                    continue;
                }
                
                String id = ExcelUtils.getCellValue(row.getCell(0));
                String button = ExcelUtils.getCellValue(row.getCell(4));
                String expectedMsg = ExcelUtils.getCellValue(row.getCell(5));
               
                testData.add(new Object[]{
                        id, button, expectedMsg
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.toArray(new Object[0][]);
    }
}
