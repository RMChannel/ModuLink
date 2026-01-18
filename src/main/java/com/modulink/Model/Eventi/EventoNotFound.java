package com.modulink.Model.Eventi;

/**
 * Eccezione unchecked lanciata quando un'operazione su un {@link EventoEntity} fallisce
 * perché l'entità richiesta non è stata trovata nel database.
 * <p>
 * Tipicamente utilizzata nel Service Layer per interrompere il flusso di esecuzione
 * e segnalare al Controller di restituire uno status HTTP 404 (Not Found).
 * Estende {@link RuntimeException}, quindi non richiede dichiarazione nel blocco throws.
 * </p>
 *
 * @see RuntimeException
 * @author Modulink Team
 * @version 1.0.1
 * @since 1.2.0
 */
public class EventoNotFound extends RuntimeException {

    /**
     * Costruisce una nuova eccezione con il messaggio di dettaglio specificato.
     *
     * @param message Il messaggio che descrive l'errore (es. "Evento con ID X non trovato").
     * @since 1.2.0
     */
    public EventoNotFound(String message) {
        super(message);
    }
}
