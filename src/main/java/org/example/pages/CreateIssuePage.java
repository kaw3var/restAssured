package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreateIssuePage extends BasePage {

    private By summaryField = By.xpath("//textarea[@data-test='summary']");
    private By descriptionContent = By.xpath("//div[@data-test='wysiwyg-editor-content']");
    private By createButtonDisabled = By.xpath("//button[@data-test='submit-button'][@disabled]");
    private By createButton = By.xpath("//button[@data-test='submit-button' and contains(span/span, 'Create')]");

    public CreateIssuePage(WebDriver driver) {
        super(driver);
    }

    public void enterSummary(String summary) {
        type(summaryField, summary);
    }

    public void enterDescription(String description) {
        type(descriptionContent, description);
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
        click(createButton);
    }
}
