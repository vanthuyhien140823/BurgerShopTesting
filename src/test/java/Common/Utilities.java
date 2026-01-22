package Common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utilities {
	// Test output folder
	public static String resultFolder = "";
	// List of driver
	public static String IEDriverPath = System.getProperty("user.dir") + "/src/test/resources/IEDriverServer.exe";
	public static String EdgeDriverPath = System.getProperty("user.dir") + "/src/test/resources/MicrosoftWebDriver.exe";
	public static String ChromeDriverPath = System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe";
	public static String MSEdgeDriverPath = System.getProperty("user.dir") + "/src/test/resources/msedgedriver.exe";
	// Log function
	public static final Logger logger = LogManager.getLogger(Utilities.class);
	public static final String LOGGER_CONF_PATH = System.getProperty("user.dir") + "/src/test/resources/log4j2.properties";

	public static String testID = "NotSet";

	public Utilities(Class<?> testClass) {

	    try {
	        Properties prop = new Properties();
	        prop.load(new FileInputStream(LOGGER_CONF_PATH));

	        // Lấy package name từ class test đang chạy
	        String packageName = testClass.getPackage().getName();

	        if (packageName.contains("TestSuite.")) {
	        	packageName = packageName.replaceFirst(".*TestSuite\\.", "");
	        }

	        String folderPath = packageName.replace('.', File.separatorChar);
	        String className = testClass.getSimpleName();

	        resultFolder = System.getProperty("user.dir")
	                        + File.separator + "result"
	                        + File.separator + folderPath
	                        + File.separator + className
	                        + File.separator;

	        createFolder(resultFolder);

	        prop.setProperty("log4j.appender.file.File", resultFolder + "ResultLog.log");
	        prop.store(new FileOutputStream(LOGGER_CONF_PATH), null);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void createFolder(String folderPath) {
		// Tạo một đối tượng File với đường dẫn của thư mục mới
        File folder = new File(folderPath);

        // Kiểm tra xem thư mục đã tồn tại hay không
        if (!folder.exists()) {
            // Tạo thư mục mới
            boolean created = folder.mkdirs(); // Dùng mkdirs để tạo các thư mục cha nếu chưa tồn tại
            if (!created) {
            	System.out.println("Không thể tạo thư mục: " + folderPath);
            }
        } else {
//            System.out.println("Thư mục đã tồn tại.");
        }
	}
	
	// Write information to log file
	public static void printWithTestID(String msg, Level level, Throwable... e) {
		switch (level.intLevel()) {
		case 0:
			logger.debug("[TestID:" + testID + "]" + msg);
			break;
		case 1:
			logger.info("[TestID:" + testID + "]" + msg);
			break;
		case 3:
			if (0 == e.length) {
				logger.error("[TestID:" + testID + "]" + msg);
			} else {
				logger.error("[TestID:" + testID + "]" + msg, e[0]);
			}
			break;
		default:
			break;
		}
	}

	// Assert fail test case
	public static void assertFail(WebDriver driver, String message) {
		Assert.fail(message);
		printWithTestID(message, Level.ERROR);
		LocalDateTime now = LocalDateTime.now();
		captureScreen(driver, testID + now.toString());
	}

	// Close browser
	public static void closeDriver(WebDriver driver) {
		try {
			driver.quit();
			wait(Constant.SIMILITY_THRESHOLD);
		} catch (Exception e) {
			printWithTestID(e.getMessage(), Level.ERROR);
		}
	}

	// Sleep time
	public static void wait(double waitSecond) {
		try {
			Thread.sleep((long) (waitSecond * 1000));
		} catch (InterruptedException e) {
			printWithTestID(e.toString(), Level.ERROR);
		}
	}

	// Wait for element visible during timeout
	public static boolean waitForElementVisibility(WebDriver driver, By locator, int timeInSecond) {
		boolean isSuccess = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSecond));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			isSuccess = true;
		} catch (Exception e) {
			printWithTestID(e.toString(), Level.ERROR);
		}
		return isSuccess;
	}

	// Wait for element visible during timeout. In this case, timeout = Constant.WAIT_ELEMENT_EXIST
	public static boolean waitForElementVisibility(WebDriver driver, By locator) {
		boolean isSuccess = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.WAIT_ELEMENT_EXIST));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			isSuccess = true;
		} catch (Exception e) {
			printWithTestID(e.toString(), Level.ERROR);
		}
		return isSuccess;
	}

	// Wait for element can clickable
	public static boolean waitForElementClickable(WebDriver driver, By locator, int timeInSecond) {
		boolean isSuccess = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSecond));
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
//			printWithTestID(e.toString(), Level.ERROR);
		}
		return isSuccess;
	}

	// Click one element and wait one element visible one time
	public static void clickObscuredElement(WebDriver driver, By locator, By newLocator, double waitSecond) {
		long startTime = System.currentTimeMillis();
		long timeout = System.currentTimeMillis() - startTime;
		int waitClick = (int) (Constant.WAIT_INTERVAL * 6);
		waitForElementClickable(driver, locator, waitClick / 2);
		while (timeout < waitClick * 1000) {
			try {
				WebElement element = driver.findElement(locator);
				Actions actions = new Actions(driver);
				actions.moveToElement(element);
				actions.perform();
//				element.click();
				
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", element);
				TimeUnit.SECONDS.sleep(1);
				break;
			} catch (Exception e) {
				printWithTestID(e.toString(), Level.ERROR);
			}
			timeout = System.currentTimeMillis() - startTime;
		}
        assertElementVisible(driver, newLocator);
	}

	// Click one element and wait one element visible, if this element is not visible, click again
	public static void clickObscuredElement(WebDriver driver, String elementXPath, String expectedElement) {
		clickObscuredElement(driver, By.xpath(elementXPath), By.xpath(expectedElement), Constant.WAIT_ELEMENT_EXIST);
	}

	// Click one element and wait one expected element disappear
	public static void clickObscuredElementToNotVisible(WebDriver driver, By locator, By expectedElementLocator,
			int timeInSecond) {
		long startTime = System.currentTimeMillis();
		long timeout = System.currentTimeMillis() - startTime;
		waitForElementClickable(driver, locator, 1);
		while (timeout < timeInSecond * 1000) {
			try {
				driver.findElement(locator).click();
				new WebDriverWait(((WebDriver) driver), Duration.ofSeconds(3))
						.until(ExpectedConditions.invisibilityOfElementLocated(expectedElementLocator));
				break;
			} catch (Exception e) {
				printWithTestID(e.getMessage(), Level.ERROR);
			}
			timeout = System.currentTimeMillis() - startTime;
		}
//		assertElementNotVisible(driver, expectedElementLocator);
	}

	// Input data into edit field
	public static void sendKeys(WebDriver driver, By locator, String inputData) {
		waitForElementVisibility(driver, locator);
		try {
			WebElement element = driver.findElement(locator);
			element.sendKeys(inputData);
			TimeUnit.SECONDS.sleep(Constant.WAIT_INTERVAL);
			element.sendKeys(Keys.TAB);
		} catch (Exception e) {
			Assert.fail("Could not input data: " + e.getMessage());
		}
	}

	// Input data into edit field
	public static void sendKeys(WebDriver driver, String xpath, String inputData) {
		sendKeys(driver, By.xpath(xpath), inputData);
	}

	// Clear input data
	public static void clearInput1(WebDriver driver, By locator) {
		try {
			WebElement element = driver.findElement(locator);
            element.click();
			element.sendKeys(Keys.CONTROL + "a");
			element.sendKeys(Keys.DELETE);
		} catch (Exception e) {
			Assert.fail("Could not clear data");
		}
	}

	

	// Input data into field and validate data after input
	public static void inputValueAndValidate(WebDriver driver, By locator, String inputData, String expectedValue) {
		if (!inputData.isBlank() && !inputData.isEmpty()) {
			clearInput(driver, locator);
			sendKeys(driver, locator, inputData);
			assertInputValue(driver, locator, expectedValue);
		}
	}
	
	// Input data into field and NOT validate data after input
	public static void inputValue(WebDriver driver, By locator, String inputData) {
		if (inputData != null) {
			sendKeys(driver, locator, inputData);
		}
	}

	// Validate one element visible
	public static void assertElementNotVisible(Object object, By locator) {
		WebElement element = null;
		try {
			if (object instanceof WebDriver) {
				element = ((WebDriver) object).findElement(locator);
			} else if (object instanceof WebElement) {
				element = ((WebElement) object).findElement(locator);
			}

		} catch (Exception e) {

		}
		if (element != null) {
			assertElementNotVisible(element);
		}
	}

	// Validate one element visible
	public static void assertElementVisible(Object object, By locator) {
		try {
			WebElement element = null;
			if (object instanceof WebDriver) {
				element = ((WebDriver) object).findElement(locator);
			} else if (object instanceof WebElement) {
				element = ((WebElement) object).findElement(locator);
			}
			assertElementVisible(element);
		} catch (Exception e) {
			Assert.fail("Element with locator " + locator + " doesn't exist");
		}
	}

	// Validate one element invisible
	public static void assertElementNotVisible(WebElement element) {
		Assert.assertFalse(element.isDisplayed(), element.toString() + " is still displayed");
	}

	// Validate one element visible
	public static void assertElementVisible(WebElement element) {
		Assert.assertTrue(element.isDisplayed(), element.toString() + " is not displayed");
	}

	// Validate text valued of checked element is same with expected value
	public static void assertTextValue(Object object, By locator, String expectedValue) {
		String textValue = null;
		WebElement element = null;
		if (object instanceof WebDriver) {
			element = ((WebDriver) object).findElement(locator);
		} else if (object instanceof WebElement) {
			element = ((WebElement) object).findElement(locator);
		}
		textValue = element.getText();
		assertString(expectedValue, textValue);
	}

	// Validate text valued of checked element is same with expected value and this element is visible
	public static void assertTextValueVisible(WebDriver driver, By locator, String expectedValue) {
		boolean isVisible = waitForElementVisibility(driver, locator, Constant.WAIT_ELEMENT_EXIST);
		Assert.assertEquals(isVisible, true, locator + "is not visible");
		assertTextValue(driver, locator, expectedValue);
	}

	// Validate value of text field
	public static void assertInputValue(Object object, By locator, String expectedValue) {
		String textValue = null;
		WebElement element = null;
		if (object instanceof WebDriver) {
			element = ((WebDriver) object).findElement(locator);
		} else if (object instanceof WebElement) {
			element = ((WebElement) object).findElement(locator);
		}
		textValue = element.getAttribute("value");
		assertString(expectedValue, textValue);
	}

	// Validate expected value with actual value
	public static void assertString(WebDriver driver, String expectedValue, String actualValue) {
		String msg = "";
		if (expectedValue == actualValue && actualValue == null) {
			msg = "Value is correct: " + expectedValue;
			Assert.assertTrue(true, msg);
		} else if (expectedValue.equals(actualValue)) {
			msg = "Value is correct: " + expectedValue;
			Assert.assertTrue(true, msg);
		} else {
			msg = "Value is not correct: Expected: \"" + expectedValue + "\"; Actual: \"" + actualValue + "\"";
			assertFail(driver, msg);

		}
	}

	// Assert 2 strings
	public static void assertString(String expectedValue, String actualValue) {
		String msg = "";
		if (expectedValue == actualValue && actualValue == null) {
			msg = "Value is correct: " + expectedValue;
			Assert.assertTrue(true, msg);
		} else if (expectedValue.equals(actualValue)) {
			msg = "Value is correct: " + expectedValue;
			Assert.assertTrue(true, msg);
		} else {
			msg = "Value is not correct: Expected: \"" + expectedValue + "\"; Actual: \"" + actualValue + "\"";
			Assert.fail(msg);
		}
	}

	// Capture screen for UI element
	public static void captureScreen(Object object, String testcase) {
		File scrFile = null;
		try {
			if (object instanceof WebDriver) {
				scrFile = ((TakesScreenshot) ((WebDriver) object)).getScreenshotAs(OutputType.FILE);
			} else if (object instanceof WebElement) {
				scrFile = ((TakesScreenshot) ((WebElement) object)).getScreenshotAs(OutputType.FILE);
			}
			try {
				String filePath = resultFolder + testcase + ".jpg";
//                System.out.println("aaa: " + filePath);
				Files.copy(scrFile.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				 Assert.fail("Could not capture screen: " + e.getMessage());
			}
		} catch (Exception e) {
			printWithTestID(e.getMessage(), Level.ERROR, e);
		}
	}

	// Get WebDriver based on browser type
	public static WebDriver getDriverOneTime(String browserType) {
		WebDriver driver = null;
//		killBrowser(browserType);
		switch (browserType) {
		case Constant.EDGE_BROWSER: {
			System.setProperty("webdriver.edge.driver", EdgeDriverPath);
			driver = new EdgeDriver();
			break;
		}
		case Constant.CHROME_BROWSER: {
			System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
			ChromeOptions options = new ChromeOptions();
//			options.addArguments("--headless");  // Chạy ẩn
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-gpu"); // (Linux thường cần)
			options.addArguments("--window-size=1920,1080"); // Đảm bảo đủ hiển thị layout
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(options);
			break;
		}
		case Constant.FIREFOX_BROWSER: {
			//
		}
		case Constant.MSEDGE_BROWSER:
			System.setProperty("webdriver.edge.driver", MSEdgeDriverPath);
			driver = new EdgeDriver();
			break;

		case Constant.IE_BROWSER: {
			System.setProperty("webdriver.ie.driver", IEDriverPath);
			driver = new InternetExplorerDriver();
			break;
		}
		default:
		}
		driver.manage().window().maximize();
		return driver;
	}


	// Initialize driver and try again if initialize fail
	public static WebDriver getDriver(String browserType) {
		WebDriver driver;
		try {
			driver = getDriverOneTime(browserType);
		} catch (Exception e) {
			driver = getDriverOneTime(browserType);
		}
		return driver;
	}

	// Kill any process
	public static void killProcess(String process) {
		// Default browser is IE
		String cmd = "taskkill /F /IM " + process + " /T";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Kill browser
	public static void killBrowser(String browserType) {
		switch (browserType) {
		case Constant.EDGE_BROWSER: {
			killProcess("MicrosoftEdge.exe");
			killProcess("MicrosoftWebDriver.exe");
			break;
		}
		case Constant.MSEDGE_BROWSER: {
			killProcess("msedgedriver.exe");
			killProcess("msedge.exe");
			break;
		}
		case Constant.CHROME_BROWSER: {
			killProcess("chrome.exe");
			killProcess("chromedriver.exe");
			break;
		}
		case Constant.FIREFOX_BROWSER: {
			killProcess("firefox.exe");
			break;
		}
		case Constant.IE_BROWSER: {
			killProcess("iexplore.exe");
			break;
		}
		default:
		}
	}

	// Refresh screen by press F5 key
	public static void refreshScreen(WebDriver driver) throws InterruptedException {
		driver.findElement(By.xpath("//body")).sendKeys(Keys.F5);
        TimeUnit.SECONDS.sleep(Constant.WAIT_REFRESH_SCREEN);
	}

	// Check element visible
	public static boolean checkElementVisible(Object object, By locator) {
		WebElement element = null;
		try {
			if (object instanceof WebDriver) {
				element = ((WebDriver) object).findElement(locator);
			} else if (object instanceof WebElement) {
				element = ((WebElement) object).findElement(locator);
			}

		} catch (Exception e) {

		}
		if (element != null) {
			return element.isDisplayed();
		}
		return false;
	}
	
	// Move the mouse cursor to a WebElement
	public static void mouseHover(WebDriver driver, String elementXpath) {
		Actions actions = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(elementXpath));
		actions.moveToElement(element).perform();
	}
	
	// Count elements by xpath
	public static int getXpathCount(WebDriver driver, String elementXpath) {
		List<WebElement> listElements = driver.findElements(By.xpath(elementXpath));
		int count = 0;
		for (WebElement element : listElements) {
			count++;
		}
		return count;
	}
	
	// Scroll to an element
	public static void scrollToElement(WebDriver driver, String elementXpath) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
        //Locating element by link text and store in variable "Element"        		
        WebElement element = driver.findElement(By.xpath(elementXpath));

        // Scrolling down the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", element);
        
        TimeUnit.SECONDS.sleep(1);
	}
	
	// Checked to checkbox
	public static void checked(WebDriver driver, By locator) {
		try {
			WebElement element = driver.findElement(locator);
			if (!element.isSelected()) {
				Actions actions = new Actions(driver);
				actions.moveToElement(element);
				actions.perform();
				element.click();
			}
		} catch (Exception e) {
			Assert.fail("Could not checked to element: " + locator + ": " + e.getMessage());
		}
	}
	
	// Click element
	public static void click(WebDriver driver, By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constant.WAIT_ELEMENT_CLICKABLE));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			Actions actions = new Actions(driver);
			actions.moveToElement(element);
			actions.perform();
			element.click();
			TimeUnit.SECONDS.sleep(Constant.WAIT_INTERVAL);
		} catch (Exception e) {
			Assert.fail("Could not click element: " + locator + ": " + e.getMessage());
		}
	}
	
	// Click dropdown
	public static void clickDropdown(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Chờ dropdown sẵn sàng để click
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        
        // Click để đảm bảo nó mở ra
        dropdown.click();
	}
	
	// Select by visible text
    public static void selectByVisibleText(WebDriver driver, By locator, String visibleText) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Chờ dropdown sẵn sàng để click
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        
        // Click để đảm bảo nó mở ra
        dropdown.click();
        TimeUnit.SECONDS.sleep(1);
        
        // Dùng Select để chọn theo text hiển thị
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
        
        // Chờ 1s
        TimeUnit.SECONDS.sleep(1);
    }
    
    public static void writeTestResult(String filePath, String sheetName, String testCaseId, String result) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Không tìm thấy sheet '" + sheetName + "'");

            // Đọc hàng đầu tiên (header row)
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) throw new RuntimeException("Không tìm thấy header trong sheet '" + sheetName + "'");

            // Tìm vị trí các cột theo tên
            Map<String, Integer> colIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                String headerName = cell.getStringCellValue().trim();
                colIndex.put(headerName, cell.getColumnIndex());
            }

            // Lấy index của các cột cần
            Integer testCaseCol = colIndex.getOrDefault("TestCaseID", 0);
            Integer resultCol   = colIndex.get("Result");
            Integer actualCol   = colIndex.get("Actual Result");
            Integer timeCol     = colIndex.get("Time");
            Integer testerCol   = colIndex.get("Tester");

            if (resultCol == null || actualCol == null || timeCol == null || testerCol == null) {
                throw new RuntimeException("Không tìm thấy 1 trong các cột: Result, Actual Result, Time, Tester trong file Excel!");
            }

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cell = row.getCell(testCaseCol);
                if (cell != null && cell.getStringCellValue().equalsIgnoreCase(testCaseId)) {
                    found = true;

                    // Ghi Result
                    row.createCell(resultCol, CellType.STRING).setCellValue(result);
                    
                    // Ghi Actual
                    row.createCell(actualCol, CellType.STRING).setCellValue("");

                    // Ghi Time
                    String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    row.createCell(timeCol, CellType.STRING).setCellValue(now);

                    // Ghi Tester
                    row.createCell(testerCol, CellType.STRING).setCellValue(Constant.TESTER_NAME);
                    break;
                }
            }

            if (!found) {
                System.out.println("Không tìm thấy TestCaseID: " + testCaseId);
            }

            // Ghi lại file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Điền kết quả test vào file test case (dò cột theo tiêu đề hàng đầu)
    public static void writeTestResult(String filePath, String sheetName, String testCaseId, String result, String actual) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Không tìm thấy sheet '" + sheetName + "'");

            // Lấy hàng tiêu đề (header)
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) throw new RuntimeException("Không tìm thấy header trong sheet '" + sheetName + "'");

            // Tạo map để lưu tên cột -> index
            Map<String, Integer> colIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                String headerName = cell.getStringCellValue().trim();
                colIndex.put(headerName, cell.getColumnIndex());
            }

            // Lấy index cột
            Integer testCaseCol = colIndex.getOrDefault("TestCaseID", 0);
            Integer resultCol   = colIndex.get("Result");
            Integer actualCol   = colIndex.get("Actual Result");
            Integer timeCol     = colIndex.get("Time");
            Integer testerCol   = colIndex.get("Tester");

            // Kiểm tra thiếu cột nào
            List<String> missingCols = new ArrayList<>();
            if (resultCol == null) missingCols.add("Result");
            if (actualCol == null) missingCols.add("Actual Result");
            if (timeCol == null) missingCols.add("Time");
            if (testerCol == null) missingCols.add("Tester");

            if (!missingCols.isEmpty()) {
                throw new RuntimeException("Thiếu cột trong file Excel: " + String.join(", ", missingCols));
            }

            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cell = row.getCell(testCaseCol);
                if (cell != null && cell.getStringCellValue().equalsIgnoreCase(testCaseId)) {
                    found = true;

                    // Ghi Result
                    row.createCell(resultCol, CellType.STRING).setCellValue(result);

                    // Ghi Actual
                    row.createCell(actualCol, CellType.STRING).setCellValue(actual);

                    // Ghi Time
                    String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    row.createCell(timeCol, CellType.STRING).setCellValue(now);

                    // Ghi Tester
                    row.createCell(testerCol, CellType.STRING).setCellValue(Constant.TESTER_NAME);

                    break;
                }
            }

            if (!found) {
                System.out.println("Không tìm thấy TestCaseID: " + testCaseId);
            }

            // Ghi lại file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Focus out
    public static void focusOut(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            // Đặt focus vào element trước
            element.click();
            // Nhấn phím TAB để focus ra khỏi nó
            Actions actions = new Actions(driver);
            actions.sendKeys("\u0009").perform(); // \u0009 là mã Unicode của phím TAB
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String keepOnlyNumbers(String input) {
        if (input == null) return "";
        return input.replaceAll("[^0-9]", "");
    }

    public static String getActualText(String input) {
        Pattern pattern = Pattern.compile("Actual: \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

 // Helper chạy lệnh CMD / Shell tùy theo OS
    public static void runCommand(String command) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();

        ProcessBuilder pb;

        if (os.contains("win")) {
            // Windows → dùng cmd.exe /c
            pb = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            // Ubuntu / Linux / macOS → dùng /bin/sh -c
            pb = new ProcessBuilder("/bin/sh", "-c", command);
        }

        pb.redirectErrorStream(true);
        Process process = pb.start();

        // In realtime log ra console
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed, exit code = " + exitCode);
        }
    }
    
	public static void checkDataInSelectionAndRemoveFirstOption(WebDriver driver, By locator, List<String> expectedOptions) throws Exception {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    // Chờ dropdown sẵn sàng để click
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        // Click để đảm bảo nó mở ra
        dropdown.click();
	        
		WebElement element = driver.findElement(locator);
		Select select = new Select(element);
		List<WebElement> options = select.getOptions();
		// Lấy toàn bộ text, trim bỏ khoảng trắng
        List<String> actualOptions = options.stream()
                .map(WebElement::getText)
//                .map(String::trim)
                .collect(Collectors.toList());
        // Bỏ phần tử đầu tiên ("Select ***")
        if (!actualOptions.isEmpty()) {
            actualOptions.remove(0);
        }
        
        // Sort trước khi compare
        Collections.sort(actualOptions);
        Collections.sort(expectedOptions);
        
        // So sánh
        Assert.assertEquals(actualOptions, expectedOptions, "Dropdown data không khớp!");
	}
	
	public static void checkDataInSelection(WebDriver driver, By locator, List<String> expectedOptions) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    // Chờ dropdown sẵn sàng để click
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
		// Click để đảm bảo nó mở ra
		dropdown.click();
	        
		WebElement element = driver.findElement(locator);
		Select select = new Select(element);
		List<WebElement> options = select.getOptions();
		// Lấy toàn bộ text, trim bỏ khoảng trắng
		List<String> actualOptions = options.stream()
               .map(WebElement::getText)
//               .map(String::trim)
               .collect(Collectors.toList());
       
       // Sort trước khi compare
       Collections.sort(actualOptions);
       Collections.sort(expectedOptions);
       
       // So sánh
       Assert.assertEquals(actualOptions, expectedOptions, "Dropdown data không khớp!");
	}
	
	public static void checkListData(WebDriver driver, By locator, List<String> expectedLabels) throws Exception {
		List<WebElement> rows = driver.findElements(locator);
		List<String> actualLabels = new ArrayList<>();
		for (WebElement row : rows) {
	        actualLabels.add(row.getText().trim());
	    }
		
	    // Sort trước khi compare
	    Collections.sort(actualLabels);
	    Collections.sort(expectedLabels);
	      
	    // So sánh
	    Assert.assertEquals(actualLabels, expectedLabels, "List data không khớp!");
	}
	
	// Validate one element checked
	public static void assertElementChecked(WebElement element) {
		Assert.assertTrue(element.isSelected(), element.toString() + " is not selected");
	}
	
	// Validate one element checked
	public static void assertElementChecked(Object object, By locator) {
		try {
			WebElement element = null;
			if (object instanceof WebDriver) {
				element = ((WebDriver) object).findElement(locator);
			} else if (object instanceof WebElement) {
				element = ((WebElement) object).findElement(locator);
			}
			assertElementChecked(element);
		} catch (Exception e) {
			Assert.fail("Element with locator " + locator + " doesn't exist");
		}
	}
	
	// Assert 2 integers
	public static void assertInteger(int expectedValue, int actualValue) {
		String msg = "";
		if (expectedValue == actualValue) {
			msg = "Value is correct: " + expectedValue;
			Assert.assertTrue(true, msg);
		} else {
			msg = "Value is not correct: Expected: \"" + expectedValue + "\"; Actual: \"" + actualValue + "\"";
			Assert.fail(msg);
		}
	}
	
	// Validate text valued of checked element is same with expected value and this element is visible
	public static void assertHTMLValidate(WebDriver driver, By locator, String expectedMsg) {
		WebElement element = driver.findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String actualMsg = (String) js.executeScript(
	            "return arguments[0].validationMessage;", element
	        );
		assertString(expectedMsg, actualMsg);
	}
	
	// Validate one element disabled
	public static void assertElementDisabled(WebElement element) {
		Assert.assertFalse(element.isEnabled(), element.toString() + " is enabled");
	}
	
	// Validate one element visible
	public static void assertElementDisabled(Object object, By locator) {
		try {
			WebElement element = null;
			if (object instanceof WebDriver) {
				element = ((WebDriver) object).findElement(locator);
			} else if (object instanceof WebElement) {
				element = ((WebElement) object).findElement(locator);
			}
			assertElementDisabled(element);
		} catch (Exception e) {
			Assert.fail("Element with locator " + locator + " doesn't exist");
		}
	}
	
	// Input data into edit field
	public static void sendKeys1(WebDriver driver, By locator, String inputData) {
		waitForElementVisibility(driver, locator);
		try {
			WebElement element = driver.findElement(locator);
			element.clear();
			if (!inputData.isEmpty()) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value='" + inputData + "';", 
				    driver.findElement(locator));

			}
		} catch (Exception e) {
			Assert.fail("Could not input data: " + e.getMessage());
		}
	}
	
	// Input data into field and validate data after input
	public static void inputValueAndValidate1(WebDriver driver, By locator, String inputData, String expectedValue) throws InterruptedException {
		if (!inputData.isEmpty() || !inputData.isBlank()) {
			clearInput(driver, locator);
			TimeUnit.SECONDS.sleep(1);
			sendKeys1(driver, locator, inputData);
			TimeUnit.SECONDS.sleep(1);
			assertInputValue(driver, locator, expectedValue);
		}
	}

    // Assert 2 doubles
    public static void assertDouble(double expectedValue, double actualValue) {
        String msg = "";
        if (expectedValue == actualValue) {
            msg = "Value is correct: " + expectedValue;
            Assert.assertTrue(true, msg);
        } else {
            msg = "Value is not correct: Expected: \"" + expectedValue + "\"; Actual: \"" + actualValue + "\"";
            Assert.fail(msg);
        }
    }
    
    public static void clearInput(WebDriver driver, By locator) {
	    WebElement element = new WebDriverWait(driver, Duration.ofSeconds(Constant.WAIT_ELEMENT_EXIST))
	            .until(ExpectedConditions.visibilityOfElementLocated(locator));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // Đảm bảo element sẵn sàng
	    js.executeScript("arguments[0].scrollIntoView(true);", element);
	    js.executeScript("arguments[0].focus();", element);

	    // Native value setter (QUAN TRỌNG)
	    js.executeScript(
	        "var input = arguments[0];" +
	        "var lastValue = input.value;" +
	        "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
	        "setter.call(input, '');" +
	        "var event = new Event('input', { bubbles: true });" +
	        "event.simulated = true;" +
	        "input.dispatchEvent(event);" +
	        "input.dispatchEvent(new Event('change', { bubbles: true }));" +
	        "input.dispatchEvent(new Event('blur', { bubbles: true }));",
	        element
	    );

	    // Validate
	    String afterClear = element.getAttribute("value");
	    Assert.assertTrue(
	        afterClear == null || afterClear.isEmpty(),
	        "Input vẫn chưa clear. Current value = [" + afterClear + "]"
	    );
	}
    
    public static void inputToEditorByFormControlAndValidate(WebDriver driver, String formControlName, String inputData, String expectedValue) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	
        // Locating element by link text and store in variable "Element"        		
        WebElement element = driver.findElement(By.cssSelector("editor[formcontrolname='" + formControlName + "']"));

        // Scrolling down the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", element);

        String actualValue = (String) js.executeScript(
            // tìm editor theo formControlName
            "var editorTag = document.querySelector(\"editor[formcontrolname='\" + arguments[0] + \"']\");" +
            "if (!editorTag) throw 'Editor tag not found';" +

            // lấy textarea ẩn của TinyMCE
            "var textarea = editorTag.querySelector('textarea');" +
            "if (!textarea) throw 'Textarea not found inside editor';" +

            // lấy instance TinyMCE
            "var editor = tinymce.get(textarea.id);" +
            "if (!editor) throw 'TinyMCE editor not initialized';" +

            // ===== INPUT =====
            "editor.setContent(arguments[1]);" +

            // sync lại textarea cho Angular form
            "editor.save();" +

            // get text to validate
            "return editor.getContent({ format: 'text' }).trim();",
            formControlName,
            inputData
        );

        // assert
        assertString(expectedValue, actualValue);
    }
    
    public static void selectDropdownByTextDisplayed(WebDriver driver, By dropdownLocator, String selectedValue) {

        if (selectedValue == null || selectedValue.isBlank()) return;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // 1. Click mở dropdown
        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(dropdownLocator)
        );
        dropdown.click();

        // 2. Đợi dropdown panel (appendTo body)
        WebElement panel = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.p-dropdown-panel")
            )
        );

