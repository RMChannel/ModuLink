package com.modulink.Controller.Register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * Classe Form (DTO) per la gestione dei dati di registrazione di una nuova <strong>Azienda</strong>.
 * <p>
 * Questo DTO raccoglie le informazioni anagrafiche e di contatto dell'azienda durante la fase di onboarding.
 * Le annotazioni di <strong>Jakarta Validation</strong> assicurano che i dati inseriti rispettino
 * i formati richiesti (es. lunghezza P.IVA, formato telefono) prima di essere passati al Service Layer.
 * <p>
 * Oltre ai campi di input utente, contiene campi ausiliari per la gestione del file di logo (bytes e nome file)
 * per facilitare il passaggio di dati o il ripristino della vista in caso di errori di validazione.
 *
 * @author Modulink Team
 * @version 1.0
 */
public class RegisterAziendaForm {

    /**
     * Il nome o la ragione sociale dell'azienda.
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuoto.</li>
     * <li>La lunghezza deve essere compresa tra 3 e 50 caratteri.</li>
     * </ul>
     */
    @NotBlank(message = "Il nome dell''azienda non può essere vuoto")
    @Size(min = 3, max = 50, message = "Il nome dell''azienda deve essere compreso tra 3 e 50 caratteri")
    private String nomeAzienda;

    /**
     * La Partita IVA dell'azienda.
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuota.</li>
     * <li>Deve essere composta esattamente da 11 caratteri (standard italiano).</li>
     * </ul>
     */
    @NotBlank(message = "La P.IVA non può essere vuota")
    @Size(min = 11, max = 11, message = "La P.IVA deve essere di 11 caratteri")
    private String piva;

    /**
     * Indirizzo della sede legale o operativa.
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuoto.</li>
     * <li>La lunghezza deve essere compresa tra 5 e 100 caratteri.</li>
     * </ul>
     */
    @NotBlank(message = "Il campo indirizzo non può essere vuoto")
    @Size(min = 5, max = 100, message = "L''indirizzo deve essere compreso tra 5 e 100 caratteri")
    private String indirizzo;

    /**
     * Città di ubicazione dell'azienda.
     */
    @NotBlank(message = "Il campo città non può essere vuoto")
    @Size(min = 3, max = 100, message = "Il nome della cità deve essere compreso tra i 3 e 100 caratteri")
    private String citta;

    /**
     * Codice di Avviamento Postale (CAP).
     * <p>
     * <strong>Vincoli di validazione:</strong>
     * <ul>
     * <li>Non può essere vuoto.</li>
     * <li>Deve essere composto esattamente da 5 caratteri.</li>
     * </ul>
     */
    @NotBlank(message = "Il campo CAP non può essere vuoto")
    @Size(min = 5, max = 5, message = "Il CAP deve essere di 5 caratteri")
    private String cap;

    /**
     * Numero di telefono aziendale.
     * <p>
     * Deve rispettare un pattern Regex per garantire un formato valido (es. +39...).
     */
    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    /**
     * Il file del logo aziendale caricato dall'utente.
     * Gestito come {@link MultipartFile}.
     */
    private MultipartFile logo;

    /**
     * Rappresentazione in array di byte del logo.
     * <p>
     * Utile per mantenere i dati del file in memoria se necessario processarli
     * o ricaricarli in caso di errore nel form, evitando che l'utente debba ricaricare il file.
     */
    private byte[] logoBytes;

    /**
     * Il nome originale del file del logo.
     * Utilizzato per mantenere il riferimento al file caricato.
     */
    private String logoFileName;

    /**
     * Restituisce il nome dell'azienda.
     * @return Il nome aziendale.
     */
    public String getNomeAzienda() {
        return nomeAzienda;
    }

    /**
     * Imposta il nome dell'azienda.
     * @param nomeAzienda Il nuovo nome.
     */
    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    /**
     * Restituisce la Partita IVA.
     * @return La stringa della P.IVA.
     */
    public String getPiva() {
        return piva;
    }

    /**
     * Imposta la Partita IVA.
     * @param piva La nuova P.IVA.
     */
    public void setPiva(String piva) {
        this.piva = piva;
    }

    /**
     * Restituisce l'indirizzo.
     * @return L'indirizzo.
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Imposta l'indirizzo.
     * @param indirizzo Il nuovo indirizzo.
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * Restituisce la città.
     * @return La città.
     */
    public String getCitta() {
        return citta;
    }

    /**
     * Imposta la città.
     * @param citta La nuova città.
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * Restituisce il CAP.
     * @return Il CAP.
     */
    public String getCap() {
        return cap;
    }

    /**
     * Imposta il CAP.
     * @param cap Il nuovo CAP.
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * Restituisce il numero di telefono.
     * @return Il telefono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono.
     * @param telefono Il nuovo telefono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Restituisce l'oggetto MultipartFile del logo.
     * @return Il file caricato.
     */
    public MultipartFile getLogo() {
        return logo;
    }

    /**
     * Imposta il file del logo.
     * @param logo Il file multipart.
     */
    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }

    /**
     * Restituisce i byte grezzi del logo.
     * @return L'array di byte.
     */
    public byte[] getLogoBytes() {
        return logoBytes;
    }

    /**
     * Imposta i byte del logo.
     * @param logoBytes L'array di byte.
     */
    public void setLogoBytes(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }

    /**
     * Restituisce il nome del file del logo.
     * @return Il nome del file.
     */
    public String getLogoFileName() {
        return logoFileName;
    }

    /**
     * Imposta il nome del file del logo.
     * @param logoFileName Il nome del file.
     */
    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }
}