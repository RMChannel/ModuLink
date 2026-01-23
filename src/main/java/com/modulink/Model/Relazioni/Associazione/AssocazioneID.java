package com.modulink.Model.Relazioni.Associazione;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta la chiave primaria composta per l'entità di unione {@link AssociazioneEntity}, gestendo l'identità della relazione tra Utenti e Ruoli.
 * <p>
 * Questa classe implementa l'interfaccia {@link Serializable} per conformità alle specifiche JPA riguardanti le chiavi composte (Composite Keys).
 * L'identità è definita dalla tripla di attributi:
 * <ul>
 * <li><strong>ID_Utente:</strong> Identificativo dell'utente assegnatario.</li>
 * <li><strong>ID_Ruolo:</strong> Identificativo del ruolo assegnato.</li>
 * <li><strong>ID_Azienda:</strong> Discriminante del tenant che garantisce l'integrità referenziale cross-entità.</li>
 * </ul>
 *
 * <p>
 * La presenza di <code>id_azienda</code> come parte della chiave primaria composta riflette il modello multi-tenant del sistema,
 * dove utenti e ruoli sono logicamente isolati per azienda.
 * </p>
 *
 * @see AssociazioneEntity
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.0.0
 */
public class AssocazioneID implements Serializable {

    /**
     * Identificativo univoco dell'Utente.
     * <p>
     * Parte della chiave esterna composta verso la tabella <code>Utente</code>.
     * </p>
     *
     * @since 1.0.0
     */
    private int id_utente;

    /**
     * Identificativo univoco del Ruolo.
     * <p>
     * Parte della chiave esterna composta verso la tabella <code>Ruolo</code>.
     * </p>
     *
     * @since 1.0.0
     */
    private int id_ruolo;

    /**
     * Identificativo dell'Azienda (Tenant Identifier).
     * <p>
     * Campo critico per l'integrità dei dati: assicura che un utente possa essere associato
     * esclusivamente a ruoli definiti all'interno della medesima organizzazione.
     * </p>
     *
     * @since 1.0.0
     */
    private int id_azienda;

    /**
     * Costruttore predefinito richiesto per la corretta istanziazione tramite i provider di persistenza (JPA/Hibernate).
     *
     * @since 1.0.0
     */
    public AssocazioneID() {}

    /**
     * Costruttore parametrico per l'inizializzazione manuale della chiave composta.
     *
     * @param id_utente  Identificativo dell'utente.
     * @param id_ruolo   Identificativo del ruolo.
     * @param id_azienda Identificativo dell'azienda.
     * @since 1.0.0
     */
    public AssocazioneID(int id_utente, int id_ruolo, int id_azienda) {
        this.id_utente = id_utente;
        this.id_ruolo = id_ruolo;
        this.id_azienda = id_azienda;
    }

    /**
     * Verifica l'uguaglianza tra istanze di chiavi composte basandosi sulla corrispondenza di tutti i campi.
     *
     * @param o Oggetto da confrontare.
     * @return true se i tre identificativi coincidono, false altrimenti.
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssocazioneID that = (AssocazioneID) o;
        return id_utente == that.id_utente &&
                id_ruolo == that.id_ruolo &&
                id_azienda == that.id_azienda;
    }

    /**
     * Calcola il valore hash per l'identità della chiave composta.
     *
     * @return Valore hash calcolato.
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_utente, id_ruolo, id_azienda);
    }

    /**
     * Recupera l'ID dell'utente.
     *
     * @return L'ID utente.
     * @since 1.0.0
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param id_utente Nuovo ID utente.
     * @since 1.0.0
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Recupera l'ID del ruolo.
     *
     * @return L'ID ruolo.
     * @since 1.0.0
     */
    public int getId_ruolo() {
        return id_ruolo;
    }

    /**
     * Imposta l'ID del ruolo.
     *
     * @param id_ruolo Nuovo ID ruolo.
     * @since 1.0.0
     */
    public void setId_ruolo(int id_ruolo) {
        this.id_ruolo = id_ruolo;
    }

    /**
     * Recupera l'ID dell'azienda.
     *
     * @return L'ID azienda.
     * @since 1.0.0
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID dell'azienda.
     *
     * @param id_azienda Nuovo ID azienda.
     * @since 1.0.0
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }
}