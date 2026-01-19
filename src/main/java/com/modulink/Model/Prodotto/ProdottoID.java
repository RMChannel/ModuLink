package com.modulink.Model.Prodotto;

import java.util.Objects;

/**
 * Classe che definisce la struttura della chiave primaria composta per l'entità {@link ProdottoEntity}.
 * <p>
 * Questa classe POJO è utilizzata dall'ORM per gestire l'identità univoca dei record nella tabella prodotti,
 * combinando l'identificativo sequenziale locale del prodotto e l'identificativo dell'azienda proprietaria.
 * </p>
 *
 * @see ProdottoEntity
 * @author Modulink Team
 * @version 1.0.5
 * @since 1.2.0
 */
public class ProdottoID {

    /**
     * Identificativo locale del prodotto.
     * Corrisponde al campo annotato con <code>@Id</code> in {@link ProdottoEntity}.
     *
     * @since 1.2.0
     */
    private int id_prodotto;

    /**
     * Identificativo dell'azienda (Foreign Key).
     * Corrisponde al campo di relazione annotato con <code>@Id</code> in {@link ProdottoEntity}.
     *
     * @since 1.2.0
     */
    private int azienda;

    /**
     * Costruttore di default.
     * Necessario per l'istanziazione tramite reflection da parte del provider di persistenza.
     *
     * @since 1.2.0
     */
    public ProdottoID() {}

    /**
     * Costruttore completo per la creazione di istanze della chiave.
     *
     * @param id_prodotto ID numerico locale del prodotto.
     * @param azienda     ID numerico dell'azienda.
     * @since 1.2.0
     */
    public ProdottoID(int id_prodotto, int azienda) {
        this.id_prodotto = id_prodotto;
        this.azienda = azienda;
    }

    /**
     * Recupera l'ID locale del prodotto.
     * @return Intero ID.
     * @since 1.2.0
     */
    public int getId_prodotto() {
        return id_prodotto;
    }

    /**
     * Imposta l'ID locale del prodotto.
     * @param id_prodotto Nuovo ID.
     * @since 1.2.0
     */
    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    /**
     * Recupera l'ID dell'azienda.
     * @return Intero ID azienda.
     * @since 1.2.0
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     * @param azienda Nuovo ID azienda.
     * @since 1.2.0
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza logica tra due chiavi composte.
     * <p>
     * Due chiavi sono considerate uguali se entrambi i componenti (id_prodotto e azienda) coincidono.
     * </p>
     *
     * @param o L'oggetto da confrontare.
     * @return <code>true</code> se gli oggetti sono uguali, <code>false</code> altrimenti.
     * @since 1.2.0
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProdottoID that = (ProdottoID) o;
        return id_prodotto == that.id_prodotto && azienda == that.azienda;
    }

    /**
     * Calcola l'hash code della chiave composta.
     * <p>
     * Utilizza l'algoritmo standard di {@link Objects#hash(Object...)} per garantire una distribuzione uniforme
     * e la coerenza con il metodo equals.
     * </p>
     *
     * @return Valore intero dell'hash.
     * @since 1.2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_prodotto, azienda);
    }
}
