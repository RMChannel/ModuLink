package com.modulink.Controller.UserModules.GDM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NewProdottoForm {
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    @NotNull(message = "La quantità è obbligatoria")
    private Integer quantita;

    @NotNull(message = "Il prezzo è obbligatorio")
    private Double prezzo;

    @Size(max = 255, message = "La descrizione dev'essere al massimo di 255 caratteri")
    private String descrizione;

    @Size(max = 255, message = "La categoria non deve superare i 255 caratteri")
    private String categoria;

    public String getNome() {
        return nome;
    }

    public NewProdottoForm() {
    }

    public NewProdottoForm(String nome, Integer quantita, Double prezzo, String descrizione, String categoria) {
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}