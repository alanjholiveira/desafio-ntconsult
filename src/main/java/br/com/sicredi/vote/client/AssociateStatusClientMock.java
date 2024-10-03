package br.com.sicredi.vote.client;

import br.com.sicredi.vote.client.response.StatusResponse;
import br.com.sicredi.vote.enums.AssociateStatus;
import org.springframework.stereotype.Component;

@Component
public class AssociateStatusClientMock implements AssociateStatusClient {

    @Override
    public StatusResponse getStatus(final String cpf) {

        return StatusResponse.from(checkCpf(cpf));
    }

    private AssociateStatus checkCpf(final String cpfValue) {
        // Remove qualquer máscara (pontos, traços, etc.)
        final var cpf = cpfValue.replaceAll("[^\\d]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return AssociateStatus.UNABLE_TO_VOTE;
        }

        // Verifica se todos os dígitos são iguais, o que é inválido
        if (cpf.matches("(\\d)\\1{10}")) {
            return AssociateStatus.UNABLE_TO_VOTE;
        }

        // Cálculo do primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier >= 10) {
            firstVerifier = 0;
        }

        // Verifica se o primeiro dígito verificador é válido
        if (firstVerifier != (cpf.charAt(9) - '0')) {
            return AssociateStatus.UNABLE_TO_VOTE;
        }

        // Cálculo do segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier >= 10) {
            secondVerifier = 0;
        }

        // Verifica se o segundo dígito verificador é válido
        if (secondVerifier != (cpf.charAt(10) - '0')) {
            return AssociateStatus.UNABLE_TO_VOTE;
        }

        // CPF válido
        return AssociateStatus.ABLE_TO_VOTE;

    }

}
