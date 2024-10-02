package br.com.sicredi.vote.client.response;

import br.com.sicredi.vote.enums.AssociateStatus;

public record StatusResponse(
        AssociateStatus status
) {

    public static StatusResponse from(final AssociateStatus status) {
        return new StatusResponse(status);
    }

}
