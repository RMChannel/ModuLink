package com.modulink.Controller.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConfirmPasswordForm {
    @NotBlank(message="C'è stato qualche problema nella gestione della richiesta, ricomincia la procedura dall'inizio")
    private String email;

    @NotBlank(message="L'OTP non è stato inserito")
    @Size(min=6,max=6,message="L'OTP dev'essere di 6 caratteri")
    private String otp;

    @NotBlank(message="Non è stata inserita una nuova password")
    @Size(min=8,max=30,message="La password dev'essere almeno di 8 caratteri")
    private String newPassword;

    @NotBlank(message="Non è stata inserita la conferma della nuova password")
    @Size(min=8,max=30,message="La password dev'essere almeno di 8 caratteri")
    private String confirmNewPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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
