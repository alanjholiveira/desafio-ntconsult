package br.com.sicredi.vote.build.entitiy;

import br.com.sicredi.vote.build.EntityBuilder;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionBuilder extends EntityBuilder<Session, UUID> {

    private SessionRepository repository;

    private PautaBuilder pautaBuilder;

    public SessionBuilder(SessionRepository repository, PautaBuilder pautaBuilder) {
        this.repository = repository;
        this.pautaBuilder = pautaBuilder;
    }

    @Override
    public Session buildEntity() throws ParseException {
        return Session.builder()
                .id(UUID.randomUUID())
                .pauta(pautaBuilder.buildEntity())
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Session persist(Session entity) {
        return repository.save(entity);
    }

    @Override
    protected Collection<Session> getByAll() {
        return repository.findAll();
    }

    @Override
    protected Optional<Session> getById(UUID id) {
        return repository.findById(id);
    }
}
