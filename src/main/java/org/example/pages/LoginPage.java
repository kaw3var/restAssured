package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private By usernameField = By.xpath("//input[@id='username']");
    private By passwordField = By.xpath("//input[@id='password']");
    private By loginButton = By.xpath("//button[@type='submit' or contains(text(), 'Log in')]");
    private By alertMessage = By.xpath("//*[contains(text(), 'Incorrect username or password')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getAlertMessageText() {
        return waitForVisible(alertMessage).getText();
    }

    public boolean isInputHighlightedRed() {
        try {
            WebElement userField = driver.findElement(usernameField);
            WebElement passField = driver.findElement(passwordField);

            String userClass = userField.getAttribute("class");
            String passClass = passField.getAttribute("class");

            return userClass.contains("error") || userClass.contains("invalid") ||
                    passClass.contains("error") || passClass.contains("invalid");
        } catch (Exception e) {
            return false;
        }
    }
}