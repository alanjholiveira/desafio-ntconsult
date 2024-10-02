package br.com.sicredi.vote.event.producer;

import br.com.sicredi.vote.dto.ResultDTO;
import br.com.sicredi.vote.event.input.MsgInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ResultPautaProducer {

    @Value("${event.rabbitmq.desafio.exchange}")
    private String exchange;

    @Value("${event.rabbitmq.desafio.exchange}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public ResultPautaProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(final ResultDTO result) {
        final var msgInput = new MsgInput<ResultDTO>();
        msgInput.setId(UUID.randomUUID());
        msgInput.setMessage(result);

        log.info("Sending event containing result pauta: {}.", msgInput.getId());
        rabbitTemplate.convertAndSend(exchange, routingKey, msgInput);
    }

}
