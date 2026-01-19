package com.modulink.Model.SupportForm;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Rappresenta una richiesta di assistenza o ticket di supporto inviato da un utente.
 * <p>
 * Questa entità mappa la tabella <code>support_form</code> e funge da registro delle segnalazioni,
 * bug report o richieste di informazioni. I dati persistiti vengono utilizzati dal pannello di amministrazione
 * per la gestione del ciclo di vita del supporto tecnico.
 * </p>
 * <p>
 * L'identificativo primario è generato tramite una sequenza dedicata (<code>support_form_seq</code>)
 * per garantire scalabilità indipendentemente dal motore di database sottostante.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.1
 * @since 1.0.0
 */
@Entity
@Table(name = "support_form", schema = "modulink")
public class SupportFormEntity {

    /**
     * Identificativo univoco del ticket di supporto.
     * <p>
     * Chiave primaria autogenerata.
     * </p>
     *
     * @since 1.0.0
     */
    @Id
    @GeneratedValue(generator = "support_form_seq")
    private int id;

    /**
     * Indirizzo email del richiedente.
     * <p>
     * Utilizzato per inviare notifiche di presa in carico o risposte.
     * Non è necessariamente vincolato a un utente registrato (supporto pre-sales).
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Categoria tematica della richiesta (es. "Tecnico", "Commerciale", "Bug").
     * <p>
     * Utile per il routing automatico del ticket al reparto competente.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name = "category", nullable = false)
    private String category;

    /**
     * Corpo del messaggio o descrizione dettagliata del problema.
     * <p>
     * Campo testuale obbligatorio.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Marca temporale di creazione della richiesta.
     * <p>
     * Impostata automaticamente al momento dell'instanziazione dell'oggetto nel costruttore parametrico.
     * </p>
     *
     * @since 1.0.0
     */
    @Column(name="datetime", nullable = false)
    private LocalDateTime datetime;

    /**
     * Costruttore predefinito (No-Args) richiesto da JPA.
     *
     * @since 1.0.0
     */
    public SupportFormEntity() {}

    /**
     * Costruttore per la creazione di un nuovo ticket.
     * <p>
     * Inizializza automaticamente il campo {@code datetime} con l'orario corrente del server ({@link LocalDateTime#now()}).
     * </p>
     *
     * @param email    Email del mittente.
     * @param category Categoria del problema.
     * @param message  Testo della richiesta.
     * @since 1.0.0
     */
    public SupportFormEntity(String email, String category, String message) {
        this.email = email;
        this.category = category;
        this.message = message;
        this.datetime = LocalDateTime.now();
    }

    /**
     * Recupera l'ID del ticket.
     * @return Intero ID.
     * @since 1.0.0
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID del ticket.
     * @param id Nuovo ID.
     * @since 1.0.0
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera l'email del mittente.
     * @return Stringa email.
     * @since 1.0.0
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email del mittente.
     * @param email Nuova email.
     * @since 1.0.0
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Recupera la categoria della richiesta.
     * @return Stringa categoria.
     * @since 1.0.0
     */
    public String getCategory() {
        return category;
    }

    /**
     * Imposta la categoria.
     * @param category Nuova categoria.
     * @since 1.0.0
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Recupera il messaggio.
     * @return Stringa messaggio.
     * @since 1.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Imposta il messaggio.
     * @param message Nuovo messaggio.
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Recupera la data e ora di creazione.
     * @return {@link LocalDateTime} di creazione.
     * @since 1.0.0
     */
    public LocalDateTime getDatetime() {
        return datetime;
    }

    /**
     * Imposta la data e ora di creazione.
     * @param datetime Nuovo timestamp.
     * @since 1.0.0
     */
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
