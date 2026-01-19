package com.modulink.Controller.AdminModules.Support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per la raccolta delle richieste di supporto.
 * <p>
 * Contiene i dati del form di contatto pubblico, con annotazioni di validazione
 * per garantire l'integrità dell'input (email valida, lunghezza minima messaggi, etc.).
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.5
 * @since 1.2.0
 */
public class SupportForm {
    /**
     * Indirizzo email del richiedente.
     * Obbligatorio, deve rispettare il pattern di un'email valida.
     */
    @NotBlank(message = "Il campo mail non può essere vuoto")
    @Size(min = 2, max = 50, message = "L'email deve essere compreso tra i 2 e i 50 caratteri")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Inserire un'email valida.")
    private String email;

    /**
     * Categoria della richiesta (es. Tecnico, Commerciale, Altro).
     * Obbligatorio.
     */
    @NotBlank(message = "La categoria  non può essere vuota")
    @Size(min = 2, max = 50, message = "La categoria deve essere compreso tra i 2 e i 50 caratteri")
    private String category;

    /**
     * Corpo del messaggio.
     * Obbligatorio.
     */
    @NotBlank(message = "Il messaggio non può essere vuoto")
    @Size(min = 2, max = 255, message = "Il messaggio deve essere compreso tra i 2 e i 255 caratteri")
    private String message;

    /**
     * Costruttore vuoto.
     * @since 1.2.0
     */
    public SupportForm() {
    }

    /**
     * Costruttore completo.
     *
     * @param email    Email utente.
     * @param category Categoria richiesta.
     * @param message  Testo messaggio.
     * @since 1.2.0
     */
    public SupportForm(String email, String category, String message) {
        this.email = email;
        this.category = category;
        this.message = message;
    }

    /**
     * Recupera l'email.
     * @return Email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email.
     * @param email Nuova email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Recupera la categoria.
     * @return Categoria.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Imposta la categoria.
     * @param category Nuova categoria.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Recupera il messaggio.
     * @return Messaggio.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Imposta il messaggio.
     * @param message Nuovo messaggio.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}