package br.com.sicredi.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AssociateVoteUniqueException extends RuntimeException {

    public AssociateVoteUniqueException() {
        super("You have already voted for this session.");
    }
}