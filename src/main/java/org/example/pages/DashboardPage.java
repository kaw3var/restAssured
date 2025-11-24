package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By issuesLink = By.xpath("//a[@data-test='ring-link issues-button']");
    private By createIssueButton = By.xpath("//a[@data-test='createIssueButton']");
    private By searchField = By.xpath("//input[@placeholder='Search or command']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(issuesLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickIssuesLink() {
        wait.until(ExpectedConditions.elementToBeClickable(issuesLink)).click();
    }

    public void clickCreateIssue() {
        wait.until(ExpectedConditions.elementToBeClickable(createIssueButton)).click();
    }

    public void searchIssueById(String issueId) {
        By searchField = By.xpath("//input[@placeholder='Search or command']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchField)).sendKeys(issueId);
        driver.findElement(searchField).submit();
    }

    public void waitForDashboardLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));
    }
}