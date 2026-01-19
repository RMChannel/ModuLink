package com.modulink.Controller.AdminModules.News;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) per la gestione del form di creazione News.
 * <p>
 * Incapsula i dati inviati dal client e definisce le regole di validazione standard
 * (come campi obbligatori) utilizzate dal layer di presentazione.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.0
 * @since 1.1.0
 */
public class NewsForm {
    /**
     * Titolo della notizia. Campo obbligatorio.
     */
    @NotEmpty(message = "Il titolo non può essere vuoto")
    private String titolo;

    /**
     * Contenuto testuale della notizia. Campo obbligatorio.
     */
    @NotEmpty(message = "Il testo non può essere vuoto")
    private String testo;

    /**
     * Data di pubblicazione/validità della notizia.
     */
    private LocalDate data;

    /**
     * Costruttore vuoto.
     * @since 1.1.0
     */
    public NewsForm() {}

    /**
     * Costruttore completo.
     *
     * @param titolo Titolo notizia.
     * @param testo  Corpo notizia.
     * @param data   Data notizia.
     * @since 1.1.0
     */
    public NewsForm(String titolo, String testo, LocalDate data) {
        this.titolo = titolo;
        this.testo = testo;
        this.data = data;
    }

    /**
     * Recupera il titolo.
     * @return Stringa titolo.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo.
     * @param titolo Nuovo titolo.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Recupera il testo.
     * @return Stringa testo.
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo.
     * @param testo Nuovo testo.
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Recupera la data.
     * @return LocalDate data.
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Imposta la data.
     * @param data Nuova data.
     */
    public void setData(LocalDate data) {
        this.data = data;
    }
}
