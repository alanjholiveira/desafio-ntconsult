package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.resource.v1.request.PautaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PautaResourceTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/pautas";

    @LocalServerPort
    private int port;

    @Autowired
    private PautaBuilder pautaBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void when_getAll_returns_success() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void when_getAll_returns_notFound() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL.concat("/error"))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void when_create_returns_success() throws ParseException, JsonProcessingException {
        final var pautaRequest = new PautaRequest("Test Create", "Description");
        final var requestJson = objectMapper.writeValueAsString(pautaRequest);

        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(pautaRequest.name()));

    }

    @Test
    void when_create_returns_serverError() throws ParseException, JsonProcessingException {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @Test
    void when_create_returns_badrequest() throws ParseException, JsonProcessingException {
        final var pautaRequest = new PautaRequest(null, "Description");
        final var requestJson = objectMapper.writeValueAsString(pautaRequest);

        given()
                .contentType(ContentType.JSON)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
