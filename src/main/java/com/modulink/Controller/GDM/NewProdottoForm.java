package com.modulink.Controller.GDM;

import jakarta.validation.constraints.Size;

public class NewProdottoForm {
    @Size(min = 2, max = 255, message = "Il nome deve essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    private int quantita;
    private double prezzo;

    @Size(max = 255, message = "La descrizione dev'essere al massimo di 255 caratteri")
    private String descrizione;

    @Size(max = 255, message = "La categoria non deve superare i 255 caratteri")
    private String categoria;

    public String getNome() {
        return nome;
    }

    public NewProdottoForm(String nome, int quantita, double prezzo, String descrizione, String categoria) {
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
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
