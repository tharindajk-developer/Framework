package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.CommonOperations;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Devdun.k
 */

public class Base {
    public WebDriver driver;

    Date date = Calendar.getInstance().getTime();
    private static final String Test_Output_Folder = "./test-output/MyReports/";
    private static final String FOLDER_NAME = "Time_" + System.currentTimeMillis() + "/";
    private static final String SPECIAL_CAPTURE_FOLDER = "/SPECIAL_CAPTURE_/";
    public CommonOperations commonOperations = new CommonOperations(driver);
    public String currentSystemUrl = null;

    /* Browser URL setup - Chrome , Firefox and headless browsers can pass from xml file
    * Env : If need to use specific environment you can configure them in here eg: Dev , QA , Staging
    * Cookies - Values - Clear : Clear browser cookies, Empty : If  this no needed you can send empty param*/
    @BeforeTest
    @Parameters({"browser", "Env", "Cookie"})
    public void setupBrowserAndURL(String browser, String Env, String Cookie) throws Exception {
        if (browser.equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("window-size=1400,2100");
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equalsIgnoreCase("HLChrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("disable-gpu");
            chromeOptions.addArguments("window-size=1400,2100");
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equalsIgnoreCase("HLFirefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            FirefoxOptions options = new FirefoxOptions();
            options.setBinary(firefoxBinary);
            driver = new FirefoxDriver(options);
        } else {
            throw new Exception("Browser is not correct in xml :" + browser);
        }
        if (Cookie.equalsIgnoreCase("clear")){
            driver.manage().deleteAllCookies();
        }
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        takeSnapShot(driver, Test_Output_Folder + SPECIAL_CAPTURE_FOLDER + methodName + System.currentTimeMillis() + ".png");
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void screenShot(ITestResult result) {
        DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
        String today = formatter.format(date);
        String outputImageFolder = Test_Output_Folder + today + "/ScreenCaptures/";
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                TakesScreenshot screenshot = (TakesScreenshot) driver;
                File src = screenshot.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(src, new File(outputImageFolder + result.getName() + "_" + System.currentTimeMillis() + ".png"));
                System.out.println("Successfully captured a screenshot");
            } catch (Exception e) {
                System.out.println("Exception while taking screenshot " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void endReport() {
        driver.quit();
    }

    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);
    }
}
