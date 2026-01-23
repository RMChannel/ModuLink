package com.modulink.Controller.UserModules.GTM;

import java.io.Serializable;

/**
 * Oggetto di trasferimento dati leggero per rappresentare un assegnatario (Utente o Ruolo) nel sistema GTM.
 * <p>
 * Utilizzato per popolare le liste di selezione (Tagify/Select2) nel frontend e trasmettere
 * le scelte di assegnazione al backend. Implementa {@link Serializable} per facilitare il trasferimento.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.2
 * @since 1.3.0
 */
public class GTMMessage implements Serializable {
    /**
     * Nome visualizzato dell'assegnatario (es. "Mario Rossi" o "Sviluppatori").
     */
    private String nome;

    /**
     * Tipo di assegnatario: "utente" o "ruolo".
     * Determina la logica di assegnazione nel controller.
     */
    private String type;

    /**
     * ID univoco dell'entità (UtenteID o RuoloID).
     */
    private int id;

    /**
     * ID dell'azienda di appartenenza (per sicurezza cross-tenant).
     */
    private int azienda;

    /**
     * Colore rappresentativo per l'interfaccia UI (es. badge colorato).
     */
    private String color;

    /**
     * Costruttore vuoto.
     * @since 1.3.0
     */
    public GTMMessage() {}

    /**
     * Costruttore completo.
     *
     * @param nome    Nome visualizzato.
     * @param type    Tipo ("utente"/"ruolo").
     * @param id      ID entità.
     * @param azienda ID azienda.
     * @param color   Colore HEX.
     * @since 1.3.0
     */
    public GTMMessage(String nome, String type, int id, int azienda, String color) {
        this.nome = nome;
        this.type = type;
        this.id = id;
        this.azienda = azienda;
        this.color = color;
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
     * Recupera il tipo.
     * @return Tipo stringa.
     */
    public String getType() {
        return type;
    }

    /**
     * Imposta il tipo.
     * @param type Nuovo tipo.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Recupera l'ID.
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'ID.
     * @param id Nuovo ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera l'azienda.
     * @return ID azienda.
     */
    public int getAzienda() {
        return azienda;
    }

    /**
     * Imposta l'azienda.
     * @param azienda Nuovo ID azienda.
     */
    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    /**
     * Recupera il colore.
     * @return Colore HEX.
     */
    public String getColor() {
        return color;
    }

    /**
     * Imposta il colore.
     * @param color Nuovo colore HEX.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Rappresentazione stringa dell'oggetto.
     * @return Stringa descrittiva.
     */
    @Override
    public String toString() {
        return "GTMMessage{" +
                "nome='" + nome + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", azienda=" + azienda +
                ", color='" + color + '\'' +
                '}';
    }
}
