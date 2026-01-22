package TestSuite.ProductMnt;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Constant;
import Common.Initialization;
import Common.Utilities;
import CommonScreen.HomeScreen;
import CommonScreen.ProductMnt.CreateScreen;
import CommonScreen.ProductMnt.ListScreen;
import DataProvider.ProductMnt.CreateData;

public class Create extends Initialization {
	@BeforeClass()
	public void setUpClass() throws Exception {
		driver = HomeScreen.openScreen(browser);
	}

	@BeforeMethod()
	public void setUpMethod(Method method) throws Exception {
		Utilities.testID = method.getName();
		if (Utilities.checkElementVisible(driver, By.xpath(CreateScreen.SAVE_BTN_XPATH))) {
			Utilities.clickObscuredElement(driver, CreateScreen.BACK_BTN_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
		}
		Utilities.clickObscuredElement(driver, HomeScreen.PRODUCTS_LINK_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
		Utilities.clickObscuredElement(driver, ListScreen.CREATE_PRODUCT_BTN_XPATH, CreateScreen.SAVE_BTN_XPATH);
		TimeUnit.SECONDS.sleep(Constant.WAIT_REFRESH_SCREEN);
		Utilities.click(driver, By.xpath(CreateScreen.CLOSE_BTN_XPATH));
		Utilities.click(driver, By.xpath(CreateScreen.CANCEL_BTN_XPATH));
	}

	@Test(dataProvider = "createData", dataProviderClass = CreateData.class)
	public void checkCreateProduct(String id, String title, String description,	String category, String productType, String vendor, String collection, String tags, String shippingClass, String personalize, String status, String shortDescription,
            String SEOEngineTitle, String SEOEngineDescription, String SEOEngineURLHandle, String price, String comparePrice, String cost, String SKU, String expectedMsg) throws Exception {
		CreateScreen.createProduct(driver, id, title, description, category, productType, vendor, collection, tags, shippingClass, personalize, status, shortDescription, SEOEngineTitle, SEOEngineDescription, SEOEngineURLHandle, price, comparePrice, cost, SKU, expectedMsg);
	}
}
