package br.com.sicredi.vote.mapper;

import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.resource.v1.request.SessionRequest;
import br.com.sicredi.vote.resource.v1.response.SessionResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionMapper {

    public static SessionResponse toResponse(final Session session) {
        return SessionResponse.from(session.getId(), session.getPauta().getName(),
                        session.getExpiration(), session.isOpenSession());
    }

    public static Session toEntity(final SessionRequest request) {
        return Session.builder()
                .expiration(request.expiration())
                .pauta(Pauta.builder().id(request.pautaId()).build())
                .build();
    }
}