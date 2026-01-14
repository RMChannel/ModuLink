package com.modulink.Controller.GTM;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class GTMForm {
    @NotBlank(message = "IL titolo non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il titolo deve essere compreso tra 2 e 50 caratteri")
    private String titolo;

    private int priorita;

    private LocalDate scadenza;

    private List<GTMMessage> messaggi;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getPriorita() {
        return priorita;
    }

    public void setPriorita(int priorita) {
        this.priorita = priorita;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    public List<GTMMessage> getMessaggi() {
        return messaggi;
    }

    public void setMessaggi(List<GTMMessage> messaggi) {
        this.messaggi = messaggi;
    }
}