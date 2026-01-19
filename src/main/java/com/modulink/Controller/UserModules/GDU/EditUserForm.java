package com.modulink.Controller.UserModules.GDU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per il form di modifica di un utente esistente (Modulo GDU).
 * <p>
 * Utilizzato dagli amministratori per aggiornare le informazioni anagrafiche e di contatto
 * dei dipendenti. Include campi per la gestione del reset password amministrativo.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.0
 * @since 1.3.0
 */
public class EditUserForm {
    /**
     * Nome dell'utente.
     */
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome dev'essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    /**
     * Cognome dell'utente.
     */
    @NotBlank(message = "Il campo cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome dev'essere compreso tra i 2 e i 50 caratteri")
    private String cognome;

    /**
     * Nuova email (se modificata).
     */
    @NotBlank(message = "il campo email non può essere vuoto")
    @Size(min = 5, max = 50, message = "l''email deve essere compresa tra 5 e 50 caratteri")
    private String email;

    /**
     * Vecchia email, utilizzata come chiave di ricerca per identificare l'utente da modificare.
     */
    @NotBlank(message = "Errore nella richiesta")
    @Size(min = 5, max = 50, message = "Errore nella richiesta")
    private String oldmail;

    /**
     * Numero di telefono.
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    /**
     * Nuova password (opzionale, solo se l'admin vuole forzare un reset).
     */
    private String newPassword;

    /**
     * Conferma nuova password.
     */
    private String confirmNewPassword;

    /**
     * Costruttore completo.
     *
     * @param nome               Nome.
     * @param cognome            Cognome.
     * @param email              Nuova email.
     * @param oldmail            Email attuale.
     * @param telefono           Telefono.
     * @param newPassword        Nuova password.
     * @param confirmNewPassword Conferma password.
     * @since 1.3.0
     */
    public EditUserForm(String nome, String cognome, String email, String oldmail, String telefono, String newPassword, String confirmNewPassword) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.oldmail = oldmail;
        this.telefono = telefono;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
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
     * Recupera la vecchia email.
     * @return Vecchia email.
     */
    public String getOldmail() {
        return oldmail;
    }

    /**
     * Imposta la vecchia email.
     * @param oldmail Vecchia email.
     */
    public void setOldmail(String oldmail) {
        this.oldmail = oldmail;
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

    /**
     * Recupera la nuova password.
     * @return Nuova password.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Imposta la nuova password.
     * @param newPassword Nuova password.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Recupera la conferma password.
     * @return Conferma password.
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Imposta la conferma password.
     * @param confirmNewPassword Conferma password.
     */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
