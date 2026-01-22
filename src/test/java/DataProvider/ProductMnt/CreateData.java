package DataProvider.ProductMnt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import Common.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CreateData {

    @DataProvider(name = "createData")
    public Object[][] createData() {

        String excelFile = "src/test/resources/TestData/TestData_ModuleProduct.xlsx";
        List<Object[]> testData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("CreateProduct");
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet 'CreateProduct' trong file Excel!");
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
                String category = ExcelUtils.getCellValue(row.getCell(6));
                String productType = ExcelUtils.getCellValue(row.getCell(7));
                String vendor = ExcelUtils.getCellValue(row.getCell(8));
                String collection = ExcelUtils.getCellValue(row.getCell(9));
                String tags = ExcelUtils.getCellValue(row.getCell(10));
                String shippingClass = ExcelUtils.getCellValue(row.getCell(11));
                String personalize = ExcelUtils.getCellValue(row.getCell(12));
                String status = ExcelUtils.getCellValue(row.getCell(13));
                String shortDescription = ExcelUtils.getCellValue(row.getCell(14));
                String SEOEngineTitle = ExcelUtils.getCellValue(row.getCell(15));
                String SEOEngineDescription = ExcelUtils.getCellValue(row.getCell(16));
                String SEOEngineURLHandle = ExcelUtils.getCellValue(row.getCell(17));
                String price = ExcelUtils.getCellValue(row.getCell(18));
                String comparePrice = ExcelUtils.getCellValue(row.getCell(19));
                String cost = ExcelUtils.getCellValue(row.getCell(20));
                String SKU = ExcelUtils.getCellValue(row.getCell(21));
                String expectedMsg = ExcelUtils.getCellValue(row.getCell(22));

                testData.add(new Object[]{
                        id, title, description,	category, productType, vendor, collection, tags, shippingClass, personalize, status, shortDescription,
                        SEOEngineTitle, SEOEngineDescription, SEOEngineURLHandle, price, comparePrice, cost, SKU, expectedMsg
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.toArray(new Object[0][]);
    }
}
