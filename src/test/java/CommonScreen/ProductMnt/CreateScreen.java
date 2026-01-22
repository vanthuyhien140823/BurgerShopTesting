package CommonScreen.ProductMnt;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Common.Constant;
import Common.Utilities;
import CommonScreen.HomeScreen;
import CommonScreen.AccountMnt.LoginScreen;

public class CreateScreen {
	public static final String FILE_PATH			= "src/test/resources/TestData/TestData_ModuleProduct.xlsx";
	public static final String SHEET_NAME			= "CreateProduct";
	
	public static final String TITLE_TXT_CSS		= "input[formcontrolname='title']";
	public static final String DESC_NAME			= "desc";
	public static final String CAT_DROPDOWN_CSS		= "p-dropdown[formcontrolname='category']";
	public static final String PRO_TYPE_DROPDOWN_CSS= "p-autocomplete[formcontrolname='product_type']";
	public static final String VENDOR_DROPDOWN_CSS	= "p-autocomplete[formcontrolname='vendor']";
	public static final String COL_DROPDOWN_CSS 	= "p-multiselect[formcontrolname='collections']";
	public static final String TAGS_DROPDOWN_CSS 	= "p-multiselect[formcontrolname='tags']";
	public static final String SHIP_DROPDOWN_CSS	= "p-dropdown[formcontrolname='class_shipping']";
	public static final String PERSONALIZE_XPATH	= "//div[@class='flex justify-between items-center']//div[@class='p-inputswitch p-component']";
	public static final String STATUS_DROPDOWN_CSS	= "p-dropdown[formcontrolname='status']";
	public static final String SHORT_DESC_NAME		= "short_desc";
	public static final String SEO_TITLE_TXT_CSS	= "input[formcontrolname='seo_title']";
	public static final String SEO_DESC_TXT_CSS		= "textarea[formcontrolname='seo_desc']";
	public static final String URI_TXT_CSS			= "input[formcontrolname='uri']";
	public static final String PRICE_TXT_CSS		= "input[formcontrolname='price_simple']";
	public static final String COMP_PRICE_TXT_CSS	= "input[formcontrolname='compare_price_simple']";
	public static final String COST_TXT_CSS			= "input[formcontrolname='cost_simple']";
	public static final String SKU_TXT_CSS			= "input[formcontrolname='sku_simple']";
	
	public static final String SAVE_BTN_XPATH 		= "(//button[@class='p-element px-5 py-3 p-button p-component'])[1]";
	public static final String CANCEL_BTN_XPATH		= "//button[text()='Cancel']";
	public static final String CLOSE_BTN_XPATH		= "//*[@aria-label='Close']";
	public static final String BACK_BTN_XPATH		= "//div[@class='aspect-square p-3 border border-solid border-neutral-300 rounded-lg cursor-pointer']";
	
	public static final String TOAST_MSG_XPATH 			= "//div[@data-pc-section='detail']";
	public static final String TITLE_ERROR_MSG_XPATH 	= "//small[@class='text-error']";
	public static final String ERROR_MSG_XPATH 			= "(//div[@class='text-error ng-star-inserted'])[1]";
	
	public static final String SUCCESS_MSG 			= "Save product success!";
	
