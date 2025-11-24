package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    private By issuesLink = By.xpath("//a[@data-test='ring-link issues-button']");
    private By createIssueButton = By.xpath("//a[@data-test='createIssueButton']");

    public DashboardPage(WebDriver driver) {
        super(driver);
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
        click(issuesLink);
    }

    public void clickCreateIssue() {
        click(createIssueButton);
    }
}
