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
    private String currentIssueId;

    @BeforeEach
    public void setupLogin() {
        new LoginPage(getDriver()).login(VALID_USER, VALID_PASS);
        new DashboardPage(getDriver()).clickIssuesLink();
    }

    @AfterEach
    public void cleanupIssue() {
        if (currentIssueId != null && !currentIssueId.isEmpty()) {
            try {
                String issueUrl = getDriver().getCurrentUrl().split("/issue/")[0] + "/issue/" + currentIssueId;
                getDriver().navigate().to(issueUrl);

                IssuePage issuePage = new IssuePage(getDriver());
                issuePage.deleteIssue();
                issuePage.isDeletionSuccessAlertPresent();

            } catch (Exception e) {
                System.err.println("Cleanup failed for Issue ID: " + currentIssueId + ". Error: " + e.getMessage());
            } finally {
                currentIssueId = null;
            }
        }
    }

    @DisplayName("TC-I1: Успешное создание Issue и клик по уведомлению")
    @Test
    public void testCreateIssueValid() {
        String summary = "TC-I1 Issue " + System.currentTimeMillis();
        String description = "Description for TC-I1";

        currentIssueId = createIssueAndGetId(summary, description);

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

    @DisplayName("TC-I3: Редактирование Issue — изменение summary и description")
    @Test
    public void testEditIssue() {
        String originalSummary = "TC-I3 Original Summary";
        String originalDescription = "Original Description for TC-I3";

        currentIssueId = createIssueAndGetId(originalSummary, originalDescription);

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
        currentIssueId = createIssueAndGetId("TC-I5 Issue", "Description");

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
