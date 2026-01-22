package Common;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {
    // Hàm tiện ích đọc giá trị cell
    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
    
    // Hàm kiểm tra dòng trống
    public static boolean isRowEmpty(Row row) {
        DataFormatter df = new DataFormatter();
        for (Cell cell : row) {
            if (!df.formatCellValue(cell).trim().isEmpty()) return false;
        }
        return true;
    }
}
