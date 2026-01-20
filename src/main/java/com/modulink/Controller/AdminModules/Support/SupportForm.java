package com.modulink.Controller.AdminModules.Support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupportForm {
    @NotBlank(message = "Il campo mail non può essere vuoto")
    @Size(min = 2, max = 50, message = "L'email deve essere compreso tra i 2 e i 50 caratteri")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Inserire un'email valida.")
    private String email;

    @NotBlank(message = "La categoria  non può essere vuota")
    @Size(min = 2, max = 50, message = "La categoria deve essere compreso tra i 2 e i 50 caratteri")
    private String category;

    @NotBlank(message = "Il messaggio non può essere vuoto")
    @Size(min = 2, max = 255, message = "Il messaggio deve essere compreso tra i 2 e i 255 caratteri")
    private String message;

    public SupportForm() {
    }

    public SupportForm(String email, String category, String message) {
        this.email = email;
        this.category = category;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}