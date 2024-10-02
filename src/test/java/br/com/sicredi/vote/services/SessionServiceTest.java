package br.com.sicredi.vote.services;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.build.entitiy.SessionBuilder;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.exception.NotFoundException;
import br.com.sicredi.vote.exception.SessionOpenException;
import br.com.sicredi.vote.repository.PautaRepository;
import br.com.sicredi.vote.repository.SessionRepository;
import br.com.sicredi.vote.service.SessionServices;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SessionServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private SessionServices service;

    @Mock
    private SessionRepository repository;

    @Mock
    private PautaRepository pautaRepository;

    @Autowired
    private SessionBuilder builder;

    @Autowired
    private PautaBuilder pautaBuilder;


    @Test
    void when_open_session_return_success() throws ParseException {
        final var sessionBuilder = builder.buildEntity();
        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(pautaRepository.findById(sessionBuilder.getPauta().getId()))
                .thenReturn(Optional.of(pautaBuilder.buildEntity()));

        final var result = service.openSession(sessionBuilder);

        assertNotNull(result);
        assertEquals(sessionBuilder, result);
    }

    @Test
    void when_open_session_return_bad_request_exist_poll() throws ParseException {
        final var sessionBuilder = builder.buildEntity();

        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(repository.existsByPauta(sessionBuilder.getPauta())).thenReturn(Boolean.TRUE);
        when(pautaRepository.findById(sessionBuilder.getPauta().getId()))
                .thenReturn(Optional.of(pautaBuilder.buildEntity()));

        assertThrows(SessionOpenException.class, () -> {
            service.openSession(sessionBuilder);
        });

    }

    @Test
    void when_open_session_return_not_found_poll() throws ParseException {
        final var sessionBuilder = builder.buildEntity();

        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(pautaRepository.findById(sessionBuilder.getPauta().getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.openSession(sessionBuilder);
        });

    }

}
