package com.modulink.Model.Ruolo;

/**
 * Eccezione personalizzata sollevata quando una ricerca di un ruolo fallisce all'interno del sistema.
 * <p>
 * Questa classe estende {@link Exception} (Checked Exception), imponendo al chiamante la gestione
 * esplicita del caso in cui un'entità {@link RuoloEntity} non venga individuata tramite il suo identificativo.
 * </p>
 * <p>
 * Tipicamente utilizzata nel {@link RuoloService} durante le operazioni di lookup o aggiornamento
 * quando i parametri di ricerca non corrispondono a nessun record persistito nel database.
 * </p>
 *
 * @author Modulink Team
 * @version 1.4.2
 * @since 1.0.0
 */
public class RuoloNotFoundException extends Exception {

    /**
     * Costruisce una nuova istanza di RuoloNotFoundException con un messaggio di errore predefinito.
     * <p>
     * Il messaggio standard impostato è: "Ruolo non trovato nella lista degli id".
     * </p>
     *
     * @since 1.0.0
     */
    public RuoloNotFoundException() {
        super("Ruolo non trovato nella lista degli id");
    }
}
