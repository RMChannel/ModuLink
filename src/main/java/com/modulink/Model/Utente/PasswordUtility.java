package com.modulink.Model.Utente;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Classe di utilità per la gestione della crittografia e verifica delle password.
 * <p>
 * Questa classe fornisce metodi statici per mettere in sicurezza le credenziali degli utenti
 * utilizzando l'algoritmo <strong>BCrypt</strong>. BCrypt è uno standard di settore che incorpora
 * automaticamente un "salt" casuale per proteggere dagli attacchi tramite rainbow table.
 * <p>
 * Caratteristiche principali:
 * <ul>
 * <li><strong>Salting Automatico:</strong> Ogni hash generato è unico, anche per password identiche.</li>
 * <li><strong>Adaptive Hashing:</strong> L'algoritmo è progettato per essere computazionalmente oneroso, rallentando gli attacchi brute-force.</li>
 * </ul>
 * <p>
 * Utilizzare questa classe durante le fasi di registrazione (per l'hashing) e di login (per la verifica).
 *
 * @see org.springframework.security.crypto.bcrypt.BCrypt
 * @author Modulink Team
 * @version 1.0
 */
public class PasswordUtility {

    /**
     * Costruttore privato per prevenire l'istanziazione della classe utility.
     * <p>
     * Lancia un'eccezione se invocato accidentalmente (anche tramite reflection),
     * garantendo che la classe sia utilizzata solo tramite i suoi metodi statici.
     */
    private PasswordUtility() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Genera un hash sicuro a partire da una password in chiaro.
     * <p>
     * Questo metodo utilizza <code>BCrypt.gensalt()</code> per generare un salt casuale
     * e applicarlo alla password. Di conseguenza, chiamando questo metodo più volte
     * con la stessa password, si otterranno hash differenti ogni volta.
     * <p>
     * Da utilizzare prima di salvare la password nel database (es. in fase di registrazione).
     * La stringa risultante include il salt e le informazioni sull'algoritmo necessarie per la verifica futura.
     *
     * @param plainPassword La password in chiaro inserita dall'utente.
     * @return Una stringa contenente l'hash cifrato della password (lunghezza tipica 60 caratteri).
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Verifica la corrispondenza tra una password in chiaro e un hash memorizzato.
     * <p>
     * Questo metodo estrae il salt dall'hash memorizzato e lo utilizza per hashare
     * la password in chiaro fornita, confrontando poi i risultati. Gestisce internamente
     * la complessità del confronto sicuro contro attacchi temporali (timing attacks).
     * <p>
     * Da utilizzare in fase di autenticazione (Login).
     *
     * @param plainPassword  La password in chiaro inserita dall'utente al momento del login.
     * @param hashedPassword L'hash della password precedentemente generato e recuperato dal database.
     * @return <code>true</code> se la password in chiaro corrisponde all'hash memorizzato, <code>false</code> altrimenti.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}