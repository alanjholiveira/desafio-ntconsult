package br.com.sicredi.vote.dto;

import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.enums.VoteType;

import java.util.Map;

public record ResultDTO(
        String id,
        String name,
        Long count,
        Map<VoteType, Long> countByType,
        SessionStatus status
) {

    public static ResultDTO from(final String id, final String name, final Long count,
                                 final Map<VoteType, Long> countByType, final SessionStatus status ) {
        return new ResultDTO(id, name, count, countByType, status);
    }

}
