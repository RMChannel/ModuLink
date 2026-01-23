package com.modulink.Controller.UserModules.GDM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per il form di inserimento di un nuovo prodotto nel magazzino (Modulo GDM).
 * <p>
 * Raccoglie i dati necessari per la creazione di una nuova entità prodotto, applicando vincoli di validazione
 * per garantire che il nome sia presente e che le quantità e i prezzi siano popolati correttamente.
 * </p>
 *
 * @author Modulink Team
 * @version 1.1.2
 * @since 1.3.0
 */
public class NewProdottoForm {
    /**
     * Nome identificativo del prodotto.
     */
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    /**
     * Giacenza iniziale in magazzino.
     */
    @NotNull(message = "La quantità è obbligatoria")
    private Integer quantita;

    /**
     * Prezzo unitario di vendita del prodotto.
     */
    @NotNull(message = "Il prezzo è obbligatorio")
    private Double prezzo;

    /**
     * Descrizione dettagliata delle caratteristiche del prodotto.
     */
    @Size(max = 255, message = "La descrizione dev'essere al massimo di 255 caratteri")
    private String descrizione;

    /**
     * Categoria merceologica di appartenenza.
     */
    @Size(max = 255, message = "La categoria non deve superare i 255 caratteri")
    private String categoria;

    /**
     * Recupera il nome del prodotto.
     * @return Stringa nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Costruttore predefinito.
     * @since 1.3.0
     */
    public NewProdottoForm() {
    }

    /**
     * Costruttore parametrico.
     *
     * @param nome        Nome prodotto.
     * @param quantita    Quantità iniziale.
     * @param prezzo      Prezzo unitario.
     * @param descrizione Descrizione prodotto.
     * @param categoria   Categoria di appartenenza.
     * @since 1.3.0
     */
    public NewProdottoForm(String nome, Integer quantita, Double prezzo, String descrizione, String categoria) {
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
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
     * @return Intero quantità.
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
     * @return Double prezzo.
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
     * @return Stringa descrizione.
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
     * @return Stringa categoria.
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