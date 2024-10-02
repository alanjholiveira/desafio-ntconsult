package br.com.sicredi.vote.resource.v1.response;

import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.enums.VoteType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ResultResponse(
        String id,
        String pauta,
        Long countVotes,
        Map<VoteType, Long> countByType,
        String status
) {

    public static ResultResponse from(final String id, final String pauta,
                                      final Long countVotes, final Map<VoteType, Long> countByType,
                                      final String status) {
        return new ResultResponse(id, pauta, countVotes, countByType, status);
    }

}
