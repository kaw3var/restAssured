package org.example.services;

import org.example.BasePageTest;
import org.example.pages.CreateIssuePage;
import org.example.pages.DashboardPage;
import org.example.pages.IssuePage;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.*;

public class IssueTestPage extends BasePageTest {

    private final String VALID_USER = "admin";
    private final String VALID_PASS = "admin";

    // Убрали лишний клик на Issues Link, начинаем с Dashboard
    @BeforeEach
    public void setupLogin() {
        new LoginPage(getDriver()).login(VALID_USER, VALID_PASS);
        DashboardPage dashboard = new DashboardPage(getDriver());
        dashboard.clickIssuesLink();
    }

    // Вспомогательный метод 1: Создает Issue и возвращает ID
    private String createIssueAndGetId(String summary, String description) {
        DashboardPage dashboard = new DashboardPage(getDriver());
        // Добавляем переход на Dashboard, чтобы гарантировать, что мы не находимся на странице Issue
        // (Обычно не нужно, если @BeforeEach работает правильно, но это мера изоляции)
        // dashboard.goToDashboard();
        dashboard.clickCreateIssue();

        CreateIssuePage createPage = new CreateIssuePage(getDriver());
        createPage.enterSummary(summary);
        createPage.enterDescription(description);
        createPage.clickCreate();

        IssuePage issuePage = new IssuePage(getDriver());
        // Клик по уведомлению, который переводит на страницу Issue
        return issuePage.clickIssueLinkInAlert();
    }

    // Вспомогательный метод 2: Создает Issue и возвращает объект IssuePage (для удобства)
    private IssuePage createIssueAndNavigate(String summary, String description) {
        createIssueAndGetId(summary, description); // Выполняем создание и переход
        return new IssuePage(getDriver()); // Возвращаем объект страницы, на которой мы находимся
    }

    // --- ИЗОЛИРОВАННЫЕ ТЕСТЫ ---

    @DisplayName("TC-I1: Успешное создание Issue и клик по уведомлению")
    @Test
    public void testCreateIssueValid() {
        String testSummary = "Auto-test Issue " + System.currentTimeMillis();
        String testDescription = "Description for TC-I1";

        String issueId = createIssueAndGetId(testSummary, testDescription);

        Assertions.assertFalse(issueId.isEmpty(), "Issue ID не был получен.");
        Assertions.assertTrue(issueId.matches("^[A-Z]+-\\d+$"), "ID не соответствует формату.");
    }

    @DisplayName("TC-I2: Ошибка при создании Issue без summary")
    @Test
    public void testCreateIssueWithoutSummary() {
        DashboardPage dashboard = new DashboardPage(getDriver());
        dashboard.clickCreateIssue();

        CreateIssuePage createPage = new CreateIssuePage(getDriver());
        createPage.enterSummary("");
        createPage.enterDescription("Description is optional");

        // Assertions.assertTrue проверяет состояние кнопки на странице CreateIssuePage
        Assertions.assertTrue(createPage.isCreateButtonDisabled(),
                "Ожидалась неактивная кнопка 'Create'.");
    }

    @DisplayName("TC-I3: Редактирование Issue — изменение summary и description")
    @Test
    public void testEditIssue() {
        String originalSummary = "Original Summary " + System.currentTimeMillis();
        String originalDescription = "Original Description";

        // Создаем Issue и переходим на нее
        IssuePage issuePage = createIssueAndNavigate(originalSummary, originalDescription);

        String newSummary = "Edited Summary " + System.currentTimeMillis();
        String newDescription = "Edited Description for TC-I3 " + System.currentTimeMillis();

        issuePage.clickEdit();
        issuePage.enterSummary(newSummary);
        issuePage.enterDescription(newDescription);
        issuePage.clickSave();

        // Проверяем актуальные данные со страницы
        Assertions.assertEquals(newSummary, issuePage.getSummaryText(),
                "Summary не обновилось после редактирования.");
        Assertions.assertEquals(newDescription, issuePage.getDescriptionText(),
                "Description не обновилось после редактирования.");
    }

    @DisplayName("TC-I4: Успешное удаление Issue")
    @Test
    public void testDeleteIssue() {
        // Создаем Issue и переходим на нее
        createIssueAndNavigate("Issue to be deleted " + System.currentTimeMillis(), "Description for deletion");

        IssuePage issuePage = new IssuePage(getDriver());
        issuePage.deleteIssue();

        // Проверяем, что появилось уведомление об успешном удалении
        Assertions.assertTrue(issuePage.isDeletionSuccessAlertPresent(),
                "Не появилось уведомление '...deleted'.");
    }

    @DisplayName("TC-I5: Успешное добавление комментария")
    @Test
    public void testAddCommentToIssue() {
        // Создаем Issue и переходим на нее
        IssuePage issuePage = createIssueAndNavigate("Issue for comment " + System.currentTimeMillis(), "Description for comment");

        String commentText = "My first autotest comment " + System.currentTimeMillis();

        issuePage.enterComment(commentText);
        issuePage.clickAddComment();

        Assertions.assertEquals(commentText, issuePage.getFirstCommentText(),
                "Текст комментария не совпадает.");
        Assertions.assertTrue(issuePage.isCommentTimestampPresent(),
                "Метка времени 'Commented' не появилась.");
    }
}