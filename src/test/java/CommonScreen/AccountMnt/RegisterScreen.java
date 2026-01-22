package CommonScreen.AccountMnt;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Common.Constant;
import Common.Utilities;

public class RegisterScreen {
	public static final String FILE_PATH				= "src/test/resources/TestData/TestData_ModuleAccount.xlsx";
	public static final String SHEET_NAME				= "SignUp";
	
	// Text field
	public static final String EMAIL_TXT_ID				= "email";
	public static final String PASSWORD_TXT_XPATH		= "//input[@placeholder='Enter your password']";
	public static final String CONF_PASSWORD_TXT_XPATH	= "//input[@placeholder='Enter your confirm password']";
	
	// Button
	public static final String BTN_XPATH				= "//button[@type='button']";

	// Message
	public static final String ERROR_MSG_XPATH 			= "(//*[@class='p-invalid ng-star-inserted'])[1]";
	public static final String TOAST_MSG_XPATH			= "//*[contains(@class,'p-toast-detail')]";
	public static final String VERIFY_EMAIL_TITLE_XPATH	= "//h3[normalize-space()='Verify email']";

	// List of messages
	public static final String SUCCESS_MSG 				= "Verify email";
	public static final String EMAIL_EXISTED_MSG		= "Email is existed";
		
	public static WebDriver openScreen(String browser) {
		WebDriver driver = null;
		if (!browser.isEmpty()) {
			driver = Utilities.getDriver(browser);
			driver.get(Constant.BASE_URL);
			Utilities.waitForElementClickable(driver, By.xpath(LoginScreen.SIGN_UP_LINK_XPATH), Constant.WAIT_ELEMENT_EXIST);
			Utilities.clickObscuredElement(driver, By.xpath(LoginScreen.SIGN_UP_LINK_XPATH), By.xpath(RegisterScreen.BTN_XPATH), Constant.WAIT_ELEMENT_EXIST);
		}
		return driver;
	}
	
	public static void register(WebDriver driver, String id, String email, String expectedMsg) throws Exception {
        try {
        	String msgXpath = "";
        	if (expectedMsg.equals(EMAIL_EXISTED_MSG)) {
        		msgXpath = TOAST_MSG_XPATH;
        	}
        	else {
        		msgXpath = ERROR_MSG_XPATH;
        	}
    		Utilities.inputValueAndValidate(driver, By.id(EMAIL_TXT_ID), email, email);
    		Utilities.clickObscuredElement(driver, BTN_XPATH, msgXpath);
    		Utilities.captureScreen(driver, id);
            // Thực hiện test
            Utilities.assertTextValue(driver, By.xpath(msgXpath), expectedMsg);         
            
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
	
	public static void register(WebDriver driver, String id, String email, String password, String confPassword, String expectedMsg) throws Exception {
        try {
    		String msgXPath = "";
    		
    		Utilities.inputValueAndValidate(driver, By.id(EMAIL_TXT_ID), email, email);
    		Utilities.clickObscuredElement(driver, BTN_XPATH, PASSWORD_TXT_XPATH);
    		Utilities.inputValueAndValidate(driver, By.xpath(PASSWORD_TXT_XPATH), password, password);
    		Utilities.inputValueAndValidate(driver, By.xpath(CONF_PASSWORD_TXT_XPATH), confPassword, confPassword);
    		
    		if (expectedMsg.equals(SUCCESS_MSG)) {
    			msgXPath = VERIFY_EMAIL_TITLE_XPATH;    			
    		}
    		else {
    			msgXPath = ERROR_MSG_XPATH;
    		}
    		Utilities.clickObscuredElement(driver, BTN_XPATH, msgXPath);
    		Utilities.captureScreen(driver, id);
            // Thực hiện test
            Utilities.assertTextValue(driver, By.xpath(msgXPath), expectedMsg);         
            
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