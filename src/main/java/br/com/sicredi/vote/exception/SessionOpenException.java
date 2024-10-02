package br.com.sicredi.vote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SessionOpenException extends RuntimeException {

    public SessionOpenException() {
        super("Pauta is now open for voting");
    }

    public SessionOpenException(final String message) {
        super(message);
    }
}
