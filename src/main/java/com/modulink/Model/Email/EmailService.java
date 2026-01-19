package com.modulink.Model.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service Component responsabile della gestione e dell'invio delle comunicazioni via posta elettronica.
 * <p>
 * Questa classe agisce come wrapper attorno all'infrastruttura di invio mail di Spring Framework ({@link JavaMailSenderImpl}),
 * fornendo un'interfaccia semplificata per il dispatching di messaggi di testo semplice (MIME type text/plain).
 * </p>
 * <p>
 * L'architettura del servizio sfrutta l'elaborazione asincrona per disaccoppiare l'operazione di I/O (invio rete SMTP)
 * dal thread principale dell'applicazione, migliorando la reattività del sistema e riducendo la latenza percepita dall'utente.
 * </p>
 *
 * @author Modulink Team
 * @version 1.3.4
 * @since 1.0.0
 */
@Service
public class EmailService {

    /**
     * Implementazione concreta del client SMTP fornito da Spring.
     * <p>
     * Gestisce la connessione al server di posta, l'autenticazione e il trasporto dei pacchetti MIME.
     * La configurazione avviene tipicamente tramite properties (host, port, username, password).
     * </p>
     *
     * @since 1.0.0
     */
    private final JavaMailSenderImpl mailSender;

    /**
     * Costruttore per la Dependency Injection.
     * <p>
     * Inizializza il servizio iniettando l'istanza configurata di {@link JavaMailSenderImpl}.
     * </p>
     *
     * @param mailSender L'istanza del sender SMTP pronta all'uso.
     * @since 1.0.0
     */
    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Esegue l'invio asincrono di un messaggio email semplice.
     * <p>
     * Questo metodo è annotato con {@link Async}, istruendo il framework a eseguirlo in un thread separato
     * (pool gestito da Spring TaskExecutor). Ciò impedisce che ritardi nella comunicazione SMTP
     * blocchino il flusso di esecuzione chiamante (es. controller HTTP).
     * </p>
     * <p>
     * <strong>Gestione Errori:</strong> Poiché l'esecuzione è asincrona, le eccezioni sollevate durante l'invio
     * non possono risalire allo stack del chiamante. Vengono invece intercettate localmente e loggate
     * sullo standard error stream per scopi di diagnosi e auditing.
     * </p>
     *
     * @param message Oggetto {@link SimpleMailMessage} contenente mittente, destinatari, oggetto e corpo del testo.
     * @since 1.1.0
     */
    @Async
    public void sendEmail(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // È importante loggare qui perché l'utente non vedrà l'errore a video
            System.err.println("Errore invio email a " + Arrays.toString(message.getTo()) + ": " + e.getMessage());
        }
    }
}
