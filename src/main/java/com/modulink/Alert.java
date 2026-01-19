package com.modulink;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Classe di utilità per la generazione standardizzata di parametri URL per la gestione degli alert frontend.
 * <p>
 * Questa classe fornisce metodi statici per costruire query string sicure (URL Encoded) da appendere ai redirect,
 * permettendo al frontend (Thymeleaf/JavaScript) di mostrare messaggi di successo o errore (Toasts/Modals).
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.0.0
 */
public class Alert {

    /**
     * Genera una stringa di parametri URL per segnalare un errore.
     * <p>
     * Il messaggio viene codificato in UTF-8 per garantire la compatibilità con i caratteri speciali nell'URL.
     * </p>
     *
     * @param message Il messaggio di errore da visualizzare all'utente.
     * @return Una stringa nel formato {@code ?error=true&message=messaggio_codificato}.
     * @since 1.0.0
     */
    public static String error(String message) {
        return "?error=true&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }

    /**
     * Genera una stringa di parametri URL per segnalare un'operazione avvenuta con successo.
     * <p>
     * Il messaggio viene codificato in UTF-8 per garantire la compatibilità con i caratteri speciali nell'URL.
     * </p>
     *
     * @param message Il messaggio di successo da visualizzare all'utente.
     * @return Una stringa nel formato {@code ?success=true&message=messaggio_codificato}.
     * @since 1.0.0
     */
    public static String success(String message) {
        return "?success=true&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}