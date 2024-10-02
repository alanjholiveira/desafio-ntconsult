package br.com.sicredi.vote.build;

import java.text.ParseException;
import java.util.Collection;
import java.util.Optional;

public abstract class EntityBuilder<E, T> {

    private EntityCustomization<E> customization;

    /**
     * Constroi a entidade, realizando as custimizações, caso necessário e
     * persistindo a mesma no banco
     *
     * @return entidade construída
     * @throws ParseException Exceção a ser lançada
     */
    public E build() throws ParseException {
        final E entity = buildEntity();
        if (isCustomized()) {
            customization.execute(entity);
        }
        return persist(entity);
    }

    /**
     * Este método permite a customização dos atributos da entidade antes da
     * persistência
     *
     * @param customization customizacao
     * @return entidade customizada
     */
    public EntityBuilder<E, T> customizar(EntityCustomization<E> customization) {
        this.customization = customization;
        return this;
    }

    /**
     * Este método deve retornar uma instância da entidade inicializada com os
     * dados padrão para todos os testes.
     *
     * @return entidade construída
     * @throws ParseException Exceção a ser lançada
     */
    public abstract E buildEntity() throws ParseException;

    /**
     * Este método deve persistir e retornar a entidade recebida no parametro
     * <b>entidade</b>
     *
     * @param entity entidade
     * @return entidade persistida
     */
    public abstract E persist(E entity);

    /**
     * Este método deve persistir e retornar a entidade recebida no parametro
     * <b>entidade</b>
     *
     * @return entidade persistida
     */
    protected abstract Collection<E> getByAll();

    /**
     * Este método deve persistir e retornar a entidade recebida no parametro
     * <b>entidade</b>
     *
     * @param id id
     * @return entidade persistida
     */
    protected abstract Optional<E> getById(T id);

    /**
     * Is customizado boolean.
     *
     * @return boolean
     */
    public boolean isCustomized() {
        return this.customization != null;
    }

    /**
     * @param customization Atribui o valor do parâmetro no atributo customizacao
     */
    public void setCustomization(EntityCustomization<E> customization) {
        this.customization = customization;
    }


}
