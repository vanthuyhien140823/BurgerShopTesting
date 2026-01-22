package Common;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public abstract class Initialization{
    public WebDriver driver = null;
    Utilities utils;
    protected String browser;

    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void beforeClass(String browserTest) throws Exception{
        // Tạo Utilities với class test thực tế
        utils = new Utilities(this.getClass());

        browser = browserTest;
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() throws Exception{      
        utils.closeDriver(driver);
    }
}