package com.modulink.Controller.EditUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object (DTO) per il form di modifica profilo utente.
 * <p>
 * Raccoglie i dati anagrafici, di contatto e le preferenze di sicurezza (password) modificabili dall'utente.
 * Include anche i campi per la gestione dell'upload dell'immagine profilo.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.0
 * @since 1.2.0
 */
public class EditUserForm {
    /**
     * Nome dell'utente. Campo obbligatorio.
     */
    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri")
    private String nome;

    /**
     * Cognome dell'utente. Campo obbligatorio.
     */
    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome deve essere compreso tra 2 e 50 caratteri")
    private String cognome;

    /**
     * Email dell'utente.
     * Utilizzata come identificativo immutabile nel contesto del form di modifica.
     */
    private String email;

    /**
     * Numero di telefono. Deve rispettare il formato internazionale o locale standard.
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    /**
     * Nuova password (opzionale).
     */
    private String password;

    /**
     * Conferma della nuova password.
     */
    private String confirmPassword;

    /**
     * Flag booleano per indicare la richiesta di rimozione dell'immagine profilo attuale.
     */
    private boolean removeImageFlag;

    /**
     * File contenente la nuova immagine profilo caricata.
     */
    private MultipartFile immagineProfilo;

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
     * Recupera il numero di telefono.
     * @return Telefono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono.
     * @param telefono Nuovo telefono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Verifica se è stata richiesta la rimozione dell'immagine.
     * @return true se da rimuovere.
     */
    public boolean isRemoveImageFlag() {
        return removeImageFlag;
    }

    /**
     * Imposta il flag di rimozione immagine.
     * @param removeImageFlag true per rimuovere.
     */
    public void setRemoveImageFlag(boolean removeImageFlag) {
        this.removeImageFlag = removeImageFlag;
    }

    /**
     * Recupera il file immagine caricato.
     * @return MultipartFile.
     */
    public MultipartFile getImmagineProfilo() {
        return immagineProfilo;
    }

    /**
     * Imposta il file immagine.
     * @param immagineProfilo Nuovo file.
     */
    public void setImmagineProfilo(MultipartFile immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }

    /**
     * Recupera la nuova password.
     * @return Password in chiaro.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la nuova password.
     * @param password Password in chiaro.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Recupera la conferma password.
     * @return Password di conferma.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Imposta la conferma password.
     * @param confirmPassword Password di conferma.
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}