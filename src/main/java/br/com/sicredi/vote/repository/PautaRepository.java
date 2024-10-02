package br.com.sicredi.vote.repository;

import br.com.sicredi.vote.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {
}
