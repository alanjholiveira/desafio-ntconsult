package br.com.sicredi.vote.build.entitiy;

import br.com.sicredi.vote.build.EntityBuilder;
import br.com.sicredi.vote.entity.Vote;
import br.com.sicredi.vote.enums.VoteType;
import br.com.sicredi.vote.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
public class VoteBuilder extends EntityBuilder<Vote, UUID> {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private PautaBuilder pautaBuilder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Override
    public Vote buildEntity() throws ParseException {
        return Vote.builder()
                .id(UUID.randomUUID())
                .voteType(VoteType.YES)
                .cpf("86514921035")
                .session(sessionBuilder.buildEntity())
                .pauta(pautaBuilder.buildEntity())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Vote persist(Vote entity) {
        return repository.save(entity);
    }

    @Override
    protected Collection<Vote> getByAll() {
        return repository.findAll();
    }

    @Override
    protected Optional<Vote> getById(UUID id) {
        return repository.findById(id);
    }
}
