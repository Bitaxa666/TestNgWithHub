package myprojects.automation.webinar5;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected DesiredCapabilities capabilities;


    private WebDriver getDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(BaseTest.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(BaseTest.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                capabilities = DesiredCapabilities.internetExplorer();
                capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);            // disable native events to speed up typing
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);   // force clean session
                return new InternetExplorerDriver(new InternetExplorerOptions(capabilities));
            case "phantomjs":
                System.setProperty(
                        "phantomjs.binary.path",
                        new File(BaseTest.class.getResource("/phantomjs.exe").getFile()).getPath());
                return new PhantomJSDriver();
            case "chrome":
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(BaseTest.class.getResource("/chromedriver").getFile()).getPath());
                return new ChromeDriver();

            case "chromeMobile":
            default:/*
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(BaseTest.class.getResource("/chromedriver").getFile()).getPath());
                capabilities = DesiredCapabilities.chrome();
                return new ChromeDriver(capabilities);*/

            System.setProperty(
                    "webdriver.chrome.driver",
                    new File(BaseTest.class.getResource("/chromedriver").getFile()).getPath());
            Map<String, String> mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", "Galaxy S5");
            Map<String, Object> chromeOptions = new HashMap<String, Object>();
            chromeOptions.put("mobileEmulation", mobileEmulation);
            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            return new ChromeDriver(capabilities);

        }
    }

    private RemoteWebDriver getRemoteDriver(String hubUrl, String browser) throws MalformedURLException {
        DesiredCapabilities capabilities;
        switch (browser) {
            case "firefox":
                capabilities = DesiredCapabilities.firefox();
                break;
            case "ie":
            case "internet explorer":
                capabilities = DesiredCapabilities.internetExplorer();
                break;
            case "phantomjs":
                capabilities = DesiredCapabilities.phantomjs();
                break;
            case "chrome":
            default:
                capabilities = DesiredCapabilities.chrome();
                break;
        }
//        capabilities.setCapability("applicationName", "Node1");
        return new RemoteWebDriver(new URL(hubUrl), capabilities);
    }

    @BeforeClass
    @Parameters({"selenium.hub", "selenium.browser"})
    public void setUp(@Optional("") String hubURL, @Optional("chrome") String browser) throws MalformedURLException {
        driver = hubURL.isEmpty() ? getDriver(browser) : getRemoteDriver(hubURL, browser);

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//        driver.manage().window().maximize();
         driver.manage().window().maximize();

        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
