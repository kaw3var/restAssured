package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.utils.PropertiesReader;
import org.example.utils.TestResultLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Базовый класс для всех UI тестов.
 */

@ExtendWith(TestResultLogger.class)
public class BaseUiTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    protected static final String BASE_URL   = PropertiesReader.get("yt.baseUrl");
    protected static final int IMPLICIT_WAIT = PropertiesReader.getInt("implicit.wait", 10);
    protected static final boolean HEADLESS_MODE = PropertiesReader.getBoolean("browser.headless", false);

    @BeforeEach
    public void setupDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        if (HEADLESS_MODE) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.set(webDriver);
    }

    @AfterEach
    public void tearDownDriver() {
        WebDriver currentDriver = getDriver();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driver.remove();
            }
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    protected void openUrl(String path) {
        String fullUrl = BASE_URL + path;
        getDriver().get(fullUrl);
    }

    protected void openAbsoluteUrl(String absoluteUrl) {
        getDriver().get(absoluteUrl);
    }

    protected String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    protected String getPageTitle() {
        return getDriver().getTitle();
    }

    protected void refreshPage() {
        getDriver().navigate().refresh();
    }

    protected void navigateBack() {
        getDriver().navigate().back();
    }

    protected void navigateForward() {
        getDriver().navigate().forward();
    }
}
