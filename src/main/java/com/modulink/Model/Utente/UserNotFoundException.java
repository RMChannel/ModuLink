package com.modulink.Model.Utente;

/**
 * Eccezione controllata lanciata quando non è possibile trovare un utente specifico.
 * <p>
 * Questa eccezione viene tipicamente sollevata dal livello di servizio (Service Layer)
 * o dal repository quando una ricerca per ID o per altri criteri non produce risultati,
 * indicando che l'utente richiesto non esiste nel contesto specificato (es. nell'azienda corrente).
 * </p>
 *
 * @author Modulink Team
 * @version 1.0.3
 */
public class UserNotFoundException extends Exception {

    /**
     * Costruisce una nuova eccezione con un messaggio di errore predefinito.
     * <p>
     * Il messaggio "Utente non trovato nella lista degli id" indica che l'operazione
     * di recupero è fallita per uno o più identificativi forniti.
     * </p>
     */
    public UserNotFoundException() {
        super("Utente non trovato nella lista degli id");
    }
}
