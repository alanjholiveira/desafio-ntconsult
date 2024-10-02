package br.com.sicredi.vote.build;

/**
 * Interface que define um contrato para permitir a custimização de uma entidade
 * no momento de sua contrução, para utilização em testes
 *
 * @param <E> Tipo da entidade a ser customizado
 */
public interface EntityCustomization<E> {

    /**
     * Executa a customização da entidade
     *
     * @param entity a ser customizada
     */
    void execute(E entity);

}
