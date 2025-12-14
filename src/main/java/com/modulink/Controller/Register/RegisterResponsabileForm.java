package com.modulink.Controller.Register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * Classe Form (DTO) per la gestione dei dati di registrazione di un <strong>Responsabile</strong>.
 * <p>
 * Questa classe viene utilizzata nel livello Controller per raccogliere, bindare e validare
 * i dati inviati dal client durante la procedura di registrazione di un nuovo utente con ruolo Responsabile.
 * Include le annotazioni di <strong>Jakarta Validation</strong> per garantire l'integrità dei dati
 * prima che vengano processati dalla logica di business.
 *
 * @author Modulink Team
 * @version 1.0
 */
public class RegisterResponsabileForm {

    /**
     * Indirizzo email del responsabile.
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuoto o nullo.</li>
     * <li>La lunghezza deve essere compresa tra 5 e 50 caratteri.</li>
     * </ul>
     */
    @NotBlank(message = "il campo email non può essere vuoto")
    @Size(min = 5, max = 50, message = "l''email deve essere compresa tra 5 e 50 caratteri")
    private String email;

    /**
     * Password scelta dall'utente.
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuota.</li>
     * <li>La lunghezza minima è di 8 caratteri (per sicurezza).</li>
     * <li>La lunghezza massima è di 50 caratteri.</li>
     * </ul>
     */
    @NotBlank(message = "Il campo password non può essere vuoto")
    @Size(min = 8, max = 50, message = "La password deve essere compresa tra 8 e 50 caratteri")
    private String password;

    /**
     * Campo di conferma della password.
     * <p>
     * Serve per verificare che l'utente non abbia commesso errori di digitazione.
     * Deve coincidere con il campo <code>password</code> (la logica di confronto avviene solitamente tramite un validatore custom o nel servizio).
     */
    @NotBlank(message = "Il campo conferma password non può essere vuoto")
    @Size(min = 8, max = 50, message = "La conferma password deve essere compresa tra 8 e 50 caratteri")
    private String confermaPassword;

    /**
     * Nome di battesimo del responsabile.
     */
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri")
    private String nome;

    /**
     * Cognome del responsabile.
     */
    @NotBlank(message = "Il campo cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome deve essere compreso tra 2 e 50 caratteri")
    private String cognome;

    /**
     * Numero di telefono del responsabile.
     * <p>
     * Deve rispettare un pattern specifico (es. +39 333 1234567 o formati simili).
     * <p>
     * <strong>Pattern Regex:</strong> <code>^(\+\d{1,3}( )?)?((\(\d{3}\))|\d{3})[- .]?\d{3}[- .]?\d{4}$</code>
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefonoutente;

    /**
     * File contenente l'immagine del profilo (opzionale).
     * <p>
     * Viene gestito come oggetto {@link MultipartFile} per permettere l'upload di dati binari.
     */
    private MultipartFile immagineProfilo;

    /**
     * Restituisce l'indirizzo email inserito nel form.
     * @return La stringa dell'email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'indirizzo email.
     * @param email La nuova email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce la password inserita.
     * @return La stringa della password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password.
     * @param password La nuova password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il valore del campo conferma password.
     * @return La stringa di conferma.
     */
    public String getConfermaPassword() {
        return confermaPassword;
    }

    /**
     * Imposta il valore di conferma password.
     * @param confermaPassword La stringa di conferma.
     */
    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = confermaPassword;
    }

    /**
     * Restituisce il nome inserito.
     * @return Il nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome.
     * @param nome Il nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome inserito.
     * @return Il cognome.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome.
     * @param cognome Il nuovo cognome.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il numero di telefono.
     * @return Il telefono formattato come stringa.
     */
    public String getTelefonoutente() {
        return telefonoutente;
    }

    /**
     * Imposta il numero di telefono.
     * @param telefono Il nuovo telefono.
     */
    public void setTelefonoutente(String telefono) {
        this.telefonoutente = telefono;
    }

    /**
     * Restituisce il file dell'immagine profilo caricato.
     * @return L'oggetto {@link MultipartFile} o null se non presente.
     */
    public MultipartFile getImmagineProfilo() {
        return immagineProfilo;
    }

    /**
     * Imposta il file dell'immagine profilo.
     * @param immagineProfilo Il file caricato.
     */
    public void setImmagineProfilo(MultipartFile immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }
}