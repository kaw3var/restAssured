package org.example.pages;

import org.example.BasePageTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginPageTest extends BasePageTest {

    @DisplayName("TC-L1: Успешный логин (валидные данные)")
    @ParameterizedTest(name = "Логин: {0}")
    @CsvSource({
            "admin, admin"
    })
    public void testSuccessfulLogin(String username, String password) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        DashboardPage dashboard = new DashboardPage(getDriver());

        Assertions.assertTrue(dashboard.isUserLoggedIn(),
                "Вход не выполнен: Не найдены элементы 'Issues' или 'admin' на странице.");
    }

    @ParameterizedTest(name = "User: {0}, Pass: {1}")
    @DisplayName("TC-L2: Неуспешный логин — неверный логин или пароль из CSV")
    @CsvFileSource(resources = "/login.csv", numLinesToSkip = 1)
    public void testLoginWithInvalidPassword(String username, String wrongPassword) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, wrongPassword);

        String errorText = loginPage.getAlertMessageText();

        Assertions.assertEquals("Incorrect username or password.", errorText,
                "Текст ошибки не совпадает с ожидаемым.");
    }

    @DisplayName("TC-L3: Неуспешный логин — пустые поля")
    @Test
    public void testLoginWithEmptyFields() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("", "");

        boolean isHighlighted = loginPage.isInputHighlightedRed();

        Assertions.assertTrue(isHighlighted,
                "Поля не подсветились красным (класс ошибки не найден в HTML тега input).");
    }
}