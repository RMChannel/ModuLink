package com.modulink.Model.Utente;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Utente non trovato nella lista degli id");
    }
}
