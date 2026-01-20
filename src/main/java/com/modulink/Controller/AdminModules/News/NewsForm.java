package com.modulink.Controller.AdminModules.News;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public class NewsForm {
    @NotEmpty(message = "Il titolo non può essere vuoto")
    private String titolo;

    @NotEmpty(message = "Il testo non può essere vuoto")
    private String testo;

    private LocalDate data;

    public NewsForm() {}

    public NewsForm(String titolo, String testo, LocalDate data) {
        this.titolo = titolo;
        this.testo = testo;
        this.data = data;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
