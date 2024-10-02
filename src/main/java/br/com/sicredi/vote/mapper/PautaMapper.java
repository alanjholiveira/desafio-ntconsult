package br.com.sicredi.vote.mapper;


import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.resource.v1.request.PautaRequest;
import br.com.sicredi.vote.resource.v1.response.PautaResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PautaMapper {

    public static PautaResponse toResponse(final Pauta entity) {
        return PautaResponse.from(entity.getId(), entity.getName(), entity.getDescription());
    }

    public static Pauta toEntity(PautaRequest request) {
        return Pauta.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

}
