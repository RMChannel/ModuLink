package com.modulink.Controller.UserModules.GTM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) per la creazione di un nuovo Task (Modulo GTM).
 * <p>
 * Raccoglie i dati iniziali per la definizione di un'attività: titolo, priorità, data di scadenza
 * e i destinatari iniziali (GTMMessage).
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.3.0
 */
public class GTMForm {
    /**
     * Titolo del task.
     */
    @NotBlank(message = "IL titolo non può essere vuoto")
    @Size(min = 2, max = 150, message = "Il titolo deve essere compreso tra 2 e 50 caratteri")
    private String titolo;

    /**
     * Livello di priorità.
     */
    private int priorita;

    /**
     * Data di scadenza.
     */
    private LocalDate scadenza;

    /**
     * Lista di assegnatari.
     */
    private List<GTMMessage> messaggi;

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
     * Recupera assegnatari.
     * @return Lista assegnazioni.
     */
    public List<GTMMessage> getMessaggi() {
        return messaggi;
    }

    /**
     * Imposta assegnatari.
     * @param messaggi Nuova lista.
     */
    public void setMessaggi(List<GTMMessage> messaggi) {
        this.messaggi = messaggi;
    }
}