//        // 3. Nhập filter (BẮT BUỘC với virtualScroll)
//        WebElement filterInput = panel.findElement(
//            By.cssSelector("input.p-dropdown-filter")
//        );
//        filterInput.clear();
//        filterInput.sendKeys(selectedValue);

        // 4. Đợi item xuất hiện
        By optionLocator = By.xpath(
            ".//li[contains(@class,'p-dropdown-item') and normalize-space(.)='" + selectedValue + "']"
        );

        WebElement option = wait.until(
            ExpectedConditions.elementToBeClickable(
                panel.findElement(optionLocator)
            )
        );

        // 5. Click chọn
        option.click();
    }


    public static void selectMultiDropdownByTextValue(WebDriver driver, By multiSelectLocator, String selectedValue) {
    	if (!selectedValue.isBlank() && !selectedValue.isEmpty()) {
    		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    		
    		// 1. Mở dropdown
    		WebElement multiSelect = driver.findElement(multiSelectLocator);
    		wait.until(ExpectedConditions.elementToBeClickable(multiSelect)).click();
    		
    		// 2. Đợi panel hiển thị (appendTo body)
    		WebElement panel = wait.until(
    			ExpectedConditions.visibilityOfElementLocated(
    			By.cssSelector("div.p-multiselect-panel")
    			)
    		);
    		
    		if (selectedValue != null) {
    			// Tạo list selectedValues
    			List<String> selectedValues = new ArrayList<>();
    			selectedValues.add(selectedValue);
    			
    			for (String value : selectedValues) {
    			
    				// 3. Locate option theo text
    				By optionLocator = By.xpath(
    					".//li[contains(@class,'p-multiselect-item')]" +
    					"[.//span[normalize-space()='" + value + "']]"
    				);
    				
    				WebElement option = panel.findElement(optionLocator);
    				
    				// 4. Nếu chưa được chọn thì click
    				if (!option.getAttribute("aria-selected").equals("true")) {
    					wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    				}
    			}
    		}
    		
    		// 5. Đóng dropdown (click ra ngoài)
    		multiSelect.click();
    	}
	}

    public static void selectAutoCompleteByTextValue(WebDriver driver, By inputLocator, String selectedValue) {
    	if (!selectedValue.isBlank() && !selectedValue.isEmpty()) {
    		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));

            // 2. Focus + clear trước khi nhập
            input.click();

