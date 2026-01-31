package org.example.ui.tests;

import org.example.base.BaseUiTest;
import org.example.ui.steps.LoginSteps;
import org.example.utils.PropertiesReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUiTest extends BaseUiTest {

    private LoginSteps loginSteps;

    private String VALID_USER;
    private String VALID_PASS;

    @BeforeEach
    public void initSteps() {
        openUrl("/login");
        loginSteps = new LoginSteps(getDriver());
        VALID_USER = PropertiesReader.get("yt.user");
        VALID_PASS = PropertiesReader.get("yt.pass");
    }

    @Test
    @DisplayName("TC-L1: Успешный логин")
    public void testSuccessfulLogin() {
        loginSteps.login(VALID_USER, VALID_PASS);

        assertTrue(loginSteps.isLoginSuccess(),
                "Вход не выполнен");
    }

    @ParameterizedTest(name = "Логин: {0}, Пароль: {1}")
    @DisplayName("TC-L1: Успешный логин (параметризованный)")
    @CsvSource({
            "admin, admin"
    })
    public void testSuccessfulLoginParameterized(String username, String password) {
        loginSteps.login(username, password);

        assertTrue(loginSteps.isLoginSuccess(),
                "Вход не выполнен для пользователя: " + username);
    }

    @ParameterizedTest(name = "User: {0}, Pass: {1}")
    @DisplayName("TC-L2: Неуспешный логин — неверный логин или пароль из CSV")
    @CsvFileSource(resources = "/login.csv", numLinesToSkip = 1)
    public void testLoginWithInvalidCredentials(String username, String wrongPassword) {
        loginSteps.login(username, wrongPassword);

        String errorText = loginSteps.getErrorMessage();

        assertEquals("Incorrect username or password.", errorText,
                "Текст ошибки не совпадает с ожидаемым.");
    }

    @ParameterizedTest(name = "User: {0}, Pass: {1}")
    @DisplayName("TC-L2: Неуспешный логин — различные невалидные комбинации")
    @CsvSource({
            "admin, wrongpass",
            "wronguser, admin",
            "wronguser, wrongpass",
            "admin, ''",
            "'', admin"
    })
    public void testLoginWithInvalidCombinations(String username, String password) {
        loginSteps.login(username, password);

        assertFalse(loginSteps.isLoginSuccess(),
                "Вход не должен быть выполнен для невалидных данных: " + username + " / " + password
        );
    }

    @Test
    @DisplayName("TC-L3: Неуспешный логин — пустые поля")
    public void testLoginWithEmptyFields() {
        loginSteps.login("","");

        assertTrue(loginSteps.areInputFieldsHighLighted(), "Поля не подсветились красным");
    }
}