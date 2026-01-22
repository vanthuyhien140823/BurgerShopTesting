package DataProvider.AccountMnt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import Common.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class RegisterWithOnlyEmailData {

    @DataProvider(name = "registerWithOnlyEmailData")
    public Object[][] registerWithOnlyEmailData() {

        String excelFile = "src/test/resources/TestData/TestData_ModuleAccount.xlsx";
        List<Object[]> testData = new ArrayList<>();

        // CHỈ ĐỊNH ROW MUỐN CHẠY (THEO SỐ DÒNG EXCEL)
        Set<Integer> rowsToRun = new HashSet<>(Arrays.asList(3,4,5,6,7,8,9,10,11,12,13,14,15,16,17));

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("SignIn");
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet 'SignIn' trong file Excel!");
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int currentRowNum = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                currentRowNum = row.getRowNum() + 1; // Excel row index (1-based)

                // Bỏ header + row không nằm trong danh sách cần chạy
                if (currentRowNum == 1 || !rowsToRun.contains(currentRowNum)) {
                    continue;
                }

                if (row == null || ExcelUtils.isRowEmpty(row)) continue;

                String id = ExcelUtils.getCellValue(row.getCell(0));
                String email = ExcelUtils.getCellValue(row.getCell(4));
                String expectedMsg = ExcelUtils.getCellValue(row.getCell(7));

                testData.add(new Object[]{
                        id, email, expectedMsg
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.toArray(new Object[0][]);
    }
}
