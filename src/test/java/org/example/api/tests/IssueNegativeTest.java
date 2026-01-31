/*
package org.example.api.tests;

import io.restassured.http.ContentType;
import org.example.base.BaseTest;
import org.example.dto.CommentDTO;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.*;

import static org.example.api.specifications.ResponseSpec.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssueNegativeTest extends BaseTest {

    @Test
    @DisplayName("TC-N1: Ошибка при создании Issue без summary")
    void createIssue_withoutSummary_shouldReturn400() {
        IssueDTO issueDTO = new IssueDTO(null, "Test description", new ProjectDTO("0-0"));

        request()
                .body(issueDTO)
                .post("/issues")
                .then()
                .spec(status400BadRequest());
    }

    @Test
    @DisplayName("TC-N2: Получение Issue, которого не существует")
    void getNotExistingIssue_shouldReturn404() {

        request()
                .get("/issues/NotExistingIssue")
                .then()
                .spec(status404NotFound());
    }

    @Test
    @DisplayName("TC-N3: Ошибка при отправке некорректного JSON")
    void updateIssue_invalidJson_shouldReturn400() {
        String issueId = createIssueAndGetId("Test summary", "Test description", "0-0");
        String invalidJson = "summary: test";

        request()
                .contentType(ContentType.JSON)
                .body(invalidJson)
                .put("/issues/" + issueId)
                .then()
                .spec(status400BadRequest());
    }

    @Test
    @DisplayName("TC-N4: Ошибка при добавлении комментария к удалённому Issue")
    void addComment_toDeletedIssue_shouldReturn404() {
        String issueId = createIssueAndGetId("Test summary", "Test description", "0-0");

        request().delete("issues/" + issueId);

        CommentDTO comment = new CommentDTO("Test Comment after delete");
        request()
                .body(comment)
                .post("/issues/" + issueId + "/comments")
                .then()
                .spec(status404NotFound());
    }

    @Test
    @DisplayName("TC-N5: Ошибка при удалении Issue в другом проекте")
    void deleteIssue_withoutPermissions_shouldReturn404() {
        String restrictedId = "2-1";

        request()
                .delete("/issues/" + restrictedId)
                .then()
                .spec(status404NotFound());
    }
}
*/
