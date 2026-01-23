package com.modulink.Controller.UserModules.GDU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per il form di creazione di un nuovo utente aziendale (Modulo GDU).
 * <p>
 * Raccoglie i dati essenziali per la registrazione di un nuovo dipendente.
 * L'indirizzo email fornito sarà utilizzato come username per il login e per l'invio della password temporanea.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.3.0
 */
public class NewUserForm {
    /**
     * Nome del nuovo utente.
     */
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome dev'essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    /**
     * Cognome del nuovo utente.
     */
    @NotBlank(message = "Il campo cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome dev'essere compreso tra i 2 e i 50 caratteri")
    private String cognome;

    /**
     * Indirizzo email (Username).
     */
    @NotBlank(message = "il campo email non può essere vuoto")
    @Size(min = 5, max = 50, message = "l''email deve essere compresa tra 5 e 50 caratteri")
    private String email;

    /**
     * Numero di telefono (formato internazionale).
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    /**
     * Costruttore predefinito.
     * @since 1.3.0
     */
    public NewUserForm() {}

    /**
     * Costruttore completo.
     *
     * @param nome     Nome.
     * @param cognome  Cognome.
     * @param email    Email.
     * @param telefono Telefono.
     * @since 1.3.0
     */
    public NewUserForm(String nome, String cognome, String email, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Recupera il nome.
     * @return Nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome.
     * @param nome Nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera il cognome.
     * @return Cognome.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome.
     * @param cognome Nuovo cognome.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
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
     * Recupera il telefono.
     * @return Telefono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il telefono.
     * @param telefono Nuovo telefono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
