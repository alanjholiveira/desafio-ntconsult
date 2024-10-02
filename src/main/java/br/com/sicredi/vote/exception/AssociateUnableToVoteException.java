package br.com.sicredi.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)

public class AssociateUnableToVoteException extends RuntimeException {

    public AssociateUnableToVoteException() {
        super("You are not authorized for this vote.");
    }

}
