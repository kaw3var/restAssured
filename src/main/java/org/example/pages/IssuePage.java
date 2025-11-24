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

    // --- Локаторы для Issue ---
    private By issueIdTitle = By.xpath("//a[@data-test='ring-link'][contains(@href, 'issue/')]");
    private By summaryReadOnly = By.xpath("//h1[@data-test='ticket-summary']");
    private By descriptionReadOnly = By.xpath("//div[contains(@class,'description__')]//p");
    private By createdByText = By.xpath("//span[@data-test='reporter-type' and contains(text(), 'Created by')]");
    private By issueLinkInAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//a[@data-test='ring-link']");
    private By deletionSuccessAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//span[contains(text(), 'deleted')]");
    private By notFoundMessage = By.xpath("//div[contains(text(), 'Issue not found')]");

    // --- Кнопки и поля редактирования ---
    private By showMoreButton = By.xpath("//button[@aria-label='Show more']");
    private By deleteIssueMenuItem = By.xpath("//button[contains(@class, 'deleteIcon') and .//span[text()='Delete issue']]");
    private By confirmDeleteButton = By.xpath("//button[@data-test='confirm-ok-button']");
    private By editButton = By.xpath("//button[@data-test='edit-issue-button']");
    private By editSummaryField = By.xpath("//textarea[@data-test='summary']");
    private By editDescriptionContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By saveButton = By.xpath("//button[@data-test='save-button']");

    // --- Локаторы для комментариев ---
    private By commentFieldContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By addCommentButton = By.xpath("//button[@data-test='post-comment'][not(@disabled)]");
    private By firstCommentTextContent = By.xpath("(//div[@data-test='comment-content']//p)[1]");
    private By commentTimestamp = By.xpath("//span[@data-test='item-toolbar-text'][contains(text(), 'Commented')]");
    private By firstCommentText = By.xpath("(//div[@data-test='comment-text'])[1]");

    public IssuePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // --- Общие методы ---
    public String getIssueId() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(issueIdTitle)).getText();
    }

    public By getFirstCommentTextLocator() {
        return firstCommentText;
    }

    public boolean isIssuePageLoaded() {
        try {
            return driver.findElement(issueIdTitle).isDisplayed() &&
                    driver.findElement(createdByText).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String clickIssueLinkInAlert() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(issueLinkInAlert));
        String issueId = link.getText();
        link.click();
        return issueId;
    }

    public void clickEdit() {
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }

    public void enterSummary(String summary) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(editSummaryField));
        field.click();
        new Actions(driver)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(summary)
                .perform();
    }

    public void enterDescription(String description) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(editDescriptionContent));
        field.clear();
        field.sendKeys(description);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public String getSummaryText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summaryReadOnly)).getText();
    }

    public String getDescriptionText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionReadOnly)).getText();
    }

    public void deleteIssue() {
        wait.until(ExpectedConditions.elementToBeClickable(showMoreButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(deleteIssueMenuItem)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    public boolean isDeletionSuccessAlertPresent() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(deletionSuccessAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isIssueNotFound() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(notFoundMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Методы для комментариев ---
    public void enterComment(String commentText) {
        WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated(commentFieldContent));
        new Actions(driver)
                .doubleClick(commentField)
                .doubleClick(commentField)
                .sendKeys(Keys.DELETE)
                .perform();
        commentField.sendKeys(commentText);
    }

    public void clickAddComment() {
        wait.until(ExpectedConditions.elementToBeClickable(addCommentButton)).click();
    }

    public String getFirstCommentText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstCommentTextContent)).getText();
    }

    public boolean isCommentTimestampPresent() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(commentTimestamp));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
