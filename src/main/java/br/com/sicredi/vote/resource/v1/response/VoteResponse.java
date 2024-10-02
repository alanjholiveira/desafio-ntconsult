package br.com.sicredi.vote.resource.v1.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VoteResponse(
        String message
){

    public static VoteResponse from(final String message) {
        return new VoteResponse(message);
    }

}
