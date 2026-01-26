package com.modulink.util;

import com.modulink.Model.Azienda.AziendaEntity;
import com.modulink.Model.OTP.OTPManager;
import com.modulink.Model.Utente.UtenteEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OTPTest {
    private final OTPManager otpManager = new OTPManager();

    @Test
    public void testGenerateOTP() {
        UtenteEntity utente=new UtenteEntity();
        utente.setNome("Roberto");
        utente.setCognome("Cito");
        utente.setAzienda(new AziendaEntity());
        otpManager.addOTP("ciao@mail.com",utente);
        assertThat(otpManager.getOTPEmail("ciao@mail.com").length()).isEqualTo(6);
        assertThat(otpManager.getOTPUser("ciao@mail.com")).isEqualTo(utente);
    }

    @Test
    public void testRegenerateOTP() {
        UtenteEntity utente=new UtenteEntity();
        utente.setNome("Roberto");
        utente.setCognome("Cito");
        utente.setAzienda(new AziendaEntity());
        otpManager.addOTP("ciao2@mail.com",utente);
        String oldOtp=otpManager.getOTPEmail("ciao2@mail.com");
        otpManager.addOTP("ciao2@mail.com",utente);
        assertThat(otpManager.getOTPEmail("ciao2@mail.com").length()).isEqualTo(6);
        assertThat(otpManager.getOTPUser("ciao2@mail.com")).isEqualTo(utente);
        assertThat(otpManager.getOTPEmail("ciao2@mail.com")).isNotEqualTo(oldOtp);
    }

    @Test
    public void testRemoveOTP() {
        UtenteEntity utente=new UtenteEntity();
        utente.setNome("Roberto");
        utente.setCognome("Cito");
        utente.setAzienda(new AziendaEntity());
        otpManager.addOTP("ciao3@mail.com",utente);
        otpManager.removeOTPEmail("ciao3@gmail.com");
        assertThat(otpManager.getOTPEmail("ciao3@gmail.com")).isEqualTo(null);
    }
}
