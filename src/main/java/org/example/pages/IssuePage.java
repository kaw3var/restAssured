package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IssuePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By summaryReadOnly = By.xpath("//h1[@data-test='ticket-summary']");
    private By descriptionReadOnly = By.xpath("//div[contains(@class,'description__')]//p");
    private By issueLinkInAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//a[@data-test='ring-link']");
    private By deletionSuccessAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//span[contains(text(), 'deleted')]");

    private By showMoreButton = By.xpath("//button[@aria-label='Show more']");
    private By deleteIssueMenuItem = By.xpath("//button[contains(@class, 'deleteIcon') and .//span[text()='Delete issue']]");
    private By confirmDeleteButton = By.xpath("//button[@data-test='confirm-ok-button']");
    private By editButton = By.xpath("//button[@data-test='edit-issue-button']");
    private By editSummaryField = By.xpath("//textarea[@data-test='summary']");
    private By editDescriptionContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By saveButton = By.xpath("//button[@data-test='save-button']");

    private By commentFieldContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By addCommentButton = By.xpath("//button[@data-test='post-comment'][not(@disabled)]");
    private By firstCommentTextContent = By.xpath("(//div[@data-test='comment-content']//p)[1]");
    private By commentTimestamp = By.xpath("//span[@data-test='item-toolbar-text'][contains(text(), 'Commented')]");

    public IssuePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // --- Методы Issue ---
    public String clickIssueLinkInAlert() {
        WebElement link = waitForClickable(issueLinkInAlert);
        String issueId = link.getText();
        link.click();
        return issueId;
    }

    public void clickEdit() {
        waitForClickable(editButton).click();
    }

    public void enterSummary(String summary) {
        WebElement field = waitForVisible(editSummaryField);
        field.click();
        new Actions(driver)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(summary)
                .perform();
    }

    public void enterDescription(String description) {
        WebElement field = waitForVisible(editDescriptionContent);
        field.clear();
        field.sendKeys(description);
    }

    public void clickSave() {
        waitForClickable(saveButton).click();
    }

    public String getSummaryText() {
        return waitForVisible(summaryReadOnly).getText();
    }

    public String getDescriptionText() {
        return waitForVisible(descriptionReadOnly).getText();
    }

    public void deleteIssue() {
        waitForClickable(showMoreButton).click();
        waitForClickable(deleteIssueMenuItem).click();
        waitForClickable(confirmDeleteButton).click();
    }

    public boolean isDeletionSuccessAlertPresent() {
        try {
            waitForVisible(deletionSuccessAlert);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Методы комментариев ---
    public void enterComment(String commentText) {
        WebElement commentField = waitForVisible(commentFieldContent);
        commentField.clear();
        commentField.sendKeys(commentText);
    }

    public void clickAddComment() {
        waitForClickable(addCommentButton).click();
    }

    public String getFirstCommentText() {
        return waitForVisible(firstCommentTextContent).getText();
    }

    public boolean isCommentTimestampPresent() {
        try {
            waitForVisible(commentTimestamp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
