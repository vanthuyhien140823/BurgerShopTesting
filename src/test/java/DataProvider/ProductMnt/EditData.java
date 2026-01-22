package DataProvider.ProductMnt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import Common.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class EditData {

    @DataProvider(name = "editData")
    public Object[][] editData() {

        String excelFile = "src/test/resources/TestData/TestData_ModuleProduct.xlsx";
        List<Object[]> testData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("EditProduct");
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet 'EditProduct' trong file Excel!");
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
                String title = ExcelUtils.getCellValue(row.getCell(4));
                String description = ExcelUtils.getCellValue(row.getCell(5));
                String shortDescription = ExcelUtils.getCellValue(row.getCell(6));
                String SEOEngineTitle = ExcelUtils.getCellValue(row.getCell(7));
                String SEOEngineDescription = ExcelUtils.getCellValue(row.getCell(8));
                String SEOEngineURLHandle = ExcelUtils.getCellValue(row.getCell(9));
                String expectedMsg = ExcelUtils.getCellValue(row.getCell(10));
                
                testData.add(new Object[]{
                        id, title, description,	shortDescription,
                        SEOEngineTitle, SEOEngineDescription, SEOEngineURLHandle, expectedMsg
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.toArray(new Object[0][]);
    }
}
