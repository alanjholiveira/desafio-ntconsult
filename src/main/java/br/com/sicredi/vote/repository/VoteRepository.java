package br.com.sicredi.vote.repository;

import br.com.sicredi.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    Boolean existsVoteByCpfAndPautaId(final String cpf, final UUID pautaId);

}