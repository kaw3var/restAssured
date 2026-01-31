package org.example.ui.tests;

import org.example.base.IssueUiTestBase;
import org.example.ui.steps.IssueSteps;
import org.junit.jupiter.api.*;

public class IssueUiTest extends IssueUiTestBase {

    private IssueSteps steps;

    @BeforeEach
    public void initSteps() {
        dashboardPage.clickIssuesLink();
        steps = new IssueSteps(getDriver());
    }

    @Test
    @DisplayName("TC-I1: Успешное создание Issue с валидными полями")
    public void testCreateIssueValid() {
        String summary = "TC-I1 Issue " + System.currentTimeMillis();
        String description = "Description for TC-I1";

        steps.createIssue(summary, description);
        String issueId = steps.getCreatedIssueId();
        trackIssueForCleanup(issueId);

        Assertions.assertFalse(issueId.isEmpty(), "Issue не создана, уведомление отсутствует");
        Assertions.assertTrue(issueId.matches("^[A-Z]+-\\d+$"), "ID задачи не соответствует формату");
    }

    @DisplayName("TC-I2: Ошибка при создании Issue без summary")
    @Test
    public void testCreateIssueWithoutSummary() {
        steps.openCreateIssueForm();
        steps.fillIssueForm(null, "Description for TC-I2");

        Assertions.assertFalse(steps.isCreateButtonEnable(),
                "Ожидалась неактивная кнопка 'Create' при пустом поле Summary, но кнопка активна.");
    }

    @Test
    @DisplayName("TC-I3: Успешное редактирование summary и description")
    public void testEditIssue() {
        steps.createIssue("TC-I3 Issue", "Description for TC-I3");
        String issueId = steps.getCreatedIssueId();
        trackIssueForCleanup(issueId);

        String editSummary = "TC-I3 Edited Summary";
        String editDescription = "Edited Description";

        steps.editIssue(editSummary, editDescription);

        Assertions.assertEquals(editSummary, steps.getSummary(), "Summary не совпадают");
        Assertions.assertEquals(editDescription, steps.getDescription(), "Description не совпадают");
    }

    @Test
    @DisplayName("TC-I4: Успешное удаление Issue")
    public void testDeleteIssue() {
        steps.createIssue("TC-I4 Issue", "Description for TC-I4");
        steps.deleteIssue(steps.getCreatedIssueId());

        Assertions.assertTrue(steps.isDeletionSuccessAlertPresent(), "Уведомление об удалении не появилось");
    }

    @Test
    @DisplayName("TC-I5: Успешное добавление комментария")
    public void testAddCommentToIssue() {
        steps.createIssue("TC-I5 Issue", "Description for TC-I5");
        String issueId = steps.getCreatedIssueId();
        trackIssueForCleanup(issueId);

        String comment = "Comment TC-I5 " + System.currentTimeMillis();

        steps.addComment(comment);

        Assertions.assertEquals(comment, steps.getFirstCommentText(),
                "Текст комментария не соответствует введенному");
        Assertions.assertTrue(steps.isCommentTimestampPresent(),
                "Метка времени комментария отсутствует");
    }
}
