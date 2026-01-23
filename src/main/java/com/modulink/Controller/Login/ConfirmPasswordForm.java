package com.modulink.Controller.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) per la conferma del reset della password.
 * <p>
 * Raccoglie i dati necessari per completare la procedura di recupero password:
 * l'email dell'utente, il codice OTP ricevuto e la nuova password (con conferma).
 * Include annotazioni di validazione per garantire che tutti i campi siano presenti e conformi.
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.5
 * @since 1.1.0
 */
public class ConfirmPasswordForm {

    /**
     * L'indirizzo email dell'account da recuperare.
     * Deve essere presente per associare la richiesta all'utente corretto.
     */
    @NotBlank(message="C'è stato qualche problema nella gestione della richiesta, ricomincia la procedura dall'inizio")
    private String email;

    /**
     * Il codice OTP (One-Time Password) inviato via email.
     * Deve essere di esattamente 6 caratteri.
     */
    @NotBlank(message="L'OTP non è stato inserito")
    @Size(min=6,max=6,message="L'OTP dev'essere di 6 caratteri")
    private String otp;

    /**
     * La nuova password desiderata dall'utente.
     * Deve rispettare i criteri minimi di sicurezza (lunghezza min 8 caratteri).
     */
    @NotBlank(message="Non è stata inserita una nuova password")
    @Size(min=8,max=30,message="La password dev'essere almeno di 8 caratteri")
    private String newPassword;

    /**
     * Conferma della nuova password per evitare errori di digitazione.
     */
    @NotBlank(message="Non è stata inserita la conferma della nuova password")
    @Size(min=8,max=30,message="La password dev'essere almeno di 8 caratteri")
    private String confirmNewPassword;

    /**
     * Recupera l'email.
     * @return Stringa email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email.
     * @param email Stringa email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Recupera l'OTP.
     * @return Stringa OTP.
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Imposta l'OTP.
     * @param otp Stringa OTP.
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * Recupera la nuova password.
     * @return Password in chiaro.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Imposta la nuova password.
     * @param newPassword Password in chiaro.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Recupera la conferma password.
     * @return Conferma password in chiaro.
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Imposta la conferma password.
     * @param confirmNewPassword Conferma password in chiaro.
     */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
