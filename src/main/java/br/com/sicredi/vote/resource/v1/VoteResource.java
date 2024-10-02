package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.mapper.ResultMapper;
import br.com.sicredi.vote.mapper.VoteMapper;
import br.com.sicredi.vote.resource.v1.request.VoteRequest;
import br.com.sicredi.vote.resource.v1.response.ResultResponse;
import br.com.sicredi.vote.resource.v1.response.VoteResponse;
import br.com.sicredi.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/v1/votes")
@RestController
@AllArgsConstructor
public class VoteResource {

    private final VoteService service;


    @PostMapping
    public ResponseEntity<VoteResponse> vote(@RequestBody @Valid VoteRequest request) {
        log.info("Receiving request to compute vote. Pauta: {}", request.pautaId());
        final var message = service.vote(VoteMapper.toEntity(request));

        return ResponseEntity.ok(VoteMapper.toResponse(message));

    }

    @GetMapping("/result/{sessionId}")
    public ResponseEntity<ResultResponse> countVotes(@PathVariable String sessionId) {
        log.info("Receiving request to post result. {}", sessionId);
        final var result = service.countVotes(UUID.fromString(sessionId));
        return ResponseEntity.ok(ResultMapper.toResponse(result));
    }

}
