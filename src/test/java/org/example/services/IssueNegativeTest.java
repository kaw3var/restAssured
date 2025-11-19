package org.example.services;

import org.example.BaseTest;
import org.example.dto.CommentDTO;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssueNegativeTest extends BaseTest {

    @Test
    @DisplayName("TC-N1: Ошибка при создании Issue без summary")
    void createIssue_withoutSummary_shouldReturn400() {
        IssueDTO issueDTO = new IssueDTO(null, "Test description", new ProjectDTO("0-0"));
        request()
                .body(issueDTO)
                .when()
                .post("/api/issues")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("TC-N2: Получение Issue, которого не существует")
    void getNotExistingIssue_shouldReturn404() {
        request()
                .when()
                .get("/api/issues/NotExistingIssue")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("TC-N3: Ошибка при отправке некорректного JSON")
    void updateIssue_invalidJson_shouldReturn400() {
        String issueId = createIssueAndGetId("Test summary", "Test description", "0-0");

        String invalidJson = "summary: test";

        request()
                .body(invalidJson)
                .when()
                .put("/api/issues/" + issueId)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("TC-N4: Ошибка при добавлении комментария к удалённому Issue")
    void addComment_toDeletedIssue_shouldReturn404() {
        String issueId = createIssueAndGetId("Test summary", "Test description", "0-0");

        request()
                .when()
                .delete("/api/issues/" + issueId)
                .then()
                .statusCode(200);

        CommentDTO comment = new CommentDTO("Test Comment after delete");
        request()
                .body(comment)
                .when()
                .post("/api/issues/" + issueId + "/comments")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("TC-N5: Ошибка при удалении Issue в другом проекте")
    void deleteIssue_withoutPermissions_shouldReturn404() {
        String restrictedId = "2-1";

        request()
                .when()
                .delete("/api/issues/" + restrictedId)
                .then()
                .log().all()
                .statusCode(404);
    }
}
