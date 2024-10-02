package br.com.sicredi.vote.resource.v1.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PautaResponse(
        UUID id,
        String name,
        String description
) {

    public static PautaResponse from(final UUID id, final String name, final String description) {
        return new PautaResponse(id, name, description);
    }

}
