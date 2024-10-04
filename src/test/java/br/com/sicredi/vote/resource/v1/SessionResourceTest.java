package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.build.entitiy.SessionBuilder;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.resource.v1.request.SessionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class SessionResourceTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/sessions";

    @LocalServerPort
    private int port;

    @Autowired
    private PautaBuilder pautaBuilder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void when_openSession_returns_success() throws ParseException, JsonProcessingException {
        var pauta = pautaBuilder.buildEntity();
        pauta = pautaBuilder.persist(pauta);
        final var request = new SessionRequest(pauta.getId(), LocalDateTime.now().plusDays(2L));
        final var requestJson = objectMapper.writeValueAsString(request);

        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post(URL.concat("/open"))
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("pauta_name", equalTo(pauta.getName()));

    }

    @Test
    void when_openSession_returns_serverError() throws ParseException, JsonProcessingException {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post(URL.concat("/open"))
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @Test
    void when_getSessionByPautaID_returns_success() throws ParseException, JsonProcessingException {
        var session = sessionBuilder.buildEntity();
        session.setExpiration(LocalDateTime.now().plusDays(2L));
        session.setStatus(SessionStatus.OPEN);
        final var pauta = pautaBuilder.persist(session.getPauta());
        session.setPauta(pauta);
        sessionBuilder.persist(session);
        final var pautaId = pauta.getId().toString();

        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL.concat("/".concat(pautaId)))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("pauta_name", equalTo(pauta.getName()));

    }

    @Test
    void when_getSessionByPautaID_returns_notFound() throws ParseException, JsonProcessingException {
        final var pauta = pautaBuilder.buildEntity();
        final var pautaId = pauta.getId().toString();

        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL.concat("/".concat(pautaId)))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

}
