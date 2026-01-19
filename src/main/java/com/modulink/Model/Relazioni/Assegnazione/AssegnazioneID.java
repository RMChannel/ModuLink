package com.modulink.Model.Relazioni.Assegnazione;

import com.modulink.Model.Task.TaskEntity;
import com.modulink.Model.Utente.UtenteEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe che rappresenta la chiave primaria composta per l'entità {@link AssegnazioneEntity}.
 * <p>
 * Questa classe implementa l'interfaccia {@link Serializable} come richiesto dalle specifiche JPA per le chiavi composte.
 * Identifica univocamente un'assegnazione combinando gli identificativi del Task, dell'Azienda e dell'Utente.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.0.0
 */
public class AssegnazioneID implements Serializable {
    /**
     * Identificativo univoco del Task.
     */
    private int id_task;

    /**
     * Identificativo univoco dell'Azienda.
     */
    private int id_azienda;

    /**
     * Identificativo univoco dell'Utente.
     */
    private int id_utente;

    /**
     * Costruttore vuoto.
     */
    public AssegnazioneID() {}

    /**
     * Costruttore completo per inizializzare la chiave composta.
     *
     * @param id_task    L'identificativo del Task.
     * @param id_azienda L'identificativo dell'Azienda.
     * @param id_utente  L'identificativo dell'Utente.
     */
    public AssegnazioneID(int id_task, int id_azienda, int id_utente) {
        this.id_task = id_task;
        this.id_azienda = id_azienda;
        this.id_utente = id_utente;
    }

    /**
     * Restituisce l'ID del Task.
     *
     * @return L'identificativo del Task.
     */
    public int getId_task() {
        return id_task;
    }

    /**
     * Imposta l'ID del Task.
     *
     * @param id_task Il nuovo identificativo del Task.
     */
    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    /**
     * Restituisce l'ID dell'Azienda.
     *
     * @return L'identificativo dell'Azienda.
     */
    public int getId_azienda() {
        return id_azienda;
    }

    /**
     * Imposta l'ID dell'Azienda.
     *
     * @param id_azienda Il nuovo identificativo dell'Azienda.
     */
    public void setId_azienda(int id_azienda) {
        this.id_azienda = id_azienda;
    }

    /**
     * Restituisce l'ID dell'Utente.
     *
     * @return L'identificativo dell'Utente.
     */
    public int getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID dell'Utente.
     *
     * @param id_utente Il nuovo identificativo dell'Utente.
     */
    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Verifica l'uguaglianza tra questo oggetto ID e un altro oggetto.
     *
     * @param o L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssegnazioneID that = (AssegnazioneID) o;
        return id_task == that.id_task && id_azienda == that.id_azienda && id_utente == that.id_utente;
    }

    /**
     * Calcola l'hash code dell'oggetto ID.
     *
     * @return Il valore hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id_task, id_azienda, id_utente);
    }
}
