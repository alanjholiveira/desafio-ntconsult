package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.mapper.SessionMapper;
import br.com.sicredi.vote.resource.v1.request.SessionRequest;
import br.com.sicredi.vote.resource.v1.response.SessionResponse;
import br.com.sicredi.vote.service.SessionServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/v1/sessions")
@RestController
@AllArgsConstructor
public class SessionResource {

    private final SessionServices service;

    @PostMapping("/open" )
    public ResponseEntity<SessionResponse> openSession(@RequestBody @Valid SessionRequest request) {
        log.info("Receiving session opening request. {}", request);
        final var result = service.openSession(SessionMapper.toEntity(request));

        return new ResponseEntity<>(SessionMapper.toResponse(result), HttpStatus.CREATED);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<SessionResponse> getSessionByPautaID(@PathVariable String pautaId) {
        log.info("Receiving session opening request. {}", pautaId);
        final var session = service.getSessionByPautaID(UUID.fromString(pautaId));

        return ResponseEntity.ok(SessionMapper.toResponse(session));

    }

}
