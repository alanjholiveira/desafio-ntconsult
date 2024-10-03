package br.com.sicredi.vote.services;

import br.com.sicredi.vote.build.entitiy.PautaBuilder;
import br.com.sicredi.vote.build.entitiy.SessionBuilder;
import br.com.sicredi.vote.build.entitiy.VoteBuilder;
import br.com.sicredi.vote.client.AssociateStatusClient;
import br.com.sicredi.vote.client.response.StatusResponse;
import br.com.sicredi.vote.config.testcontainers.AbstractIntegrationTest;
import br.com.sicredi.vote.dto.ResultDTO;
import br.com.sicredi.vote.entity.Vote;
import br.com.sicredi.vote.enums.AssociateStatus;
import br.com.sicredi.vote.enums.SessionStatus;
import br.com.sicredi.vote.event.producer.ResultPautaProducer;
import br.com.sicredi.vote.exception.*;
import br.com.sicredi.vote.repository.SessionRepository;
import br.com.sicredi.vote.repository.VoteRepository;
import br.com.sicredi.vote.service.VoteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ResultPautaProducer producerEvent;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private PautaBuilder pautaBuilder;

    @Autowired
    private VoteBuilder builder;

    @Mock
    private AssociateStatusClient associateStatusClient;

    @Test
    void when_vote_returns_success() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();

        when(sessionRepository.existsByPautaId(any(UUID.class)))
                .thenReturn(true);
        when(sessionRepository.findByPautaId(any(UUID.class)))
                .thenReturn(Optional.of(session));

        when(repository.existsVoteByCpfAndPautaId(anyString(), any(UUID.class)))
                .thenReturn(Boolean.FALSE);
        when(sessionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(session));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);

        when(associateStatusClient.getStatus(anyString()))
                .thenReturn(StatusResponse.from(AssociateStatus.ABLE_TO_VOTE));

        final var result = service.vote(voteBuilder);

        assertNotNull(result);
        assertEquals("Vote registered successfully.", result);
    }

    @Test
    void when_vote_returns_voting_closed_exception() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();

        session.setExpiration(session.getCreatedAt());

        when(sessionRepository.existsByPautaId(any(UUID.class)))
                .thenReturn(true);
        when(sessionRepository.findByPautaId(any(UUID.class)))
                .thenReturn(Optional.of(session));
        when(repository.existsVoteByCpfAndPautaId(anyString(), any(UUID.class)))
                .thenReturn(Boolean.FALSE);
        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);


        assertThrows(VotingClosedException.class, () -> {
            service.vote(voteBuilder);
        });

    }

    @Test
    void when_vote_returns_session_not_found_exception() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();

        when(sessionRepository.existsByPautaId(any(UUID.class)))
                .thenReturn(false);
        when(sessionRepository.findByPautaId(any(UUID.class)))
                .thenReturn(Optional.of(session));
        when(repository.existsVoteByCpfAndPautaId(anyString(), any(UUID.class)))
                .thenReturn(Boolean.FALSE);
        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);


        assertThrows(NotFoundException.class, () -> {
            service.vote(voteBuilder);
        });
    }

    // API Client está offline
    @Test
    void when_vote_returns_associate_unable_to_vote_exception() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();

        when(sessionRepository.existsByPautaId(any(UUID.class)))
                .thenReturn(true);
        when(sessionRepository.findByPautaId(any(UUID.class)))
                .thenReturn(Optional.of(session));

        when(repository.existsVoteByCpfAndPautaId(anyString(), any(UUID.class)))
                .thenReturn(Boolean.FALSE);
        when(sessionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(session));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);

        when(associateStatusClient.getStatus(anyString()))
                .thenReturn(StatusResponse.from(AssociateStatus.UNABLE_TO_VOTE));

        assertThrows(AssociateUnableToVoteException.class, () -> {
            service.vote(voteBuilder);
        });
    }

    @Test
    void when_vote_returns_associate_vote_unique_exception() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();

        when(sessionRepository.existsByPautaId(any(UUID.class)))
                .thenReturn(true);
        when(sessionRepository.findByPautaId(any(UUID.class)))
                .thenReturn(Optional.of(session));

        when(repository.existsVoteByCpfAndPautaId(anyString(), any(UUID.class)))
                .thenReturn(Boolean.TRUE);
        when(sessionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(session));
        when(repository.save(voteBuilder)).thenReturn(voteBuilder);

        when(associateStatusClient.getStatus(anyString()))
                .thenReturn(StatusResponse.from(AssociateStatus.ABLE_TO_VOTE));

        assertThrows(AssociateVoteUniqueException.class, () -> {
            service.vote(voteBuilder);
        });
    }

    @Test
    void when_count_votes_session_return_success() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();
        session.setExpiration(session.getCreatedAt());
        session.setVotes(List.of(voteBuilder));

        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));

       final var result = service.countVotes(voteBuilder.getSession().getId());

       assertNotNull(result);
    }

    @Test
    void when_count_votes_session_return_session_not_count_vote_exception() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();
        session.setVotes(List.of(voteBuilder));

        when(sessionRepository.findById(voteBuilder.getSession().getId()))
                .thenReturn(Optional.of(session));

        assertThrows(SessionOpenException.class, () -> {
            service.countVotes(voteBuilder.getSession().getId());
        });
    }

    @Test
    void when_counting_votes_session_return_success() throws ParseException {
        final var voteBuilder = builder.buildEntity();
        final var session = voteBuilder.getSession();
        session.setExpiration(session.getCreatedAt());
        session.setVotes(List.of(voteBuilder));
        when(sessionRepository.findByStatus(SessionStatus.OPEN))
                .thenReturn(List.of(session));

        final var votes = session.getVotes();
        final var voteCounts = votes.stream()
                .collect(Collectors.groupingBy(Vote::getVoteType, Collectors.counting()));

        final var result = ResultDTO.from(session.getId().toString(), session.getPauta().getName(),
                (long) votes.size(), voteCounts, session.getStatus());

        service.countingVotesEvent();

        producerEvent.send(result);

        verify(producerEvent).send(isA(ResultDTO.class));
    }

}
