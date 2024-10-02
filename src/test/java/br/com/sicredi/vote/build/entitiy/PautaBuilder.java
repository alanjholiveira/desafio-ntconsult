package br.com.sicredi.vote.build.entitiy;

import br.com.sicredi.vote.build.EntityBuilder;
import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
public class PautaBuilder extends EntityBuilder<Pauta, UUID> {

    @Autowired
    private PautaRepository repository;

    @Override
    public Pauta buildEntity() throws ParseException {
        setCustomization(null);
        return Pauta.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Pauta persist(Pauta entity) {
        return repository.save(entity);
    }

    @Override
    protected Collection<Pauta> getByAll() {
        return repository.findAll();
    }

    @Override
    protected Optional<Pauta> getById(UUID id) {
        return repository.findById(id);
    }
}
