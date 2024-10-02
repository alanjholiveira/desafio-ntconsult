package br.com.sicredi.vote.service;

import br.com.sicredi.vote.entity.Pauta;
import br.com.sicredi.vote.repository.PautaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PautaServices {

    private final PautaRepository repository;

    @Transactional(readOnly = true)
    public List<Pauta> findAll() {
        log.info("Searching every pauta registered in the base.");
        return repository.findAll();
    }

    public Pauta save(final Pauta pauta) {
        log.info("Saving pauta at base");
        return repository.save(pauta);
    }

}
