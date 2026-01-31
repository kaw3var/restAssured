package org.example.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultLogger implements TestWatcher {

//    @Override
//    public void testFailed(ExtensionContext context, Throwable cause) {
//        WebDriver driver = BasePageTest.getDriver();
//        if (driver != null) {
//            takeScreenshot(driver, context.getDisplayName());
//        }
//        System.out.println("TEST FAILED: " + context.getDisplayName());
//    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("TEST PASSED: " + context.getDisplayName());
    }

    private void takeScreenshot(WebDriver driver, String testName) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "screenshots/" + testName + "_" + timeStamp + ".png";
        try {
            Files.createDirectories(Paths.get("screenshots"));
            Files.copy(srcFile.toPath(), Paths.get(fileName));
            System.out.println("Screenshot saved: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}