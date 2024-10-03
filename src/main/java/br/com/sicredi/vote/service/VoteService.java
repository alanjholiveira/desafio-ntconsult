package br.com.sicredi.vote.service;

import br.com.sicredi.vote.client.AssociateStatusClient;
import br.com.sicredi.vote.client.response.StatusResponse;
import br.com.sicredi.vote.dto.ResultDTO;
import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.entity.Session;
import br.com.sicredi.vote.entity.Vote;
import br.com.sicredi.vote.enums.AssociateStatus;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.enums.VoteType;
import br.com.sicredi.vote.event.producer.ResultPautaProducer;
import br.com.sicredi.vote.exception.*;
import br.com.sicredi.vote.repository.SessionRepository;
import br.com.sicredi.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final SessionRepository sessionRepository;
    private final AssociateStatusClient associateStatusClient;
    private final ResultPautaProducer resultPautaProducer;

    @Transactional
    public String vote(Vote entity) {
        log.info("Computing vote.");

        final var checkSession = getCheckSession(entity.getPauta().getId());
        if (!checkSession) {
            log.info("No Sessions Found");
            throw new NotFoundException("No Sessions Found");
        }

        final var openSession = getOpenSessionByPautaId(entity.getPauta().getId());
        if (openSession.isOpenSession().equals(Boolean.FALSE)) {
            log.info("It is not possible to compute the vote with Session closed.");
            throw new VotingClosedException();
        }

        final var checkCpf = checkCpfIsVote(entity.getCpf());
        if (AssociateStatus.UNABLE_TO_VOTE.equals(checkCpf)) {
            log.info("Member not authorized to vote.");
            throw new AssociateUnableToVoteException();
        }

        final var isVote = isVote(entity.getCpf(), entity.getPauta());
        if (isVote.equals(Boolean.TRUE)) {
            log.info("Member has already voted in this session.");
            throw new AssociateVoteUniqueException();
        }

        entity.setSession(openSession);
        repository.save(entity);
        log.info("Vote successfully tallied.");

        return "Vote registered successfully.";

    }

    public ResultDTO countVotes(final UUID sessionId) {
        log.info("Checking if the agenda is closed. Session: {}", sessionId);
        Session session = getOpenSessionById(sessionId);

        if (session.isOpenSession().equals(Boolean.TRUE)) {
            final var message = String.format("It is not possible to count votes from the poll with an open session. " +
                    "Session: %s", sessionId);
            log.info(message);
            throw new SessionOpenException(message);
        }
        log.info("Votes successfully counted. Session: {}", sessionId);
        return getResultBuild(session);

    }

    public void countingVotesEvent() {
        log.info("Starting vote counting");
        getCloseSession().stream()
                .map(this::getResultBuild)
                .forEach(resultPautaProducer::send);
    }

    private List<Session> getCloseSession() {
        final var sessions = sessionRepository.findByStatus(SessionStatus.OPEN).parallelStream()
                .filter(session -> session.isOpenSession().equals(Boolean.FALSE))
                .map(session -> {
                    session.setStatus(SessionStatus.CLOSED);
                    return session;
                }).toList();

        return sessionRepository.saveAll(sessions);
    }

    private ResultDTO getResultBuild(final Session session) {
        final var votes = session.getVotes();
        final var voteCounts = votes.stream()
                .collect(Collectors.groupingBy(Vote::getVoteType, Collectors.counting()));

        return ResultDTO.from(session.getId().toString(), session.getPauta().getName(),
                (long) session.getVotes().size(), voteCounts, session.getStatus());
    }


    private AssociateStatus checkCpfIsVote(final String cpf) {
        // API Está offline, foi criado MOCK com comportamento proximo da API
         final var result = Optional.of(associateStatusClient.getStatus(cpf));
//        final var result = Optional.of(StatusResponse.from(AssociateStatus.ABLE_TO_VOTE));

        return result.map(StatusResponse::status)
                .orElseThrow(NotFoundException::new);
    }

    private Session getOpenSessionByPautaId(final UUID pautaId) {
        return sessionRepository.findByPautaId(pautaId)
                .orElseThrow(NotFoundException::new);
    }

    private Session getOpenSessionById(final UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(NotFoundException::new);
    }

    private Boolean getCheckSession(final UUID pautaId) {
        return sessionRepository.existsByPautaId(pautaId);
    }

    private Boolean isVote(final String cpf, final Pauta pauta) {
        return repository.existsVoteByCpfAndPautaId(cpf, pauta.getId());
    }

}
