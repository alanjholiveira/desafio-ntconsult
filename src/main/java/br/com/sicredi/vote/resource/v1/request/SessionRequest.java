package br.com.sicredi.vote.resource.v1.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SessionRequest(
        UUID pautaId,
        LocalDateTime expiration
) {
}
