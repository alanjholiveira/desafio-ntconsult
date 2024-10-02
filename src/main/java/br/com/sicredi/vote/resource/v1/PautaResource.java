package br.com.sicredi.vote.resource.v1;

import br.com.sicredi.vote.mapper.PautaMapper;
import br.com.sicredi.vote.resource.v1.request.PautaRequest;
import br.com.sicredi.vote.resource.v1.response.PautaResponse;
import br.com.sicredi.vote.service.PautaServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/v1/pautas")
@RestController
@AllArgsConstructor
@Validated
public class PautaResource {

    private final PautaServices service;

    @GetMapping
    public ResponseEntity<List<PautaResponse>> getAll() {
        log.info("Receiving request to search all poll");
        final var responseList = service.findAll().stream()
                .map(PautaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    public ResponseEntity<PautaResponse> cadastrarPauta(@RequestBody @Valid PautaRequest request) {
        log.info("Receiving a request to register a new pauta. {}", request);
        final var pauta = service.save(PautaMapper.toEntity(request));

        return new ResponseEntity<>(PautaMapper.toResponse(pauta), HttpStatus.CREATED);
    }

}
