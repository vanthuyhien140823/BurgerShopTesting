package CommonScreen.AccountMnt;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Common.Constant;
import Common.Utilities;
import CommonScreen.HomeScreen;

public class LoginScreen {
	public static final String FILE_PATH				= "src/test/resources/TestData/TestData_ModuleAccount.xlsx";
	public static final String SHEET_NAME				= "SignIn";
	
	// Link
	public static final String SIGN_UP_LINK_XPATH		= "//a[normalize-space()='Sign up']";
	
	// Text field
	public static final String EMAIL_TXT_ID				= "username";
	public static final String PASSWORD_TXT_ID			= "password";
	
	// Button
	public static final String LOGIN_BTN_XPATH			= "//button[@type='button']";
	public static final String CONT_BTN_XPATH			= "//div[@class='flex flex-col p-0']//button[@type='button']";
	
	// Message
	public static final String ERROR_MSG_XPATH 			= "(//*[@class='p-invalid ng-star-inserted'])[1]";
	public static final String TOAST_MSG_XPATH			= "//*[contains(@class,'p-toast-detail')]";

	// List of messages
	public static final String SUCCESS_MSG 				= "Login successfully";
	public static final String EMAIL_INCORRECT_MSG		= "Email is incorrect";	
	public static final String INCORRECT_MSG			= "The email or password is incorrect";	
	
	public static WebDriver openScreen(String browser) {
		WebDriver driver = null;
		if (!browser.isEmpty()) {
			driver = Utilities.getDriver(browser);
			driver.get(Constant.BASE_URL);
		}
		return driver;
	}
	
	public static void login(WebDriver driver, String email, String password) throws Exception {
		Utilities.inputValueAndValidate(driver, By.id(EMAIL_TXT_ID), email, email);
		Utilities.click(driver, By.xpath(LOGIN_BTN_XPATH));
		Utilities.inputValueAndValidate(driver, By.id(PASSWORD_TXT_ID), password, password);
		Utilities.click(driver, By.xpath(CONT_BTN_XPATH));
	}
	
	public static void login(WebDriver driver, String id, String email, String expectedMsg) throws Exception {
        try {
        	String msgXpath = "";
        	if (expectedMsg.equals(EMAIL_INCORRECT_MSG)) {
        		msgXpath = TOAST_MSG_XPATH;
        	}
        	else {
        		msgXpath = ERROR_MSG_XPATH;
        	}
    		Utilities.inputValueAndValidate(driver, By.id(EMAIL_TXT_ID), email, email);
    		Utilities.clickObscuredElement(driver, LOGIN_BTN_XPATH, msgXpath);
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
	
	public static void login(WebDriver driver, String id, String email, String password, String expectedMsg) throws Exception {
        try {
    		String msgXPath = "";
    		
    		Utilities.inputValueAndValidate(driver, By.id(EMAIL_TXT_ID), email, email);
    		Utilities.clickObscuredElement(driver, By.xpath(LOGIN_BTN_XPATH), By.id(PASSWORD_TXT_ID), Constant.WAIT_ELEMENT_EXIST);
    		Utilities.inputValueAndValidate(driver, By.id(PASSWORD_TXT_ID), password, password);
    		
    		if (expectedMsg.equals(SUCCESS_MSG)) {
    			msgXPath = HomeScreen.PRODUCTS_LINK_XPATH;
    		}
    		else if (expectedMsg.equals(INCORRECT_MSG)){
    			msgXPath = TOAST_MSG_XPATH;
    		}
    		else {
    			msgXPath = ERROR_MSG_XPATH;
    		}
    		Utilities.click(driver, By.xpath(CONT_BTN_XPATH));
    		Utilities.captureScreen(driver, id);
            // Thực hiện test
            if (expectedMsg.equals(SUCCESS_MSG)) {
            	TimeUnit.SECONDS.sleep(5);
            	Utilities.assertElementVisible(driver, By.xpath(msgXPath));
            }
            else {
            	Utilities.assertTextValue(driver, By.xpath(msgXPath), expectedMsg);   
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