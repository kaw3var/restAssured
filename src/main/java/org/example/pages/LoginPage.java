package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.xpath("//*[@id='username']");
    private By passwordField = By.xpath("//*[@id='password']");
    private By loginButton = By.xpath("//button[@type='submit' or contains(text(), 'Log in')]");
    private By alertMessage = By.xpath("//*[contains(text(), 'Incorrect username or password')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        if (username != null && !username.isEmpty()) {
            field.sendKeys(username);
        }
    }

    public void enterPassword(String password) {
        WebElement field = driver.findElement(passwordField);
        field.clear();
        if (password != null && !password.isEmpty()) {
            field.sendKeys(password);
        }
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getAlertMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(alertMessage)).getText();
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