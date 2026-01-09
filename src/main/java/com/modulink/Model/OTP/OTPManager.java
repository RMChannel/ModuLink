package com.modulink.Model.OTP;

import com.modulink.Model.Utente.UtenteEntity;

import java.util.HashMap;
import java.util.Random;

//Gestore globale OTP
public class OTPManager {
    private static final HashMap<String, String> otpMap = new HashMap<>();
    private static final HashMap<String, UtenteEntity> otpUserEntityMap = new HashMap<>();
    public OTPManager() {}

    private String generateOTP() { //Generatore codice casuale
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return sb.toString();
    }

    //Aggiunge all'hashmap il codice otp generato, nel caso ne sia stato già generato uno viene sostituito col nuovo
    public void addOTP(String email, UtenteEntity utente) {
        otpMap.remove(email);
        otpUserEntityMap.remove(email);
        otpMap.put(email, generateOTP());
        otpUserEntityMap.put(email, utente);
    }

    public String getOTPEmail(String email) {
        return otpMap.get(email);
    }

    public void removeOTPEmail(String email) {
        otpMap.remove(email);
    }

    public UtenteEntity getOTPUser(String email) {
        return otpUserEntityMap.get(email);
    }

    public void removeOTPUser(String email) {
        otpUserEntityMap.remove(email);
    }
}
