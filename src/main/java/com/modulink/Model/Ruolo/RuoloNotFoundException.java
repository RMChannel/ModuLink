package com.modulink.Model.Ruolo;

public class RuoloNotFoundException extends Exception {
    public RuoloNotFoundException() {
        super("Ruolo non trovato nella lista degli id");
    }
}
