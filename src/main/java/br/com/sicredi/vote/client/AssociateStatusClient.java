package br.com.sicredi.vote.client;

import br.com.sicredi.vote.client.response.StatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ObterSituacaoAssociado", url = "https://user-info.herokuapp.com/")
public interface AssociateStatusClient {

    @GetMapping(value = "/users/{cpf}", produces = "application/json")
    StatusResponse getStatus(@PathVariable("cpf") String cpf);

}