//            // 3. Nhập text để trigger autocomplete
//            input.sendKeys(selectedValue);

            // 4. Chờ panel suggestion hiển thị và item xuất hiện
            By optionLocator = By.xpath(
                "//li[contains(@class,'p-autocomplete-item')]" +
                "[.//span[normalize-space()='" + selectedValue + "']]"
            );

            WebElement option = wait.until(
                ExpectedConditions.visibilityOfElementLocated(optionLocator)
            );

            // 5. Click chọn item
            option.click();

//            // 6. Validate (optional nhưng khuyến nghị)
//            wait.until(ExpectedConditions.attributeToBe(input, "value", selectedValue));
    	}
    }
    
    public static void inputTextareaAndValidate(WebDriver driver, By textareaLocator, String inputData, String expectedValue) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement textarea = wait.until(
            ExpectedConditions.visibilityOfElementLocated(textareaLocator)
        );

        // 1. Focus + clear
        textarea.click();
        textarea.sendKeys(Keys.CONTROL + "a");
        textarea.sendKeys(Keys.DELETE);

        // 2. Input data
        textarea.sendKeys(inputData);

        // 3. Force trigger Angular update
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            textarea
        );

        // 4. Get actual value
        String actualValue = textarea.getAttribute("value");

        // 5. Validate
        assertString(expectedValue, actualValue);
    }

}