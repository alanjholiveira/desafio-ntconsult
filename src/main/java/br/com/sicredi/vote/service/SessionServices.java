package br.com.sicredi.vote.service;

import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.exception.NotFoundException;
import br.com.sicredi.vote.exception.SessionOpenException;
import br.com.sicredi.vote.repository.PautaRepository;
import br.com.sicredi.vote.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SessionServices {

    private final SessionRepository repository;
    private final PautaRepository pautaRepository;

    @Transactional
    public Session openSession(final Session entity) {
        log.info("Opening session.");
        entity.setExpiration(getExpiration(entity.getExpiration()));
        entity.setStatus(SessionStatus.OPEN);

        checkPautaOpen(entity.getPauta());

        final var pauta = getPauta(entity);

        entity.setPauta(pauta);

        log.info("Saving poll session to database");
        return repository.save(entity);
    }

    public Session getSessionByPautaID(final UUID pautaId) {
        return repository.findByPautaId(pautaId)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void checkSessionValid() {
        final var sessions = repository.findByStatus(SessionStatus.OPEN);

        for (final var session : sessions) {
            final var checkValid = checkExpiration(session.getExpiration());
            if (!checkValid) {
                session.setStatus(SessionStatus.FINISHED);
                repository.save(session);
            }
        }
    }

    private void checkPautaOpen(final Pauta pauta) {
        log.info("Checking if the poll has a registered section.");
        final var check = repository.existsByPauta(pauta);
        if (check.equals(Boolean.TRUE)) {
            log.error("Poll already has a registered section.");
            throw new SessionOpenException();
        }
    }

    private Pauta getPauta(final Session entity) {
        log.info("Searching for pauta at the base");
        return pautaRepository.findById(entity.getPauta().getId())
                .orElseThrow(NotFoundException::new);
    }

    private LocalDateTime getExpiration(final LocalDateTime expiration) {
        log.info("Checking session expiration.");
        return ObjectUtils.isEmpty(expiration) ? LocalDateTime.now().plusMinutes(1) : expiration;
    }

    private Boolean checkExpiration(final LocalDateTime expiration) {
        log.info("Checking session expiration.");
        return expiration.isAfter(LocalDateTime.now());
    }



}
