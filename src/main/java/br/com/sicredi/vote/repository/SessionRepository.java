package br.com.sicredi.vote.repository;

import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    Boolean existsByPauta(final Pauta pauta);

    Boolean existsByPautaId(final UUID pautaId);

    List<Session> findByStatus(final SessionStatus status);

    Optional<Session> findByPautaId(final UUID pautaId);
}
