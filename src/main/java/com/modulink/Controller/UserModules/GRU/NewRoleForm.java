package com.modulink.Controller.UserModules.GRU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per la creazione di un nuovo Ruolo Aziendale (Modulo GRU).
 * <p>
 * Incapsula i dati definitivi del ruolo (Nome, Colore identificativo, Descrizione)
 * e include le validazioni per garantire la consistenza visiva e logica nel sistema.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.5
 * @since 1.2.0
 */
public class NewRoleForm {
    /**
     * Nome del ruolo.
     */
    @NotBlank(message = "Il nome del ruolo non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri")
    private String nome;

    /**
     * Colore esadecimale associato al ruolo (es. #RRGGBB).
     * Utilizzato per l'etichettatura visiva nelle dashboard.
     */
    @NotBlank(message = "Il colore è obbligatorio")
    @Size(min = 7, max = 7,  message = "Il colore non è in un formato standard esadecimale")
    private String colore;

    /**
     * Descrizione opzionale delle responsabilità del ruolo.
     */
    @Size(max = 255, message = "La descrizione non può superare i 255 caratteri")
    private String descrizione;

    /**
     * Identificativo opzionale, usato in fase di update.
     */
    private int idRuolo; // Optional for update

    /**
     * Costruttore predefinito.
     * @since 1.2.0
     */
    public NewRoleForm() {}

    /**
     * Costruttore completo.
     *
     * @param nome        Nome ruolo.
     * @param colore      Colore HEX.
     * @param descrizione Descrizione.
     * @since 1.2.0
     */
    public NewRoleForm(String nome, String colore, String descrizione) {
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
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
     * Recupera il colore.
     * @return Stringa colore HEX.
     */
    public String getColore() {
        return colore;
    }

    /**
     * Imposta il colore.
     * @param colore Nuovo colore.
     */
    public void setColore(String colore) {
        this.colore = colore;
    }

    /**
     * Recupera la descrizione.
     * @return Descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione.
     * @param descrizione Nuova descrizione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Recupera l'ID del ruolo (se presente).
     * @return ID ruolo.
     */
    public int getIdRuolo() {
        return idRuolo;
    }

    /**
     * Imposta l'ID del ruolo.
     * @param idRuolo Nuovo ID.
     */
    public void setIdRuolo(int idRuolo) {
        this.idRuolo = idRuolo;
    }
}
