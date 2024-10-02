package br.com.sicredi.vote.resource.v1.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VoteRequest(
        String pautaId,
        String cpf,
        Boolean vote
) {
}
