package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.CreateIssuePage;
import org.example.pages.DashboardPage;
import org.example.pages.IssuePage;
import org.example.utils.TestResultLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith(TestResultLogger.class)
public class BasePageTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver.set(new ChromeDriver(options));
        getDriver().manage().window().maximize();
        getDriver().get("http://localhost:9091/login");
    }

    @AfterEach
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public String createIssueAndGetId(String summary, String description) {
        DashboardPage dashboard = new DashboardPage(getDriver());
        dashboard.clickCreateIssue();

        CreateIssuePage createPage = new CreateIssuePage(getDriver());
        createPage.enterSummary(summary);
        createPage.enterDescription(description);
        createPage.clickCreate();

        IssuePage issuePage = new IssuePage(getDriver());
        return issuePage.clickIssueLinkInAlert();
    }
}
