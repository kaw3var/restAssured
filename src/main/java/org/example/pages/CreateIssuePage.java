package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreateIssuePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By summaryField = By.xpath("//textarea[@data-test='summary']");
    private By descriptionContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By createButtonDisabled = By.xpath("//button[@data-test='submit-button'][@disabled]");
    private By createButton = By.xpath("//button[@data-test='submit-button' and contains(span/span, 'Create')]");
    private By requiredError = By.xpath("//*[contains(text(), 'Summary is required')]");

    public CreateIssuePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterSummary(String summary) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(summaryField)).sendKeys(summary);
    }

    public void enterDescription(String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionContent)).sendKeys(description);
    }

    public boolean isCreateButtonDisabled() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(createButtonDisabled));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCreate() {
        wait.until(ExpectedConditions.elementToBeClickable(createButton)).click();
    }

    public String getSummaryRequiredError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(requiredError)).getText();
    }
}