	public static WebDriver openScreen(String browser) throws Exception {
		WebDriver driver = null;
		if (!browser.isEmpty()) {
			driver = Utilities.getDriver(browser);
			driver.get(Constant.BASE_URL);
			LoginScreen.login(driver, Constant.EMAIL, Constant.PASSWORD);
			// Chuyển đổi sang Văn Hiền Store
			Utilities.clickObscuredElement(driver, HomeScreen.AHA_LINK_XPATH, HomeScreen.VAN_HIEN_STORE_LINK_XPATH);
			Utilities.click(driver, By.xpath(HomeScreen.VAN_HIEN_STORE_LINK_XPATH));
			// Mở màn hình Create Product
			Utilities.clickObscuredElement(driver, HomeScreen.PRODUCTS_LINK_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
			Utilities.clickObscuredElement(driver, ListScreen.CREATE_PRODUCT_BTN_XPATH, CreateScreen.SAVE_BTN_XPATH);
			Utilities.click(driver, By.xpath(CreateScreen.CLOSE_BTN_XPATH));
			Utilities.click(driver, By.xpath(CreateScreen.CANCEL_BTN_XPATH));
		}
		return driver;
	}
	
	public static void createProduct(WebDriver driver, String id, String title, String description,	String category, String productType, String vendor, String collection, String tags, String shippingClass, String personalize, String status, String shortDescription,
            String SEOEngineTitle, String SEOEngineDescription, String SEOEngineURLHandle, String price, String comparePrice, String cost, String SKU, String expectedMsg) throws Exception {
        try {
    		String msgXPath = "";

    		Utilities.inputValueAndValidate(driver, By.cssSelector(TITLE_TXT_CSS), title, title.length() <= 255 ? title : title.substring(0, 255));
    		Utilities.inputToEditorByFormControlAndValidate(driver, DESC_NAME, description, description.length() <= 4000 ? description : description.substring(0, 4000));
    		Utilities.selectDropdownByTextDisplayed(driver, By.cssSelector(CAT_DROPDOWN_CSS), category);
    		Utilities.selectAutoCompleteByTextValue(driver, By.cssSelector(PRO_TYPE_DROPDOWN_CSS), productType);
    		Utilities.selectAutoCompleteByTextValue(driver, By.cssSelector(VENDOR_DROPDOWN_CSS), vendor);
    		Utilities.selectMultiDropdownByTextValue(driver, By.cssSelector(COL_DROPDOWN_CSS), collection);
    		Utilities.selectMultiDropdownByTextValue(driver, By.cssSelector(TAGS_DROPDOWN_CSS), tags);
    		Utilities.selectDropdownByTextDisplayed(driver, By.cssSelector(SHIP_DROPDOWN_CSS), shippingClass);
    		if (!personalize.isBlank() && !personalize.isEmpty()) {
    			Utilities.click(driver, By.xpath(PERSONALIZE_XPATH));
    		}
    		Utilities.selectDropdownByTextDisplayed(driver, By.cssSelector(STATUS_DROPDOWN_CSS), status);
    		Utilities.inputToEditorByFormControlAndValidate(driver, SHORT_DESC_NAME, shortDescription, shortDescription.length() <= 1000 ? shortDescription : shortDescription.substring(0, 1000));
    		Utilities.inputValueAndValidate(driver, By.cssSelector(SEO_TITLE_TXT_CSS), SEOEngineTitle, SEOEngineTitle.length() <= 255 ? SEOEngineTitle : SEOEngineTitle.substring(0, 255));
    		Utilities.inputTextareaAndValidate(driver, By.cssSelector(SEO_DESC_TXT_CSS), SEOEngineDescription, SEOEngineDescription.length() <= 4000 ? SEOEngineDescription : SEOEngineDescription.substring(0, 4000));
    		Utilities.inputValueAndValidate(driver, By.cssSelector(URI_TXT_CSS), SEOEngineURLHandle, SEOEngineURLHandle);
    		Utilities.inputValueAndValidate(driver, By.cssSelector(PRICE_TXT_CSS), price, price);
    		Utilities.inputValueAndValidate(driver, By.cssSelector(COMP_PRICE_TXT_CSS), comparePrice, comparePrice);
    		Utilities.inputValueAndValidate(driver, By.cssSelector(COST_TXT_CSS), cost, cost);
    		Utilities.inputValueAndValidate(driver, By.cssSelector(SKU_TXT_CSS), SKU, SKU);
    		
    		if (expectedMsg.equals(SUCCESS_MSG)) {
    			msgXPath = TOAST_MSG_XPATH;
    		}
    		else if (expectedMsg.contains("Title")){
    			msgXPath = TITLE_ERROR_MSG_XPATH;
    		}
    		else {
    			msgXPath = ERROR_MSG_XPATH;
    		}
    		Utilities.click(driver, By.xpath(SAVE_BTN_XPATH));
    		TimeUnit.SECONDS.sleep(Constant.WAIT_REFRESH_SCREEN);
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
