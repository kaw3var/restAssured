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

    // Локаторы для Issue (просмотр)
    private By issueIdTitle = By.xpath("//a[@data-test='ring-link'][contains(@href, 'issue/')]");
    private By summaryReadOnly = By.xpath("//h1[@data-test='issue-summary']");
    private By descriptionReadOnly = By.xpath("//div[@data-test='issue-description']");
    private By createdByText = By.xpath("//span[@data-test='reporter-type' and contains(text(), 'Created by')]");
    private By issueLinkInAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//a[@data-test='ring-link']");
    private By deletionSuccessAlert = By.xpath("//div[@data-test='alert' and @data-test-type='success']//span[contains(text(), 'deleted')]");
//    private By editButton = By.xpath("//button[contains(text(), 'Edit')]");
//    private By saveButton = By.xpath("//button[contains(text(), 'Save')]");

    private By showMoreButton = By.xpath("//button[@aria-label='Show more']");
    private By deleteIssueMenuItem = By.xpath("//button[contains(@class, 'deleteIcon') and .//span[text()='Delete issue']]");
    private By confirmDeleteButton = By.xpath("//button[@data-test='confirm-ok-button']");

    private By editButton = By.xpath("//button[@data-test='edit-issue-button']");
    private By editSummaryField = By.xpath("//textarea[@data-test='summary']"); // Поля те же, что и при создании
    private By editDescriptionContent = By.xpath("//div[@data-test='wysiwyg-editor-content']"); // Поля те же
    private By saveButton = By.xpath("//button[@data-test='save-button']");

    // Локаторы для комментариев (TC-I5, TC-I7)
    private By commentFieldContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");

    // 2. Кнопка "Add comment" (которая становится активной)
    private By addCommentButton = By.xpath("//button[@data-test='post-comment'][not(@disabled)]");

    // 3. Текст опубликованного комментария (для проверки)
// Используем data-test="comment-content" для поиска содержимого комментария
    private By firstCommentTextContent = By.xpath("(//div[@data-test='comment-content']//p)[1]");

    // 4. Метка времени (Commented just now, Commented 1 minute ago)
    private By commentTimestamp = By.xpath("//span[@data-test='item-toolbar-text'][contains(text(), 'Commented')]");

    // Локатор для проверки текста комментария. Используем относительный путь
    private By firstCommentText = By.xpath("(//div[@data-test='comment-text'])[1]");
    private By emptyCommentError = By.xpath("//*[contains(text(), 'Comment cannot be empty')]");

    private By notFoundMessage = By.xpath("//div[contains(text(), 'Issue not found')]");

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(editDescriptionContent))
                .clear();
        driver.findElement(editDescriptionContent).sendKeys(description);
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

    // --- Методы комментирования (TC-I5, TC-I7) ---
    public void enterComment(String commentText) {
        // Ждем, пока поле ввода станет видимым и кликабельным
        WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated(commentFieldContent));

        // Очистка поля (на всякий случай, если там есть плейсхолдер или старый текст)
        // Для div contenteditable лучше использовать тройной клик + DELETE
        new Actions(driver)
                .doubleClick(commentField) // Двойной клик для выделения
                .doubleClick(commentField) // Тройной клик для выделения всего
                .sendKeys(Keys.DELETE)      // Удаление выделенного
                .perform();

        // Ввод текста
        commentField.sendKeys(commentText);
    }

    public void clickAddComment() {
        // Ждем, пока кнопка станет кликабельной (то есть, атрибут disabled исчезнет)
        wait.until(ExpectedConditions.elementToBeClickable(addCommentButton)).click();
    }

    public String getFirstCommentText() {
        // Ждем появления самого первого комментария
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