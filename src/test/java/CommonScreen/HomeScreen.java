package CommonScreen;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Common.Constant;
import Common.Utilities;
import CommonScreen.AccountMnt.LoginScreen;

public class HomeScreen {
	public static final String AHA_LINK_XPATH				= "(//div[@class='py-3 px-7 flex justify-between items-center cursor-pointer hover:bg-primary-50'])[1]";
	public static final String VAN_HIEN_STORE_LINK_XPATH	= "//div[@class='menu__item ng-star-inserted']";
	
	public static final String PRODUCTS_LINK_XPATH 			= "//span[normalize-space()='Products']";


	public static WebDriver openScreen(String browser) throws Exception {
		WebDriver driver = null;
		if (!browser.isEmpty()) {
			driver = Utilities.getDriver(browser);
			driver.get(Constant.BASE_URL);
			LoginScreen.login(driver, Constant.EMAIL, Constant.PASSWORD);
			// Chuyển đổi sang Văn Hiền Store
			Utilities.clickObscuredElement(driver, AHA_LINK_XPATH, VAN_HIEN_STORE_LINK_XPATH);
			Utilities.click(driver, By.xpath(VAN_HIEN_STORE_LINK_XPATH));
			TimeUnit.SECONDS.sleep(Constant.WAIT_ELEMENT_CLICKABLE);
		}
		return driver;
	}
}
