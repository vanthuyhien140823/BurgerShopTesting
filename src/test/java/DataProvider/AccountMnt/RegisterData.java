package DataProvider.AccountMnt;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import Common.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class RegisterData {

    @DataProvider(name = "registerData")
    public Object[][] registerData() {

        String excelFile = "src/test/resources/TestData/TestData_ModuleAccount.xlsx";
        List<Object[]> testData = new ArrayList<>();

        // CHỈ ĐỊNH ROW MUỐN CHẠY (THEO SỐ DÒNG EXCEL)
        Set<Integer> rowsToRun = new HashSet<>(Arrays.asList(2,18,19,20,21,22,23,24,25));

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("SignUp");
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet 'SignUp' trong file Excel!");
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
                String password = ExcelUtils.getCellValue(row.getCell(5));
                String confPassword = ExcelUtils.getCellValue(row.getCell(6));
                String expectedMsg = ExcelUtils.getCellValue(row.getCell(7));

                testData.add(new Object[]{
                        id, email, password, confPassword, expectedMsg
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.toArray(new Object[0][]);
    }
}
