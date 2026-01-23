package com.modulink.Controller.UserModules.GTM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) per la modifica di un Task esistente (Modulo GTM).
 * <p>
 * Contiene i campi aggiornabili di un task: titolo, priorità, scadenza, stato di completamento
 * e la lista aggiornata dei destinatari (utenti o ruoli).
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.3.0
 */
public class GTMEditForm {
    /**
     * ID univoco del task da modificare.
     */
    private int idTask;

    /**
     * Titolo descrittivo del task.
     */
    @NotBlank(message = "IL titolo non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il titolo deve essere compreso tra 2 e 50 caratteri")
    private String titolo;

    /**
     * Livello di priorità (es. 0-5).
     */
    private int priorita;

    /**
     * Data di scadenza prevista.
     */
    private LocalDate scadenza;

    /**
     * Stato di completamento del task.
     */
    private boolean completato;

    /**
     * Lista dei destinatari aggiornata.
     */
    private List<GTMMessage> messaggi;

    /**
     * Recupera ID task.
     * @return ID.
     */
    public int getIdTask() {
        return idTask;
    }

    /**
     * Imposta ID task.
     * @param idTask Nuovo ID.
     */
    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    /**
     * Recupera titolo.
     * @return Titolo.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta titolo.
     * @param titolo Nuovo titolo.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Recupera priorità.
     * @return Priorità.
     */
    public int getPriorita() {
        return priorita;
    }

    /**
     * Imposta priorità.
     * @param priorita Nuova priorità.
     */
    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    /**
     * Recupera scadenza.
     * @return Data scadenza.
     */
    public LocalDate getScadenza() {
        return scadenza;
    }

    /**
     * Imposta scadenza.
     * @param scadenza Nuova data.
     */
    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    /**
     * Verifica completamento.
     * @return true se completato.
     */
    public boolean isCompletato() {
        return completato;
    }

    /**
     * Imposta completamento.
     * @param completato true per completato.
     */
    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    /**
     * Recupera lista assegnatari.
     * @return Lista messaggi/assegnazioni.
     */
    public List<GTMMessage> getMessaggi() {
        return messaggi;
    }

    /**
     * Imposta lista assegnatari.
     * @param messaggi Nuova lista.
     */
    public void setMessaggi(List<GTMMessage> messaggi) {
        this.messaggi = messaggi;
    }
}
