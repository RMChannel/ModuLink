package com.modulink.Controller.UserModules.GDU;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EditUserForm {
    @NotBlank(message = "Il campo nome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il nome dev'essere compreso tra i 2 e i 50 caratteri")
    private String nome;

    @NotBlank(message = "Il campo cognome non può essere vuoto")
    @Size(min = 2, max = 50, message = "Il cognome dev'essere compreso tra i 2 e i 50 caratteri")
    private String cognome;

    @NotBlank(message = "il campo email non può essere vuoto")
    @Size(min = 5, max = 50, message = "l''email deve essere compresa tra 5 e 50 caratteri")
    private String email;

    @NotBlank(message = "Errore nella richiesta")
    @Size(min = 5, max = 50, message = "Errore nella richiesta")
    private String oldmail;

    @NotBlank(message = "Il numero di telefono non può essere vuoto")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Inserire un numero di telefono valido (es. +39 333 1234567)")
    private String telefono;

    private String newPassword;

    private String confirmNewPassword;

    public EditUserForm(String nome, String cognome, String email, String oldmail, String telefono, String newPassword, String confirmNewPassword) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.oldmail = oldmail;
        this.telefono = telefono;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
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

    public String getOldmail() {
        return oldmail;
    }

    public void setOldmail(String oldmail) {
        this.oldmail = oldmail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
