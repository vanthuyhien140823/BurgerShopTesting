package TestSuite.AccountMnt;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Initialization;
import Common.Utilities;
import CommonScreen.AccountMnt.RegisterScreen;
import DataProvider.AccountMnt.RegisterData;
import DataProvider.AccountMnt.RegisterWithOnlyEmailData;

public class Register extends Initialization {
	@BeforeMethod()
	public void setUpMethod(Method method) throws Exception {
		Utilities.testID = method.getName();
		driver = RegisterScreen.openScreen(browser);
	}
	
	@AfterMethod()
	public void tearDownMethod() throws Exception {
		Utilities.closeDriver(driver);
	}

	@Test(dataProvider = "registerData", dataProviderClass = RegisterData.class)
	public void checkRegister(String id, String email, String password, String confPassword, String expectedMsg) throws Exception {
		RegisterScreen.register(driver, id, email, password, confPassword, expectedMsg);
	}
	
	@Test(dataProvider = "registerWithOnlyEmailData", dataProviderClass = RegisterWithOnlyEmailData.class)
	public void checkEmail(String id, String email, String expectedMsg) throws Exception {
		RegisterScreen.register(driver, id, email, expectedMsg);
	}
}
