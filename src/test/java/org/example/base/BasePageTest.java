package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.DashboardPage;
import org.example.pages.IssuePage;
import org.example.pages.LoginPage;
import org.example.utils.PropertiesReader;
import org.example.utils.TestResultLogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@ExtendWith(TestResultLogger.class)
public class BasePageTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected String currentIssueId;

    protected static final String VALID_USER = PropertiesReader.get("yt.user");
    protected static final String VALID_PASS = PropertiesReader.get("yt.pass");
    protected static final String BASE_URL   = PropertiesReader.get("yt.baseUrl");

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver());
        getDriver().manage().window().maximize();
        getDriver().get(BASE_URL + "/login");
    }

    @AfterEach
    public void tearDown() {
        cleanupIssue();

        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    protected void performLogin() {
        new LoginPage(getDriver()).login(VALID_USER, VALID_PASS);
        new DashboardPage(getDriver()).clickIssuesLink();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    private void cleanupIssue() {
        if (currentIssueId == null || currentIssueId.isEmpty()) {
            return;
        }

        try {
            String issueUrl = BASE_URL + "/issue/" + currentIssueId;
            getDriver().navigate().to(issueUrl);

            IssuePage issuePage = new IssuePage(getDriver());
            issuePage.deleteIssue();
            issuePage.isDeletionSuccessAlertPresent();

        } catch (Exception e) {
            System.err.println(
                    "Cleanup failed for Issue ID: " + currentIssueId +
                            ". Error: " + e.getMessage()
            );
        } finally {
            currentIssueId = null;
        }
    }
}
