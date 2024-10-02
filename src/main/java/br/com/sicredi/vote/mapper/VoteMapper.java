package br.com.sicredi.vote.mapper;

import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.entity.Vote;
import br.com.sicredi.vote.enums.VoteType;
import br.com.sicredi.vote.resource.v1.request.VoteRequest;
import br.com.sicredi.vote.resource.v1.response.VoteResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteMapper {

    public static Vote toEntity(final VoteRequest request) {
        return Vote.builder()
                .pauta(Pauta.builder().id(UUID.fromString(request.pautaId())).build())
                .cpf(request.cpf())
                .voteType(VoteType.from(request.vote()))
                .build();
    }

    public static VoteResponse toResponse(final String message) {
        return VoteResponse.from(message);
    }

}
