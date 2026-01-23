package com.modulink.Model.Relazioni.Partecipazione;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta la chiave primaria composta per l'entità {@link PartecipazioneEntity}.
 * <p>
 * Definisce l'unicità di una partecipazione: un utente non può partecipare allo stesso evento più di una volta.
 * Include l'ID Azienda per la coerenza multi-tenant.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.2.0
 */
public class PartecipazioneID implements Serializable {
    /**
     * ID dell'utente partecipante.
     */
    private int id_utente;

    /**
     * ID dell'evento.
     */
    private int id_evento;

    /**
     * ID dell'azienda di appartenenza.
     */
    private int id_azienda;

    /**
     * Costruttore vuoto.
     *
     * @since 1.2.0
     */
    public PartecipazioneID() {
    }

    /**
     * Costruttore completo.
     *
     * @param id_utente  ID Utente.
     * @param id_evento  ID Evento.
     * @param id_azienda ID Azienda.
     * @since 1.2.0
     */
    public PartecipazioneID(int id_utente, int id_evento, int id_azienda) {
        this.id_utente = id_utente;
        this.id_evento = id_evento;
        this.id_azienda = id_azienda;
    }

    /**
     * Recupera l'ID utente.
     *
     * @return ID utente.
     * @since 1.2.0
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID utente.
     *
     * @param id_utente Nuovo ID utente.
     * @since 1.2.0
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Recupera l'ID evento.
     *
     * @return ID evento.
     * @since 1.2.0
     */
    public int getId_evento() {
        return id_evento;
    }

    /**
     * Imposta l'ID evento.
     *
     * @param id_evento Nuovo ID evento.
     * @since 1.2.0
     */
    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    /**
     * Recupera l'ID azienda.
     *
     * @return ID azienda.
     * @since 1.2.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID azienda.
     *
     * @param id_azienda Nuovo ID azienda.
     * @since 1.2.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Verifica l'uguaglianza tra due chiavi.
     *
     * @param o Oggetto da confrontare.
     * @return true se identici.
     * @since 1.2.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartecipazioneID that = (PartecipazioneID) o;
        return id_utente == that.id_utente && id_evento == that.id_evento && id_azienda == that.id_azienda;
    }

    /**
     * Genera hash code.
     *
     * @return Hash code.
     * @since 1.2.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_utente, id_evento, id_azienda);
    }

    /**
     * Rappresentazione stringa dell'oggetto.
     *
     * @return Stringa descrittiva.
     * @since 1.2.0
     */
    @Override
    public String toString() {
        return "PartecipazioneID{" +
                "id_utente=" + id_utente +
                ", id_evento=" + id_evento +
                ", id_azienda=" + id_azienda +
                '}';
    }
}