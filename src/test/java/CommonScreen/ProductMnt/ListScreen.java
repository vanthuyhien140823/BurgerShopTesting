package CommonScreen.ProductMnt;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Common.Utilities;

public class ListScreen {
	public static final String FILE_PATH			= "src/test/resources/TestData/TestData_ModuleProduct.xlsx";
	public static final String SHEET_NAME			= "DeleteProduct";
	
	public static final String CREATE_PRODUCT_BTN_XPATH		= "(//button[@class='p-ripple p-element p-button p-component inline-flex items-center gap-2'])[1]";
	
	public static final String ACTION_BTN_XPATH				= "//tbody/tr[1]/td[7]/div[1]/a[3]";
	public static final String DUPLICATE_BTN_XPATH			= "//body[1]/div[1]/div[1]/div[1]/p-button[1]/button[1]";
	public static final String EDIT_BTN_XPATH				= "//body[1]/div[1]/div[1]/div[1]/p-button[2]/button[1]";
	public static final String DELETE_BTN_XPATH				= "//body[1]/div[1]/div[1]/div[1]/p-button[3]/button[1]";

	public static final String YES_BTN_XPATH				= "//span[normalize-space()='Yes']";
	public static final String CANCEL_BTN_XPATH				= "//span[normalize-space()='Cancel']";

	public static final String TOAST_MSG_XPATH 				= "//div[@data-pc-section='detail']";
	
	public static final String SUCCESS_MSG 					= "Product deleted";
	
	public static void deleteProduct(WebDriver driver, String id, String button, String expectedMsg) throws Exception {
        try {
    		Utilities.clickObscuredElement(driver, ACTION_BTN_XPATH, DELETE_BTN_XPATH);
    		Utilities.clickObscuredElement(driver, DELETE_BTN_XPATH, YES_BTN_XPATH);
    		
    		if (expectedMsg.equals(SUCCESS_MSG)) {
    			Utilities.clickObscuredElement(driver, YES_BTN_XPATH, TOAST_MSG_XPATH);
    		}
    		else {
    			Utilities.click(driver, By.xpath(CANCEL_BTN_XPATH));
    		}
    		
    		Utilities.captureScreen(driver, id);
            // Thực hiện test
    		if (expectedMsg.equals(SUCCESS_MSG)) {
    			Utilities.assertTextValue(driver, By.xpath(TOAST_MSG_XPATH), expectedMsg);  
    		}
    		             
            // Nếu thành công
            Utilities.writeTestResult(FILE_PATH, SHEET_NAME, id, "PASS");
        } catch (NoSuchElementException e) {
            Utilities.captureScreen(driver, id);
            String actual = "Không hiển thị thông báo lỗi \"" + expectedMsg + "\"";
            Utilities.writeTestResult(FILE_PATH, SHEET_NAME, id, "FAIL", actual);
            throw e;
        } catch (AssertionError e) {
            Utilities.captureScreen(driver, id);
            String actual = "Hiển thị thông báo lỗi \"" + Utilities.getActualText(e.getMessage()) + "\"";
            Utilities.writeTestResult(FILE_PATH, SHEET_NAME, id, "FAIL", actual);
            throw e;
        }
	}
}
