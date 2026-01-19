package com.modulink.Model.Prodotto;

/**
 * Eccezione controllata (Checked Exception) sollevata quando la ricerca di un prodotto non produce risultati.
 *
 * @author Modulink Team
 * @version 1.0.0
 * @since 1.2.0
 */
public class ProdottoNotFoundException extends Exception {

    /**
     * Costruttore predefinito.
     * <p>
     * Inizializza l'eccezione con un messaggio di errore standardizzato: "Il prodotto indicato non è stato trovato".
     * </p>
     *
     * @since 1.2.0
     */
    public ProdottoNotFoundException() {
        super("Il prodotto indicato non è stato trovato");
    }
}
