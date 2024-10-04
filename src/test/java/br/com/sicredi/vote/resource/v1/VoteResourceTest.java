package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.build.entitiy.SessionBuilder;
import br.com.sicredi.vote.build.entitiy.VoteBuilder;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.enums.VoteType;
import br.com.sicredi.vote.resource.v1.request.VoteRequest;
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

class VoteResourceTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/votes";

    @LocalServerPort
    private int port;

    @Autowired
    private PautaBuilder pautaBuilder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private VoteBuilder voteBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void when_vote_returns_success() throws ParseException, JsonProcessingException {
        final var cpf = "44677744084";
        final var vote = voteBuilder.buildEntity();
        final var pauta = pautaBuilder.persist(vote.getPauta());
        var session = vote.getSession();
        session.setPauta(pauta);
        session.setExpiration(LocalDateTime.now().plusDays(4L));
        session = sessionBuilder.persist(session);

        final var pautaId = pauta.getId().toString();
        final var sessionId = session.getId().toString();

        final var request = new VoteRequest(pautaId, cpf, Boolean.TRUE);
        final var requestJson = objectMapper.writeValueAsString(request);

        given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo("Vote registered successfully."));
    }

    @Test
    void when_vote_returns_serverError() throws ParseException, JsonProcessingException {

        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void when_countVotes_returns_success() throws ParseException, JsonProcessingException {
        final var cpf = "44677744084";
        var vote = voteBuilder.buildEntity();
        final var pauta = pautaBuilder.persist(vote.getPauta());
        var session = vote.getSession();
        session.setPauta(pauta);
        session.setStatus(SessionStatus.FINISHED);
        session.setExpiration(session.getCreatedAt());
        session = sessionBuilder.persist(session);

        vote.setPauta(pauta);
        vote.setSession(session);
        vote.setVoteType(VoteType.YES);
        vote.setCpf(cpf);
        voteBuilder.persist(vote);

        final var sessionId = session.getId().toString();

        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL.concat("/result/").concat(sessionId))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void when_countVotes_returns_badRequest() throws ParseException, JsonProcessingException {
        final var cpf = "44677744084";
        var vote = voteBuilder.buildEntity();
        final var pauta = pautaBuilder.persist(vote.getPauta());
        var session = vote.getSession();
        session.setStatus(SessionStatus.OPEN);
        session.setPauta(pauta);
        session.setExpiration(LocalDateTime.now().plusDays(4L));
        session = sessionBuilder.persist(session);

        vote.setPauta(pauta);
        vote.setSession(session);
        vote.setVoteType(VoteType.YES);
        vote.setCpf(cpf);
        voteBuilder.persist(vote);

        final var sessionId = session.getId().toString();

        given()
                .accept(ContentType.JSON)
                .when()
                .get(URL.concat("/result/").concat(sessionId))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


}
