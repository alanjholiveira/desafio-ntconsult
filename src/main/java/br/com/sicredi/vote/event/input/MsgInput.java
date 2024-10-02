package br.com.sicredi.vote.event.input;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MsgInput<I> {

    private UUID id;
    private I message;

}
