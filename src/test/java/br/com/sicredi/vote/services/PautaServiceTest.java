package br.com.sicredi.vote.services;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.repository.PautaRepository;
import br.com.sicredi.vote.service.PautaServices;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class PautaServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private PautaServices service;

    @Mock
    private PautaRepository repository;

    @Autowired
    private PautaBuilder builder;

    @Test
    void when_getAll_returns_success() throws ParseException {
        final var pautaBuilder = builder.buildEntity();
        when(repository.findAll()).thenReturn(List.of(pautaBuilder));

        final var result = service.findAll();

        assertNotNull(result);
        assertEquals(List.of(pautaBuilder), result);
    }

    @Test
    void when_save_return_success() throws ParseException {
        final var pautaBuilder = builder.buildEntity();
        when(repository.save(pautaBuilder)).thenReturn(pautaBuilder);

        final var result = service.save(pautaBuilder);

        assertNotNull(result);
        assertEquals(pautaBuilder, result);
    }


}
