package org.example.api;

import org.example.base.BaseTest;
import org.example.dto.CommentDTO;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.example.api.specifications.ResponseSpec.*;
import static org.hamcrest.Matchers.notNullValue;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class IssueTest extends BaseTest {

    private String issueId;

    private static final String TEST_SUMMARY = "Test summary";
    private static final String TEST_DESCRIPTION = "Test description";
    private static final String COMMENT_TEXT = "Test Comment from API";

    @BeforeEach
    void createIssue() {
        issueId = createIssueAndGetId(TEST_SUMMARY, TEST_DESCRIPTION, "0-0");
    }

    @AfterEach
    void cleanupIssue() {
        if (issueId != null) {
            request()
                    .delete("/issues/" + issueId)
                    .then()
                    .spec(cleanupSuccess());
        }
    }

    @Test
    @DisplayName("TC-P1: Создание Issue с валидными полями")
    void createIssue_validFields_shouldReturn200AndId() {

        withFields("id,summary,description")
                .get("/issues/" + issueId)
                .then()
                .spec(issueDetails(issueId, TEST_SUMMARY, TEST_DESCRIPTION));
    }

    @Test
    @DisplayName("TC-P2: Получение существующего Issue")
    void getIssue_existing_shouldReturn200AndId() {

        withFields("id,summary,description")
                .get("/issues/" + issueId)
                .then()
                .spec(issueDetails(issueId, TEST_SUMMARY, TEST_DESCRIPTION));
    }

    @Test
    @DisplayName("TC-P3: Обновление summary и description")
    void updateIssue_shouldReturn200AndUpdatedFields() {
        String updatedSummary = "Updated summary";
        String updatedDescription = "Updated description";

        String updatedJson = """
                {
                    "summary": "%s",
                    "description": "%s"
                }
                """.formatted(updatedSummary, updatedDescription);

        request()
                .body(updatedJson)
                .post("/issues/" + issueId + "?fields=summary,description")
                .then()
                .spec(issueUpdateSuccess(updatedSummary, updatedDescription));
    }

    @Test
    @DisplayName("TC-P4: Добавление комментария к Issue (YouTrack API)")
    void addComment_shouldReturn200AndCommentAdded() {
        CommentDTO commentDto = new CommentDTO(COMMENT_TEXT);

        String commentId = request()
                .body(commentDto)
                .post("/issues/" + issueId + "/comments")
                .then()
                .spec(status200())
                .extract().path("id");

        withFields("id,text,author(name,id),created")
                .get("/issues/" + issueId + "/comments")
                .then()
                .spec(commentVerification(commentId, COMMENT_TEXT));
    }

    @Test
    @DisplayName("TC-P5: Удаление существующего Issue")
    void deleteIssue_shouldReturn200AndId() {

        request()
                .delete("/issues/" + issueId)
                .then()
                .spec(status200());

        issueId = null;
    }

    @ParameterizedTest
    @DisplayName("Создание Issue из CSV")
    @CsvFileSource(resources = "/create-issues.csv", numLinesToSkip = 1)
    void createIssue_csvFile_shouldReturn200AndId(String summary, String description, String projectId) {
        String csvIssueId = createIssueAndGetId(summary, description, projectId);

        withFields("id,summary,description")
                .get("/issues/" + csvIssueId)
                .then()
                .spec(issueDetails(csvIssueId, summary, description));

        request()
                .delete("/issues/" + csvIssueId)
                .then()
                .spec(cleanupSuccess());
    }
}