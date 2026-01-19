package com.modulink.Controller.UserModules.GDM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per il form di aggiornamento di un prodotto esistente (Modulo GDM).
 * <p>
 * Estende le informazioni di base con l'identificativo univoco del prodotto (ID_Prodotto) per permettere
 * la ricerca e la modifica mirata dell'entità nel database.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.5
 * @since 1.3.0
 */
public class UpdateProdottoForm {
    /**
     * Identificativo univoco del prodotto da aggiornare.
     */
    private int idProdotto;

    /**
     * Nuovo nome del prodotto.
     */
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 255, message = "Il nome deve essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    /**
     * Nuova quantità in giacenza.
     */
    @NotNull(message = "La quantità è obbligatoria")
    private Integer quantita;

    /**
     * Nuovo prezzo di vendita.
     */
    @NotNull(message = "Il prezzo è obbligatorio")
    private Double prezzo;

    /**
     * Nuova descrizione del prodotto.
     */
    @Size(max = 255, message = "La descrizione dev'essere al massimo di 255 caratteri")
    private String descrizione;

    /**
     * Nuova categoria di appartenenza.
     */
    @Size(max = 255, message = "La categoria non deve superare i 255 caratteri")
    private String categoria;

    /**
     * Recupera il nome.
     * @return Nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Costruttore predefinito.
     * @since 1.3.0
     */
    public UpdateProdottoForm() {
    }

    /**
     * Costruttore completo per il caricamento dei dati pre-modifica.
     *
     * @param idProdotto  Identificativo prodotto.
     * @param nome        Nome prodotto.
     * @param quantita    Quantità attuale.
     * @param prezzo      Prezzo attuale.
     * @param descrizione Descrizione attuale.
     * @param categoria   Categoria attuale.
     * @since 1.3.0
     */
    public UpdateProdottoForm(int idProdotto, String nome, Integer quantita, Double prezzo, String descrizione, String categoria) {
        this.idProdotto = idProdotto;
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    /**
     * Recupera l'ID del prodotto.
     * @return ID prodotto.
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'ID del prodotto.
     * @param idProdotto Nuovo ID.
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Imposta il nome del prodotto.
     * @param nome Nuovo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera la quantità.
     * @return Quantità.
     */
    public Integer getQuantita() {
        return quantita;
    }

    /**
     * Imposta la quantità.
     * @param quantita Nuova quantità.
     */
    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    /**
     * Recupera il prezzo.
     * @return Prezzo.
     */
    public Double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo.
     * @param prezzo Nuovo prezzo.
     */
    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
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
     * Recupera la categoria.
     * @return Categoria.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Imposta la categoria.
     * @param categoria Nuova categoria.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}