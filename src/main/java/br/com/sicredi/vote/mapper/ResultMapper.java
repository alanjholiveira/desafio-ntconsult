package br.com.sicredi.vote.mapper;

import br.com.sicredi.vote.dto.ResultDTO;
import br.com.sicredi.vote.resource.v1.response.ResultResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultMapper {

    public static ResultResponse toResponse(final ResultDTO result) {
        return ResultResponse.from(result.id(), result.name(), result.count(),
                result.countByType(), result.status().name());
    }

}
