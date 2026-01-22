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
import CommonScreen.ProductMnt.EditScreen;
import CommonScreen.ProductMnt.ListScreen;
import DataProvider.ProductMnt.EditData;

public class Edit extends Initialization {
	@BeforeClass()
	public void setUpClass() throws Exception {
		driver = HomeScreen.openScreen(browser);
	}

	@BeforeMethod()
	public void setUpMethod(Method method) throws Exception {
		Utilities.testID = method.getName();
		if (Utilities.checkElementVisible(driver, By.xpath(EditScreen.SAVE_BTN_XPATH))) {
			Utilities.clickObscuredElement(driver, EditScreen.BACK_BTN_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
		}
		Utilities.clickObscuredElement(driver, HomeScreen.PRODUCTS_LINK_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
		Utilities.clickObscuredElement(driver, ListScreen.CREATE_PRODUCT_BTN_XPATH, EditScreen.SAVE_BTN_XPATH);
		TimeUnit.SECONDS.sleep(Constant.WAIT_REFRESH_SCREEN);
		Utilities.click(driver, By.xpath(EditScreen.CLOSE_BTN_XPATH));
		Utilities.click(driver, By.xpath(EditScreen.CANCEL_BTN_XPATH));
	}

	@Test(dataProvider = "editData", dataProviderClass = EditData.class)
	public void checkEditProduct(String id, String title, String description, String shortDescription,
            String SEOEngineTitle, String SEOEngineDescription, String SEOEngineURLHandle, String expectedMsg) throws Exception {
		EditScreen.editProduct(driver, id, title, description, shortDescription, SEOEngineTitle, SEOEngineDescription, SEOEngineURLHandle, expectedMsg);
	}
}
