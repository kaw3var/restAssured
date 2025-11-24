package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By issuesLink = By.xpath("//a[@data-test='ring-link issues-button']");
    private By createIssueButton = By.xpath("//a[@data-test='createIssueButton']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isUserLoggedIn() {
        try {
            waitForVisible(issuesLink);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickIssuesLink() {
        waitForClickable(issuesLink).click();
    }

    public void clickCreateIssue() {
        waitForClickable(createIssueButton).click();
    }
}
