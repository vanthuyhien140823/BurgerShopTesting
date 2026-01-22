package TestSuite.AccountMnt;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Initialization;
import Common.Utilities;
import CommonScreen.AccountMnt.LoginScreen;
import DataProvider.AccountMnt.LoginData;
import DataProvider.AccountMnt.LoginWithOnlyEmailData;

public class Login extends Initialization {
	@BeforeMethod()
	public void setUpMethod(Method method) throws Exception {
		Utilities.testID = method.getName();
		driver = LoginScreen.openScreen(browser);
	}
	
	@AfterMethod()
	public void tearDownMethod() throws Exception {
		Utilities.closeDriver(driver);
	}

//	@Test(dataProvider = "loginData", dataProviderClass = LoginData.class)
//	public void checkLogin(String id, String email, String password, String expectedMsg) throws Exception {
//		LoginScreen.login(driver, id, email, password, expectedMsg);
//	}
	
	@Test(dataProvider = "loginWithOnlyEmailData", dataProviderClass = LoginWithOnlyEmailData.class)
	public void checkEmail(String id, String email, String expectedMsg) throws Exception {
		LoginScreen.login(driver, id, email, expectedMsg);
	}
}
