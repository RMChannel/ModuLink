package com.modulink.Model.Eventi;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe che definisce la chiave primaria composta per l'entità {@link EventoEntity}.
 * <p>
 * Implementa l'interfaccia {@link Serializable} come richiesto dalle specifiche JPA per le classi ID composite.
 * Questa classe viene utilizzata in congiunzione con l'annotazione {@link jakarta.persistence.IdClass}
 * per mappare la chiave composta formata da {@code id_evento} e {@code azienda}.
 * </p>
 * <p>
 * È fondamentale che i nomi dei campi in questa classe corrispondano esattamente ai nomi
 * dei campi annotati con {@code @Id} nell'entità principale.
 * </p>
 *
 * @see EventoEntity
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.2.0
 */
public class EventoID implements Serializable {

    /**
     * Identificativo locale dell'evento.
     * Corrisponde al campo {@code id_evento} in {@link EventoEntity}.
     *
     * @since 1.2.0
     */
    private int id_evento;

    /**
     * Identificativo dell'azienda (Foreign Key).
     * Corrisponde al campo {@code azienda} in {@link EventoEntity}.
     * <p>
     * Nota: JPA mappera automaticamente l'intero della foreign key a questo campo.
     * </p>
     *
     * @since 1.2.0
     */
    private int azienda;

    /**
     * Costruttore vuoto di default.
     * Necessario per la deserializzazione e l'instanziazione tramite reflection.
     *
     * @since 1.2.0
     */
    public EventoID() {}

    /**
     * Costruttore completo per la creazione di istanze della chiave.
     *
     * @param id_evento ID locale evento.
     * @param azienda   ID azienda.
     * @since 1.2.0
     */
    public EventoID(int id_evento, int azienda) {
        this.id_evento = id_evento;
        this.azienda = azienda;
    }

    /**
     * Verifica l'uguaglianza tra due istanze della chiave composta.
     * <p>
     * Fondamentale per il corretto funzionamento del caching di primo e secondo livello di Hibernate/JPA.
     * </p>
     *
     * @param o Oggetto con cui confrontare.
     * @return {@code true} se gli oggetti sono identici per valore.
     * @since 1.2.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoID eventoID = (EventoID) o;
        return id_evento == eventoID.id_evento && azienda == eventoID.azienda;
    }

    /**
     * Calcola l'hash code per l'istanza della chiave.
     * <p>
     * Deve essere coerente con il metodo {@link #equals(Object)}.
     * </p>
     *
     * @return Valore hash calcolato sui campi componenti la chiave.
     * @since 1.2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_evento, azienda);
    }

    /**
     * Getter per id_evento.
     * @return ID locale evento.
     * @since 1.2.0
     */
    public int getId_evento() {
        return id_evento;
    }

    /**
     * Setter per id_evento.
     * @param id_evento Nuovo ID locale.
     * @since 1.2.0
     */
    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    /**
     * Getter per id_azienda.
     * @return ID azienda.
     * @since 1.2.0
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Setter per id_azienda.
     * @param azienda Nuovo ID azienda.
     * @since 1.2.0
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }
}
