package org.example.ui;

import org.example.base.BasePageTest;
import org.example.pages.CreateIssuePage;
import org.example.pages.DashboardPage;
import org.example.pages.IssuePage;
import org.junit.jupiter.api.*;

public class IssuePageTest extends BasePageTest {

    public String createIssueAndGetId(String summary, String description) {
        DashboardPage dashboard = new DashboardPage(getDriver());
        dashboard.clickCreateIssue();

        CreateIssuePage createPage = new CreateIssuePage(getDriver());
        createPage.enterSummary(summary);
        createPage.enterDescription(description);
        createPage.clickCreate();

        IssuePage issuePage = new IssuePage(getDriver());
        return issuePage.clickIssueLinkInAlert();
    }

    @BeforeEach
    void login() {
        performLogin();
    }

    @DisplayName("TC-I1: Успешное создание Issue с валидными полями")
    @Test
    public void testCreateIssueValid() {
        currentIssueId = createIssueAndGetId("TC-I1 Issue " + System.currentTimeMillis(), "Description for TC-I1");

        Assertions.assertFalse(currentIssueId.isEmpty(), "Issue не создана, уведомление отсутствует");
        Assertions.assertTrue(currentIssueId.matches("^[A-Z]+-\\d+$"), "ID задачи не соответствует формату");
    }

    @DisplayName("TC-I2: Ошибка при создании Issue без summary")
    @Test
    public void testCreateIssueWithoutSummary() {
        DashboardPage dashboard = new DashboardPage(getDriver());
        dashboard.clickCreateIssue();

        CreateIssuePage createPage = new CreateIssuePage(getDriver());
        createPage.enterSummary("");
        createPage.enterDescription("Description for TC-I2");

        Assertions.assertTrue(createPage.isCreateButtonDisabled(),
                "Ожидалась неактивная кнопка 'Create' при пустом поле Summary, но кнопка активна.");
    }

    @DisplayName("TC-I3: Успешное редактирование summary и description")
    @Test
    public void testEditIssue() {
        currentIssueId = createIssueAndGetId("TC-I3 Original Summary " + System.currentTimeMillis(), "Original Description for TC-I3");

        String newSummary = "TC-I3 Edited Summary";
        String newDescription = "Edited Description";

        IssuePage issuePage = new IssuePage(getDriver());
        issuePage.clickEdit();
        issuePage.enterSummary(newSummary);
        issuePage.enterDescription(newDescription);
        issuePage.clickSave();

        Assertions.assertEquals(newSummary, issuePage.getSummaryText());
        Assertions.assertEquals(newDescription, issuePage.getDescriptionText());
    }

    @DisplayName("TC-I4: Успешное удаление Issue")
    @Test
    public void testDeleteIssue() {
        currentIssueId = createIssueAndGetId("TC-I4 Issue " + System.currentTimeMillis(), "Description");

        IssuePage issuePage = new IssuePage(getDriver());
        issuePage.deleteIssue();

        Assertions.assertTrue(issuePage.isDeletionSuccessAlertPresent());
        currentIssueId = null;
    }

    @DisplayName("TC-I5: Успешное добавление комментария")
    @Test
    public void testAddCommentToIssue() {
        currentIssueId = createIssueAndGetId("TC-I5 Issue " + System.currentTimeMillis(), "Description");

        String comment = "Comment TC-I5 " + System.currentTimeMillis();
        IssuePage issuePage = new IssuePage(getDriver());
        issuePage.enterComment(comment);
        issuePage.clickAddComment();

        Assertions.assertEquals(comment, issuePage.getFirstCommentText(),
                "Текст опубликованного комментария не соответствует введенному.");
        Assertions.assertTrue(issuePage.isCommentTimestampPresent(),
                "Метка времени 'Commented' не появилась, комментарий не опубликован.");
    }
}
