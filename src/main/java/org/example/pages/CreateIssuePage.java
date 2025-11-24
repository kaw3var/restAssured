package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public CreateIssuePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void enterSummary(String summary) {
        WebElement field = waitForVisible(summaryField);
        field.clear();
        field.sendKeys(summary);
    }

    public void enterDescription(String description) {
        WebElement field = waitForVisible(descriptionContent);
        field.clear();
        field.sendKeys(description);
    }

    public boolean isCreateButtonDisabled() {
        try {
            waitForVisible(createButtonDisabled);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCreate() {
        waitForClickable(createButton).click();
    }
}
