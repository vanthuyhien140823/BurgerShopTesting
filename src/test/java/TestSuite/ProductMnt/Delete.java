package TestSuite.ProductMnt;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Initialization;
import Common.Utilities;
import CommonScreen.HomeScreen;
import CommonScreen.ProductMnt.ListScreen;
import DataProvider.ProductMnt.DeleteData;

public class Delete extends Initialization {
	@BeforeClass()
	public void setUpClass() throws Exception {
		driver = HomeScreen.openScreen(browser);
	}

	@BeforeMethod()
	public void setUpMethod(Method method) throws Exception {
		Utilities.testID = method.getName();
		Utilities.clickObscuredElement(driver, HomeScreen.PRODUCTS_LINK_XPATH, ListScreen.CREATE_PRODUCT_BTN_XPATH);
	}

	@Test(dataProvider = "deleteData", dataProviderClass = DeleteData.class)
	public void checkDeleteProduct(String id, String button, String expectedMsg) throws Exception {
		ListScreen.deleteProduct(driver, id, button, expectedMsg);
	}
}
