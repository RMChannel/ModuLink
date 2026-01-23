package com.modulink.Model.OTP;

import com.modulink.Model.Utente.UtenteEntity;

import java.util.HashMap;
import java.util.Random;

/**
 * Gestore globale per la generazione, memorizzazione e validazione dei codici One-Time Password (OTP).
 * <p>
 * Questa classe implementa un meccanismo di storage in-memory basato su {@link HashMap} per associare temporaneamente
 * codici di sicurezza univoci e oggetti {@link UtenteEntity} all'indirizzo email dell'utente.
 * Viene utilizzata prevalentemente nei flussi di autenticazione a due fattori (2FA) e nel recupero credenziali.
 * </p>
 * <p>
 * <strong>Nota Tecnica:</strong> La memorizzazione è volatile; il riavvio dell'applicazione comporta
 * la perdita di tutti i codici OTP attivi.
 * </p>
 *
 * @author Modulink Team
 * @version 1.2.7
 * @since 1.0.0
 */
public class OTPManager {

    /**
     * Mappa di associazione tra l'indirizzo email dell'utente e il codice OTP generato.
     * <p>
     * La chiave è la stringa email, il valore è la stringa alfanumerica dell'OTP.
     * </p>
     */
    private static final HashMap<String, String> otpMap = new HashMap<>();

    /**
     * Mappa di associazione tra l'indirizzo email dell'utente e l'entità utente completa.
     * <p>
     * Memorizza lo stato dell'utente durante il processo di verifica per permettere
     * operazioni post-validazione senza ulteriori query al database.
     * </p>
     */
    private static final HashMap<String, UtenteEntity> otpUserEntityMap = new HashMap<>();

    /**
     * Costruttore predefinito della classe.
     *
     * @since 1.0.0
     */
    public OTPManager() {}

    /**
     * Genera internamente un codice alfanumerico casuale di 6 caratteri.
     * <p>
     * Utilizza un set di caratteri comprendente lettere maiuscole (A-Z) e cifre (0-9).
     * La casualità è garantita dall'istanza di {@link Random}.
     * </p>
     *
     * @return Una stringa di 6 caratteri rappresentante l'OTP.
     * @since 1.0.0
     */
    private String generateOTP() { //Generatore codice casuale
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return sb.toString();
    }

    /**
     * Genera e registra un nuovo codice OTP per una specifica email utente.
     * <p>
     * Se per l'email fornita esiste già un codice attivo, questo viene invalidato e rimosso
     * prima di inserire il nuovo identificativo. Associa inoltre l'entità utente per persistenza temporanea.
     * </p>
     *
     * @param email  L'indirizzo email del destinatario.
     * @param utente L'oggetto {@link UtenteEntity} associato alla sessione di verifica.
     * @since 1.0.0
     */
    public void addOTP(String email, UtenteEntity utente) {
        otpMap.remove(email);
        otpUserEntityMap.remove(email);
        otpMap.put(email, generateOTP());
        otpUserEntityMap.put(email, utente);
    }

    /**
     * Recupera il codice OTP associato a un'email.
     *
     * @param email La chiave di ricerca (email utente).
     * @return La stringa del codice OTP se presente, altrimenti {@code null}.
     * @since 1.0.0
     */
    public String getOTPEmail(String email) {
        return otpMap.get(email);
    }

    /**
     * Rimuove il codice OTP attivo per un'email specifica.
     * <p>
     * Da utilizzare tipicamente dopo una validazione andata a buon fine o in caso di scadenza timeout.
     * </p>
     *
     * @param email L'email da ripulire.
     * @since 1.1.0
     */
    public void removeOTPEmail(String email) {
        otpMap.remove(email);
    }

    /**
     * Recupera l'entità utente associata a una sessione OTP attiva.
     *
     * @param email L'email associata all'utente.
     * @return L'istanza di {@link UtenteEntity} salvata nel manager.
     * @since 1.0.0
     */
    public UtenteEntity getOTPUser(String email) {
        return otpUserEntityMap.get(email);
    }

    /**
     * Rimuove l'associazione dell'entità utente per un'email specifica.
     *
     * @param email L'email da ripulire.
     * @since 1.1.0
     */
    public void removeOTPUser(String email) {
        otpUserEntityMap.remove(email);
    }
}
