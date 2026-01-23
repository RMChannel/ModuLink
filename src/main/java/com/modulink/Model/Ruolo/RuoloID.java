package com.modulink.Model.Ruolo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe che definisce la struttura della chiave primaria composta per l'entità {@link RuoloEntity}.
 * <p>
 * In accordo con le specifiche JPA 3.x, quando un'entità utilizza una chiave composta derivata da relazioni
 * (es. <code>@ManyToOne</code> che fa parte dell'ID), è necessario definire una classe ID corrispondente.
 * </p>
 * <p>
 * La scelta di una chiave composta (ID Ruolo + ID Azienda) abilita lo schema Multi-Tenancy a livello di applicazione,
 * permettendo la duplicazione degli ID sequenziali dei ruoli attraverso diversi tenant.
 * </p>
 *
 * @see RuoloEntity
 * @author Modulink Team
 * @version 1.5.0
 * @since 1.0.0
 */
public class RuoloID implements Serializable {

    /**
     * Identificativo numerico locale del ruolo.
     * <p>
     * Corrisponde all'attributo {@code id_ruolo} dell'entità {@link RuoloEntity}.
     * Il nome del campo deve coincidere esattamente con quello della classe Entity.
     * </p>
     *
     * @since 1.0.0
     */
    private int id_ruolo;

    /**
     * Identificativo numerico dell'azienda (Foreign Key).
     * <p>
     * Corrisponde all'attributo di relazione {@code azienda} dell'entità {@link RuoloEntity}.
     * Anche se nell'entità è un oggetto, qui viene mappato come tipo primitivo (int)
     * corrispondente al tipo della Primary Key dell'entità target ({@link com.modulink.Model.Azienda.AziendaEntity}).
     * </p>
     *
     * @since 1.0.0
     */
    private int azienda;

    /**
     * Costruttore predefinito (No-Args).
     * <p>
     * Essenziale per permettere al provider JPA (Hibernate) di istanziare la classe
     * durante le operazioni di caricamento (Lazy Loading) o merge.
     * </p>
     *
     * @since 1.0.0
     */
    public RuoloID(){}

    /**
     * Costruttore parametrico per la creazione diretta di istanze della chiave.
     *
     * @param id_ruolo L'ID numerico del ruolo.
     * @param azienda  L'ID numerico dell'azienda associata.
     * @since 1.0.0
     */
    public RuoloID(int id_ruolo, int azienda) {
        this.id_ruolo = id_ruolo;
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza logica tra due istanze di chiave composta.
     * <p>
     * Il confronto avviene per valore su entrambi i campi costitutivi ({@code id_ruolo} e {@code azienda}).
     * Fondamentale per il corretto funzionamento delle Collection Java e delle Cache di secondo livello di Hibernate.
     * </p>
     *
     * @param o L'oggetto da confrontare.
     * @return {@code true} se gli oggetti sono strutturalmente identici, {@code false} altrimenti.
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuoloID ruoloID = (RuoloID) o;
        return id_ruolo == ruoloID.id_ruolo && azienda == ruoloID.azienda;
    }

    /**
     * Calcola il codice hash univoco per la chiave composta.
     * <p>
     * Utilizza {@link Objects#hash(Object...)} per generare un hash consistente basato
     * sui valori dei campi {@code id_ruolo} e {@code azienda}.
     * </p>
     *
     * @return Valore intero dell'hash.
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_ruolo, azienda);
    }

    /**
     * Recupera l'identificativo del ruolo.
     * @return L'ID intero del ruolo.
     * @since 1.0.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta l'identificativo del ruolo.
     * @param id_ruolo Il nuovo ID intero.
     * @since 1.0.0
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Recupera l'identificativo dell'azienda.
     * @return L'ID intero dell'azienda.
     * @since 1.0.0
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'identificativo dell'azienda.
     * @param azienda Il nuovo ID intero dell'azienda.
     * @since 1.0.0
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }
}