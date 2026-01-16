package com.modulink.Model.Email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {
    private final JavaMailSenderImpl mailSender;

    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

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
