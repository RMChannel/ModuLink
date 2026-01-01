package com.modulink.Controller.GDR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewRoleForm {
    @NotBlank(message = "Il nome del ruolo non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome deve essere compreso tra 2 e 50 caratteri")
    private String nome;

    @NotBlank(message = "Il colore è obbligatorio")
    @Size(min = 7, max = 7,  message = "Il colore non è in un formato standard esadecimale")
    private String colore;

    @Size(max = 255, message = "La descrizione non può superare i 255 caratteri")
    private String descrizione;

    private int idRuolo; // Optional for update

    public NewRoleForm() {}

    public NewRoleForm(String nome, String colore, String descrizione) {
        this.nome = nome;
        this.colore = colore;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getIdRuolo() {
        return idRuolo;
    }

    public void setIdRuolo(int idRuolo) {
        this.idRuolo = idRuolo;
    }
}
