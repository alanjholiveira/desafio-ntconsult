package br.com.sicredi.vote.resource.v1.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SessionResponse(
        UUID sessionId,
        String pautaName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime expiration,
        Boolean isOpenSession

) {

    public static SessionResponse from(final UUID id, final String pauta, final LocalDateTime expiration,
                                       final Boolean isOpenSession) {
        return new SessionResponse(id, pauta, expiration, isOpenSession);
    }

}
