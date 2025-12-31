package com.modulink.Controller.GDU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class NewUserForm {
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome dev'essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    @NotBlank(message = "Il campo cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome dev'essere compreso tra i 2 e i 50 caratteri")
    private String cognome;

    @NotBlank(message = "il campo email non può essere vuoto")
    @Size(min = 5, max = 50, message = "l''email deve essere compresa tra 5 e 50 caratteri")
    private String email;

    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    public NewUserForm() {}

    public NewUserForm(String nome, String cognome, String email, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
