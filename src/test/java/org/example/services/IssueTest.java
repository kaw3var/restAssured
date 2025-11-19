package org.example.services;

import org.example.BaseTest;
import org.example.dto.CommentDTO;
import org.example.dto.IssueDTO;
import org.example.dto.ProjectDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssueTest extends BaseTest {

    private String issueId;

    @BeforeEach
    void init() {
        issueId = createIssueAndGetId("Test summary", "Test description", "0-0");
    }

    @Test
    @Order(1)
    @DisplayName("TC-P1: Создание Issue с валидными полями")
    void createIssue_validFields_shouldReturn200AndId() {
        request
                .when()
                .get("/api/issues/" + issueId + "?fields=id,summary,description")
                .then()
                .statusCode(200)
                .body("id", equalTo(issueId))
                .body("summary", equalTo("Test summary"))
                .body("description", equalTo("Test description"));
    }

    @Test
    @Order(2)
    @DisplayName("TC-P2: Получение существующего Issue")
    void getIssue_existing_shouldReturn200AndId() {
        request
                .when()
                .get("/api/issues/" + issueId + "?fields=id,summary,description")
                .then()
                .statusCode(200)
                .body("id", equalTo(issueId))
                .body("summary", equalTo("Test summary"))
                .body("description", equalTo("Test description"));
        System.out.println("Got Issue successfully");
    }

    @Test
    @Order(3)
    @DisplayName("TC-P3: Обновление summary и description")
    void updateIssue_shouldReturn200AndUpdatedFields() {
        IssueDTO updatedIssueDTO = new IssueDTO("Updated summary", "Updated description", new ProjectDTO("0-0"));

        request
                .body(updatedIssueDTO)
                .when()
                .put("/api/issues/" + issueId)
                .then()
                .statusCode(200)
                .body("summary", equalTo("Updated summary"))
                .body("description", equalTo("Updated description"));
        System.out.println("Updated Issue successfully");
    }

    @Test
    @Order(4)
    @DisplayName("TC-P4: Добавление комментария к Issue (YouTrack API)")
    void addComment_shouldReturn200AndCommentAdded() {
        CommentDTO commentDto = new CommentDTO("Test Comment from API");

        String commentId = request
                .body(commentDto)
                .when()
                .post("/api/issues/" + issueId + "/comments")
                .then()
                .statusCode(200)
                .extract().path("id");

        System.out.println("Created Comment ID: " + commentId);

        request
                .queryParam("fields", "id,text,author(name,id),created")
                .when()
                .get("/api/issues/" + issueId + "/comments")
                .then()
                .statusCode(200)
                .body("find { it.id == '" + commentId + "' }.text", equalTo("Test Comment from API"))
                .body("find { it.id == '" + commentId + "' }.author.name", notNullValue())
                .body("find { it.id == '" + commentId + "' }.created", greaterThan(0L));
        System.out.println("Comment verified successfully");
    }

    @Test
    @Order(5)
    @DisplayName("TC-P5: Удаление существующего Issue")
    void deleteIssue_shouldReturn200AndId() {
        request
                .delete("/api/issues/" + issueId)
                .then()
                .statusCode(200);
        System.out.println("Deleted Issue ID: " + issueId);
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Создание Issue из CSV")
    @CsvFileSource(resources = "/issues.csv", numLinesToSkip = 1)
    void createIssue_csvFile_shouldReturn200AndId(String summary, String description, String projectId) {
        String csvIssueId = createIssueAndGetId(summary, description, projectId);

        request
                .when()
                .get("/api/issues/" + csvIssueId + "?fields=id,summary,description")
                .then()
                .statusCode(200)
                .body("id", equalTo(csvIssueId))
                .body("summary", equalTo(summary))
                .body("description", equalTo(description));

        System.out.println("Created from CSV Issue: " + csvIssueId);
    }
}
