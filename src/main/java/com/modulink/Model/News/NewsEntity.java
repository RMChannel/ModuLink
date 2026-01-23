package com.modulink.Model.News;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

/**
 * Rappresenta un'entità informativa (Notizia o Avviso) all'interno del sistema Modulink.
 * <p>
 * Questa classe mappa la tabella delle notizie utilizzata per diffondere comunicazioni broadcast,
 * aggiornamenti di piattaforma o avvisi di manutenzione agli utenti del sistema.
 * </p>
 * <p>
 * La persistenza è gestita tramite JPA, con identificativo autogenerato mediante sequenza dedicata.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.3
 * @since 1.0.0
 */
@Entity
public class NewsEntity {

    /**
     * Identificativo univoco (Primary Key) della notizia.
     * <p>
     * Il valore è generato automaticamente dal database utilizzando il generatore di sequenza "news_seq".
     * Questo approccio garantisce l'unicità e l'ordine di inserimento (in database che supportano le sequenze).
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(generator = "news_seq")
    private int id;

    /**
     * Il titolo o l'intestazione della notizia.
     * <p>
     * Campo obbligatorio (NOT NULL). Deve sintetizzare il contenuto del messaggio per l'elenco riepilogativo.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(nullable = false)
    private String titolo;

    /**
     * Il corpo testuale completo della notizia.
     * <p>
     * Campo obbligatorio (NOT NULL). Può contenere testo formattato o semplice, a seconda dell'implementazione del frontend.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(nullable = false)
    private String testo;

    /**
     * La data di pubblicazione o validità della notizia.
     * <p>
     * Campo obbligatorio (NOT NULL). Utilizzato per ordinare cronologicamente il feed delle notizie
     * o per determinare la scadenza della visibilità.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Costruttore predefinito (No-Args) necessario per la riflessione JPA.
     *
     * @since 1.0.0
     */
    public NewsEntity() {}

    /**
     * Costruttore parametrico per la creazione di una nuova istanza.
     *
     * @param titolo Il titolo della notizia.
     * @param testo  Il contenuto testuale.
     * @param data   La data di riferimento.
     * @since 1.0.0
     */
    public NewsEntity(String titolo, String testo, LocalDate data) {
        this.titolo = titolo;
        this.testo = testo;
        this.data = data;
    }

    /**
     * Recupera l'ID della notizia.
     * @return Intero rappresentante la chiave primaria.
     * @since 1.0.0
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID della notizia.
     * @param id Nuovo ID.
     * @since 1.0.0
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera il titolo.
     * @return Stringa del titolo.
     * @since 1.0.0
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo.
     * @param titolo Nuovo titolo.
     * @since 1.0.0
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Recupera il testo della notizia.
     * @return Stringa del corpo del messaggio.
     * @since 1.0.0
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo della notizia.
     * @param testo Nuovo testo.
     * @since 1.0.0
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Recupera la data della notizia.
     * @return Oggetto {@link LocalDate}.
     * @since 1.0.0
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Imposta la data della notizia.
     * @param data Nuova data.
     * @since 1.0.0
     */
    public void setData(LocalDate data) {
        this.data = data;
    }
}
